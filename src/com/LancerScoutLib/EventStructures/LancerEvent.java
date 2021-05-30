/*
 * Library to support the development of FRC and FTC Scouting Applications
 *     Copyright (C) 2021  RoboLancers
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.LancerScoutLib.EventStructures;

import com.LancerScoutLib.Utils.BPRCalculator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * An extendable class for a per-season event. This is hopefully a decent model to build off of.
 */
public abstract class LancerEvent implements Serializable {
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

    /**
     * This updates a bunch of really useful statistics that I like a lot. You can read more about them online, and if you can't just email me (check the README file for my email)
     */
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

    /**
     * Just a utility function. You should add some of your own as well.
     * @param matchNumber
     * @return
     */
    public Match getMatch(int matchNumber) {
        return matches.get(matchNumber);
    }


}
