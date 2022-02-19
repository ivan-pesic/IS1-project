/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Filijala;
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
public class CreateFilijala extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem1PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        try {
            String Naziv = message.getStringProperty("Naziv");
            String Adresa = message.getStringProperty("Adresa");
            int IdM = message.getIntProperty("IdM");
            
            Filijala filijala = new Filijala();
            filijala.setNaziv(Naziv);
            filijala.setAdresa(Adresa);
            filijala.setIdM(IdM);
            
            Mesto mesto = em.find(Mesto.class, IdM);
            if(mesto == null) throw new Exception("GRESKA: Mesto sa zadatim IdM ne postoji.");
            
            em.getTransaction().begin();
            em.persist(filijala);
            em.getTransaction().commit();
            
            returnMessage = super.createStringReturnMessage(Codes.OK, "Filijala uspesno kreirana u mestu: " + IdM, context);
            //povratnaPoruka = new Poruka(Codes.OK, null);
            
        } catch (JMSException ex) {
            Logger.getLogger(CreateMesto.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Kreiranje mesta nije uspelo.", context);
        } catch (Exception ex) {
            Logger.getLogger(CreateFilijala.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, ex.getMessage(), context);
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
        String Adresa = null;
        String Naziv = null;
        int IdM = -1;
        try {
            Naziv = message.getStringProperty("Naziv");
            Adresa = message.getStringProperty("Adresa");
            IdM = message.getIntProperty("IdM");
        } catch (JMSException ex) {
            Logger.getLogger(CreateMesto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Kreiranje mesta."
                + "\nPodaci o zahtevu:\n"
                + "\t1. Naziv: " + Naziv
                + "\t2. Adresa: " + Adresa
                + "\t3. IdM: " + IdM;
    }
    
}
