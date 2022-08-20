package com.eezywards.user;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.eezywards.user.db.DBConnectionUser;
import com.microsoft.azure.functions.*;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.*;



/**
 * Azure Functions with HTTP Trigger.
 */
public class SignUp {
    /**
     * This function listens at endpoint "/api/SignUp". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/SignUp
     * 2. curl {your host}/api/SignUp?name=HTTP%20Query
     */
    @FunctionName("SignUp")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");
        JSONObject reqj=new JSONObject(request.getBody().get());

        String email=reqj.getString("email");
        String ethAddress = reqj.getString("ethAddress");
        JSONObject verificationResponse = reqj.getJSONObject("verificationResponse");

        JSONObject verifyIsHuman = makeVerifyRequest(verificationResponse, context);

        if(verifyIsHuman.getString("success").equals("true")){
            String nullifier_hash = verifyIsHuman.getString("nullifier_hash");
            DBConnectionUser db = new DBConnectionUser();
            try{
                db.createAccountUser(email, ethAddress,nullifier_hash);
            }catch(Exception e){
                context.getLogger().info("Error: " + e.getMessage());
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage()).build();
            }
            return request.createResponseBuilder(HttpStatus.OK).body("{\"status\":\"success\"}").build();
        }


        // Parse query parameter
        return request.createResponseBuilder(HttpStatus.OK).body("Java HTTP trigger processed a request.").build();
       
    }

    public JSONObject makeVerifyRequest(JSONObject verificationResponse, final ExecutionContext context) {
        try{
        //make http request
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://developer.worldcoin.org/api/v1/verify");
            httpPost.setHeader("Content-Type", "application/json");
            
            httpPost.setEntity(new StringEntity(verificationResponse.toString()));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            context.getLogger().info("Response Code : " + response.getStatusLine().getStatusCode());
            context.getLogger().info("Response Message : " + response.getStatusLine().getReasonPhrase());
            context.getLogger().info("Response Body : " + response.getEntity().getContent());
            return new JSONObject(response.getEntity().getContent());
        }catch(Exception e){
            context.getLogger().info("Error: " + e.getMessage());
        }
        return new JSONObject();
    }
}
