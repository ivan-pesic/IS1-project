/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Komitent;
import entities.Racun;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
public class CreateRacun extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        try {
            int IdK = message.getIntProperty("IdK");
            int IdM = message.getIntProperty("IdM");
            double dozvMinus = message.getDoubleProperty("DozvMinus");
            
            Komitent komitent = em.find(Komitent.class, IdK);
            if(komitent == null) throw new Exception("GRESKA: Komitent sa zadatim IdK ne postoji pa se racun ne moze kreirati.");
            
            Racun racun = new Racun();
            racun.setIdK(komitent);
            racun.setIdM(IdM);
            racun.setStanje(0);
            racun.setStatus('A');
            racun.setBrojTransakcija(0);
            racun.setDozvMinus(dozvMinus);
            
            Date dateAndTime = new Date();
            racun.setDatumIVreme(dateAndTime);
             
            
            em.getTransaction().begin();
            em.persist(racun);
            em.getTransaction().commit();
            
            returnMessage = super.createStringReturnMessage(Codes.OK, "Racun uspesno kreiran za komitenta: " + IdK + " u mestu: " + IdM +
                    " sa dozvoljenim minusom koji iznosi: " + dozvMinus, context);
            //povratnaPoruka = new Poruka(Codes.OK, null);
            
        } catch (JMSException ex) {
            Logger.getLogger(CreateRacun.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Kreiranje racuna nije uspelo.", context);
        } catch (Exception ex) {
            Logger.getLogger(CreateRacun.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, ex.getMessage(), context);
        } finally {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Transakcija pri kreiranju racuna nije uspela.", context);
            }
            em.close();
        }
        emf.close();
        
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        int IdM = -1;
        int IdK = -1;
        double dozvMinus = -1;
        try {
            IdK = message.getIntProperty("IdK");
            IdM = message.getIntProperty("IdM");
            dozvMinus = message.getDoubleProperty("DozvMinus");
        } catch (JMSException ex) {
            Logger.getLogger(CreateRacun.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Kreiranje racuna."
                + "\nPodaci o zahtevu:\n"
                + "\t1. IdK: " + IdK
                + "\t2. IdM: " + IdM
                + "\t3. Dozvoljeni minus: " + dozvMinus;
    }
    
}
