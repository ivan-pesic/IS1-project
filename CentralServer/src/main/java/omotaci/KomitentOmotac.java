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
 */public class KomitentOmotac implements Serializable {
    private String adresa;
    private String naziv;
    private int IdM;
    
    public KomitentOmotac(String naziv, String adresa, int IdM) {
        this.naziv = naziv;
        this.adresa = adresa;
        this.IdM = IdM;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getNaziv() {
        return naziv;
    }

    public int getIdM() {
        return IdM;
    }
}
