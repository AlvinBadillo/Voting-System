package main;
import java.util.Comparator;

import data_structures.ArrayList;
import interfaces.List;

/**This class will have all the elements related to a ballot
 * It will include the ballot number and the candidates along with all the votes
 * The implementation that Ive decided to use is that each ballot object will contain a list of candidates
 * This list will hold all candidates and the index of where they are stored will be the vote recived
 */
public class Ballot {
    Integer ballotNum;
    boolean missing_rank = false;
    boolean isEmpty = false;

    List<Candidate> ranks = new ArrayList<Candidate>(5);
    int ballotType = 0;

    /* Creates a ballot based on the line it receives. The format for line is
    id#,candidate_name . It also receives a List of all the candidates in the
    elections.*/
    public Ballot(String line, List<Candidate> candidates){

        /*this is an array of strings, it separates strings every , */
        String[] fullString = line.split(",");
        ballotNum = Integer.parseInt(fullString[0]);

        //this looks to see if ballot will be empty
        if(fullString.length == 1){
            isEmpty = true;
        }

        /*loop to make list of ranks(Index) and candidates (value)*/
        for(int i = 1; i < fullString.length; i++){
            String[] id_rank = fullString[i].split(":");
            /*this verifies that the rank is the same as the index + 1. If its not the same, it means that there are missing ranks*/
            if(Integer.parseInt(id_rank[1]) != i){
                missing_rank = true;
            }        
            Integer id = Integer.parseInt(id_rank[0]);
            /*this for loop looks for candidate and adds it to the array list, the first person added is the one with the higest rank.*/
            for(int j = 0; j < candidates.size(); j++){
                if(candidates.get(j).getId() == id){
                    ranks.add(candidates.get(j));
                }
            }
            
        }
    }
    /**This method returns the ballot number */
    public int getBallotNum(){
        return ballotNum;
    }
    /**Returns the rank for that candidate, if no rank is available return -1*/
    public int getRankByCandidate(int candidateID){
        for(int i = 0; i < ranks.size(); i++){
            if(ranks.get(i).getId() == candidateID){
                return i + 1;
            }
        }
        return -1;
    }
    /**Returns the candidate with that rank, if no candidate is available return -1.*/
    public int getCandidateByRank(int rank){
        /*first we verify that rank is valid*/
        if(rank < 1 || rank > ranks.size()){
            return -1;
        }
        /*sice the rank is index + 1 we simply return the id of tha candidate of the given rank. 
        Ex: if rank = 2, we return candidate at index 1 */
        return ranks.get(rank - 1).getId();
    }
    //*This method will eliminate the candidate passed as a parameter */
    public boolean eliminate(int candidateId){
        for(int i = 0; i < ranks.size(); i++){
            if(ranks.get(i).getId() == candidateId){
                ranks.remove(i);
                return true;
            }
        }
        return false;
    }
    /* Returns an integer that indicates if the ballot is: 0 – valid, 1 – blank or 2 -
    invalid */
    public int getBallotType(){        
        /*this checks if its empty or not*/
        if(isEmpty){
            ballotType = 1;
            return 1;
        }
        /*this checks if there are missing ranks*/
        if(missing_rank){ 
            ballotType = 2;
            return 2;
        }
        /* to verify if there are any duplicates*/
        for(int i = 0; i < ranks.size(); i++){
            for(int j = i + 1; j < ranks.size(); j++){
                if(ranks.get(i) == ranks.get(j)){
                    /*we found a duplicate so its invalid*/
                    ballotType = 2;
                    return 2;
                }
            }
        }
        ballotType = 0;
        return 0;
    }
    public List<Candidate> getRanks() {
        return ranks;
    }
}
