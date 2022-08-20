package com.eezywards.user;

import java.util.*;

import org.json.JSONObject;

import com.microsoft.azure.functions.annotation.*;
import com.eezywards.user.db.DBConnectionUser;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Transaction {
    /**
     * This function listens at endpoint "/api/Transaction". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/Transaction
     * 2. curl {your host}/api/Transaction?name=HTTP%20Query
     */
    @FunctionName("transaction")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        JSONObject jsonr = new JSONObject(request.getBody().get());
        String ethAddress = jsonr.getString("ethAddress");
        String amount = jsonr.get("amount")+"";
        String businessId = jsonr.getString("businessId");

        DBConnectionUser db = new DBConnectionUser();
        try{
            db.saveTransaction(ethAddress, amount,businessId);
            return request.createResponseBuilder(HttpStatus.OK).body("{\"success\": true}").build();
        }catch(Exception e){
            context.getLogger().info("Error: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage()).build();
        }

    }
}
