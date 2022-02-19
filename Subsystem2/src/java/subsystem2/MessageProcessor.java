/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subsystem2;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 *
 * @author Ivan
 */
public class MessageProcessor {
    public static void process(Message msg) {
        try {
            if(msg instanceof ObjectMessage) {
                ObjectMessage objMsg = (ObjectMessage) msg;
                int type = objMsg.getIntProperty("Type");
                switch(type) {
                    case 5: {
                        
                        break;
                    }
                    case 6: {
                        
                        break;
                    }
                    case 7: {
                        
                        break;
                    }
                    case 8: {
                        
                        break;
                    }
                    case 9: {
                        
                        break;
                    }
                    case 13: {
                        
                        break;
                    }
                    case 14: {
                        
                        break;
                    }
                    case 16: {
                        
                        break;
                    }
                }
            }
        } catch (JMSException ex) {
            Logger.getLogger(MessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
