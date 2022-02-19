/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subsystem2;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(s2_queue);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
        EntityManager em = emf.createEntityManager(); 
        
        while(true) {
            
            Message msg = consumer.receive();
            
            MessageProcessor.process(msg);
            
        }        
    }
}
