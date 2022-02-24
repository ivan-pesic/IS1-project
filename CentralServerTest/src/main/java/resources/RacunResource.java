/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import com.mycompany.centralserver.resources.ContextSingleton;
import entities.Racun;
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
import javax.ws.rs.core.Response;

/**
 *
 * @author Ivan
 */
@Path("racun")
public class RacunResource {
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
    @Path("kreiranje/{idk}/{idm}/{dozvM}")
    public Response otvoriRacun_5(@PathParam("idk") int IdK, @PathParam("idm") int IdM, @PathParam("dozvM") double DozvMinus) {
        /*
            IdK = message.getIntProperty("IdK");
            IdM = message.getIntProperty("IdM");
            dozvMinus = message.getDoubleProperty("DozvMinus");
        */
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.OTVARANJE_RACUNA);
            request.setIntProperty("IdK", IdK);
            request.setIntProperty("IdM", IdM);
            request.setDoubleProperty("DozvMinus", DozvMinus);
            request.setJMSReplyTo(central_queue);
            
            producer.send(s2_queue, request);
            Message reply = consumer.receive();
            
            message = reply.getStringProperty("Poruka");
        } catch (JMSException ex) {
            Logger.getLogger(KomitentResource.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
        }
        
        return Response.ok(message).build();
    }
    
    @POST
    @Path("zatvaranje/{idr}")
    public Response zatvoriRacun_6(@PathParam("idr") int IdR) {
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.ZATVARANJE_RACUNA);
            request.setIntProperty("IdR", IdR);
            request.setJMSReplyTo(central_queue);
            
            producer.send(s2_queue, request);
            Message reply = consumer.receive();
            
            message = reply.getStringProperty("Poruka");
        } catch (JMSException ex) {
            Logger.getLogger(KomitentResource.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
        }
        
        return Response.ok(message).build();
    }
    
    @GET
    @Path("sve/{idk}")
    public Response dohvatiSveRacuneZaKomitenta_13(@PathParam("idk") int IdK) {
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.DOHVATANJE_SVIH_RACUNA_ZA_KOMITENTA);
            request.setIntProperty("IdK", IdK);
            request.setJMSReplyTo(central_queue);
            
            producer.send(s2_queue, request);
            Message reply = consumer.receive();
            
            ObjectMessage objMsg = (ObjectMessage) reply;
            List r_list = (List) objMsg.getObject();
            
            List<Racun> racuni = r_list;

            return Response.ok().entity(racuni).build();
        } catch (JMSException ex) {
            Logger.getLogger(KomitentResource.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
        }
        
        return Response.ok(message).build();
    }
}
