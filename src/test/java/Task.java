
import POJO.task1a;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Task {
    /** Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */

    @Test
    public void task1(){ // POJO : Json objecti Plain Old Java Object
        task1a task=
                given()

                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        // .log().body()
                        .statusCode(200) // statüs kontrolü
                        .extract().as(task1a.class);
        ;
        System.out.println("task = " + task);

    }
    /**
     * Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */
    @Test
    public void task2(){ // POJO : Json objecti Plain Old Java Object

                given()

                        .when()
                        .get("https://httpstat.us/203")
                        .then()
                        .log().body()
                        .statusCode(203) // statüs kontrolü
                        .contentType(ContentType.TEXT)
        ;

    }
    /**
     * Task 3
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
    @Test
    public void task3(){ // POJO : Json objecti Plain Old Java Object

        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200) // statüs kontrolü
                .contentType(ContentType.JSON)
                .body("title",equalTo("quis ut nam facilis et officia qui"))
        ;

    }
    /** Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * expect content type JSON
     * expect third item have:
     *      title = "fugiat veniam minus"
     *      userId = 1
     */




}
