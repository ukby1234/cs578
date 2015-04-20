<?php
class AttributeController extends BaseController {
	public function createEnvironmentAttribute($pid) {
		$attribute = new Attribute();
		$information = Input::get('information');
		$source = Input::get('source');
		$attribute->post_id = $pid;
		$attribute->information = $information;
		$attribute->source = $source;
		$attribute->type = 'environment';
		$attribute->save();
		$results = array();
		$results['ack'] = 'Status successfully created';
		$results['attribute_id'] = $attribute->id;
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}

	public function getAttributes($pid) {
		$attributes = Attribute::where('post_id', '=', $pid)->get();
		$results = array();
		$i = 0;
		foreach($attributes as $attribute) {
			$results[$i]['id'] = $attribute->id;
			$results[$i]['source'] = $attribute->source;
			$results[$i]['information'] = $attribute->information;
			$results[$i]['post_id'] = $attribute->post_id;
			$results[$i]['type'] = $attribute->type;
			$i++;
		}
		$response = Response::make(json_encode($results) , 200);
		$response->header('Content-Type', 'application/json');
		return $response;
	}
}