/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package omotaci;

import java.io.Serializable;

/**
 *
 * @author Ivan
 */
public class MestoOmotac implements Serializable {
    private String pb;
    private String naziv;
    
    public MestoOmotac(String naziv, String pb) {
        this.naziv = naziv;
        this.pb = pb;
    }

    public String getPb() {
        return pb;
    }

    public String getNaziv() {
        return naziv;
    }
}
