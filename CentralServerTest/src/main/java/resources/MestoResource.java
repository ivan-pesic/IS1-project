/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import com.mycompany.centralserver.resources.ContextSingleton;
import entities.Mesto;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import message.ReturnMessage;

/**
 *
 * @author Ivan
 */
@Path("mesto")
public class MestoResource {
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
    @Produces("text/plain")
    @Path("kreiranje/{naziv}/{pb}")
    public Response kreiranjeMesta_1(@PathParam("naziv") String Naziv, @PathParam("pb") String PB) {
        String message = null;
        int code = Codes.NOT_OK;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.KREIRANJE_MESTA);
            request.setStringProperty("Naziv", Naziv);
            request.setStringProperty("PB", PB);
            request.setJMSReplyTo(central_queue);
            
            producer.send(s1_queue, request);
            Message reply = consumer.receive();
            code = reply.getIntProperty("Tip");
            message = reply.getStringProperty("Poruka");
        } catch (JMSException ex) {
            Logger.getLogger(KomitentResource.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
        }
        
        return Response.ok(message).build();
    }
    
    @GET
    @Produces("text/plain")
    @Path("sve")
    public Response dohvatiSvаMesta10() {
        String message = null;
        int code = Codes.NOT_OK;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.DOHVATANJE_SVIH_MESTA);
            request.setJMSReplyTo(central_queue);
            
            producer.send(s1_queue, request);
            Message reply = consumer.receive();
            
            code = reply.getIntProperty("Tip");
            
            ObjectMessage objMsg = (ObjectMessage) reply;
            List m_list = (List) objMsg.getObject();
            
            ArrayList<Mesto> mesta = (ArrayList<Mesto>) m_list;
            StringBuilder sb = new StringBuilder();
            for (Mesto mesto : mesta) {
                sb.append(mesto.toString()).append("\n");
            }
            message = sb.toString();
            return Response.ok().entity(message).build();
        } catch (JMSException ex) {
            Logger.getLogger(KomitentResource.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
        }
        ReturnMessage returnMessage = new ReturnMessage(code);
        returnMessage.setMessage(message);
        return Response.ok(returnMessage).build();
    }
}
