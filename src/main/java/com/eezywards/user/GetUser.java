package com.eezywards.user;

import java.util.*;

import org.json.JSONObject;

import com.microsoft.azure.functions.annotation.*;
import com.eezywards.user.db.DBConnectionUser;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class GetUser {
    /**
     * This function listens at endpoint "/api/GetUser". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/GetUser
     * 2. curl {your host}/api/GetUser?name=HTTP%20Query
     */
    @FunctionName("getUser")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("GetUser.java");

        DBConnectionUser db = new DBConnectionUser();

        String ethAddress = request.getQueryParameters().get("ethAddress");

        JSONObject toRet = new JSONObject();
        try{
            toRet = db.getUser(ethAddress);
            
        }catch(Exception e){
            context.getLogger().info("Error: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage()).build();
        }

        return request.createResponseBuilder(HttpStatus.OK).body(toRet.toString()).build();
    }
}
