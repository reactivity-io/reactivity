module .exports = {
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
};