"use strict";

const gulp = require('gulp'); 
const concat = require('gulp-concat'); 
const ngAnnotate = require('gulp-ng-annotate'); 
const uglify = require('gulp-uglify'); 
const babel = require('gulp-babel');
const cachebuster = require('gulp-cachebust');
const clean = require('gulp-clean');
const touch = require('gulp-touch-fd');
const eslint = require('gulp-eslint');
const gulpIf = require('gulp-if');

var cachebust = new cachebuster();

function cleanJs() {
	return gulp.src('src/main/webapp/app/scripts', {read: false, allowEmpty: true}) 
		.pipe(clean());
}

function isFixed(file) {
    return file.eslint !== null && file.eslint.fixed;
}

function eslintJS() { 
    return gulp.src('src/main/javascript/**/*.js')
    .pipe(eslint({ fix: true }))
    .pipe(eslint.format())
    .pipe(gulpIf(isFixed, gulp.dest('src/main/javascript/')))
    .pipe(eslint.failAfterError())
};

function coreJs() {
    return gulp
		.src([ 
			'src/main/javascript/app.js',
			'src/main/javascript/franchiseLogistics.js',
			'src/main/javascript/decorators/*.js',
			'src/main/javascript/services/*.js',
			'src/main/javascript/factories/*.js',
			'src/main/javascript/directives/*.js',
			], { sourcemaps: true })
	    .pipe(concat('AppCore.min.js', {newLine: ';'}))
		.pipe(ngAnnotate({add: true})) 
		.pipe(babel({presets: ['@babel/preset-env']}))
		.pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
		.pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

function vendorJs() {
    return gulp
		.src([ 
			'src/main/javascript/vendors.js',
			], { sourcemaps: true })
		.pipe(concat('AppVendor.min.js', {newLine: ';'}))
		.pipe(ngAnnotate({add: true})) 
		.pipe(babel({presets: ['@babel/preset-env']}))
		.pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
		.pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

function componentsJs() {
	return gulp
		.src([
			'src/main/javascript/components/**/*.js'
			], { sourcemaps: true })
        .pipe(concat('AppComponents.min.js', {newLine: ';'}))
        .pipe(ngAnnotate({add: true})) 
        .pipe(babel({presets: ['@babel/preset-env']}))
        .pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
	    .pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

function adminJs() {
    return gulp
		.src([ 
		    'src/main/javascript/routers/AdminRouter.js', 
		    'src/main/javascript/controllers/admin/*.js',
		    'src/main/javascript/controllers/supplier/RequestsCtrl.js',
		    'src/main/javascript/controllers/supplier/StockCtrl.js'
		    ], { sourcemaps: true })
		.pipe(concat('AppAdmin.min.js', {newLine: ';'}))
		.pipe(ngAnnotate({add: true})) 
		.pipe(babel({presets: ['@babel/preset-env']}))
		.pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
		.pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

function franchiseeJs() {
    return gulp
		.src([ 
		    'src/main/javascript/routers/FranchiseeRouter.js', 
		    'src/main/javascript/controllers/franchisee/*.js'
		    ], { sourcemaps: true })
		.pipe(concat('AppFranchisee.min.js', {newLine: ';'}))
		.pipe(ngAnnotate({add: true})) 
		.pipe(babel({presets: ['@babel/preset-env']}))
		.pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
		.pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

function franchisorJs() {
    return gulp
		.src([ 
		    'src/main/javascript/routers/FranchisorRouter.js', 
		    'src/main/javascript/controllers/franchisor/*.js'
		    ], { sourcemaps: true })
		.pipe(concat('AppFranchisor.min.js', {newLine: ';'}))
		.pipe(ngAnnotate({add: true})) 
		.pipe(babel({presets: ['@babel/preset-env']}))
		.pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
		.pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

function supplierJs() {
    return gulp
		.src([ 
		    'src/main/javascript/routers/SupplierRouter.js', 
		    'src/main/javascript/controllers/supplier/*.js'
		    ], { sourcemaps: true })
		.pipe(concat('AppSupplier.min.js', {newLine: ';'}))
		.pipe(ngAnnotate({add: true})) 
		.pipe(babel({presets: ['@babel/preset-env']}))
		.pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
		.pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

function loginJs() {
    return gulp
		.src([ 
		    'src/main/javascript/routers/LoginRouter.js', 
		    'src/main/javascript/controllers/login/*.js'
		    ], { sourcemaps: true })
		.pipe(concat('AppLogin.min.js', {newLine: ';'}))
		.pipe(ngAnnotate({add: true})) 
		.pipe(babel({presets: ['@babel/preset-env']}))
		.pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
		.pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

function commonJs() {
    return gulp
		.src([ 
			'src/main/javascript/controllers/SystemUpdateInfoCtrl.js',
			'src/main/javascript/controllers/RadioVinilCtrl.js',
			'src/main/javascript/controllers/ConfigurationCtrl.js',
			'src/main/javascript/controllers/MessagesCtrl.js',
			'src/main/javascript/controllers/MessageLabelsCtrl.js',
			'src/main/javascript/controllers/ErrorCtrl.js',
			'src/main/javascript/controllers/MenuCtrl.js'
			], { sourcemaps: true })
		.pipe(concat('AppCommon.min.js', {newLine: ';'}))
		.pipe(ngAnnotate({add: true})) 
		.pipe(babel({presets: ['@babel/preset-env']}))
		.pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
		.pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

function printJs() {
    return gulp
		.src([ 
			'src/main/javascript/appPrint.js'
			], { sourcemaps: true })
		.pipe(concat('AppPrint.min.js', {newLine: ';'}))
		.pipe(ngAnnotate({add: true})) 
		.pipe(babel({presets: ['@babel/preset-env']}))
		.pipe(uglify({mangle: true}))
		.pipe(cachebust.resources())
		.pipe(gulp.dest('src/main/webapp/app/scripts/', { sourcemaps: './maps' }));
}

const compileJs = gulp.parallel(coreJs, vendorJs, componentsJs, commonJs, adminJs, supplierJs, franchiseeJs, franchisorJs, loginJs, printJs);

function masterPageTag() {
	return gulp
		.src('src/main/html/templates/tags/masterpage.tag')
		.pipe(cachebust.references())
		.pipe(gulp.dest('src/main/webapp/WEB-INF/tags/'))
		.pipe(touch());
}

function adminJsp() {
	return gulp
		.src('src/main/html/templates/admin/home.jsp')
		.pipe(cachebust.references())
		.pipe(gulp.dest('src/main/webapp/WEB-INF/jsp/admin/'))
		.pipe(touch());
}

function franchiseeJsp() {
	return gulp
		.src('src/main/html/templates/franchisee/home.jsp')
		.pipe(cachebust.references())
		.pipe(gulp.dest('src/main/webapp/WEB-INF/jsp/franchisee/'))
		.pipe(touch());
}

function franchisorJsp() {
	return gulp
		.src('src/main/html/templates/franchisor/home.jsp')
		.pipe(cachebust.references())
		.pipe(gulp.dest('src/main/webapp/WEB-INF/jsp/franchisor/'))
		.pipe(touch());
}

function loginJsp() {
	return gulp
		.src('src/main/html/templates/login/home.jsp')
		.pipe(cachebust.references())
		.pipe(gulp.dest('src/main/webapp/WEB-INF/jsp/login/'))
		.pipe(touch());
}

function supplierJsp() {
	return gulp
		.src('src/main/html/templates/supplier/home.jsp')
		.pipe(cachebust.references())
		.pipe(gulp.dest('src/main/webapp/WEB-INF/jsp/supplier/'))
		.pipe(touch());
}

function printJsp() {
	return gulp
		.src('src/main/html/templates/supplier/deliveryRequestsPrint.jsp')
		.pipe(cachebust.references())
		.pipe(gulp.dest('src/main/webapp/WEB-INF/jsp/supplier/'))
		.pipe(touch());
}

const compileHtml = gulp.parallel(masterPageTag, adminJsp, franchiseeJsp, franchisorJsp, loginJsp, supplierJsp, printJsp);

const compile = gulp.series(cleanJs, compileJs, compileHtml);

function watch() {
	gulp.watch([
		'src/main/html/templates',
		'src/main/javascript'
	], compile);
}

//export tasks
exports.default = compile;
exports.watch = watch;
exports.eslintJS = eslintJS;
