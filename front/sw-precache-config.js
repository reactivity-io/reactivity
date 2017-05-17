module.exports = {
    staticFileGlobs: [
        '/index.html',
        '/manifest.json',
        '/bower_components/webcomponentsjs/webcomponents-lite.js',
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
                    // maxEntries: 5,
                    name: 'api'
                }
            }
        },
        {
            urlPattern: /subscribe/,
            handler: 'networkFirst',
            options: {
                cache: {
                    // maxEntries: 5,
                    name: 'api'
                }
            }
        },
        {
            urlPattern: /artifacts/,
            handler: 'networkFirst',
            options: {
                cache: {
                    // maxEntries: 5,
                    name: 'api'
                }
            }
        }
    ]
};
