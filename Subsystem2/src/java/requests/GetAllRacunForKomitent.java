/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Komitent;
import entities.Racun;
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
public class GetAllRacunForKomitent extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        Message returnMessage = null;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
            EntityManager em = emf.createEntityManager();
            
            int IdK = message.getIntProperty("IdK");
            Komitent k = em.find(Komitent.class, IdK);
            if(k == null) throw new Exception("GRESKA: Komitent sa zadatim IdK ne postoji.");
            
            TypedQuery<Racun> query = em.createNamedQuery("SELECT r FROM Racun r WHERE r.idk = :idk", Racun.class).setParameter("idk", IdK);
            List<Racun> listOfRacun = query.getResultList();
            
            em.close();
            emf.close();
            
            returnMessage = super.createListReturnMessage(Codes.OK, listOfRacun, "Dohvaceni racuni za komitenta: " + IdK, context);
        } catch (JMSException ex) {
            Logger.getLogger(GetAllRacunForKomitent.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Ne mogu se dohvatiti racuni.", context);
        } catch (Exception ex) {
            Logger.getLogger(GetAllRacunForKomitent.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, ex.getMessage(), context);
        }
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        int IdK = -1;
        try {
            IdK = message.getIntProperty("IdK");
        } catch (JMSException ex) {
            Logger.getLogger(GetAllRacunForKomitent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Dohvatanje svih racuna za komitenta."
                + "\nPodaci o zahtevu:\n"
                + "\t1. IdK: " + IdK;
    }
    
}
