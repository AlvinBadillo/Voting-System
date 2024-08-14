package main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.nio.file.Paths;
import java.util.function.Function;

import data_structures.ArrayList;
import interfaces.List;

/**My election class will performe the election with all the required specifications
 * It contains many elements that allow to keep track of all relevant data 
 * It includes the following:
 *      - all ballots: list of ballots that contain all the ballots passed when constructed
 *      - ballots: this is a second list of ballots, but this one only contains all the ballots that will be used in the election(valid ballots)
 *      - candidates: this is a list of candidates, it stores all the candidates in any given election
 *        it is used to keep track of what candidates are eliminated as the election ocurrs
 *      - eliminated list: this is of type string, it contains all the names of eliminated candidates
 *        it is used during the getWinner method, as the method searches for a winner, every eliminated candidate will be stored in this list
 *      - list of list: This is a list that contains lists of ballots, this is the main component of the election logic
 *        The way I implemented this list of list is that each index of the outer list will represent a candidate 
 *        and each inner list will contain a list of ballots, where each ballot within that list is a ballot whos highes rank candidate 
 *        corresponds to the candidates index
 *  
 */

public class Election {
    /*each index has a list of ballots, the ballots in index 0 are the ballots that have candidate 1 as rank 1
    index 2 has the ballots where candidate 2 is rank 1 and so on */
    List<ArrayList<Ballot>> listOfLists = new ArrayList<>();

