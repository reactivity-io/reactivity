const config = {
    framework: 'jasmine',
    seleniumAddress: 'http://localhost:4444/wd/hub',
    specs: ['spec.js']
};
console.log('travis process env : ', process.env.TRAVIS);
if (process.env.TRAVIS) {
    console.log("TRAVISSSSSSSS !!!!!!!")
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
