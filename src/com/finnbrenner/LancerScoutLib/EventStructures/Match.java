package com.finnbrenner.LancerScoutLib.EventStructures;

public class Match {
    public enum MatchRanking {
        SEED,
        SEMIFINALS,
        FINALS
    }
    private Alliance[] alliances;
    private int[] scores = new int[2];
    private Alliance winner;

    public Match(Alliance allianceRed, Alliance allianceBlue) {
        alliances = new Alliance[]{allianceRed,allianceBlue};
    }

    /**
     * Set the scores for a match
     * @param allianceRedScore Overall score for the red team
     * @param allianceBlueScore Overall score for the blue team
     */
    public void setScores(int allianceRedScore, int allianceBlueScore) { //Todo: migrate to detailed scoring system.
        scores[0] = allianceRedScore;
        scores[1] = allianceBlueScore;
                
    }
    
    public Alliance getWinner(){
        return winner;
    }

}
