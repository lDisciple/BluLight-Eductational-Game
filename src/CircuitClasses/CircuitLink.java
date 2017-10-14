/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CircuitClasses;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Jonathan Botha
 */
public class CircuitLink {
    protected final ArrayList<String> connections;
    protected final ArrayList<byte[]> links;
    protected final ArrayList<Float> linkRes;
    protected final ArrayList<Boolean> activeLinks;
    
    public CircuitLink(){
        connections = new ArrayList();
        linkRes = new ArrayList();
        activeLinks = new ArrayList();
        links = new ArrayList();
    }
    
    public void addConnectionPoint(String name){
        connections.add(name);
    }
    
    private int getLinkIndex(byte start,byte end){
        for (byte i = (byte)0; i < links.size();i++) {
            if (start > end) {
                if (links.get(i)[1] == start && links.get(i)[0] == end ) {
                    return i;
                }
            }else{
                if (links.get(i)[0] == start && links.get(i)[1] == end ) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private int getLinkIndex(String name1,String name2){
        boolean exists1 = false;
        boolean exists2 = false;
        byte[] cons = new byte[2];
        for (byte i = 0; i < connections.size(); i++) {
            if (connections.get(i).equals(name1)) {
                if (exists2) {//Num order
                    cons[1] = i;
                }else{
                    cons[0] = i;
                }
                exists1 = true;
            }
            if (connections.get(i).equals(name2)) {
                if (exists1) {//Num order
                    cons[1] = i;
                }else{
                    cons[0] = i;
                }
                exists2 = true;
            }
        }
        if (exists1 && exists2) {
            for (int i = 0; i < links.size(); i++) {
                if (cons[0] == links.get(i)[0] && cons[1] == links.get(i)[1]) {
                    return i;
                }
            }
            return -1;
        }else{
            return -1;
        }
    }
    
    public boolean setLinkActive(String name1,String name2, boolean active){
        int linkIndex = this.getLinkIndex(name1, name2);
        if (linkIndex == -1) {
            return false;
        }else{
            activeLinks.set(linkIndex, active);
            return true;
        }
        
    }
    
    public ArrayList<byte[]> getLinks(){
        return links;
    }
    
    public ArrayList<String> getConnections(){
        return connections;
    }
    
    public ArrayList<Boolean> getActiveLinks(){
        return activeLinks;
    }
    
    public ArrayList<Float> getLinkResistances(){
        return linkRes;
    }
    
    public boolean genRandomPath(String start,String end){
        boolean exists1 = false;
        boolean exists2 = false;
        byte startIndex = 0;
        byte endIndex = 0;
        //Test existance
        for (byte i = 0; i < connections.size(); i++) {//Check of exists
            if (connections.get(i).equals(start)) {
                startIndex = i;
                exists1 = true;
            }
            if (connections.get(i).equals(end)) {
                endIndex = i;
                exists2 = true;
            }
        }
        if (exists1 && exists2) {//Get start paths
            ArrayList<byte[]> curLinks = new ArrayList();
            for (byte[] link : links) {
                if (link[0] == startIndex || link[1] == startIndex) {
                    curLinks.add(link);
                }
            }
            byte startPaths = (byte)(Math.round(Math.random()*(curLinks.size()-2))+2);
            
            for (int i = 0; i < startPaths; i++) {
                byte curBranch = startIndex;
                while (curBranch != endIndex && curBranch != -1 ) {
                    curBranch = randomBranch(curBranch);//Branch out
                }
            }
        }
        return exists1 && exists2;
    }
    
    private byte randomBranch(byte startIndex){
        byte curBranch;
        ArrayList<byte[]> curLinks = new ArrayList();
        curBranch = startIndex;
        byte numActive = 0;
        for (int i = 0; i < links.size(); i++) {//get links available
            if (links.get(i)[0] == curBranch || links.get(i)[1] == curBranch) {
                if (!activeLinks.get(i)) {//Non active links
                    if (links.get(i)[0] == curBranch) {//Checks that the path does no go backward
                        if (links.get(i)[1] != curBranch-1) {
                            curLinks.add(links.get(i));
                        }
                    }else{
                        if (links.get(i)[0] != curBranch-1) {
                            curLinks.add(links.get(i));
                        }
                    }
                }else{
                    numActive++;
                }
            }
        }
        if (numActive > 1 && curBranch != 3) {//Start index
            return -1;
        }
        if (!curLinks.isEmpty()) {
            byte linkIndex;
            do {//Get next point
                linkIndex = (byte)Math.round(Math.random()*(curLinks.size()-1));//Gets random value for next point
                linkIndex = (byte)this.getLinkIndex(curLinks.get(linkIndex)[0], curLinks.get(linkIndex)[1]);
                if (links.get(linkIndex)[0] == startIndex) {
                    activeLinks.set(this.getLinkIndex(curBranch,links.get(linkIndex)[1] ),true);
                    curBranch = links.get(linkIndex)[1];
                }else{
                    activeLinks.set(this.getLinkIndex(curBranch,links.get(linkIndex)[0] ),true);
                    curBranch = links.get(linkIndex)[0];
                }
                
            } while (!activeLinks.get(linkIndex));
            activeLinks.set(linkIndex,true);
            return curBranch;
        }else{
            return -1;
        }
        
    }
    
    private byte[] getCurActiveLinks(byte pt, boolean backTrack){
        ArrayList<Byte> curLinks = new ArrayList();
        for (byte i = 0; i < links.size(); i++) {//get links available
            if (links.get(i)[0] == pt || links.get(i)[1] == pt) {
                if (activeLinks.get(i)) {//active links
                    if (links.get(i)[0] == pt) {
                        if (links.get(i)[1] != pt-1 || backTrack) {//Checks that the path does no go backward
                            curLinks.add(i);
                        }
                    }else{
                        if (links.get(i)[0] != pt-1 || backTrack) {
                            curLinks.add(i);
                        }else{
                            if (links.get(i)[1] == pt && backTrack) {
                                curLinks.add(i);
                            }
                        }
                    }
                }
            }
        }
        byte[] arr = new byte[curLinks.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = curLinks.get(i);
        }
        return arr;
    }
    
    private ArrayList<byte[]> getAllCurLinks(byte pt){
        byte curBranch;
        ArrayList<byte[]> curLinks = new ArrayList();
        curBranch = pt;
        for (byte[] link : links) {//get links available
            if (link[0] == curBranch || link[1] == curBranch) {
                if (link[0] == curBranch) {//Checks that the path does no go backward
                    if (link[1] != curBranch-1) {
                        curLinks.add(link);
                    }
                }else{
                    if (link[0] != curBranch-1) {
                        curLinks.add(link);
                    }
                }
            }
        }
        return curLinks;
    }
    
    public boolean addLink(String name1,String name2, float resistance){
        boolean exists1 = false;
        boolean exists2 = false;
        byte[] cons = new byte[2];
        for (byte i = 0; i < connections.size(); i++) {
            if (connections.get(i).equals(name1)) {
                if (exists2) {//Num order
                    cons[1] = i;
                }else{
                    cons[0] = i;
                }
                exists1 = true;
            }
            if (connections.get(i).equals(name2)) {
                if (exists1) {//Num order
                    cons[1] = i;
                }else{
                    cons[0] = i;
                }
                exists2 = true;
            }
        }
        if (exists1 && exists2) {
            links.add(cons);
            activeLinks.add(false);
            linkRes.add(resistance);
        }
        return exists1 && exists2;
    }
    
    public boolean deleteLink(String name1,String name2){
        int linkIndex = this.getLinkIndex(name1, name2);
        if (linkIndex != -1) {
            links.remove(linkIndex);
            linkRes.remove(linkIndex);
            activeLinks.remove(linkIndex);
        }
        return linkIndex != -1;
    }
    
    public void resetActiveLinks(){
        for (int i = 0; i < activeLinks.size(); i++) {
            activeLinks.set(i, Boolean.FALSE);
        }
    }
    
    public float getCurrent(int voltage, String start, String end){
        boolean exists1 = false;
        boolean exists2 = false;
        byte startIndex = 0;
        byte endIndex = 0;
        for (byte i = 0; i < connections.size(); i++) {
            if (connections.get(i).equals(start)) {
                startIndex = i;
                exists1 = true;
            }
            if (connections.get(i).equals(end)) {
                endIndex = i;
                exists2 = true;
            }
        }
        if (exists1 && exists2) {
            ArrayList<Branch> branches = new ArrayList();
            branches.addAll(Arrays.asList(getBranches(startIndex,endIndex)));
            int c = 0;
            ///Remove branches with no end/start
            while (c < branches.size()) {
                boolean hasEnd = false;
                boolean hasStart = false;
                
                int c2 = 0;
                while (c2 < branches.size()) {
                    if (c2 != c) {
                        if (branches.get(c).getStartPt() == branches.get(c2).getEndPt()) {
                        hasStart = true;
                        }
                        if (branches.get(c2).getStartPt() == branches.get(c).getEndPt()) {
                            hasEnd = true;
                        }
                    }
                    if (branches.get(c).getStartPt() == startIndex) {
                        hasStart = true;
                    }
                    if (branches.get(c).getEndPt() == endIndex) {
                        hasEnd = true;
                    }
                    c2++;
                }
                if (hasEnd && hasStart && c < branches.size()) {
                    c++;
                }else{
                    branches.remove(c);
                }
            }
                
            
            final ArrayList<Branch> tempBranches = new ArrayList();
            boolean changesMade = true;
            while (changesMade) {
                changesMade = false;
                //<editor-fold defaultstate="collapsed" desc="Remove dupes">
                tempBranches.addAll(branches);
                branches.clear();

                while (!tempBranches.isEmpty()) {
                    byte startPt = tempBranches.get(0).getStartPt();
                    byte endPt = tempBranches.get(0).getEndPt();
                    float res = 0f;

                    for (Branch tb : tempBranches.toArray(new Branch[0])) {
                        if (tb.getStartPt() == startPt && endPt == tb.getEndPt()) {
                            res += 1f/tb.getTotalResistance();
                            if (!tb.equals(tempBranches.get(0))) {
                                changesMade = true;
                            }
                            tempBranches.remove(tempBranches.indexOf(tb));
                        }

                    }
                    Branch br = new Branch();
                    br.setStartPt(startPt);
                    br.setEndPt(endPt);
                    br.setTotalResistance(1/res);
                    branches.add(br);
                }
    //</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="Combine branches">
                tempBranches.clear();
                tempBranches.addAll(branches);
                branches.clear();

                c = 0;
                while (!tempBranches.isEmpty()) {
                    float res = tempBranches.get(c).getTotalResistance();
                    byte startPt = tempBranches.get(c).getStartPt();
                    byte endPt = tempBranches.get(c).getEndPt();

                    int c2 = 0;
                    while (c2 < tempBranches.size()) {
                        if (tempBranches.get(c).getStartPt() == startIndex) {
                            if (tempBranches.get(c2).getStartPt() == tempBranches.get(c).getEndPt() && tempBranches.get(c2).getEndPt() == endIndex) {
                                endPt = tempBranches.get(c2).getEndPt();
                                res += tempBranches.get(c2).getTotalResistance();

                                tempBranches.remove(c2);
                                changesMade = true;
                            }else{
                                c2++;
                            }
                        }else{
                            if (tempBranches.get(c2).getStartPt() == tempBranches.get(c).getEndPt()) {
                                endPt = tempBranches.get(c2).getEndPt();
                                res += tempBranches.get(c2).getTotalResistance();

                                tempBranches.remove(c2);
                                changesMade = true;
                            }else{
                                c2++;
                            }
                        }
                    }
                    tempBranches.remove(c);

                    Branch br = new Branch();
                    br.setStartPt(startPt);
                    br.setEndPt(endPt);
                    br.setTotalResistance(res);
                    branches.add(br);
                }
    //</editor-fold>

            }
            //<editor-fold defaultstate="collapsed" desc="Single out the true path">
            c = 0;
            Branch finalBranch = null;
            while (c < branches.size() && finalBranch == null) {
                if (branches.get(c).getStartPt() == startIndex && branches.get(c).getEndPt() == endIndex) {
                    finalBranch = branches.get(c);
                }
                c++;
            }
//</editor-fold>
            if (finalBranch == null) {
                return 0;
            }else{
                return voltage/finalBranch.getTotalResistance();
            }
        }
        
        
        
        return 0;
        
    }
    
    
    public void clear(){
        linkRes.clear();
        activeLinks.clear();
        connections.clear();
    }
    
    public Branch[] getBranches(byte start, byte end){
        ArrayList<Branch> br = new ArrayList();
        byte[] startLinks = getCurActiveLinks(start,true);
        ArrayList<Byte> branchToGet = new ArrayList();//Prevents the 4/7 going to make a 7/3 branch (Contact Dev for explanation)
        for (byte startLink : startLinks) {
            ArrayList<Byte> curLinks = new ArrayList();
            byte curLink;
            byte prevLink = start;
            boolean inBranch = true;
            Branch branch = new Branch();
            branch.setStartPt(start);
            branch.setTotalResistance(linkRes.get(startLink));
            
            activeLinks.set(startLink, false);
            
            if (links.get(startLink)[0] == prevLink) {//Set the next point as cur
                curLink = links.get(startLink)[1];
            }else{
                curLink = links.get(startLink)[0];
            }
            
            while (inBranch && curLink != end) {
                int linkIndex = this.getLinkIndex(curLink, prevLink);
                curLinks.clear();
                for (byte cl : getCurActiveLinks(curLink,true)) {// get cur links
                    if (cl != linkIndex) {
                        curLinks.add(cl);
                    }
                }
                if (curLinks.size() == 1 && !branchToGet.contains(curLink)) {
                    if (links.get(curLinks.get(0))[0] == curLink) {//Set the next point as cur
                        prevLink = links.get(curLinks.get(0))[0];
                        curLink = links.get(curLinks.get(0))[1];
                    }else{
                        prevLink = links.get(curLinks.get(0))[1];
                        curLink = links.get(curLinks.get(0))[0];
                    }
                    activeLinks.set(curLinks.get(0), false);
                    branch.setTotalResistance(branch.getTotalResistance() + linkRes.get(curLinks.get(0)));
                }else{
                    inBranch = false;
                    if (curLinks.size() > 1) {
                        activeLinks.set(linkIndex, false);
                        branchToGet.add(curLink);
                    }
                }
            }
            branch.setEndPt(curLink);
            br.add(branch);
        }
        
        for (int i = 0; i < branchToGet.size()-1; i++) {//Remove dupes
            for (int j = i+1; j < branchToGet.size(); j++) {
                if (branchToGet.get(i).equals(branchToGet.get(j))) {
                    branchToGet.set(j, (byte)-1);//Flag is -1
                }
            }
        }
        while (branchToGet.indexOf((byte)-1) != -1) {
            branchToGet.remove(branchToGet.indexOf((byte)-1));
        }
        for (Byte btg : branchToGet) {
            for (Branch btgb : getBranches(btg,end)) {
                br.add(btgb);
            }
        }
        
        return br.toArray(new Branch[0]);
    }
    
    @Override
    public String toString(){
        String s = "";
        for (int i = 0; i < links.size(); i++) {
            s += links.get(i)[0] + " (" + links.get(i)[0] + ")"+
                    " - " + links.get(i)[1] + " (" + links.get(i)[1] + ") :  Active/"+  activeLinks.get(i) + " Res/" + linkRes.get(i) + "\n";
            
        }
       return s; 
    }
    
    @Override
    public CircuitLink clone(){
        CircuitLink cl = new CircuitLink();
        cl.connections.addAll(connections);
        cl.links.addAll(links);
        cl.activeLinks.addAll(activeLinks);
        cl.linkRes.addAll(linkRes);
        return cl;
    }
    
    public class Branch{
        private byte startPt,endPt;
        private float totalRes;
        
        /**
        *Sets the object's startPt.
        * @param  newStartPt new startPt
        * */
        public void setStartPt (byte newStartPt){
                startPt = newStartPt;
        }

        /**
        *Gets the object's startPt.
        * @return  The object's startPt.
        * */
        public byte getStartPt (){
                return startPt;
        }

        /**
        *Sets the object's endPt.
        * @param  newEndPt new endPt
        * */
        public void setEndPt (byte newEndPt){
                endPt = newEndPt;
        }

        /**
        *Gets the object's endPt.
        * @return  The object's endPt.
        * */
        public byte getEndPt (){
                return endPt;
        }
        
        public float getTotalResistance (){
            return totalRes;
        }
        
        public void setTotalResistance (float res){
            totalRes = res;
        }
    }
}
