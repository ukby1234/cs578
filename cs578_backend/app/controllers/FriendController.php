<?php
class FriendController extends BaseController {
	public function getFriends($uid) {
		$friends = Friend::where('user_id', '=', $uid)->get();
		$results = array();
		$i = 0;
		foreach($friends as $friend) {
			$results[$i]['id'] = $friend->id;
			$results[$i]['friend_id'] = $friend->friend_id;
			$results[$i]['friend_username'] = $friend->friend->username;
			if ($friend->accepted == 1) {
				$results[$i]['accepted'] = true;
			}
			else {
				$results[$i]['accepted'] = false;
			}
			if ($friend->direction == 1) {
				$results[$i]['direction'] = true;
			}
			else {
				$results[$i]['direction'] = false;
			}
			$i++;
		}
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function addFriend($uid, $friend_id) {
		$results = array();
		$results['ack'] = 'Friend successfully added';
		$results['id'] = array();
		$friend = new Friend();
		$friend->user_id = $uid;
		$friend->friend_id = $friend_id;
		$friend->accepted = 0;
		$friend->direction = 0;
		$friend->save();
		$results['id'][0] = $friend->id;
		$friend = new Friend();
		$friend->user_id = $friend_id;
		$friend->friend_id = $uid;
		$friend->accepted = 0;
		$friend->direction = 1;
		$friend->save();
		$results['id'][1] = $friend->id;
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function deleteFriend($uid, $friend_id) {
		Friend::where('user_id', '=', $uid)->where('friend_id', '=', $friend_id)->delete();
		Friend::where('user_id', '=', $friend_id)->where('friend_id', '=', $uid)->delete();
		$results = array();
		$results['ack'] = 'Friend successfully deleted';
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function approveFriend($uid, $friend_id) {
		$friend = Friend::where('user_id', '=', $uid)->where('friend_id', '=', $friend_id)->get()->first();
		$friend->accepted = 1;
		$friend->save();
		$friend = Friend::where('user_id', '=', $friend_id)->where('friend_id', '=', $uid)->get()->first();
		$friend->accepted = 1;
		$friend->save();
		$results = array();
		$results['ack'] = 'Friend successfully approved';
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function getCandidates($uid) {
		$friends = Friend::where('user_id', '=', $uid)->get();
		$users = User::get();
		$results = array();
		$i = 0;
		foreach ($users as $user) {
			$isFound = false;
			foreach($friends as $friend) {
				if ($friend->friend_id == $user->id) {
					$isFound = true;
				}
			}
			if (!$isFound && $user->id != $uid) {
				$results[$i]['id'] = $user->id;
				$results[$i]['username'] = $user->username;
				$i++;
			}
		}
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}
}