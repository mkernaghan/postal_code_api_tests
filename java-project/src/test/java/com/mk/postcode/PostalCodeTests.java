package com.mk.postcode;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import io.restassured.response.Response;

public class PostalCodeTests {	

	String api_url = "http://api.zippopotam.us/";

	String surrey_post_code = "V3T";   //"V3T 5X3"
	String ny_post_code = "10022";
	String london_post_code = "SE1";  //"SE1 9SG"
	String dublin_post_code = "A94 A0D0";

	String canada = "ca";
	String us = "us";
	String gb = "gb";
	String ireland = null;

	@Test 
	public void shouldGetExpectedKeysForSurreyOffice() {


		given().expect().statusCode(200)
		.when().get(api_url+canada+"/"+surrey_post_code)
		.then()
		.body("$", hasKey("post code"))
		.body("$", hasKey("country"))
		.body("$", hasKey("country abbreviation"))
		.body("$", hasKey("places"))
		.body("places.any { it.containsKey('place name') }", is(true))
		.body("places.any { it.containsKey('longitude') }", is(true))
		.body("places.any { it.containsKey('state') }", is(true))
		.body("places.any { it.containsKey('state abbreviation') }", is(true))
		.body("places.any { it.containsKey('latitude') }", is(true));

	}

	@Test 
	public void shouldGetPostCode_Surrey() {

		given().expect().statusCode(200)
		.when().get(api_url+canada+"/"+surrey_post_code)
		.then()
		.body("'post code'",  equalTo(surrey_post_code));

	}

	@Test 
	public void shouldReturnOnePlaces_Surrey() {

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+canada+"/"+surrey_post_code)
				.then()
				.extract()
				.response();

