package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {

    @BeforeClass
    void Setup(){
        baseURI="https://gorest.co.in/public/v2/";
    }
    public String getRandomName(){
        return RandomStringUtils.randomAlphabetic(6);
    }
    public String getRandomEmail(){
        return RandomStringUtils.randomAlphabetic(8).toLowerCase()+"@gnail.com";
    }

    @Test(enabled = false)
    public void createUser(){
        int userID=
        given()
               // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")
                .contentType(ContentType.JSON)
                .body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")

                .when()
                .post("users")

                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id");
        ;
        System.out.println("userID = " + userID);

    }
    @Test(enabled = false)
    public void createUserMap(){
        Map<String,String> newUser=new HashMap<>();
        newUser.put("name",getRandomName());
        newUser.put("gender","male");
        newUser.put("email",getRandomEmail());
        newUser.put("status","active");


        int userID=
                given()
                        // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                        .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        //.log().body()

                        .when()
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
        ;
        System.out.println("userID = " + userID);

    }
    int userID=0;
    @Test
    public void createUserObject(){
       User newUser=new User();
       newUser.setName(getRandomName());
       newUser.setGender("male");
       newUser.setEmail(getRandomEmail());
       newUser.setStatus("active");

         userID=
                given()
                        // api metoduna gitmeden önceki hazırlıklar : token , gidecek body , parametreleri
                        .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")
                        .contentType(ContentType.JSON)
                        .body(newUser)
                        //.log().body()

                        .when()
                        .post("users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.extract().path("id");
                        .extract().jsonPath().getInt("id")
                // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
                // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
        ;
        System.out.println("userID = " + userID);

    }
    @Test(dependsOnMethods = "createUserObject",priority = 1)
    public void updateUserObject(){

        Map<String,String> updateUser=new HashMap<>();
        updateUser.put("name","selim545454");

                given()
                        .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")
                        .contentType(ContentType.JSON)
                        .body(updateUser)
                        .log().body()
                        .pathParam("userID",userID)

                        .when()
                        .put("users/{userID}")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("name",equalTo("selim545454"))
                ;

    }
    @Test(dependsOnMethods = "createUserObject",priority = 2)
    public void getUserByID(){

        given()
                .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID",userID)

                .when()
                .get("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .body("id",equalTo(userID))
        ;

    }
    @Test(dependsOnMethods = "createUserObject",priority = 3)
    public void deleteUserByID(){

        given()
                .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID",userID)

                .when()
                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(204)
        ;

    }
    @Test(dependsOnMethods = "deleteUserByID")
    public void deleteUserByIDnegative(){

        given()
                .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")
                .contentType(ContentType.JSON)
                .log().body()
                .pathParam("userID", userID)

                .when()
                .delete("users/{userID}")

                .then()
                .log().body()
                .statusCode(404)
        ;

    }
    @Test
    public void getUsers(){

        Response response=
        given()
                .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")

                .when()
                .get("users")

                .then()
                .log().body()
                .statusCode(200)
                .extract().response()
        ;
        // TODO : 3 usersın id sini alınız , path ve jsonpath ile yapılacak
        int idUser3path=response.path("[2].id");
        int idUser3Jsonpath=response.jsonPath().getInt("[2].id");
        System.out.println("idUser3path = " + idUser3path); // dönüşüm yapılmadan geldi , her ikisi de sonucu veriyor
        System.out.println("idUser3Jsonpath = " + idUser3Jsonpath); // int dönüşümü yapılarak geldi , her ikisi de sonucu veriyor
        // TODO : tüm gelen veriyi bir nesneye atınız
        User[] usersPath=response.as(User[].class);
        System.out.println("Arrays.toString(usersPath) = " + Arrays.toString(usersPath));

        List<User> usersJsonPath=response.jsonPath().getList("",User.class);
        System.out.println("usersJsonPath = " + usersJsonPath);

        // TODO : get userByID testinde dönen userı bir nesneye atınız
        // altta çözümü var
    }

    @Test
    public void getUserByIDExtract(){

        // TODO : get userByID testinde dönen userı bir nesneye atınız

        User user=
        given()
                .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")
                .contentType(ContentType.JSON)
                .pathParam("userID",3587)

                .when()
                .get("users/{userID}")

                .then()
                .log().body()
                .statusCode(200)
                //.extract().as(User.class)
                .extract().jsonPath().getObject("",User.class) // json path hali
        ;
        System.out.println("user = " + user);

    }
    @Test
    public void getUsersV1(){

        Response response=
                given()
                        .header("Authorization","Bearer 9c67ce3d088bad84df6716e216e413bb005fce5fc717b16c50f66e7c43d86df3")

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response();
        // response.as(); // tüm gelen response uygun nesnelerin için tüm classların yapılması gerekiyor.
        List<User> dataUsers=response.jsonPath().getList("data",User.class);
        // jsonpath response içindeki bir parçayı nesneye dönüştürebiliriz
        System.out.println("dataUsers = " + dataUsers);

        // Daha önceki örneklerde Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.




    }

}

class User{
    private int id;
    private String name;
    private String gender;
    private String email;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
