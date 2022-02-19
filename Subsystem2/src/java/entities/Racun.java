/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ivan
 */
@Entity
@Table(name = "racun")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Racun.findAll", query = "SELECT r FROM Racun r"),
    @NamedQuery(name = "Racun.findByIdR", query = "SELECT r FROM Racun r WHERE r.idR = :idR"),
    @NamedQuery(name = "Racun.findByIdM", query = "SELECT r FROM Racun r WHERE r.idM = :idM"),
    @NamedQuery(name = "Racun.findByStatus", query = "SELECT r FROM Racun r WHERE r.status = :status"),
    @NamedQuery(name = "Racun.findByStanje", query = "SELECT r FROM Racun r WHERE r.stanje = :stanje"),
    @NamedQuery(name = "Racun.findByDozvMinus", query = "SELECT r FROM Racun r WHERE r.dozvMinus = :dozvMinus"),
    @NamedQuery(name = "Racun.findByDatumIVreme", query = "SELECT r FROM Racun r WHERE r.datumIVreme = :datumIVreme"),
    @NamedQuery(name = "Racun.findByBrojTransakcija", query = "SELECT r FROM Racun r WHERE r.brojTransakcija = :brojTransakcija")})
public class Racun implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdR")
    private Integer idR;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdM")
    private int idM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Status")
    private Character status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Stanje")
    private double stanje;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DozvMinus")
    private double dozvMinus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumIVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumIVreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "BrojTransakcija")
    private int brojTransakcija;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRSa")
    private List<Transakcija> transakcijaList;
    @JoinColumn(name = "IdK", referencedColumnName = "IdK")
    @ManyToOne(optional = false)
    private Komitent idK;

    public Racun() {
    }

    public Racun(Integer idR) {
        this.idR = idR;
    }

    public Racun(Integer idR, int idM, Character status, double stanje, double dozvMinus, Date datumIVreme, int brojTransakcija) {
        this.idR = idR;
        this.idM = idM;
        this.status = status;
        this.stanje = stanje;
        this.dozvMinus = dozvMinus;
        this.datumIVreme = datumIVreme;
        this.brojTransakcija = brojTransakcija;
    }

    public Integer getIdR() {
        return idR;
    }

    public void setIdR(Integer idR) {
        this.idR = idR;
    }

    public int getIdM() {
        return idM;
    }

    public void setIdM(int idM) {
        this.idM = idM;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public double getStanje() {
        return stanje;
    }

    public void setStanje(double stanje) {
        this.stanje = stanje;
    }

    public double getDozvMinus() {
        return dozvMinus;
    }

    public void setDozvMinus(double dozvMinus) {
        this.dozvMinus = dozvMinus;
    }

    public Date getDatumIVreme() {
        return datumIVreme;
    }

    public void setDatumIVreme(Date datumIVreme) {
        this.datumIVreme = datumIVreme;
    }

    public int getBrojTransakcija() {
        return brojTransakcija;
    }

    public void setBrojTransakcija(int brojTransakcija) {
        this.brojTransakcija = brojTransakcija;
    }

    @XmlTransient
    public List<Transakcija> getTransakcijaList() {
        return transakcijaList;
    }

    public void setTransakcijaList(List<Transakcija> transakcijaList) {
        this.transakcijaList = transakcijaList;
    }

    public Komitent getIdK() {
        return idK;
    }

    public void setIdK(Komitent idK) {
        this.idK = idK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idR != null ? idR.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Racun)) {
            return false;
        }
        Racun other = (Racun) object;
        if ((this.idR == null && other.idR != null) || (this.idR != null && !this.idR.equals(other.idR))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Racun[ idR=" + idR + " ]";
    }
    
}
