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
}