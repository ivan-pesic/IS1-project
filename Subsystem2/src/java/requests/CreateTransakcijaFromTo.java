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
public class CreateTransakcijaFromTo extends Request {

    @Override
    public Message execute(Message message, JMSContext context) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Subsystem2PU");
        EntityManager em = emf.createEntityManager();
        Message returnMessage = null;
        
        try {
            int IdR1 = message.getIntProperty("IdR1");
            int IdR2 = message.getIntProperty("IdR2");
            double Iznos = message.getDoubleProperty("Iznos");
            String Svrha = message.getStringProperty("Svrha");
            
            Racun racun1 = em.find(Racun.class, IdR1);
            Racun racun2 = em.find(Racun.class, IdR2);
            if(racun1 == null) throw new Exception("GRESKA: Racun sa zadatim IdR1 ne postoji.");
            if(racun2 == null) throw new Exception("GRESKA: Racun sa zadatim IdR2 ne postoji.");
            if(racun1.getStatus() == 'U') throw new Exception("GRESKA: Racun sa zadatim IdR1 ne postoji");
            if(racun1.getStatus() == 'B') throw new Exception("GRESKA: Racun sa zadatim IdR1 je blokiran");
            if(racun2.getStatus() == 'U') throw new Exception("GRESKA: Racun sa zadatim IdR2 ne postoji");
            
            Transakcija transakcija = new Transakcija();
            transakcija.setDatumIVreme(new Date());
            transakcija.setIdF(null);
            transakcija.setIdR1(racun1);
            transakcija.setIdR2(racun2);
            transakcija.setIznos(Iznos);
            transakcija.setSvrha(Svrha);
            transakcija.setRedniBroj(racun1.getBrojTransakcija() + 1);
            transakcija.setTip('P');
            
            em.getTransaction().begin();
            em.persist(transakcija);
            
            racun1.setBrojTransakcija(racun1.getBrojTransakcija() + 1);
            racun2.setBrojTransakcija(racun2.getBrojTransakcija() + 1);
            racun1.setStanje(racun1.getStanje() - Iznos);
            racun2.setStanje(racun2.getStanje() + Iznos);
            if(racun1.getStanje() < -racun1.getDozvMinus())
                racun1.setStatus('B');
            if(racun2.getStanje() > -racun2.getDozvMinus() && racun2.getStatus() == 'B')
                racun2.setStatus('A');
            
            em.persist(racun1);
            em.persist(racun2);
            
            em.getTransaction().commit();
            
            returnMessage = super.createStringReturnMessage(Codes.OK, "Uspesno kreirana transakcija: uplata sa racuna: " + IdR1  +
                    " na racun: " + IdR2 + " uplaceno: " + Iznos + " sa svrhom: " + Svrha, context);
            //povratnaPoruka = new Poruka(Codes.OK, null);
            
        } catch (JMSException ex) {
            Logger.getLogger(CreateTransakcijaFromTo.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Kreiranje transakcije-prenos nije uspelo.", context);
        } catch (Exception ex) {
            Logger.getLogger(CreateTransakcijaFromTo.class.getName()).log(Level.SEVERE, null, ex);
            returnMessage = super.createStringReturnMessage(Codes.NOT_OK, ex.getMessage(), context);
        } finally {
            if(em.getTransaction().isActive()) {
                em.getTransaction().rollback();
                returnMessage = super.createStringReturnMessage(Codes.NOT_OK, "GRESKA: Transakcija pri kreiranju "
                        + "transakcije-prenos nije uspela.", context);
            }
            em.close();
        }
        emf.close();
        
        return returnMessage;
    }

    @Override
    public String getMessageInfo(Message message) {
        //  idrac, svrha, iznos i idfil
        int IdR1 = -1;
        int IdR2 = -1;
        String Svrha = null;
        double Iznos = -1;
        try {
            IdR1 = message.getIntProperty("IdR1");
            IdR2 = message.getIntProperty("IdR2");
            Iznos = message.getDoubleProperty("Iznos");
            Svrha = message.getStringProperty("Svrha");
        } catch (JMSException ex) {
            Logger.getLogger(CreateTransakcijaFromTo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Zahtev: Kreiranje transakcije - isplate sa racuna."
                + "\nPodaci o zahtevu:\n"
                + "\t1. IdR1: " + IdR1
                + "\t2. IdR2: " + IdR2
                + "\t3. Iznos: " + Iznos
                + "\t4. Svrha: " + Svrha;
    }
    
}
