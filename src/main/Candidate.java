package main;
public class Candidate {
    String name;
    Integer id;
    boolean active;
    /*  Creates  a  Candidate  from  the  line.  The  line  will  have  the  format ID#,candidate_name . */
    public Candidate(String line){
        //This line gives me an array of the imput line separated by comas
        String[] fullString = line.split(",");
        //Assing Id and Name to variables
        id = Integer.parseInt(fullString[0]);
        name = fullString[1]; 
        active = true;
    }
    /**
     * This method will return the Id of the candidate
     * @return int id
     */
    public int getId(){
        return id;
    }
    /** This method determines whether the candidate is still active in the election 
     * @return bool active */ 
    public boolean isActive(){
        return active;
    }
    /**This setter is used during the electioin process to deactivate candidates who are no longer under consideration */
    public void setActive(Boolean valid){
        active = valid;
    }
    /** This method returns the name of the candidate */
    public String getName(){
        return name;
    }
}
