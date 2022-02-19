/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poruke;

import java.io.Serializable;
import javax.jms.Destination;

/**
 *
 * @author Ivan
 */
public class Poruka implements Serializable {
    private Serializable objekat;
    private int status;

    public Poruka(int status, Serializable objekat) {
        this.objekat = objekat;
        this.status = status;
    }
    
    public Serializable getObjekat() {
        return objekat;
    }

    public void setObjekat(Serializable objekat) {
        this.objekat = objekat;
    }
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
    