/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import BluLight.Main;
import Entities.PersonalResearchEntity;
import Entities.ResearchEntity;
import Entities.UserProfileEntity;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.persistence.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Jonathan Botha
 */
public class DatabaseManager{
    public ArrayList<UserProfileEntity> profileList;
    public ArrayList<ResearchEntity> researchList;
    public ArrayList<PersonalResearchEntity> personalResearchList;
    
    private EntityManager entityManager;
    
    public DatabaseManager(){
        profileList = new ArrayList();
        researchList = new ArrayList();
        personalResearchList = new ArrayList();
        //Setup the persistance setting according to the computer drives
        Map<String, String> persistenceMap = new HashMap();
        persistenceMap.put("javax.persistence.jdbc.url", "jdbc:derby:"+ System.getenv("SystemDrive") +"\\Users\\Public\\BluLightDB;create=true");
        persistenceMap.put("javax.persistence.jdbc.user", "nbuser");
        persistenceMap.put("javax.persistence.jdbc.password", "nbuser");
        persistenceMap.put("javax.persistence.jdbc.driver", "org.apache.derby.jdbc.EmbeddedDriver");
        entityManager = Persistence.createEntityManagerFactory("BluLightCreationUnit",persistenceMap).createEntityManager();
        Query q = entityManager.createNativeQuery(" select * from SYS.SYSTABLES\n" +
                                            "where TABLETYPE = 'T'");//Checks what tables exist
        ArrayList<String> tables = new ArrayList();//Get tables in DB from Sys.SYSTables
        List<Object[]> sysTable = q.getResultList();
        for (Object[] row : sysTable) {
            tables.add((String)row[1]);
        }

        //<editor-fold defaultstate="collapsed" desc="Create ProfileTable">
        if (!tables.contains("TBLPROFILES")) {
            entityManager.getTransaction().begin();
            q  =entityManager.createNativeQuery("create table \"NBUSER\".TBLPROFILES\n" +
                    "(\n" +
                    "	ID VARCHAR(36) not null primary key,\n" +
                    "	SHARED BOOLEAN not null,\n" +
                    "	OWNER VARCHAR(35),\n" +
                    "	SETTINGRESOLUTION VARCHAR(9) default '800x600' not null,\n" +
                    "	SETTINGANTIALIASING BOOLEAN default true not null,\n" +
                    "	SETTINGPARTICLEAMOUNT SMALLINT default 2 not null,\n" +
                    "	FREEFALLHIGHSCORE SMALLINT default 0 not null,\n" +
                    "	FREEFALLPOINTS INTEGER default 0 not null,\n" +
                    "	FREEFALLDEATHS SMALLINT default 0 not null,\n" +
                    "	REFRACTIONHIGHSCORE SMALLINT default 0 not null,\n" +
                    "	REFRACTIONPOINTS INTEGER not null,\n" +
                    "	REFRACTIONDEATHS SMALLINT default 0 not null\n" +
                    ")");
            q.executeUpdate();
            entityManager.getTransaction().commit();
        }
//</editor-fold>
        
        boolean addBasicResearch = false;
        //<editor-fold defaultstate="collapsed" desc="Create Research Table">
        if (!tables.contains("TBLRESEARCH")) {
            entityManager.getTransaction().begin();
            q  =entityManager.createNativeQuery("create table \"NBUSER\".TBLRESEARCH\n" +
                    "(\n" +
                    "	ID VARCHAR(60) not null primary key,\n" +
                    "	RELATEDFORMULA VARCHAR(35),\n" +
                    "	TEXT LONG VARCHAR not null,\n" +
                    "	RATING DOUBLE default 5 not null,\n" +
                    "	VOTEAMOUNT INTEGER default 0 not null\n" +
                    ")");
            q.executeUpdate();
            addBasicResearch = true;
            entityManager.getTransaction().commit();
        }
//</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Create Personalised research Table">
        if (!tables.contains("TBLPERSONALISEDRESEARCH")) {
            entityManager.getTransaction().begin();
            q  =entityManager.createNativeQuery("create table \"NBUSER\".TBLPERSONALISEDRESEARCH\n" +
                    "(\n" +
                    "	TRANSACTIONID INTEGER not null primary key,\n" +
                    "	USERID VARCHAR(36) not null,\n" +
                    "	RESEARCHID VARCHAR(60) not null,\n" +
                    "	CATEGORY VARCHAR(20),\n" +
                    "	VOTED BOOLEAN not null,\n" +
                    "	DELETED BOOLEAN not null\n" +
                    ")");
            q.executeUpdate();
            entityManager.getTransaction().commit();
        }
//</editor-fold>
        
        entityManager = Persistence.createEntityManagerFactory("BluLightPU",persistenceMap).createEntityManager();
        
        Query profileQuery = entityManager.createNativeQuery("SELECT * FROM TBLPROFILES",UserProfileEntity.class);
        profileList.addAll(profileQuery.getResultList());
        Query researchQeury = entityManager.createNativeQuery("SELECT * FROM TBLRESEARCH",ResearchEntity.class);
        researchList.addAll(researchQeury.getResultList());
        Query qeury = entityManager.createNativeQuery("SELECT * FROM TBLPERSONALISEDRESEARCH",PersonalResearchEntity.class);
        personalResearchList.addAll(qeury.getResultList());
        //Adds basic research to new research table
        if (addBasicResearch) {
            try{
                Scanner scnFile = new Scanner(new File(DatabaseManager.class.getResource("/BluLight/BasicResearch.txt").getFile()));
                scnFile.useDelimiter("§");
                boolean fileValid = true;
                while (scnFile.hasNext() && fileValid) {
                    String[] segments = scnFile.next().split("¿");
                    if (segments.length == 5) {
                            ResearchEntity res = new ResearchEntity();
                            res.setId(segments[0].trim());
                            res.setRelatedformula(segments[1].trim());
                            res.setText(segments[2].trim());
                            res.setRating(Double.parseDouble(segments[3].trim()));
                            res.setVoteamount(Integer.parseInt(segments[4].trim()));
                            
                            try{
                                researchList.add(res);
                                entityManager.getTransaction().begin();
                                entityManager.persist(res);
                                entityManager.getTransaction().commit();
                                researchList.clear();
                                researchList.addAll(researchQeury.getResultList());
                            }catch(javax.persistence.RollbackException ex){
                                for (PersonalResearchEntity pr : personalResearchList) {
                                    if (pr.getUserid().equals(Main.curProfile.getId()) && pr.getResearchid().equals(res.getId())) {
                                        pr.setDeleted(false);
                                    }
                                }
                            }
                    }else{
                        JOptionPane.showMessageDialog(null, "Incorrect file format.", "Error", JOptionPane.ERROR_MESSAGE);
                        fileValid = false;
                    }
                }
                
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "File not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }catch (NumberFormatException | RollbackException | ArrayIndexOutOfBoundsException corr){
                JOptionPane.showMessageDialog(null, "Incorrect file format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshProfileList(){
        Query profileQuery = entityManager.createNativeQuery("SELECT * FROM TBLPROFILES",UserProfileEntity.class);
        profileList.clear();
        profileList.addAll(profileQuery.getResultList());
    }
    
    public void refreshPersonalResearchList(){
        Query qeury = entityManager.createNativeQuery("SELECT * FROM TBLPERSONALISEDRESEARCH",PersonalResearchEntity.class);
        personalResearchList.clear();
        personalResearchList.addAll(qeury.getResultList());
    }
    
    public void refreshResearchList(){
        Query researchQeury = entityManager.createNativeQuery("SELECT * FROM TBLRESEARCH",ResearchEntity.class);
        profileList.clear();
        researchList.addAll(researchQeury.getResultList());
    }
    
    public void refresh(){
        refreshResearchList();
        refreshProfileList();
        refreshPersonalResearchList();
    }
    
    public void addProfileRecord(UserProfileEntity user){
        profileList.add(user);
        entityManager.getTransaction().begin();
        entityManager.persist(user);
        entityManager.getTransaction().commit();
        refreshProfileList();
    }
    
    public void deleteProfileRecord(UserProfileEntity user){
        entityManager.getTransaction().begin();
        entityManager.remove(user);
        entityManager.getTransaction().commit();
        refreshProfileList();
    }
    
    public void updateRecords(){
        entityManager.getTransaction().begin();
        entityManager.getTransaction().commit();
        refresh();
    }
    
    public void addResearchRecord(ResearchEntity r){
        try{
        researchList.add(r);
        entityManager.getTransaction().begin();
        entityManager.persist(r);
        entityManager.getTransaction().commit();
        generatePersonalisedResearch();
        }catch(javax.persistence.RollbackException ex){
            for (PersonalResearchEntity pr : personalResearchList) {
                if (pr.getUserid().equals(Main.curProfile.getId()) && pr.getResearchid().equals(r.getId())) {
                    pr.setDeleted(false);
                }
            }
        }
    }
    
    public void deleteResearchRecord(ResearchEntity res){
        entityManager.getTransaction().begin();
        entityManager.remove(res);
        entityManager.getTransaction().commit();
        refreshResearchList();
    }
    
    private void addPRRecord(PersonalResearchEntity pr){
        personalResearchList.add(pr);
        entityManager.getTransaction().begin();
        entityManager.persist(pr);
        entityManager.getTransaction().commit();
        refreshProfileList();
    }
    
    public void deletePersonalResearchRecord(PersonalResearchEntity pr){
        entityManager.getTransaction().begin();
        entityManager.remove(pr);
        entityManager.getTransaction().commit();
        refreshPersonalResearchList();
    }
    
    public void voteResearch(ResearchEntity res, byte b){
        int prIndex = getPersonalResearchByResearchEntity(res);
        personalResearchList.get(prIndex).setVoted(true);
        double newRating = res.getRating()*res.getVoteamount();
        res.setVoteamount(res.getVoteamount()+1);
        newRating = (newRating + b)/(double)res.getVoteamount();
        res.setRating(newRating);
        updateRecords();
    }
    
    public int getPersonalResearchByResearchEntity(ResearchEntity res){
        int c = 0;
        while (!personalResearchList.get(c).getResearchid().equals(res.getId()) || !personalResearchList.get(c).getUserid().equals(Main.curProfile.getId())) {
            c++;
        }
        return c;
    }
    
    public int getPersonalResearchByResearchID(String id){
        int c = 0;
        while (!personalResearchList.get(c).getResearchid().equals(id) || !personalResearchList.get(c).getUserid().equals(Main.curProfile.getId())) {
            c++;
        }
        return c;
    }
    
    public int getResearchByID(String id){
        try{
            int c = 0;
            while (!researchList.get(c).getId().equalsIgnoreCase(id)) {
                c++;
            }
            return c;
        }catch(IndexOutOfBoundsException x){
            return -1;
        }
    }
    
    public int getUserByID(String id){
        try{
            int c = 0;
            while (!profileList.get(c).getId().equalsIgnoreCase(id)) {
                c++;
            }
            return c;
        }catch(IndexOutOfBoundsException x){
            return -1;
        }
    }
    
    public void generatePersonalisedResearch(){
        HashMap<String,ArrayList<String>> pResMap = new HashMap();
        //Loads current personal research
        for (PersonalResearchEntity pRes : personalResearchList.toArray(new PersonalResearchEntity[0])) {//Puts an array of research logged with users.
            if (!pResMap.containsKey(pRes.getUserid())) {
                pResMap.put(pRes.getUserid(), new ArrayList());
            }
            pResMap.get(pRes.getUserid()).add(pRes.getResearchid());
        }
        //Adds profiles that do not exist
        for (UserProfileEntity profile : profileList.toArray(new UserProfileEntity[0])) {
            if (!pResMap.containsKey(profile.getId())) {
                pResMap.put(profile.getId(), new ArrayList());
            }
            //Adds personal research for research that is ot personalised
            for (ResearchEntity res : researchList.toArray(new ResearchEntity[0])) {
                if (!pResMap.get(profile.getId()).contains(res.getId())) {
                    PersonalResearchEntity pr = new PersonalResearchEntity();
                    if (personalResearchList.isEmpty()) {
                        pr.setTransactionid(1);
                    }else{
                        pr.setTransactionid(personalResearchList.get(personalResearchList.size()-1).getTransactionid()+1);
                    }
                    pr.setUserid(profile.getId().trim());
                    pr.setResearchid(res.getId());
                    if (res.getId().substring(36).equals("74E6E225267C                BluLight")) {
                        pr.setCategory("BluLight");
                        pr.setVoted(true);
                    }else{
                        pr.setCategory(null);
                        pr.setVoted(false);
                    }
                    pr.setDeleted(false);
                    addPRRecord(pr);
                }
            }
        }
    }
    
    public void testDeletedResearch(){
        for (ResearchEntity res : researchList) {
            boolean alive = false;
            for (PersonalResearchEntity pr : personalResearchList) {
                if (pr.getResearchid().equals(res.getId()) && !pr.getDeleted()) {
                    alive = true;
                }
            }
            if (!alive) {
                for (PersonalResearchEntity pr : personalResearchList) {
                    if (pr.getResearchid().equals(res.getId())) {
                        deletePersonalResearchRecord(pr);
                    }
                }
                deleteResearchRecord(res);
            }
        }
    }
    
    public ArrayList<String> getUserIDs(){
        ArrayList<String> ids = new ArrayList();
        for (UserProfileEntity p : profileList) {
            ids.add(p.getId());
        }
        return ids;
    }
}
