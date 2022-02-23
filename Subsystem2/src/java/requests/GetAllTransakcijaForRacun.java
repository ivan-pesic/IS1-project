/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Komitent;
import entities.Racun;
import entities.Transakcija;
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
import subsystem2.Codes;

/**
 *
 * @author Ivan
 */
public class GetAllTransakcijaForRacun extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        Message returnMessage = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
            EntityManager em = emf.createEntityManager();
            
            int IdR = message.getIntProperty("IdR");
            Racun r = em.find(Racun.class, IdR);
            if(r == null) throw new Exception("GRESKA: Racun sa zadatim IdR ne postoji.");
            
            TypedQuery<Transakcija> query = em
                    .createNamedQuery("SELECT t FROM Transakcija t WHERE t.idr1 = :idr OR t.idr2 = :idr", Transakcija.class)
                    .setParameter("idr", IdR);
            List<Transakcija> listOfTransakcija = query.getResultList();
            
            em.close();
            emf.close();
            
            returnMessage = super.createListReturnMessage(Codes.OK, listOfTransakcija, "Dohvacene transakcije za racun: " + IdR, context);
        } catch (JMSException ex) {
            Logger.getLogger(GetAllTransakcijaForRacun.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Ne mogu se dohvatiti transakcije.", context);
        } catch (Exception ex) {
            Logger.getLogger(GetAllRacunForKomitent.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, ex.getMessage(), context);
        }
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        int IdR = -1;
        try {
            IdR = message.getIntProperty("IdR");
        } catch (JMSException ex) {
            Logger.getLogger(GetAllRacunForKomitent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Dohvatanje svih transakcija za racun."
                + "\nPodaci o zahtevu:\n"
                + "\t1. IdR: " + IdR;
    }
    
}

