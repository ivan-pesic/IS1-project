/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Mesto;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import subsystem1.Codes;

/**
 *
 * @author Ivan
 */
public class GetAllMesto extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem1PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        
        TypedQuery<Mesto> query = em.createNamedQuery("Mesto.findAll", Mesto.class);
        List<Mesto> listOfMesto = query.getResultList();

        em.close();
        emf.close();
        
        returnMessage = super.createListReturnMessage(Codes.OK, listOfMesto, "Dohvacena mesta.", context);
        
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        return "Zahtev: Dohvatanje svih mesta.";
    }
    
}
