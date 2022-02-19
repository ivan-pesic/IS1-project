/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.centralserver.resources;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 *
 * @author Ivan
 */
public class JMSResursi {    
    private static JMSContext context = null;
    
    public static JMSContext getContext(ConnectionFactory connectionFactory) {
        if(context == null) {
            context = connectionFactory.createContext();
        }
        return context;
    }
}
