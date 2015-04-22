<?php
class UserController extends BaseController {

	public function authentication() {
		$username = Input::get('username');
		$password = sha1(Input::get('passwd'));
		$user = User::where('username', '=', $username)->where('password', '=', $password)->get();
		if ($user->isEmpty()) {
			$arr = array();
			$arr['ack'] = 'authentication failed';
			$response = Response::make(json_encode($arr) , 401);
			$response->header('Content-Type', 'application/json');
			return $response;
		}
		else {
			$arr = array();
			$arr['ack'] = 'authentication successful';
			$arr['uid'] = $user->first()->id;
			$arr['username'] = $user->first()->username;
			$response = Response::make(json_encode($arr) , 200);
			$response->header('Content-Type', 'application/json');
			return $response;
		}
	}

	public function getUsers() {
		$users = User::get();
		$i = 0;
		$results = array();
		foreach($users as $user) {
			$results[$i]['id'] = $user->id;
			$results[$i]['user_name'] = $user->username;
			$i++;
		}
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}
}