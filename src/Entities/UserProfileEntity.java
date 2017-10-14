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
@Table(name = "TBLPROFILES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserProfileEntity.findAll", query = "SELECT u FROM UserProfileEntity u"),
    @NamedQuery(name = "UserProfileEntity.findById", query = "SELECT u FROM UserProfileEntity u WHERE u.id = :id"),
    @NamedQuery(name = "UserProfileEntity.findByShared", query = "SELECT u FROM UserProfileEntity u WHERE u.shared = :shared"),
    @NamedQuery(name = "UserProfileEntity.findByOwner", query = "SELECT u FROM UserProfileEntity u WHERE u.owner = :owner"),
    @NamedQuery(name = "UserProfileEntity.findBySettingresolution", query = "SELECT u FROM UserProfileEntity u WHERE u.settingresolution = :settingresolution"),
    @NamedQuery(name = "UserProfileEntity.findBySettingantialiasing", query = "SELECT u FROM UserProfileEntity u WHERE u.settingantialiasing = :settingantialiasing"),
    @NamedQuery(name = "UserProfileEntity.findBySettingparticleamount", query = "SELECT u FROM UserProfileEntity u WHERE u.settingparticleamount = :settingparticleamount"),
    @NamedQuery(name = "UserProfileEntity.findByFreefallhighscore", query = "SELECT u FROM UserProfileEntity u WHERE u.freefallhighscore = :freefallhighscore"),
    @NamedQuery(name = "UserProfileEntity.findByFreefallpoints", query = "SELECT u FROM UserProfileEntity u WHERE u.freefallpoints = :freefallpoints"),
    @NamedQuery(name = "UserProfileEntity.findByFreefalldeaths", query = "SELECT u FROM UserProfileEntity u WHERE u.freefalldeaths = :freefalldeaths"),
    @NamedQuery(name = "UserProfileEntity.findByRefractionhighscore", query = "SELECT u FROM UserProfileEntity u WHERE u.refractionhighscore = :refractionhighscore"),
    @NamedQuery(name = "UserProfileEntity.findByRefractionpoints", query = "SELECT u FROM UserProfileEntity u WHERE u.refractionpoints = :refractionpoints"),
    @NamedQuery(name = "UserProfileEntity.findByRefractiondeaths", query = "SELECT u FROM UserProfileEntity u WHERE u.refractiondeaths = :refractiondeaths")})
public class UserProfileEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private String id;
    @Basic(optional = false)
    @Column(name = "SHARED")
    private Boolean shared;
    @Column(name = "OWNER")
    private String owner;
    @Basic(optional = false)
    @Column(name = "SETTINGRESOLUTION")
    private String settingresolution;
    @Basic(optional = false)
    @Column(name = "SETTINGANTIALIASING")
    private Boolean settingantialiasing;
    @Basic(optional = false)
    @Column(name = "SETTINGPARTICLEAMOUNT")
    private short settingparticleamount;
    @Basic(optional = false)
    @Column(name = "FREEFALLHIGHSCORE")
    private short freefallhighscore;
    @Basic(optional = false)
    @Column(name = "FREEFALLPOINTS")
    private int freefallpoints;
    @Basic(optional = false)
    @Column(name = "FREEFALLDEATHS")
    private short freefalldeaths;
    @Basic(optional = false)
    @Column(name = "REFRACTIONHIGHSCORE")
    private short refractionhighscore;
    @Basic(optional = false)
    @Column(name = "REFRACTIONPOINTS")
    private int refractionpoints;
    @Basic(optional = false)
    @Column(name = "REFRACTIONDEATHS")
    private short refractiondeaths;

    public UserProfileEntity() {
    }

    public UserProfileEntity(String id) {
        this.id = id;
    }

    public UserProfileEntity(String id, Boolean shared, String settingresolution, Boolean settingantialiasing, short settingparticleamount, short freefallhighscore, int freefallpoints, short freefalldeaths, short refractionhighscore, int refractionpoints, short refractiondeaths) {
        this.id = id;
        this.shared = shared;
        this.settingresolution = settingresolution;
        this.settingantialiasing = settingantialiasing;
        this.settingparticleamount = settingparticleamount;
        this.freefallhighscore = freefallhighscore;
        this.freefallpoints = freefallpoints;
        this.freefalldeaths = freefalldeaths;
        this.refractionhighscore = refractionhighscore;
        this.refractionpoints = refractionpoints;
        this.refractiondeaths = refractiondeaths;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSettingresolution() {
        return settingresolution;
    }

    public void setSettingresolution(String settingresolution) {
        this.settingresolution = settingresolution;
    }

    public Boolean getSettingantialiasing() {
        return settingantialiasing;
    }

    public void setSettingantialiasing(Boolean settingantialiasing) {
        this.settingantialiasing = settingantialiasing;
    }

    public short getSettingparticleamount() {
        return settingparticleamount;
    }

    public void setSettingparticleamount(short settingparticleamount) {
        this.settingparticleamount = settingparticleamount;
    }

    public short getFreefallhighscore() {
        return freefallhighscore;
    }

    public void setFreefallhighscore(short freefallhighscore) {
        this.freefallhighscore = freefallhighscore;
    }

    public int getFreefallpoints() {
        return freefallpoints;
    }

    public void setFreefallpoints(int freefallpoints) {
        this.freefallpoints = freefallpoints;
    }

    public short getFreefalldeaths() {
        return freefalldeaths;
    }

    public void setFreefalldeaths(short freefalldeaths) {
        this.freefalldeaths = freefalldeaths;
    }

    public short getRefractionhighscore() {
        return refractionhighscore;
    }

    public void setRefractionhighscore(short refractionhighscore) {
        this.refractionhighscore = refractionhighscore;
    }

    public int getRefractionpoints() {
        return refractionpoints;
    }

    public void setRefractionpoints(int refractionpoints) {
        this.refractionpoints = refractionpoints;
    }

    public short getRefractiondeaths() {
        return refractiondeaths;
    }

    public void setRefractiondeaths(short refractiondeaths) {
        this.refractiondeaths = refractiondeaths;
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
        if (!(object instanceof UserProfileEntity)) {
            return false;
        }
        UserProfileEntity other = (UserProfileEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.UserProfileEntity[ id=" + id + " ]";
    }
    
}
