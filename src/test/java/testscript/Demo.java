package testscript;

import java.util.List;
import java.util.Map;

import org.hamcrest.core.StringStartsWith;
import org.testng.Assert;
import org.testng.annotations.Test;

import constant.Status_code;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Demo {
	
	@Test(enabled=false)
	public void getPhoneNumber() {
		RestAssured.baseURI="https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io";
		Response res=RestAssured.given()
			.headers("Content-Type","application/json")
			.when()
			.get("/test");
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(),Status_code.ok);
		System.out.println(res.jsonPath().getString("phoneNumbers"));
		
	}
	@Test
	public void getPhoneNumberType() {
		RestAssured.baseURI="https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io";
		Response res=RestAssured.given()
			.headers("Content-Type","application/json")
			.when()
			.get("/test");
		//System.out.println(res.asPrettyString());
		//Assert.assertEquals(res.getStatusCode(),Status_code.ok);
		//System.out.println(res.jsonPath().getString("phoneNumbers.number"));
		List<Object>listOfNumber=res.jsonPath().getList("phoneNumbers");
		System.out.println(listOfNumber);
		System.out.println(listOfNumber.size());
		Map<String, String> mapOfList = (Map<String, String>)listOfNumber.get(0);
		//System.out.println(mapOfList.get("type")+"--"+mapOfList.get("number"));
		for(Object obj:listOfNumber) {
		if(mapOfList.get("type").equals("iPhone")) 
			Assert.assertTrue(mapOfList.get("number").startsWith("3456"));
			else if (mapOfList.get("type").equals("home"))
				Assert.assertTrue(mapOfList.get("number").startsWith("9876"));
		}	}	
	}

