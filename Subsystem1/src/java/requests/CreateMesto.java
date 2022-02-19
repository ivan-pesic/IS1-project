/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Mesto;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import subsystem1.Codes;

/**
 *
 * @author Ivan
 */
public class CreateMesto extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem1PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        try {
            String PB = message.getStringProperty("PB");
            String Naziv = message.getStringProperty("Naziv");
            
            Mesto mesto = new Mesto();
            mesto.setNaziv(Naziv);
            mesto.setPb(PB);
            
            em.getTransaction().begin();
            em.persist(mesto);
            em.getTransaction().commit();
            
            returnMessage = super.createStringReturnMessage(Codes.OK, "Mesto uspesno kreirano.", context);
            //povratnaPoruka = new Poruka(Codes.OK, null);
            
        } catch (JMSException ex) {
            Logger.getLogger(CreateMesto.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Kreiranje mesta nije uspelo.", context);
        } finally {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Transakcija pri kreiranju mesta nije uspela.", context);
            }
            em.close();
        }
        emf.close();
        
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        String PB = null;
        String Naziv = null;
        try {
            PB = message.getStringProperty("PB");
            Naziv = message.getStringProperty("Naziv");
        } catch (JMSException ex) {
            Logger.getLogger(CreateMesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Kreiranje mesta."
                + "\nPodaci o zahtevu:\n"
                + "\t1. Naziv: " + Naziv
                + "\t2. PB: " + PB;
    }
}
