<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the Closure to execute when that URI is requested.
|
*/

Route::post('/login', 'UserController@authentication');
Route::get('/user/', 'UserController@getUsers');
Route::post('/user/', 'UserController@createUser');
Route::get('/post/{uid}/', 'PostController@getPosts');
Route::post('/post/{uid}/', 'PostController@createPost');
Route::delete('/post/{uid}/{pid}/', 'PostController@deletePost');
Route::get('/comment/{postid}/', 'CommentController@getComments');
Route::post('/comment/{postid}/{uid}/', 'CommentController@createComment');
Route::delete('/comment/{cid}/', 'CommentController@deleteComment');
Route::get('/attribute/{postid}/', 'AttributeController@getAttributes');
Route::post('/attribute/{postid}/environment/', 'AttributeController@createEnvironmentAttribute');
Route::get('/message/{receiver}/', 'MessageController@receiveMessages');
Route::post('/message/{receiver}/{sender}/', 'MessageController@sendMessage');
