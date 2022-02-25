/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import com.mycompany.centralserver.resources.ContextSingleton;
import entities.Filijala;
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
import javax.ws.rs.core.Response;

/**
 *
 * @author Ivan
 */
@Path("filijala")
public class FilijalaResource {
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
    @Path("kreiranje/{naziv}/{adresa}/{idm}")
    public Response kreiranjeFilijale_2(@PathParam("naziv") String Naziv, @PathParam("adresa") String Adresa, @PathParam("idm") int IdM) {
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.KREIRANJE_FILIJALE_U_MESTU);
            request.setStringProperty("Naziv", Naziv);
            request.setStringProperty("Adresa", Adresa);
            request.setIntProperty("IdM", IdM);
            request.setJMSReplyTo(central_queue);
            
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
    @Produces("text/plain")
    @Path("sve")
    public Response dohvatiSveFilijale11() {
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.DOHVATANJE_SVIH_FILIJALA);
            request.setJMSReplyTo(central_queue);
            
            producer.send(s1_queue, request);
            Message reply = consumer.receive();
            
            ObjectMessage objMsg = (ObjectMessage) reply;
            List f_list = (List) objMsg.getObject();
            
            List<Filijala> filijale = f_list;
            StringBuilder sb = new StringBuilder();
            for (Filijala filijala : filijale) {
                sb.append(filijala.toString()).append("\n");
            }

            return Response.ok().entity(sb.toString()).build();
        } catch (JMSException ex) {
            Logger.getLogger(KomitentResource.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
        }
        
        return Response.ok(message).build();
    }
}
