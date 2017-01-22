const gulp = require('gulp');
const browserSync = require('browser-sync').create();
const proxyMiddleware = require('http-proxy-middleware');
const historyApiFallback = require('connect-history-api-fallback');
const fs = require('fs');

const options = {
    target: 'http://localhost:8080',
    changeOrigin: true,
    pathRewrite: {
        '^/api': '/'
    }
};

var deleteFolderRecursive = (path) => {
    if( fs.existsSync(path) ) {
        fs.readdirSync(path).forEach((file,index) => {
            var curPath = path + "/" + file;
            if(fs.lstatSync(curPath).isDirectory()) { // recurse
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
                proxyMiddleware('/api', options)
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

gulp.task('dist', gulp.series("dist-clean", "dist-bower", "dist-src", "dist-index"));
