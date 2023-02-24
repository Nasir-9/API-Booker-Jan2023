package testscript;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import constant.Status_code;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.request.Bookingdates;
import pojo.request.CreateBooking;

public class createbooking {
	int bookingId;
	CreateBooking payload;
	String token;
	@BeforeMethod
	public void generateToken() {
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
		Assert.assertEquals(res.statusCode(),Status_code.ok);
		System.out.println(res.asPrettyString());
		token=res.jsonPath().getString("token");
		System.out.println(token);
		
	}
	@Test(enabled=false)
	public void createBooking() {
		
		Response res=RestAssured.given()
			.headers("Content-Type","application/json")
			.headers("Accept","application/json")
			.body("{\r\n" + 
					"    \"firstname\" : \"Jim\",\r\n" + 
					"    \"lastname\" : \"Brown\",\r\n" + 
					"    \"totalprice\" : 111,\r\n" + 
					"    \"depositpaid\" : true,\r\n" + 
					"    \"bookingdates\" : {\r\n" + 
					"        \"checkin\" : \"2018-01-01\",\r\n" + 
					"        \"checkout\" : \"2019-01-01\"\r\n" + 
					"    },\r\n" + 
					"    \"additionalneeds\" : \"Breakfast\"\r\n" + 
					"}")
			.when()
			.post("/booking");
			
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(),200);
	}
	@Test
	public void createBookingWithPojo() {
		Bookingdates booking = new Bookingdates();
		booking.setCheckin("2018-01-01");
		booking.setCheckout("2019-01-01");
		payload=new CreateBooking();
		payload.setFirstname("Nasir");
		payload.setLastname("Ahmed");
		payload.setTotalprice(100);
		payload.setDepositpaid(true);
		payload.setAdditionalneeds("Lunch");
		payload.setBookingdates(booking);	
		Response res = RestAssured.given()
			.headers("Content-Type","application/json")
			.headers("Accept","application/json")
			.body(payload)
			.log().all()
			.when()
			.post("/booking");	
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(),200);
		 bookingId=res.jsonPath().getInt("bookingid");
		System.out.println(res.asPrettyString());
		validateResponse(res, payload, "booking.");
		
}
	@Test(priority=1,enabled=false)
	public void getAllBookingTest() {
		Response res=RestAssured.given()
				.headers("Accept","application/json")	
				.log().all()
				.when()
				.get("/booking");
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(), 200);
		List<Integer>listofbooking=res.jsonPath().getList("bookingid");
		System.out.println(listofbooking.size());
		Assert.assertTrue(listofbooking.contains(bookingId));
	}
	@Test(priority=2,enabled=false)
	public void getBookingIdest() {
		Response res=RestAssured.given()
				.headers("Accept","application/json")	
				//.log().all()
				.when()
				.get("/booking/"+bookingId);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(), 200);
		validateResponse(res, payload, "");
	
}
	@Test(priority=3,enabled=false)
	public void getBookingIdDeserialization() {
		Response res=RestAssured.given()
				.headers("Accept","application/json")	
				//.log().all()
				.when()
				.get("/booking/"+bookingId);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(), 200);
		CreateBooking responseBody=res.as(CreateBooking.class);
		Assert.assertEquals(payload.firstname, responseBody.firstname);
		Assert.assertTrue(responseBody.equals(payload));
	
	}
	@Test(priority=4,enabled=false)
	public void updateBookingId() {
		payload.setFirstname("Nisha");
		Response res=RestAssured.given()
				.headers("Content-Type","application/json")
				.headers("Accept","application/json")
				.headers("Cookie","token="+token)
				.body(payload)
				.log().all()
				.when()
				.put("/booking/"+bookingId);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(), 200);
		CreateBooking responseBody=res.as(CreateBooking.class);
		Assert.assertEquals(payload.firstname, responseBody.firstname);
		Assert.assertTrue(responseBody.equals(payload));
	}
	
	@Test(priority=5,enabled=false)
	public void getUpdateBookingIdest() {
		Response res=RestAssured.given()
				.headers("Accept","application/json")	
				//.log().all()
				.when()
				.get("/booking/"+bookingId);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(), 200);
		validateResponse(res, payload, "");
	}
	@Test(priority=6,enabled=false)
	public void partialUpdateBooking() {
		payload.setFirstname("Sikha");
		payload.setLastname("Yadav");
		String partialpayload="{\r\n" + 
				"    \"firstname\" : \""+payload.getFirstname()+"\",\r\n" + 
				"    \"lastname\" : \""+payload.getLastname()+"\"\r\n" + 
				"}";
		
		Response res=RestAssured.given()
				.headers("Content-Type","application/json")
				.headers("Accept","application/json")
				.headers("Cookie","token="+token)
				.body(partialpayload)
				.log().all()
				.when()
				.patch("/booking/"+bookingId);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(),Status_code.ok);
		CreateBooking responseBody=res.as(CreateBooking.class);
		//Assert.assertEquals(partialpayload.firstname, responseBody.firstname);
		Assert.assertTrue(responseBody.equals(payload));
	}
	@Test(priority=7)
	public void deleteBookingId() {
		Response res=RestAssured.given()
				.headers("Content-Type","application/json")
				.headers("Cookie","token="+token)
				.when()
				.delete("/booking/"+bookingId);
		System.out.println(res.asPrettyString());
		Assert.assertEquals(res.getStatusCode(),Status_code.delete);
		//CreateBooking responseBody=res.as(CreateBooking.class);
		//Assert.assertEquals(partialpayload.firstname, responseBody.firstname);
		//Assert.assertTrue(responseBody.equals(payload));
	}
	@Test(priority=8)
	public void getDeleteBookingId() {
		Response res=RestAssured.given()
				.headers("Accept","application/json")	
				//.log().all()
				.when()
				.get("/booking/"+bookingId);
		Assert.assertEquals(res.getStatusCode(),Status_code.notFound);
	
	}
	
	private void validateResponse(Response res,CreateBooking payload, String object) {
		Assert.assertEquals(res.jsonPath().getString(object+"firstname"),payload.getFirstname());
		Assert.assertEquals(res.jsonPath().getString(object+"lastname"),payload.getLastname());
		Assert.assertEquals(res.jsonPath().getInt(object+"totalprice"),payload.getTotalprice());
		Assert.assertEquals(res.jsonPath().getBoolean(object+"depositpaid"),payload.depositpaid);
		Assert.assertEquals(res.jsonPath().getString(object+"additionalneeds"),payload.getAdditionalneeds());
		Assert.assertEquals(res.jsonPath().getString(object+"bookingdates.checkin"),payload.getBookingdates().getCheckin());
		Assert.assertEquals(res.jsonPath().getString(object+"bookingdates.checkout"),payload.getBookingdates().getCheckout());
	}
}
