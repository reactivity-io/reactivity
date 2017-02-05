const gulp = require('gulp');
const file = require('gulp-file');;
const browserSync = require('browser-sync').create();
const proxyMiddleware = require('http-proxy-middleware');
const historyApiFallback = require('connect-history-api-fallback');
const fs = require('fs');
const through = require('through2');
const yaml = require('js-yaml');

// Keep the global.config above any of the gulp-tasks that depend on it
global.config = {
    build: {
        rootDirectory: 'dist',
        bundledDirectory: 'bundled',
        unbundledDirectory: 'unbundled',
        // A bundled version will be vulcanized and sharded. An unbundled version will not have its files combined
        // This is for projects using HTTP/2 server push.
        bundleType: 'both'
    },
    // Path to your service worker, relative to the build root directory
    serviceWorkerPath: 'service-worker.js',
    // Service Worker precache options based on https://github.com/GoogleChrome/sw-precache#options-parameter
    swPrecacheConfig: {
        staticFileGlobs: [
            '/index.html',
            '/manifest.json',
            '/bower_components/webcomponentsjs/webcomponents-lite.min.js',
            '/src/**/*.*',
        ],
        navigateFallback: '/index.html',
        importScripts: ['/bower_components/sw-toolbox/sw-toolbox.js'],
        runtimeCaching: [],
        // See sample below for actual runtime caching:
        //runtimeCaching: [{
        //    urlPattern: /\/network-first\//,
        //    handler: 'networkFirst'
        //}, {
        //    urlPattern: /\/cache-first\//,
        //    handler: 'cacheFirst',
        //    options: {
        //        cache: {
        //            maxEntries: 5,
        //            name: 'cache-first'
        //        }
        //    }
        //}],
    }
};

const buildTask = require('./gulp_tasks/build.task.js');
const project = require('./gulp_tasks/polymer-build.task.js');
const clean = require('./gulp_tasks/clean.task.js');

const options = {
    target: 'http://localhost:8080',
    changeOrigin: true,
    pathRewrite: {
        '^/api': '/'
    }
};

const deleteFolderRecursive = (path) => {
    if (fs.existsSync(path) ) {
        fs.readdirSync(path).forEach((file,index) => {
            var curPath = path + "/" + file;
            
            if( fs.lstatSync(curPath).isDirectory()) { // recurse
                deleteFolderRecursive(curPath);
            } else { // delete file
                fs.unlinkSync(curPath);
            }
        });

        fs.rmdirSync(path);
    }
};

// Watch scss AND html files, doing different things with each.
gulp.task('default', () => {
    // Serve files from the root of this project
    browserSync.init({
        server: {
            baseDir: './',
            index: 'index.html',
            middleware: [
                historyApiFallback(),
                proxyMiddleware('/api', options),
                {
                    route: "/domain-api.json",
                    handle: function (req, res, next) {
                        res.end('["http://localhost:3000/api"]');
                    }
                }
            ]
        }
    });
});

gulp.task('dist-clean', (done) => {
    deleteFolderRecursive('./dist');
    done();
});

gulp.task('dist-bower', (done) => {    
    gulp.src(['./bower_components/**/*.{js,html}'])
        .pipe(gulp.dest("./dist/bower_components"));
    done();
});

gulp.task('dist-src', (done) => {
    gulp.src(['./src/**/*']).pipe(gulp.dest("./dist/src"));
    done();
});

gulp.task('dist-index', (done) => {
    gulp.src(['./index.html']).pipe(gulp.dest("./dist"));
    done();
});

gulp.task('staticfile', (done) => {
    file('./dist/Staticfile', "", { src: true }).pipe(gulp.dest('./'));
    done();
});

// This task expose in api-domain.json file the location of API endpoints to be consumed
// This information is contained in the route attributes of manifest.yml, the CloudFoundry configuration file
// The goal of this task is to parse the yaml file and extract the desired route into a file that will be uploaded on the HTTP server
gulp.task('dist-api-domain', (done) => {
    gulp.src(['../manifest.yml'])
        .pipe(through.obj((chunk, enc, cb) => {
            var doc = yaml.safeLoad(fs.readFileSync(chunk.path, 'utf8'));
            var apiDomain = '[';
            
            doc.applications.every(application => {
                if (application.name === 'reactivity-broadcaster') {
                    application.routes.every((item, index) => {
                        if (index > 0) {
                            index += ", ";
                        }
                        
                        apiDomain += "'" + item.route + "'";
                        
                        return true;
                    });
                    
                    return false;
                }
                
                return true;
            });
            
            cb(null, apiDomain + ']');
        }))
        .pipe(fs.createWriteStream('./dist/api-domain.json'));
        
    done();
});

gulp.task('dist', gulp.series("dist-clean", "dist-bower", "dist-src", "dist-index", "staticfile", "dist-api-domain"));

gulp.task('dist2', gulp.series([
  clean,
  project.merge(buildTask.source, buildTask.dependencies),
  project.serviceWorker,
]));