package com.eezywards;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;
import org.json.*;
import com.eezywards.db.DBConnectionM;


/**
 * Azure Functions with HTTP Trigger.
 */
public class UploadProducts {
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("uploadProducts")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        JSONObject reqj = new JSONObject(request.getBody().orElse("{}"));
        
        JSONArray products = reqj.getJSONArray("products");
        
        // for(int i=0;i<products.length();i++){
        //     JSONObject product = products.getJSONObject(i);
        //     context.getLogger().info("Product: " + product.toString());
        // }

        //connect to db and insert products mysql
        //insert into products (name,  price) values ('name',  'price',');
        DBConnectionM db = new DBConnectionM();
        try{
            db.insertProducts(products);
        }catch(Exception e){
            context.getLogger().info("Error: " + e.getMessage());
        }             

        return request.createResponseBuilder(HttpStatus.OK).body("").build();
    }
}
