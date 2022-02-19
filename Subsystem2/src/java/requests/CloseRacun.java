/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Racun;
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
public class CloseRacun extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        try {
            int IdR = message.getIntProperty("IdR");
            Racun racun = em.find(Racun.class, IdR);
            
            if(racun == null) throw new Exception("GRESKA: Racun sa zadatim IdR ne postoji.");
            racun.setStatus('U');
            em.getTransaction().begin();
            em.persist(racun);
            em.getTransaction().commit();
            
            returnMessage = super.createStringReturnMessage(Codes.OK, "Racun: " + IdR + " uspesno ugasen.", context);
            //povratnaPoruka = new Poruka(Codes.OK, null);
            
        } catch (JMSException ex) {
            Logger.getLogger(CloseRacun.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Gasenje racuna nije uspelo.", context);
        } catch (Exception ex) {
            Logger.getLogger(CloseRacun.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, ex.getMessage(), context);
        } finally {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Transakcija pri gasenju racuna nije uspela.", context);
            }
            em.close();
        }
        emf.close();
        
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        int IdR = -1;
        try {
            IdR = message.getIntProperty("IdR");
        } catch (JMSException ex) {
            Logger.getLogger(CloseRacun.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Zatvaranje racuna."
                + "\nPodaci o zahtevu:\n"
                + "\t1. IdR: " + IdR;
    }
    
}
