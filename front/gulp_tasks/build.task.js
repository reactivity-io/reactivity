const gulp = require('gulp');
const project = require('./polymer-build.task.js');
// const compile = require('./compile.task.js');

// The source task will split all of your source files into one big ReadableStream. Source files are those in src/** as
// well as anything added to the sourceGlobs property of polymer.json.
function source() {
  return project.splitSource()
    // Add your own build tasks here!
    //.pipe(gulpif('**/*.{png,gif,jpg,svg}', images.minify()))
    //.pipe(gulpif(/\.js$/, compile()))
    .pipe(project.rejoin()); // Call rejoin when you're finished
}

// The dependencies task will split all of your bower_components files into one big ReadableStream
function dependencies() {
  return project.splitDependencies()
    .pipe(project.rejoin());
}

module.exports = {
  source,
  dependencies,
};


