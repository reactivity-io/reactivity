module.exports = {
    staticFileGlobs: [
        '/index.html',
        '/manifest.json',
        '/bower_components/webcomponentsjs/webcomponents-lite.min.js',
        '/src/**/*.*'
    ],
    navigateFallback: '/index.html',
    importScripts: ['/bower_components/sw-toolbox/sw-toolbox.js'],

    runtimeCaching: [{
        urlPattern: '/domain-api.json',
        handler: 'networkFirst',
        options: {
            cache: {
                name: 'constants'
            }
        }
    },
        {
            urlPattern: /organizations/,
            handler: 'networkFirst',
            options: {
                cache: {
                    maxEntries: 5,
                    name: 'api'
                }
            }
        },
        {
            urlPattern: /subscribe/,
            handler: 'networkFirst',
            options: {
                cache: {
                    maxEntries: 5,
                    name: 'api'
                }
            }
        },
        {
            urlPattern: /artifacts/,
            handler: 'networkFirst',
            options: {
                cache: {
                    maxEntries: 5,
                    name: 'api'
                }
            }
        }
    ]
    // .
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
};