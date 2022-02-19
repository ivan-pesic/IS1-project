package com.mycompany.centralserver.resources;

import com.mycompany.centralserver.JAXRSConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import omotaci.FilijalaOmotac;
import omotaci.KomitentOmotac;
import omotaci.MestoOmotac;
import poruke.Poruka;
import poruke.Tipovi;

/**
 *
 * @author 
 */

@Path("javaee8")
public class JavaEE8Resource {
    
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
    
    @GET
    @Path("mesto/{imeMesta}/{postBr}")
    public Response ping(@PathParam("imeMesta") String ime, @PathParam("postBr") String pb){
        String m = null;
        try {
                System.err.println("Pre kreiranja konteksta");
                JMSContext context = JMSResursi.getContext(connectionFactory);
                JMSConsumer consumer = context.createConsumer(central_queue);
                JMSProducer producer = context.createProducer();
                System.err.println("Prosao kreiranje konteksta");
                MestoOmotac mestoOmotac = new MestoOmotac(ime, pb);
                Poruka poruka = new Poruka(Tipovi.KREIRANJE_MESTA, mestoOmotac);
                ObjectMessage objMsg = context.createObjectMessage(poruka);
                
                objMsg.setIntProperty("TIP", Tipovi.KREIRANJE_MESTA);
                objMsg.setJMSReplyTo(central_queue);
                
                System.out.println("Priprema za slanje poruke: " + objMsg.toString());
                producer.send(s1_queue, objMsg);
                System.out.println("Poslata poruka: " + objMsg.toString());
                
                ObjectMessage msg = (ObjectMessage) consumer.receive();
                System.out.println("Primljena poruka" + msg.toString());
                Poruka p = (Poruka) msg.getObject();
                
                if(p.getStatus() != Tipovi.OK) {
                    m = "Mesto nije kreirano";
                } else {
                    m = "Uspesno kreirano mesto.";
                }
                
            } catch (JMSException ex) {}
        return Response.ok(m).build();
    }
    
    @GET
    @Path("test")
    public Response test() { 
        JMSConsumer consumer = JMSResursi.getContext(connectionFactory).createConsumer(central_queue);        
        Message receive = consumer.receive();
        return Response.ok("OOOOOOOOOOOOOOOOOOKKKKKKKKKKKKKKKK").build();
    }
    
    @GET
    @Path("aj")
    public Response ajde() {
        try {
            JMSContext context = JMSResursi.getContext(connectionFactory);
            JMSProducer producer = context.createProducer();
            JMSConsumer consumer = context.createConsumer(central_queue);
            
            ObjectMessage objMsg = context.createObjectMessage();
            objMsg.setIntProperty("id", 123);
            
            producer.send(central_queue, objMsg);
            
            ObjectMessage receive = (ObjectMessage) consumer.receive();
            
            
            return Response.ok(receive.getIntProperty("id")).build();
        } catch (JMSException ex) {
            Logger.getLogger(JavaEE8Resource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.ok("ajde").build();
    }
    
    @GET
    @Path("komitent/{imeKomitenta}/{adresa}/{idMesta}")
    public Response kom(@PathParam("imeKomitenta") String ime, @PathParam("adresa") String adresa, @PathParam("idMesta") int idMesta) {
        String m = null;
        try {
                System.err.println("Pre kreiranja konteksta");
                JMSContext context = JMSResursi.getContext(connectionFactory);
                JMSConsumer consumer = context.createConsumer(central_queue);
                JMSProducer producer = context.createProducer();
                System.err.println("Prosao kreiranje konteksta");
                KomitentOmotac komitentOmotac = new KomitentOmotac(ime, adresa, idMesta);
                Poruka poruka = new Poruka(Tipovi.KREIRANJE_KOMITENTA, komitentOmotac);
                ObjectMessage objMsg = context.createObjectMessage(poruka);
                
                objMsg.setIntProperty("TIP", Tipovi.KREIRANJE_KOMITENTA);
                objMsg.setJMSReplyTo(central_queue);
                
                System.out.println("Priprema za slanje poruke: " + objMsg.toString());
                producer.send(s1_queue, objMsg);
                System.out.println("Poslata poruka: " + objMsg.toString());
                
                ObjectMessage msg = (ObjectMessage) consumer.receive();
                System.out.println("Primljena poruka" + msg.toString());
                Poruka p = (Poruka) msg.getObject();
                
                if(p.getStatus() != Tipovi.OK) {
                    m = "Komitent nije kreiran";
                } else {
                    m = "Uspesno kreiran komitent.";
                }
                
            } catch (JMSException ex) {}
        return Response.ok(m).build();
    }
    
    @GET
    @Path("filijala/{imeFilijale}/{adresa}/{idMesta}")
    public Response fil(@PathParam("imeFilijale") String ime, @PathParam("adresa") String adresa, @PathParam("idMesta") int idMesta) {
        String m = null;
        try {
                System.err.println("Pre kreiranja konteksta");
                JMSContext context = JMSResursi.getContext(connectionFactory);
                JMSConsumer consumer = context.createConsumer(central_queue);
                JMSProducer producer = context.createProducer();
                System.err.println("Prosao kreiranje konteksta");
                FilijalaOmotac filijalaOmotac = new FilijalaOmotac(ime, adresa, idMesta);
                Poruka poruka = new Poruka(Tipovi.KREIRANJE_FILIJALE_U_MESTU, filijalaOmotac);
                ObjectMessage objMsg = context.createObjectMessage(poruka);
                
                objMsg.setIntProperty("TIP", Tipovi.KREIRANJE_FILIJALE_U_MESTU);
                objMsg.setJMSReplyTo(central_queue);
                
                System.out.println("Priprema za slanje poruke: " + objMsg.toString());
                producer.send(s1_queue, objMsg);
                System.out.println("Poslata poruka: " + objMsg.toString());
                
                ObjectMessage msg = (ObjectMessage) consumer.receive();
                System.out.println("Primljena poruka" + msg.toString());
                Poruka p = (Poruka) msg.getObject();
                
                if(p.getStatus() != Tipovi.OK) {
                    m = "Filijala nije kreirana";
                } else {
                    m = "Uspesno kreirana filijala.";
                }
                
            } catch (JMSException ex) {}
        return Response.ok(m).build();
    }
}
