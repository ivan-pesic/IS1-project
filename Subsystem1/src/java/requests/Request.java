/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

/**
 *
 * @author Ivan
 */
public abstract class Request implements Serializable {
    public abstract Message execute(Message message, JMSContext context);
    public abstract String getMessageInfo(Message message);   
    public Message createStringReturnMessage(int code, String message, JMSContext context) {
        Message msg = context.createMessage();
        try {
            msg.setIntProperty("Tip", code);
            msg.setStringProperty("Poruka", message);
        } catch (JMSException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("*************************************************");
        }
        return msg;
    }
    public Message createListReturnMessage(int code, List list, String message, JMSContext context) {
        ObjectMessage msg = context.createObjectMessage((Serializable) list);
        try {
            msg.setIntProperty("Tip", code);
            msg.setStringProperty("Poruka", message);
        } catch (JMSException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("*************************************************");
        }
        return msg;
    }
}
