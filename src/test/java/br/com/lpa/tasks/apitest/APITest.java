package br.com.lpa.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}

	@Test
	public void mustReturnTasks() {

		RestAssured.given().when().get("/todo").prettyPeek().then().statusCode(200);

	}

	@Test
	public void addTask() {

		String body = "{\"task\":\"Teste via API2\", \"dueDate\":\"2022-05-28\"}";

		RestAssured.given().contentType(ContentType.JSON).body(body).when().post("/todo").then().statusCode(201);

	}

	@Test
	public void shouldNotAddInvalidTask() {

		String body = "{\"task\":\"Teste via API - Erro\", \"dueDate\":\"2021-05-28\"}";

		RestAssured.given().contentType(ContentType.JSON).body(body).when().post("/todo").prettyPeek().then()
				.statusCode(400).body("message", CoreMatchers.is("Due date must not be in past"));
	}

	@Test
	public void removeTask() {
		String body = "{\"task\":\"Tarefa de remoção\", \"dueDate\":\"2022-07-28\"}";

		Integer id = RestAssured.given().contentType(ContentType.JSON).body(body)
					.when().post("/todo").then().log().all().statusCode(201).extract().path("id");
		
		RestAssured.given().when().delete("/todo/"+id).then().statusCode(204);

	}

}
