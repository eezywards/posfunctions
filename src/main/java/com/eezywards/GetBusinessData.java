package com.eezywards;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import com.eezywards.db.DBConnectionM;
import org.json.*;


/**
 * Azure Functions with HTTP Trigger.
 */
public class GetBusinessData {
    /**
     * This function listens at endpoint "/api/GetBusinessData". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/GetBusinessData
     * 2. curl {your host}/api/GetBusinessData?name=HTTP%20Query
     */
    @FunctionName("GetBusinessData")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        DBConnectionM db = new DBConnectionM();
        JSONObject toRet = new JSONObject();
        try{
            JSONObject business = db.readAccountBusiness();
            toRet.put("business", business);
        }catch(Exception e){
            context.getLogger().info("Error: " + e.getMessage());
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage()).build();
        }
        return request.createResponseBuilder(HttpStatus.OK).body(toRet.toString()).build();

    }
}
