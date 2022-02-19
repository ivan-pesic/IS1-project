/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subsystem2;

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
import requests.CloseRacun;
import requests.CreateKomitent;
import requests.CreateRacun;
import requests.CreateTransakcijaIsplata;
import requests.CreateTransakcijaUplata;
import requests.Request;

/**
 *
 * @author Ivan
 */
public class Main {
    
    @Resource(lookup="projectConnectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup="subsystem1_queue")
    private static Queue s1_queue;
    
    @Resource(lookup="subsystem2_queue")
    private static Queue s2_queue;

    @Resource(lookup="subsystem3_queue")
    private static Queue s3_queue;
    
    @Resource(lookup="cs_queue")
    private static Queue central_queue;
    
    static EntityManagerFactory emf = null;
    static EntityManager em = null;

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(s2_queue);
        JMSProducer producer = context.createProducer();
        Destination receiver = null;
        emf = Persistence.createEntityManagerFactory("Subsystem2PU");
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
                    case Codes.KREIRANJE_KOMITENTA: {
                        request = new CreateKomitent();
                        break;
                    }
                    case Codes.OTVARANJE_RACUNA: {
                        request = new CreateRacun();
                        break;
                    }
                    case Codes.ZATVARANJE_RACUNA: {
                        request = new CloseRacun();
                        break;
                    }
                    case Codes.KREIRANJE_TRANSAKCIJE_UPLATA: {
                        request = new CreateTransakcijaUplata();
                        break;
                    }
                    case Codes.KREIRANJE_TRANSAKCIJE_ISPLATA: {
                        request = new CreateTransakcijaIsplata();
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
