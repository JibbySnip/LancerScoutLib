package com.finnbrenner.LancerScoutLib.EventStructures;

import com.finnbrenner.LancerScoutLib.MathTools.OPRCalculator;

import java.util.ArrayList;
import java.util.Date;

import static com.finnbrenner.LancerScoutLib.EventStructures.Alliance.AllianceColor.BLUE;

public class LancerEvent {
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

    private void updateOPR(double[][] data) {
        var oprArray = OPRCalculator.getOPR(data, teams, completedMatches, true);

        for (int t = 0; t < teams.size(); t++) {
             teams.get(t).setEventOPR(this,oprArray[t]);
         }
    }

    private void updateDPR(double[][] data) {
        var dprArray = OPRCalculator.getOPR(data, teams, completedMatches, false);

        for (int t = 0; t < teams.size(); t++) {
            teams.get(t).setEventDPR(this,dprArray[t]);
        }
    }

    private void updateCCWM() {
        for (Team t : teams){
            t.updateCCWM(this);
        }
    }

    public void updateBaseScoreStatistics(double[][] data) {
        updateOPR(data);
        updateDPR(data);
        updateCCWM();
    }


}
