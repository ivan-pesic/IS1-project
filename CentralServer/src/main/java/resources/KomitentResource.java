/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import com.mycompany.centralserver.resources.ContextSingleton;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author Ivan
 */
@Path("komitent")
public class KomitentResource {
    @Resource(lookup="projectConnectionFactory")
    ConnectionFactory connectionFactory;
    
    @Resource(lookup="subsystem1_queue")
    Queue s1_queue;
    
    @Resource(lookup="subsystem2_queue")
    Queue s2_queue;

    @Resource(lookup="subsystem3_queue")
    Queue s3_queue;
    
    @Resource(lookup="cs_queue")
    Queue central_queue;
    
    @POST
    @Path("{naziv}/{adresa}/{idm}")
    public Response kreiranjeKomitenta_3(@PathParam("naziv") String Naziv, @PathParam("adresa") String Adresa, @PathParam("idm") int IdM) {
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.KREIRANJE_KOMITENTA);
            request.setStringProperty("Naziv", Naziv);
            request.setStringProperty("Adresa", Adresa);
            request.setIntProperty("IdM", IdM);
            
            producer.send(s1_queue, request);
            Message reply = consumer.receive();
            
            message = reply.getStringProperty("Poruka");
        } catch (JMSException ex) {
            Logger.getLogger(KomitentResource.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
        }
        
        return Response.ok(message).build();
    }
    
    @GET
    @Path("test")
    public Response promenaSedistaKomitenta_4() {
        return Response.ok("SVE JE OK").build();
    }
}
