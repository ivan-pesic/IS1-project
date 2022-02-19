/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queuecleaner;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

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


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws JMSException {
        // TODO code application logic here
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer1 = context.createConsumer(central_queue);
        JMSConsumer consumer2 = context.createConsumer(s1_queue);
        JMSConsumer consumer3 = context.createConsumer(s2_queue);
        JMSConsumer consumer4 = context.createConsumer(s3_queue);
        
        Message message=null;
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int counter4 = 0;

        for(int i = 0; i<100000; i++) {            
            message = consumer1.receiveNoWait();
            if(message!=null) counter1++;
            
            message = consumer2.receiveNoWait();
            if(message!=null) counter2++;
            
            message = consumer3.receiveNoWait();
            if(message!=null) counter3++;
            
            message = consumer4.receiveNoWait();
            if(message!=null) counter4++;
        }
        
        System.out.println("Deleted "+counter1+" messages from 'central_queue'");
        System.out.println("Deleted "+counter2+" messages from 'subsystem1_queue'");
        System.out.println("Deleted "+counter3+" messages from 'subsystem2_queue'");
        System.out.println("Deleted "+counter4+" messages from 'subsystem3_queue'");
    }
    
}
