<?php

use Illuminate\Auth\UserTrait;
use Illuminate\Auth\UserInterface;
use Illuminate\Auth\Reminders\RemindableTrait;
use Illuminate\Auth\Reminders\RemindableInterface;

class Friend extends Eloquent {


	/**
	 * The database table used by the model.
	 *
	 * @var string
	 */
	protected $table = 'friends';
	public $timestamps = false;
	
	public function user() {
		return $this->belongsTo('User', 'user_id');
	}

	public function friend() {
		return $this->belongsTo('User', 'friend_id');
	}

}