    List<Candidate> candidates = new ArrayList<>();
    List<Ballot> ballots = new ArrayList<>();
    List<String> eliminatedList = new ArrayList<String>();
    List<Ballot> allBallots = new ArrayList<>();
    /* Constructor that implements the election logic using the files candidates.csv and ballots.csv as input. (Default constructor) */
    int winnerVotes = 0;
    public Election() {
        try{
            /*reading candidate files and storing them*/
            BufferedReader candidateReader = new BufferedReader(new FileReader("inputFiles/candidates.csv"));
            String line;
            while((line = candidateReader.readLine()) != null){
                candidates.add(new Candidate(line));
            }
            candidateReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            /*reading ballots files and stroing them */
            BufferedReader ballotReader = new BufferedReader(new FileReader("inputFiles/ballots.csv"));
            //BufferedReader ballotReader = new BufferedReader(new FileReader("inputFiles/testb.csv"));
            String line2;
            int index = 0;
            while((line2 = ballotReader.readLine()) != null){
                allBallots.add(index,new Ballot(line2, candidates));
                index++;
            }
            ballotReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*populate listOfLists with lists*/
        for (int i = 0; i < candidates.size(); i++) {
            listOfLists.add(new ArrayList<>());
        }
        /*now that i have both of the imputs saved, i want to make 2D array with them*/
        for(int i = 0; i < allBallots.size(); i++){
            if(allBallots.get(i).getBallotType() == 0){
                int indexToAdd = allBallots.get(i).ranks.get(0).getId() - 1;
                listOfLists.get(indexToAdd).add(allBallots.get(i));
                ballots.add(allBallots.get(i));
            }
        }
        /*Here we will make output files*/
        String winner = this.getWinner();
        String adjustedWinner = winner.replace(' ', '_').toLowerCase();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("outputFiles/" + adjustedWinner + winnerVotes + ".txt"));
            writer.write("Number of ballots: " + this.getTotalBallots());
            writer.write("\nNumber of blank ballots: " + this.getTotalBlankBallots());
            writer.write("\nNumber of invalid ballots: " + this.getTotalInvalidBallots());
            int round = 1;
            for(int i = 0; i < eliminatedList.size(); i++){
                String[] name_votes = eliminatedList.get(i).split("-");
                writer.write("\nRound " + round + ": " + name_votes[0] + " was eliminated with " + name_votes[1] + " #1's");
                round++;
            }
            writer.write("\nWinner: " + winner + " wins with " + winnerVotes + " #1's");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* Constructor that receives the name of the candidate and ballot files and applies the election logic. Note: The files should be found in the input folder. */ 
    public Election(String candidates_filename, String ballot_filename){
        try{
            /*reading candidate files and storing them*/
            BufferedReader candidateReader = new BufferedReader(new FileReader("inputFiles/" + candidates_filename));
            String line;
            while((line = candidateReader.readLine()) != null){
                candidates.add(new Candidate(line));
            }
            candidateReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            /*reading ballots files and stroing them*/
            BufferedReader ballotReader = new BufferedReader(new FileReader("inputFiles/" + ballot_filename));
            //BufferedReader ballotReader = new BufferedReader(new FileReader("inputFiles/testb.csv"));
            String line2;
            int index = 0;
            while((line2 = ballotReader.readLine()) != null){
                allBallots.add(index,new Ballot(line2, candidates));
                index++;
            }
            ballotReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*populate listOfLists with lists*/
        for (int i = 0; i < candidates.size(); i++) {
            listOfLists.add(new ArrayList<>());
        }
        /*now that i have both of the imputs saved, i want to make 2D array with them*/
        for(int i = 0; i < allBallots.size(); i++){
            if(allBallots.get(i).getBallotType() == 0){
                int indexToAdd = allBallots.get(i).ranks.get(0).getId() - 1;
                listOfLists.get(indexToAdd).add(allBallots.get(i));
                ballots.add(allBallots.get(i));
            }
        }

        /*Here we wil make output files*/
        String winner = this.getWinner();
        String adjustedWinner = winner.replace(' ', '_').toLowerCase();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("outputFiles/" + adjustedWinner + winnerVotes + ".txt"));
            writer.write("Number of ballots: " + this.getTotalBallots());
            writer.write("\nNumber of blank ballots: " + this.getTotalBlankBallots());
            writer.write("\nNumber of invalid ballots: " + this.getTotalInvalidBallots());
            int round = 1;
            for(int i = 0; i < eliminatedList.size(); i++){
                String[] name_votes = eliminatedList.get(i).split("-");
                writer.write("\nRound " + round + ": " + name_votes[0] + " was eliminated with " + name_votes[1] + " #1's");
                round++;
            }
            writer.write("\nWinner: " + winner + " wins with " + winnerVotes + " #1's");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* This method will run the election, it will return a string with the name of the candidate
     * This is an alternative voting system where the winner is not only chossen on who has the most votes, it takes account of 
     * all the rating of all candidate and all voters. The way Ive implemented this method is storing all the votes on a list of list
     * This list of list will contain all ballots and will be stored in a manner that will allow to move all the ballots as needed
     * As this method runs it will keep track of all candidates, the votes they had when eliminated and of course the winner with how many votes
    */
    public String getWinner(){
        int counter = 0;
        while(true){
            /*lets first check if any candidate has more than 50% of the votes*/
            for(int i = 0; i < listOfLists.size(); i++){
                if(listOfLists.get(i).size() > ballots.size() / 2){
                    winnerVotes = listOfLists.get(i).size();
                    return candidates.get(i).getName();
                }
            }
            /*if no one has more than 50% then we eliminate the candidate with the lowest amout of 1s*/
            List<Integer> temp = new ArrayList<>();
            int minVotes = 0;
            int i = 0;
            while(i < listOfLists.size()) {
                if(candidates.get(i).isActive()){
                    minVotes = listOfLists.get(i).size();
                    temp.add(i);
                    i++;
                    break;
                }
                i++;
            }
            while(i < listOfLists.size()){
                if(candidates.get(i).isActive()){
                    /*this if handels when we found a smaller list */
                    if(listOfLists.get(i).size() < minVotes){
                        temp.clear();
                        temp.add(i);
                        minVotes = listOfLists.get(i).size();
                    }
                    else if(listOfLists.get(i).size() == minVotes) {
                        temp.add(i);
                    }
                }
                i++;
            }
            boolean elimCan = false;
            int rankPos = 0;
            while (!elimCan){
                /*set candidates as inactive
                remove the top candidate of ballots that had rank 1 as candidate to eliminate
                relocate inner list to new indec of corresponding rank*/
                if(temp.size() == 1){
                    candidates.get(temp.get(0)).setActive(false);
                    eliminatedList.add(candidates.get(temp.get(0)).getName() + '-' + listOfLists.get(temp.get(0)).size());
                    /* this iterates through the candidates list who we want to eliminate*/
                    while(listOfLists.get(temp.get(0)).size() >= 1){
                        listOfLists.get(temp.get(0)).get(0).getRanks().remove(0);
                        int tempo = 0;
                        while(!listOfLists.get(temp.get(0)).get(0).getRanks().get(tempo).isActive()){
                            tempo++;
                        }
                        listOfLists.get(listOfLists.get(temp.get(0)).get(0).getRanks().get(tempo).getId() - 1).add(listOfLists.get(temp.get(0)).get(0));
                        listOfLists.get(temp.get(0)).remove(0);
                        elimCan = true;
                    }
                }
                else{
                    /*if there is a tie we want to eliminate one of those candidates
                    if there is another tie within thoes candidates we want to eliminate based on the next lower level of votes
                    if there are ties we have to compare the next rank of each candidate that are currently tied
                    The outer loop iterates through the temp list which has the ids of the tied candidates
                    the inner loop goes through every ballot and adding 1 to totalSecondVotes if in the current ballot the second vote goes to the current candidate
                    Note: the ids of the candidates that are tied are stored in temp, the second places votes are stored in totalSecondVotes, 
                    the number in totalSecondVotes[i] corresponds to the votes of candidate at temp[i]*/   
                    rankPos++;  
                    if(rankPos >= candidates.size()){
                        /*we have to eliminate candidate with highest id*/
                        temp.remove(temp.size());

                    }  
                    else{
                        List<Integer> totalNextVotes = new ArrayList<>();
                        for(int a = 0; a < temp.size(); a++){
                            totalNextVotes.add(0);
                        }
                        /*we are counting the votes of the next lower level and storing them in totalNextVotes*/
                        for(i = 0; i < temp.size(); i++){
                            for(int j = 0; j < ballots.size(); j++){ 
                                int tempRank = rankPos;
                                while(!ballots.get(j).getRanks().get(tempRank).isActive()){
                                    tempRank++;
                                }
                                if(ballots.get(j).getRanks().get(tempRank).getId() - 1 == temp.get(i)){
                                    totalNextVotes.set(i, totalNextVotes.get(i) + 1);
                                }    
                            }
                        }
                        /*now we eliminate the candidate with the fewest votes
                        temp2 is storing ids of tied candidates*/
                        int min = totalNextVotes.get(0);
                        List<Integer> temp2 = new ArrayList<>();
                        temp2.add(temp.get(0));
                        for(int j = 1; j < temp.size(); j++){
                            if(totalNextVotes.get(j) < min){
                                temp2.clear();
                                temp2.add(temp.get(j));
                                min = totalNextVotes.get(j);
                            }
                            else if (totalNextVotes.get(j) == min){
                                temp2.add(temp.get(j));
                            }
                        }
                        temp = temp2;
                    }
                }
            }
        }         
    }
    /*Returns the total amount of ballots submitted*/
    public int getTotalBallots(){
        return allBallots.size();
    }
    /*Returns the total amount of invalid ballots */
    public int getTotalInvalidBallots(){
        int count = 0;
        for(int i = 0; i < allBallots.size(); i++){
            if(allBallots.get(i).getBallotType() == 2){
                count++;
            }
        }
        return count;
    }
    /*Returns the total amount of blank ballots*/
    public int getTotalBlankBallots(){
        int count = 0;
        for(int i = 0; i < allBallots.size(); i++){
            if(allBallots.get(i).getBallotType() == 1){
                count++;
            }
        }
        return count;
    }
    /*Returns the total amount of valid ballots*/
    public int getTotalValidBallots(){
        return ballots.size();
    }
    /* List of names for the eliminated candidates with the numbers of 1s they had, must be in order of elimination. 
    Format should be <candidate name>-<number of 1s when eliminated>*/
    public List<String> getEliminatedCandidates(){
        List<String> result = new ArrayList<String>();
        result = eliminatedList;
        return result;
    }
    /*This method will return the list of list, it was used duting the get winner for accesing any given value withn it */
    public List<ArrayList<Ballot>> getListOfList() {
        return listOfLists;
    }
}

