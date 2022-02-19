/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package posiljalac;

import entities.Mesto;
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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import omotaci.MestoOmotac;
import poruke.Poruka;

/**
 *
 * @author Ivan
 */
public class Main {
        @PersistenceContext
    EntityManager em;
    
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
    
    private static EntityManagerFactory emf = null;
    private static EntityManager emng = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            try {
                // TODO code application logic here
                JMSContext context = connectionFactory.createContext();
                JMSConsumer consumer = context.createConsumer(central_queue);
                JMSProducer producer = context.createProducer();
                
                
                
//                Message msg = context.createMessage();
//                msg.setIntProperty("Tip", Tipovi.KREIRANJE_MESTA);
//                msg.setStringProperty("PB", "26000");
//                msg.setStringProperty("Naziv", "Pancevo");
//                msg.setStringProperty("Poruka", "Kreiranje mesta: Naziv: Pancevo, PB: 26000");

//                Message msg = context.createMessage();
//                msg.setIntProperty("Tip", Tipovi.KREIRANJE_FILIJALE_U_MESTU);
//                msg.setStringProperty("Naziv", "Unicredit Banka");
//                msg.setStringProperty("Adresa", "Vojvode Radomira Putnika 22");
//                msg.setIntProperty("IdM", 1);

//                Message msg = context.createMessage();
//                msg.setIntProperty("Tip", Tipovi.PROMENA_SEDISTA_ZA_KOMITENTA);
//                msg.setStringProperty("Naziv", "Dragan Mitrasinovic");
//                msg.setStringProperty("Adresa", "Kralja Petra I 70v");
//                msg.setIntProperty("IdM", 2);
//                msg.setIntProperty("IdK", 1);


                Message msg = context.createMessage();
                msg.setIntProperty("Tip", Tipovi.DOHVATANJE_SVIH_FILIJALA);
                
                msg.setJMSReplyTo(central_queue);
                producer.send(s1_queue, msg);
                
//                MestoOmotac mestoOmotac = new MestoOmotac("Beograd", "11000");
//                Poruka poruka = new Poruka(Tipovi.KREIRANJE_MESTA, mestoOmotac);
//                ObjectMessage objMsg = context.createObjectMessage(poruka);
//                
//                objMsg.setIntProperty("TIP", Tipovi.KREIRANJE_MESTA);
//                objMsg.setJMSReplyTo(central_queue);
//                
//                producer.send(s1_queue, objMsg);

                System.out.println("Poslata poruka. Cekanje na prijem.");
                
                msg = consumer.receive();
                int tip = msg.getIntProperty("Tip");
                String poruka = msg.getStringProperty("Poruka");
                System.out.println("Primljena poruka: " + poruka);
                ObjectMessage objMsg = (ObjectMessage) msg;
                List m = (List) objMsg.getObject();
                System.out.println(m.toString());
//                List<Mesto> mesta = m;
//                for (Mesto mesto : mesta) {
//                    System.out.println(mesto);
//                }
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        

    }
    
}
