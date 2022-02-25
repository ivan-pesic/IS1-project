/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import com.mycompany.centralserver.resources.ContextSingleton;
import entities.Transakcija;
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

/**
 *
 * @author Ivan
 */

@Path("transakcija")
public class TransakcijaResource {
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
    @Path("prenos/{idR1}/{idR2}/{iznos}/{svrha}")
    public Response prenosNovcaSaNa_7(@PathParam("idR1") int IdR1, @PathParam("idR2") int IdR2,
            @PathParam("iznos") double Iznos, @PathParam("svrha") String Svrha) {
        /*
         IdR1 = message.getIntProperty("IdR1");
            IdR2 = message.getIntProperty("IdR2");
            Iznos = message.getDoubleProperty("Iznos");
            Svrha = message.getStringProperty("Svrha");
        */
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.KREIRANJE_TRANSAKCIJE_PRENOS);
            request.setIntProperty("IdR1", IdR1);
            request.setIntProperty("IdR2", IdR2);
            request.setDoubleProperty("Iznos", Iznos);
            request.setStringProperty("Svrha", Svrha);
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
    @Path("uplata/{idR}/{idF}/{iznos}/{svrha}")
    public Response uplataNaRacun_8(@PathParam("idR") int IdR, @PathParam("idF") int IdF,
            @PathParam("iznos") double Iznos, @PathParam("svrha") String Svrha) {
        /*
                + "\t1. IdR: " + IdR
                + "\t2. IdF: " + IdF
                + "\t3. Iznos: " + Iznos
                + "\t4. Svrha: " + Svrha;
        */
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.KREIRANJE_TRANSAKCIJE_UPLATA);
            request.setIntProperty("IdR", IdR);
            request.setIntProperty("IdF", IdF);
            request.setDoubleProperty("Iznos", Iznos);
            request.setStringProperty("Svrha", Svrha);
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
    @Path("isplata/{idR}/{idF}/{iznos}/{svrha}")
    public Response isplataSaRacuna_9(@PathParam("idR") int IdR, @PathParam("idF") int IdF,
            @PathParam("iznos") double Iznos, @PathParam("svrha") String Svrha) {
        /*
                + "\t1. IdR: " + IdR
                + "\t2. IdF: " + IdF
                + "\t3. Iznos: " + Iznos
                + "\t4. Svrha: " + Svrha;
        */
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.KREIRANJE_TRANSAKCIJE_ISPLATA);
            request.setIntProperty("IdR", IdR);
            request.setIntProperty("IdF", IdF);
            request.setDoubleProperty("Iznos", Iznos);
            request.setStringProperty("Svrha", Svrha);
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sve/{idr}")
    public Response dohvatiSveTransakcijeZaRacun_14(@PathParam("idr") int IdR) {
        String message = null;
        try {
            JMSContext context = ContextSingleton.getContext(connectionFactory);
            Message request = context.createMessage();
            JMSConsumer consumer = context.createConsumer(central_queue);
            JMSProducer producer = context.createProducer();
            
            request.setIntProperty("Tip", Codes.DOHVATANJE_SVIH_TRANSAKCIJA_ZA_RACUN);
            request.setIntProperty("IdR", IdR);
            request.setJMSReplyTo(central_queue);
            
            producer.send(s2_queue, request);
            Message reply = consumer.receive();
            
            ObjectMessage objMsg = (ObjectMessage) reply;
            List t_list = (List) objMsg.getObject();
            
            List<Transakcija> transakcije = t_list;

            return Response.ok().entity(transakcije).build();
        } catch (JMSException ex) {
            Logger.getLogger(KomitentResource.class.getName()).log(Level.SEVERE, null, ex);
            message = ex.getMessage();
        }
        
        return Response.ok(message).build();
    }
}
