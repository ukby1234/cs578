<?php
class MessageController extends BaseController {
	public function sendMessage($receiver, $sender) {
		$message = new Message();
		$text = Input::get('text');
		$message->sender_id = $sender;
		$message->receiver_id = $receiver;
		$message->text = $text;
		$message->save();
		$results = array();
		$results['ack'] = 'Status successfully created';
		$results['message_id'] = $message->id;
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function receiveMessages($receiver) {
		$messages = Message::where('receiver_id', '=', $receiver)->orwhere('sender_id','=',$receiver)->get();
		$results = array();
		$i = 0;
		foreach($messages as $message) {
			$results[$i]['id'] = $message->id;
			$results[$i]['sender_id'] = $message->sender_id;
			$results[$i]['receiver_id'] = $message->receiver_id;
			$results[$i]['text'] = $message->text;
			$results[$i]['timestamp'] = $message->timestamp;
			$results[$i]['sender_name'] = $message->sender->username;
			$results[$i]['receiver_name'] = $message->receiver->username;
			$i++;
		}
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}
}