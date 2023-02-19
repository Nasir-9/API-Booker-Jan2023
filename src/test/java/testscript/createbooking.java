package testscript;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class createbooking {
	@Test
	public void createbooking() {
		RestAssured.baseURI="https://restful-booker.herokuapp.com";
		Response res=RestAssured.given()
		.headers("Content-Type","application/json")
		.body("{\r\n"
		+ 
				"    \"username\" : \"admin\",\r\n" + 
				"    \"password\" : \"password123\"\r\n" + 
				"}")
		.when()
		.post("/auth");
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(),200);
		System.out.println(res.asPrettyString());
	}
	
	
}
