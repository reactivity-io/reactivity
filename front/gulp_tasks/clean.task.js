const del = require('del');

// Clean the build directory
function clean() {
  return del([global.config.build.rootDirectory], {dot: true});
}

module.exports = clean;
