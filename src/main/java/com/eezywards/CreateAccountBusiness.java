package com.eezywards;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import org.json.*;
import com.eezywards.db.DBConnectionM;
/**
 * Azure Functions with HTTP Trigger.
 */
public class CreateAccountBusiness {
    /**
     * This function listens at endpoint "/api/createAccountBusiness". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/createAccountBusiness
     * 2. curl {your host}/api/createAccountBusiness?name=HTTP%20Query
     */
    @FunctionName("createAccountBusiness")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter


        JSONObject reqj = new JSONObject(request.getBody().orElse("{}"));
        String businessName = reqj.getString("businessName");
        String businessEmail = reqj.getString("businessEmail");
        String ethAddress = reqj.getString("ethAddress");
        
        //save to db 
        DBConnectionM db = new DBConnectionM();
        try{
            db.createAccountBusiness(businessName, businessEmail, ethAddress);
        }catch(Exception e){
            context.getLogger().info("Error: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage()).build();
        }
        return request.createResponseBuilder(HttpStatus.OK).body("{\"status\":\"success\"}").build();


        
    }
}
