/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Komitent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import subsystem2.Codes;

/**
 *
 * @author Ivan
 */
public class UpdateKomitent extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        try {
            int IdM = message.getIntProperty("IdM");
            int IdK = message.getIntProperty("IdK");
            
            Komitent komitent = em.find(Komitent.class, IdK);
            
            komitent.setIdM(IdM);
            em.getTransaction().begin();
            em.persist(komitent);
            em.getTransaction().commit();
            
            returnMessage = super.createStringReturnMessage(Codes.OK, "Komitentu uspesno azurirano mesto stanovanja: " + IdM, context);
            
        } catch (JMSException ex) {
            Logger.getLogger(UpdateKomitent.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Azuriranje mesta stanovanja komitenta nije uspelo.", context);
        } catch (Exception ex) {
            Logger.getLogger(UpdateKomitent.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, ex.getMessage(), context);
        } finally {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Transakcija pri azuriranju komitenta nije uspela.", context);
            }
            em.close();
        }
        emf.close();
        
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        int IdK = -1;
        int IdM = -1;
        try {
            IdK = message.getIntProperty("IdK");
            IdM = message.getIntProperty("IdM");
        } catch (JMSException ex) {
            Logger.getLogger(UpdateKomitent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Promena sedista za komitenta."
                + "\nPodaci o zahtevu:\n"
                + "\t1. IdK: " + IdK
                + "\t2. IdM: " + IdM;
    }
    
}
