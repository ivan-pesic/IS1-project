/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import javax.jms.JMSContext;
import javax.jms.Message;

/**
 *
 * @author Ivan
 */
public class CreateTransakcijaUplata extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getMessageInfo(Message message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
