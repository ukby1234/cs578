<?php

use Illuminate\Auth\UserTrait;
use Illuminate\Auth\UserInterface;
use Illuminate\Auth\Reminders\RemindableTrait;
use Illuminate\Auth\Reminders\RemindableInterface;

class Message extends Eloquent {


	/**
	 * The database table used by the model.
	 *
	 * @var string
	 */
	protected $table = 'messages';
	public $timestamps = false;

	public function sender() {
		return $this->belongsTo('User', 'sender_id');;
	}

	public function receiver() {
		 return $this->belongsTo('User', 'receiver_id');
	}

}
