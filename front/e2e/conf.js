const config = {
    framework: 'jasmine',
    seleniumAddress: 'http://localhost:4444/wd/hub',
    specs: ['spec.js']
};
if (process.env.TRAVIS) {
    config.sauceUser = process.env.SAUCE_USERNAME;
    config.sauceKey = process.env.SAUCE_ACCESS_KEY;
    config.capabilities = {
        'browserName': 'chrome',
        'tunnel-identifier': process.env.TRAVIS_JOB_NUMBER,
        'build': process.env.TRAVIS_BUILD_NUMBER
    };
    config.chromeOnly = true;
    config.directConnect = true;
}

// conf.js
exports.config = config;