		int places = response.jsonPath().getList("places").size();
		assertEquals(places, 1);

	}

	@Test 
	public void shouldGetExpectedKeys_US() {

		given().expect().statusCode(200)
		.when().get(api_url+us+"/"+ny_post_code)
		.then()
		.body("$", hasKey("post code"))
		.body("$", hasKey("country"))
		.body("$", hasKey("country abbreviation"))
		.body("$", hasKey("places"))
		.body("places.any { it.containsKey('place name') }", is(true))
		.body("places.any { it.containsKey('longitude') }", is(true))
		.body("places.any { it.containsKey('state') }", is(true))
		.body("places.any { it.containsKey('state abbreviation') }", is(true))
		.body("places.any { it.containsKey('latitude') }", is(true));

	}

	@Test 
	public void shouldGetPostCode_US() {

		given().expect().statusCode(200)
		.when().get(api_url+us+"/"+ny_post_code)
		.then()
		.body("'post code'",  equalTo(ny_post_code));

	}

	@Test 
	public void shouldReturnOnePlaces_US() {

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+us+"/"+ny_post_code)
				.then()
				.extract()
				.response();

		int places = response.jsonPath().getList("places").size();
		assertEquals(places, 1);

	}

	@Test 
	public void shouldGetExpectedKeys_London() {

		given().expect().statusCode(200)
		.when().get(api_url+gb+"/"+london_post_code)
		.then()
		.body("$", hasKey("post code"))
		.body("$", hasKey("country"))
		.body("$", hasKey("country abbreviation"))
		.body("$", hasKey("places"))
		.body("places.any { it.containsKey('place name') }", is(true))
		.body("places.any { it.containsKey('longitude') }", is(true))
		.body("places.any { it.containsKey('state') }", is(true))
		.body("places.any { it.containsKey('state abbreviation') }", is(true))
		.body("places.any { it.containsKey('latitude') }", is(true));

	}

	@Test 
	public void shouldGetPostCode_London() {

		given().expect().statusCode(200)
		.when().get(api_url+gb+"/"+london_post_code)
		.then()
		.body("'post code'",  equalTo(london_post_code));

	}

	@Test 
	public void shouldReturnMultiplePlaces_London() {

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+gb+"/"+london_post_code)
				.then()
				.extract()
				.response();

		int places = response.jsonPath().getList("places").size();
		assertThat(places, greaterThan(1));

	}

	@Test 
	public void shouldHaveAtLeastTwoPlacesWithBridgeInTheName_London() {

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+gb+"/"+london_post_code)
				.then()
				.extract()
				.response();

		List<String> bridge_places = response.jsonPath().get("places['place name']"); 

		int finds = 0;
		for (int i = 0; i < bridge_places.size(); i++) { 

			String x = bridge_places.get(i); 
			boolean isFound = x.contains("Bridge");
			if (isFound) {
				finds = finds + 1;
			};

		}

		assertTrue( finds >= 2 );

	}

	@Test 
	public void shouldHaveOnlyOnePlacesWithBoroughInTheName_London() {

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+gb+"/"+london_post_code)
				.then()
				.extract()
				.response();

		List<String> bridge_places = response.jsonPath().get("places['place name']"); 

		int finds = 0;
		for (int i = 0; i < bridge_places.size(); i++) { 

			String x = bridge_places.get(i); 
			boolean isFound = x.contains("Borough");
			if (isFound) {
				finds = finds + 1;
			};

		}

		assertEquals( finds,1 );

	}

	@Test 
	public void shouldGet404ForIrelandPostCode_Dublin() {

		//Using "gb" here as country code because there is none defined for Ireland.

		given().expect().statusCode(404)
		.when().get(api_url+gb+"/"+dublin_post_code);

	}

	@Test 
	public void shouldVerifyFormatOfLongtitudes_London() {    //checking all longitudes from London Post code

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+gb+"/"+london_post_code)
				.then()
				.extract()
				.response();

		methods.assertLongitudeFormatIsFloat(response);

	}

	@Test 
	public void shouldVerifyFormatOfLongtitudes_Canada() {    //checking all longitudes from Canada Post code

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+canada+"/"+surrey_post_code)
				.then()
				.extract()
				.response();

		methods.assertLongitudeFormatIsFloat(response);

	}

	@Test 
	public void shouldVerifyFormatOfLongtitudes_US() {    //checking all longitudes from US Post code

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+us+"/"+ny_post_code)
				.then()
				.extract()
				.response();

		methods.assertLongitudeFormatIsFloat(response);

	}

	@Test 
	public void shouldVerifyFormatOfLatitudes() {  //checking all latitudes from London Post code

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+gb+"/"+london_post_code)
				.then()
				.extract()
				.response();

		methods.assertLatitudeFormatIsFloat(response);

	}

	@Test 
	public void shouldVerifyFormatOfLatitudes_Canada() {    //checking all longitudes from Canada Post code

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+canada+"/"+surrey_post_code)
				.then()
				.extract()
				.response();

		methods.assertLatitudeFormatIsFloat(response);

	}

	@Test 
	public void shouldVerifyFormatOfLatitudes_US() {    //checking all longitudes from US Post code"

		Response response = 
				given().expect().statusCode(200)
				.when()
				.get(api_url+us+"/"+ny_post_code)
				.then()
				.extract()
				.response();

		methods.assertLatitudeFormatIsFloat(response);

	}

	@Test 
	public void shouldGet405ForPostRequest() {

		Map<String, Object>  jsonAsMap = new HashMap<>();
		jsonAsMap.put("What I Want", "a 405");

		given()
		.body(jsonAsMap)
		.expect().statusCode(405)
		.when()
		.post(api_url+gb+"/"+london_post_code);

	}

	@Test 
	public void shouldGet405ForPutRequest() {

		Map<String, Object>  jsonAsMap = new HashMap<>();
		jsonAsMap.put("What I Want", "a 405");

		given()
		.body(jsonAsMap)
		.expect().statusCode(405)
		.when()
		.put(api_url+gb+"/"+london_post_code);

	}

	@Test 
	public void shouldGet405ForDeleteRequest() {

		Map<String, Object>  jsonAsMap = new HashMap<>();
		jsonAsMap.put("What I Want", "a 405");

		given()
		.expect().statusCode(405)
		.when()
		.delete(api_url+gb+"/"+london_post_code + "/slug");

	}

}


