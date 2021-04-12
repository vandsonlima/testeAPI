package com.thecat;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class TesteApi {

    private String vote_id;

    @BeforeAll
    public static void urlbase() {
        RestAssured.baseURI = "https://api.thecatapi.com/v1/";
    }

    @Test
    public void cadastro(){
        String url = "http://api.thecatapi.com/v1/user/passwordlesssignup";
        String corpo = "{\"email\": \"vandson.vslima@gmail.com\", \"appDescription\": \"teste the cat api\"}";

        //estrutura do teste

        //given (dado que)
           Response response = given().contentType("application/json").body(corpo)
                    .when().post(url);
        //VALIDAÇÃO
        response.then().body("message", containsString("SUCCESS")).statusCode(200);

        System.out.println("RETORNO =>" + response.body().asString());

        //then (então)
    }

    @Test
    public void votar(){
        String url = "http://api.thecatapi.com/v1/votes/";

        Response response = given().contentType("application/json").body("{\"image_id\": \"61h\", \"value\": \"true\", \"sub_id\": \"demo-c26f9f\"}")
                .when().post(url);

        response.then().statusCode(HttpStatus.SC_OK).body("message", containsString("SUCCESS"));

        //Recuperar dados do json de resposta
        vote_id = response.jsonPath().getString("id");
    }

    public void deleteVote(){
        String url = "http://api.thecatapi.com/v1/votes/{vote_id}";

        Response response =
                given()
                .contentType("application/json")
                .header("x-api-key","69b58f6c-2d19-456e-9676-bfda4841fe67")
                .pathParam("vote_id", vote_id)
                .when()
                .delete(url);

        System.out.println("RETORNO => " + response.body().asString());

        response.then().statusCode(HttpStatus.SC_OK).body("message", containsString("SUCCESS"));
    }

    @Test
    @DisplayName("Cria e deleta em seguida a votação criada")
    public void deletaVotacao(){
        votar();
        deleteVote();
    }
}
