/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jonathan.botha
 */
@Entity
@Table(name = "TBLRESEARCH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResearchEntity.findAll", query = "SELECT r FROM ResearchEntity r"),
    @NamedQuery(name = "ResearchEntity.findById", query = "SELECT r FROM ResearchEntity r WHERE r.id = :id"),
    @NamedQuery(name = "ResearchEntity.findByRelatedformula", query = "SELECT r FROM ResearchEntity r WHERE r.relatedformula = :relatedformula"),
    @NamedQuery(name = "ResearchEntity.findByRating", query = "SELECT r FROM ResearchEntity r WHERE r.rating = :rating"),
    @NamedQuery(name = "ResearchEntity.findByVoteamount", query = "SELECT r FROM ResearchEntity r WHERE r.voteamount = :voteamount")})
public class ResearchEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Column(name = "RELATEDFORMULA")
    private String relatedformula;
    @Basic(optional = false)
    @Lob
    @Column(name = "TEXT")
    private String text;
    @Basic(optional = false)
    @Column(name = "RATING")
    private double rating;
    @Basic(optional = false)
    @Column(name = "VOTEAMOUNT")
    private int voteamount;

    public ResearchEntity() {
    }

    public ResearchEntity(String id) {
        this.id = id;
    }

    public ResearchEntity(String id, String text, double rating, int voteamount) {
        this.id = id;
        this.text = text;
        this.rating = rating;
        this.voteamount = voteamount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelatedformula() {
        return relatedformula;
    }

    public void setRelatedformula(String relatedformula) {
        this.relatedformula = relatedformula;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVoteamount() {
        return voteamount;
    }

    public void setVoteamount(int voteamount) {
        this.voteamount = voteamount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ResearchEntity)) {
            return false;
        }
        ResearchEntity other = (ResearchEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ResearchEntity[ id=" + id + " ]";
    }
    
}
