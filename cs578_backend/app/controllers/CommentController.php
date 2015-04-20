<?php
class CommentController extends BaseController {
	public function getComments($postid) {
		$comments = Comment::where('post_id', '=', $postid)->orderBy('timestamp', 'DESC')->get();
		$results = array();
		$i = 0;
		foreach($comments as $comment) {
			$results[$i]['id'] = $comment->id;
			$results[$i]['user'] = $comment->user->username;
			$results[$i]['text'] = $comment->text;
			$i++;
		}
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}
}