<?php
class PostController extends BaseController {

	public function getPosts($uid) {
		$posts = Post::orderBy('timestamp', 'DESC')->get();
		$friends = Friend::where('user_id', '=', $uid)->get();
		$results = array();
		$i = 0;
		foreach($posts as $post) {
			$found = false;
			foreach($friends as $friend) {
				if (($post->user_id == $friend->friend_id && $friend->accepted )) {
					$found = true;
					break;
				}
			}
			if ($found || $post->anonymous || $post->user_id == $uid) {
				$results[$i]['id'] = $post->id;
				$results[$i]['user'] = $post->user->username;
				$results[$i]['uid'] = $post->user_id;
				$results[$i]['text'] = $post->text;
				$results[$i]['timestamp'] = $post->timestamp;
				if ($post->anonymous) {
					$results[$i]['anonymous'] = true;
				}
				else {
					$results[$i]['anonymous'] = false;
				}
				$i++;
			}
		}
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function createPost($uid) {
		$post = new Post();
		$text = Input::get('text');
		$anonymous = Input::get('anonymous');
		if ($anonymous == 'true') {
			$anonymous = 1;
		}
		else {
			$anonymous = 0;
		}
		$post->user_id = $uid;
		$post->text = $text;
		$post->anonymous = $anonymous;
		$post->save();
		$results = array();
		$results['ack'] = 'Status successfully created';
		$results['post_id'] = $post->id;
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function deletePost($uid, $pid) {
		Post::where('id', '=', $pid)->delete();
		Comment::where('post_id', '=', $pid)->delete();
		Attribute::where('post_id', '=', $pid)->delete();
		$results = array();
		$results['ack'] = 'Status successfully deleted';
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}
}