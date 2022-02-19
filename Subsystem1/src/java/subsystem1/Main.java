/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subsystem1;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import requests.CreateMesto;
import requests.Request;

/**
 *
 * @author Ivan
 */
public class Main {
    
    @Resource(lookup="projectConnectionFactory")
    static ConnectionFactory connectionFactory;
    
    @Resource(lookup="subsystem1_queue")
    static Queue s1_queue;
    
    @Resource(lookup="subsystem2_queue")
    static Queue s2_queue;

    @Resource(lookup="subsystem3_queue")
    static Queue s3_queue;
    
    @Resource(lookup="cs_queue")
    static Queue central_queue;
    
    static EntityManagerFactory emf = null;
    static EntityManager em = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(s1_queue);
        JMSProducer producer = context.createProducer();
        Destination receiver = null;
        emf = Persistence.createEntityManagerFactory("Subsystem1PU");
        em = emf.createEntityManager(); 
        
        while(true) {
            try {
                System.out.println("Cekanje na poruku...");
                Message msg = consumer.receive();
                System.out.println("Primljena poruka: " + msg.toString());
                
                int type = msg.getIntProperty("Tip");
                receiver = msg.getJMSReplyTo();
                if(receiver == null) throw new Exception("GRESKA: Primalac je null");
                
                Request request = null;
                
                switch(type) {
                    case Codes.KREIRANJE_MESTA: {
                        request = new CreateMesto();
                        break;
                    }
                    default: throw new Exception("GRESKA: Poruka sadrzi nepostojeci tip.");
                }
                
                if(request == null) throw new Exception("GRESKA: Zahtev je null.");
                
                Message result = request.execute(msg, context);
                producer.send(receiver, result);
                
                System.out.println("Poslata poruka: " + result.toString() + "\nNa red: " + receiver + "\n");
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("--------------------------------------------------------------------------------------\n");
        }
    }
}
