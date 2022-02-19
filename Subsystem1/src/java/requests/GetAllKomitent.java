/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Komitent;
import java.util.List;
import javax.jms.JMSContext;
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
public class GetAllKomitent extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem1PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        
        TypedQuery<Komitent> query = em.createNamedQuery("Komitent.findAll", Komitent.class);
        List<Komitent> listOfMesto = query.getResultList();

        em.close();
        emf.close();
        
        returnMessage = super.createListReturnMessage(Codes.OK, listOfMesto, "Dohvaceni komitenti.", context);
        
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        return "Zahtev: Dohvatanje svih komitenata.";
    }
    
}
