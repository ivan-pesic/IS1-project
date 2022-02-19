/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package requests;

import entities.Racun;
import entities.Transakcija;
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
public class CreateTransakcijaUplata extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        try {
            int IdR = message.getIntProperty("IdR");
            int IdF = message.getIntProperty("IdF");
            double Iznos = message.getDoubleProperty("Iznos");
            String Svrha = message.getStringProperty("Svrha");
            
            Racun racun = em.find(Racun.class, IdR);
            if(racun == null) throw new Exception("GRESKA: Racun sa zadatim IdR ne postoji.");
            if(racun.getStatus() == 'U') throw new Exception("GRESKA: Racun sa zadatim IdR ne postoji");
            
            Transakcija transakcija = new Transakcija();
            transakcija.setDatumIVreme(new Date());
            transakcija.setIdF(IdF);
            transakcija.setIdR1(racun);
            transakcija.setIdR2(null);
            transakcija.setIznos(Iznos);
            transakcija.setSvrha(Svrha);
            transakcija.setRedniBroj(racun.getBrojTransakcija() + 1);
            transakcija.setTip('U');
            
            em.getTransaction().begin();
            em.persist(transakcija);
            
            racun.setBrojTransakcija(racun.getBrojTransakcija() + 1);
            racun.setStanje(Iznos + racun.getStanje());
            if(racun.getStanje() > racun.getDozvMinus() && racun.getStatus() == 'B')
                racun.setStatus('A');
            em.persist(racun);
            
            em.getTransaction().commit();
            
            returnMessage = super.createStringReturnMessage(Codes.OK, "Uspesno kreirana transakcija: uplata na racun: " + IdR  +
                    " u filijali: " + IdF + " uplaceno: " + Iznos + " sa svrhom: " + Svrha, context);
            //povratnaPoruka = new Poruka(Codes.OK, null);
            
        } catch (JMSException ex) {
            Logger.getLogger(CreateTransakcijaUplata.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Kreiranje transakcije-uplata nije uspelo.", context);
        } catch (Exception ex) {
            Logger.getLogger(CreateTransakcijaUplata.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, ex.getMessage(), context);
        } finally {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Transakcija pri kreiranju "
                        + "transakcije-uplate nije uspela.", context);
            }
            em.close();
        }
        emf.close();
        
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        //  idrac, svrha, iznos i idfil
        int IdR = -1;
        int IdF = -1;
        String Svrha = null;
        double Iznos = -1;
        try {
            IdR = message.getIntProperty("IdR");
            IdF = message.getIntProperty("IdF");
            Iznos = message.getDoubleProperty("Iznos");
            Svrha = message.getStringProperty("Svrha");
        } catch (JMSException ex) {
            Logger.getLogger(CreateTransakcijaUplata.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Kreiranje transakcije - uplate na racun."
                + "\nPodaci o zahtevu:\n"
                + "\t1. IdR: " + IdR
                + "\t2. IdF: " + IdF
                + "\t3. Iznos: " + Iznos
                + "\t4. Svrha: " + Svrha;
    }
    
}