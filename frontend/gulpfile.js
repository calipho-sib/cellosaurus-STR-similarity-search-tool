// Adapted to Gulp 4.0.0 from model by Amardeep Rai
// https://medium.com/devux/minifying-your-css-js-html-files-using-gulp-2113d7fcbd16
'use strict';

const gulp = require('gulp');
const htmlmin = require('gulp-htmlmin');
const csso = require('gulp-csso');
const terser = require('gulp-terser');

// Gulp task to minify HTML files
gulp.task('pages', done => {
    gulp.src(['./*.html'])
    // Minify the file
    .pipe(htmlmin({
        collapseWhitespace: true,
        removeComments: true
    }))
    // Output
    .pipe(gulp.dest('./out'));
    done();
});

// Gulp task to minify CSS files
gulp.task('styles', done => {
    gulp.src('./assets/css/*.css')
    // Minify the file
    .pipe(csso())
    // Output
    .pipe(gulp.dest('./out/assets/css'));
    done();
});

// Gulp task to minify JavaScript files
gulp.task('scripts', done => {
    gulp.src('./assets/js/*.js')
    // Minify the file
    .pipe(terser())
    // Output
    .pipe(gulp.dest('./out/assets/js'));
    done();
});

// Gulp task to minify all files
gulp.task('default', gulp.series(gulp.parallel('pages', 'styles', 'scripts')));
