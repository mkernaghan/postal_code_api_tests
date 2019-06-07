package com.mk.postcode;

import static org.junit.Assert.assertEquals;

import java.util.List;

import io.restassured.response.Response;

public class methods {
	
	static void assertLatitudeFormatIsFloat(Response response) {
		List<String> latitudes = response.jsonPath().get("places['latitude']"); 

		//Latitudes range from -90.000 to 90.0000
		String floatRegexp="^([+-]?(\\d+\\.)?\\d+)$";
		
		int finds = 0;
		for (int i = 0; i < latitudes.size(); i++) { 

			String x = latitudes.get(i); 
			boolean isFound = x.matches(floatRegexp);
			if (isFound) {
				finds = finds + 1;
			};

		}
		//Every latitude returned by get matches regex
		assertEquals( finds,latitudes.size());
	}
	
	static void assertLongitudeFormatIsFloat(Response response) {
		List<String> longitudes = response.jsonPath().get("places['longitude']"); 

		//Longitudes range from -180.000 to 180.0000
		String floatRegexp="^([+-]?(\\d+\\.)?\\d+)$";
		
		int finds = 0;
		for (int i = 0; i < longitudes.size(); i++) { 

			String x = longitudes.get(i); 
			boolean isFound = x.matches(floatRegexp);
			if (isFound) {
				finds = finds + 1;
			};

		}

		//Every longitude returned by get matches regex
		assertEquals( finds,longitudes.size());
	}

}
