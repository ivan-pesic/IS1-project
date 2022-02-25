package com.mycompany.centralservertest.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import message.ReturnMessage;
import resources.Codes;

/**
 *
 * @author 
 */
@Path("test")
public class JavaEE8Resource {
    
    @GET
    public Response ping(){
        ReturnMessage mojObjekat = new ReturnMessage(Codes.OK);
        mojObjekat.setMessage("Hello world!");
        return Response
                .ok(mojObjekat)
                .build();
    }
}
