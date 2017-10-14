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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jonathan Botha
 */
@Entity
@Table(name = "TBLPERSONALISEDRESEARCH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonalResearchEntity.findAll", query = "SELECT p FROM PersonalResearchEntity p"),
    @NamedQuery(name = "PersonalResearchEntity.findByTransactionid", query = "SELECT p FROM PersonalResearchEntity p WHERE p.transactionid = :transactionid"),
    @NamedQuery(name = "PersonalResearchEntity.findByUserid", query = "SELECT p FROM PersonalResearchEntity p WHERE p.userid = :userid"),
    @NamedQuery(name = "PersonalResearchEntity.findByResearchid", query = "SELECT p FROM PersonalResearchEntity p WHERE p.researchid = :researchid"),
    @NamedQuery(name = "PersonalResearchEntity.findByCategory", query = "SELECT p FROM PersonalResearchEntity p WHERE p.category = :category"),
    @NamedQuery(name = "PersonalResearchEntity.findByVoted", query = "SELECT p FROM PersonalResearchEntity p WHERE p.voted = :voted"),
    @NamedQuery(name = "PersonalResearchEntity.findByDeleted", query = "SELECT p FROM PersonalResearchEntity p WHERE p.deleted = :deleted")})
public class PersonalResearchEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "TRANSACTIONID")
    private Integer transactionid;
    @Basic(optional = false)
    @Column(name = "USERID")
    private String userid;
    @Basic(optional = false)
    @Column(name = "RESEARCHID")
    private String researchid;
    @Column(name = "CATEGORY")
    private String category;
    @Basic(optional = false)
    @Column(name = "VOTED")
    private Boolean voted;
    @Basic(optional = false)
    @Column(name = "DELETED")
    private Boolean deleted;

    public PersonalResearchEntity() {
    }

    public PersonalResearchEntity(Integer transactionid) {
        this.transactionid = transactionid;
    }

    public PersonalResearchEntity(Integer transactionid, String userid, String researchid, Boolean voted, Boolean deleted) {
        this.transactionid = transactionid;
        this.userid = userid;
        this.researchid = researchid;
        this.voted = voted;
        this.deleted = deleted;
    }

    public Integer getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(Integer transactionid) {
        this.transactionid = transactionid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getResearchid() {
        return researchid;
    }

    public void setResearchid(String researchid) {
        this.researchid = researchid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionid != null ? transactionid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonalResearchEntity)) {
            return false;
        }
        PersonalResearchEntity other = (PersonalResearchEntity) object;
        if ((this.transactionid == null && other.transactionid != null) || (this.transactionid != null && !this.transactionid.equals(other.transactionid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.PersonalResearchEntity[ transactionid=" + transactionid + " ]";
    }
    
}
