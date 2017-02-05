const babel = require('gulp-babel');

// Compile javascript
// Mainly used to transform the javascript part of the polymer components
function compile() {
  return babel({ "presets": ["es2016"] });
}

module.exports = compile;
