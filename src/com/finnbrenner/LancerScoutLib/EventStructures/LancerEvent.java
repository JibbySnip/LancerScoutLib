package com.finnbrenner.LancerScoutLib.EventStructures;

import com.finnbrenner.LancerScoutLib.MathTools.BPRCalculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class LancerEvent implements Serializable {
    private final int teamsPerAlliance;
    private final ArrayList<Team> teams = new ArrayList<>();
    private final ArrayList<Match> matches = new ArrayList<>();
    private final ArrayList<Match> completedMatches = new ArrayList<>();
    public final String eventName;
    public final Date eventStartDate;

    public LancerEvent(int teamsPerAlliance, String eventName, Date eventStartDate) {
        this.teamsPerAlliance = teamsPerAlliance;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
    }

    private void updateOPR() {
        // Assemble OPR data matrix
        int[][] oprData = new int[matches.size()][2];
        for (int i = 0; i < matches.size(); i++) {
            oprData[i][0] = matches.get(i).getRedScore();
            oprData[i][1] = matches.get(i).getBlueScore();
        }
        double[] oprArray = BPRCalculator.getBPR(oprData, teams, completedMatches);

        for (int t = 0; t < teams.size(); t++) {
             teams.get(t).setEventOPR(this,oprArray[t]);
         }
    }

    private void updateDPR() {
        int[][] dprData = new int[matches.size()][2];
        for (int i = 0; i < matches.size(); i++) {
            dprData[i][0] = matches.get(i).getBlueScore();
            dprData[i][1] = matches.get(i).getRedScore();
        }
        double[] dprArray = BPRCalculator.getBPR(dprData, teams, completedMatches);

        for (int t = 0; t < teams.size(); t++) {
            teams.get(t).setEventDPR(this,dprArray[t]);
        }
    }

    private void updateCCWM() {
        for (Team t : teams){
            t.updateCCWM(this);
        }
    }

    private void updateBaseScoreStatistics() {
        updateOPR();
        updateDPR();
        updateCCWM();
    }

    /**
     * Updates the event scores based on new score data
     * @param data A 2D array with matches on the y-axis and alliance scores ([red, blue] should be the order!) on the x-axis
     */
    public void updateScores(int[][] data) {
        assert (data.length == teams.size());
        for (int i = 0; i < data.length; i++) {
            getMatch(i).setScores(data[i][0], data[i][1]);
        }
    }

    public void addMatch(int matchNumber, Alliance allianceRed, Alliance allianceBlue) {
        matches.add(new Match(matchNumber, allianceRed, allianceBlue, this));
    }

    public Match getMatch(int matchNumber) {
        return matches.get(matchNumber);
    }


}
