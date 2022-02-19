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
public class CreateKomitent extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        try {
            String Naziv = message.getStringProperty("Naziv");
            String Adresa = message.getStringProperty("Adresa");
            int IdM = message.getIntProperty("IdM");
            
            Komitent komitent = new Komitent();
            komitent.setNaziv(Naziv);
            komitent.setAdresa(Adresa);
            komitent.setIdM(IdM);
            
            em.getTransaction().begin();
            em.persist(komitent);
            em.getTransaction().commit();
            
            returnMessage = super.createStringReturnMessage(Codes.OK, "Komitent uspesno kreiran u mestu: " + IdM, context);
            //povratnaPoruka = new Poruka(Codes.OK, null);
            
        } catch (JMSException ex) {
            Logger.getLogger(CreateKomitent.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Kreiranje komitenta nije uspelo.", context);
        } catch (Exception ex) {
            Logger.getLogger(CreateKomitent.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, ex.getMessage(), context);
        } finally {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Transakcija pri kreiranju komitenta nije uspela.", context);
            }
            em.close();
        }
        emf.close();
        
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        String Adresa = null;
        String Naziv = null;
        int IdM = -1;
        try {
            Naziv = message.getStringProperty("Naziv");
            Adresa = message.getStringProperty("Adresa");
            IdM = message.getIntProperty("IdM");
        } catch (JMSException ex) {
            Logger.getLogger(CreateKomitent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Kreiranje komitenta."
                + "\nPodaci o zahtevu:\n"
                + "\t1. Naziv: " + Naziv
                + "\t2. Adresa: " + Adresa
                + "\t3. IdM: " + IdM;
    }
    
}