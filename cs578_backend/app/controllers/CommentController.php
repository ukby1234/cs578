<?php
class CommentController extends BaseController {
	public function getComments($postid, $uid) {
		$comments = Comment::where('post_id', '=', $postid)->orderBy('timestamp', 'DESC')->get();
		$friends = Friend::where('user_id', '=', $uid)->get();
		$results = array();
		$i = 0;
		foreach($comments as $comment) {
			$found = false;
			foreach($friends as $friend) {
				if ($comment->user_id == $friend->friend_id && $friend->accepted) {
					$found = true;
					break;
				}
			}
			if ($found || $comment->user_id == $uid) {
				$results[$i]['id'] = $comment->id;
				$results[$i]['user'] = $comment->user->username;
				$results[$i]['text'] = $comment->text;
				$results[$i]['post_id'] = $comment->post_id;
				$results[$i]['user_id'] = $comment->user_id;
				$results[$i]['timestamp'] = $comment->timestamp;
				$i++;
			}
		}
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function createComment($postid, $uid) {
		$comment = new Comment();
		$comment->user_id = $uid;
		$comment->post_id = $postid;
		$comment->text = Input::get('text');
		$comment->save();
		$results = array();
		$results['ack'] = 'Comment successfully created';
		$results['comment_id'] = $comment->id;
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function deleteComment($cid) {
		$posts = Comment::where('id', '=', $cid)->delete();
		$results = array();
		$results['ack'] = 'Comment successfully deleted';
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}
}