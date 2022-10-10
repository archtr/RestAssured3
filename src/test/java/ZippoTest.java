import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import io.restassured.specification.RedirectSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test(){
        given()
                // hazırlık işlemlerini yapacağız . token , send , body , parametreler
                .when()
                // link ve metodu veriyoruz
                .then();
                // assertion ve verileri ele alma extract
    }

    @Test
    public void statusCodeTest(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   // log().all() bütün respons u gösterir
                .statusCode(200) // statüs kontrolü
        ;
    }

    @Test
    public void contentTypeTest(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()   // log().all() bütün respons u gösterir
                .statusCode(200) // statüs kontrolü
                .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void checkStateInResponseBody(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country",equalTo("United States")) // body.country==United States ?
                .statusCode(200) // statüs kontrolü
        ;
    }
    @Test
    public void bodyJsonPathTest2(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state",equalTo("California")) // 0. elemanın state i california mı ?
                .statusCode(200) // statüs kontrolü
        ;
    }
    @Test
    public void bodyJsonPathTest3(){
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body()
                .body("places.'place name'",hasItem("Çaputçu Köyü")) // place name lerde Çaputçu Köyü var mı ? ,
                // bir index verilmezse dizinin bütün elemanlarında arar
                .statusCode(200) // statüs kontrolü
                // "places.'place name'",hasItem("Çaputçu Köyü") bu item e sahip mi ?
        ;
    }
    @Test
    public void bodyArrayHasSizeTest(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places",hasSize(1)) //
                .statusCode(200) // statüs kontrolü
        ;
    }
    @Test
    public void combiningTest(){
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places",hasSize(1)) //
                .body("places.state",hasItem("California"))
                .body("places[0].'place name'",equalTo("Beverly Hills"))
                .statusCode(200) // statüs kontrolü
        ;
    }

    @Test
    public void pathParamTest(){
        given()
                .pathParam("Country","us")
                .pathParam("ZipKod","90210")
                .log().uri()


                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                .then()
                .log().body()

                .statusCode(200) // statüs kontrolü
        ;
    }
    @Test
    public void pathParamTest2(){
        // 90210 dan 90213 ye kadar test sonuçlarında places size nın hepsinde 1 geldiğini test ediniz
        for(int i=90210;i<90213;i++){
        given()
                .pathParam("Country","us")
                .pathParam("ZipKod",i)
                .log().uri()


                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipKod}")

                .then()
                .log().body()
                .body("places",hasSize(1))
                .statusCode(200) // statüs kontrolü
        ;
        }
    }
    @Test
    public void queryParamTest(){
            given() // 1. sayfa da sayfa numarası 1 mi diye sorgulama
                    .param("page",1) // 1. page üzerinde arama
                    .log().uri()// request linki

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page",equalTo(1)) // sayfa 1 mi ? page==1 mi ?
                    .statusCode(200) // statüs kontrolü
            ;
    }
    @Test
    public void queryParamTest2(){
        for (int i=1;i<11;i++){ // 10 sayfayı da sayfa numarası doğru mu diye kontrol etme
        given()
                .param("page",i) //  page i üzerinde arama
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .body("meta.pagination.page", equalTo(i)) // sayfa i mi ? page==i mi ?
                .statusCode(200) // statüs kontrolü
        ;
        }
    }

    RequestSpecification requestSpecs;
    ResponseSpecification responseSpecs;

    @BeforeClass
    void Setup(){
        baseURI = "https://gorest.co.in/public/v1";
        requestSpecs=new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setAccept(ContentType.JSON)
                .build();

        responseSpecs=new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();

    }
    @Test
    public void requestResponseSpecification(){

            given()
                    .param("page",1) //  page i üzerinde arama
                    .spec(requestSpecs)

                    .when()
                    .get("/users") // url nin başına http yoksa baseuri deki değer otomatik gelir

                    .then()
                    .body("meta.pagination.page", equalTo(1)) // sayfa i mi ? page==i mi ?
                    .spec(responseSpecs)
            ;
        }

    @Test
    public void extractingJsonPath(){
        String placeName=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                // .log().body()
                .statusCode(200) // statüs kontrolü
                .extract().path("places[0].'place name'") // en son yazılmalı yoksa sonrakiler hata verir
                // extract metodu ile given ile başlayan satır , bir değer döndürür hale geldi , en sonda extract olmalı
        ;
        System.out.println("placeName = " + placeName);
    }
    @Test
    public void extractingJsonPathInt(){

        int limit=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        // .log().body()
                        .statusCode(200) // statüs kontrolü
                        .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);
        Assert.assertEquals(limit,10,"test sonucu");

    }

    @Test
    public void extractingJsonPathInt2(){

        int id=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        // .log().body()
                        .statusCode(200) // statüs kontrolü
                        .extract().path("data[2].id")
                ;
        System.out.println("id = " + id);
    }

    @Test
    public void extractingJsonPathIntList(){

        List<Integer> idler=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        // .log().body()
                        .statusCode(200) // statüs kontrolü
                        .extract().path("data.id") // data daki bütün id leri bir list şeklinde verir
                ;
        System.out.println("idler = " + idler);
        Assert.assertTrue(idler.contains(3045));
    }
    @Test
    public void extractingJsonPathIntList2(){

        List<String> nameler=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        // .log().body()
                        .statusCode(200) // statüs kontrolü
                        .extract().path("data.name") // data daki bütün id leri bir list şeklinde verir
                ;
        System.out.println("nameler = " + nameler);
        Assert.assertTrue(nameler.contains("Navin Patil"));
    }

    @Test
    public void extractingJsonPathResponseAll(){

        Response response=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        // .log().body()
                        .statusCode(200) // statüs kontrolü
                        .extract().response() // bütün body alındı
                ;
        List<Integer> idler=response.path("data.id");
        List<String> nameler=response.path("data.name");
        int limit=response.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("nameler = " + nameler);
        System.out.println("idler = " + idler);
    }

    @Test
    public void extractingJsonPOJO(){ // POJO : Json objecti Plain Old Java Object
        Location yer=
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        // .log().body()
                        .statusCode(200) // statüs kontrolü
                        .extract().as(Location.class); // location şablonu
                ;
        System.out.println("yer = " + yer);
        System.out.println("yer.getCountry() = " + yer.getCountry());
        System.out.println("yer.getPlaces().get(0).getPlacename() = " + yer.getPlaces().get(0).getPlacename());
    }




}




