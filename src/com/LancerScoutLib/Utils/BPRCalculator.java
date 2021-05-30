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

package com.LancerScoutLib.Utils;

import com.LancerScoutLib.EventStructures.Alliance;
import com.LancerScoutLib.EventStructures.Match;
import com.LancerScoutLib.EventStructures.Team;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;

/**
 * BPR is breakdown power rating. It refers to any sort of OPR-like metric solved using linear algebra. This can be used for component OPR as well.
 */
public class BPRCalculator {
    /**
     *This is cool! You shouldn't really touch this method but I like it a fair bit.
     * @param data Make sure this always goes red, blue for the x-axis.
     * @param teams Teams involved.
     * @param matches All the matches to run. You can do this as a competition progresses
     * @return
     */
    protected static double[][][] convertDataToTeamMatrix(int[][] data, ArrayList<Team> teams, ArrayList<Match> matches) {

        int numTeams = teams.size();
        int numMatches = matches.size();
        double[][] teamMatrix = new double[numMatches*2][numTeams];
        double[][] scoreMatrix = new double[numMatches*2][1];

        for (int i = 0; i < numMatches; i++) { // Cycling along y-axis (first bracket set) by match
            for (int j = 0; j < numTeams; j++) { // Cycling along x-axis (second bracket set) by team
                boolean containsTeamRed = matches.get(i).containsTeam(teams.get(j), Alliance.AllianceColor.RED);
                boolean containsTeamBlue = matches.get(i).containsTeam(teams.get(j), Alliance.AllianceColor.BLUE);
                teamMatrix[i*2][j] = containsTeamRed ? 1 : 0;
                teamMatrix[i*2+1][j] = containsTeamBlue ? 1 : 0; // sure hope this doesn't give an arrayindexoutofbounds
                scoreMatrix[i*2][1] = data[i][0];
                scoreMatrix[i*2+1][1] = data[i][1];
            }
        }
        return new double[][][]{teamMatrix, scoreMatrix};
    }

    public static double[] getColumn(double[][] array, int index) {
        double[] column = new double[array[0].length]; // Here I assume a rectangular 2D array!
        for (int i = 0; i < column.length; i++) {
            column[i] = array[i][index];
        }
        return column;
    }

    public static SimpleRegression getRegression(double[][] teamMatrix, double[][] scoreMatrix) {
        SimpleRegression regression = new SimpleRegression(false);
        regression.addObservations(teamMatrix, getColumn(scoreMatrix, 0));
        return regression;
    }

    /**
     * Get a BPR rating for a certain component of the matches. This is cool!
     * @param data 2D array of matches on the y-axis and alliances on the x. The intersection is the metric to be BPR'd.
     * @param teams teams!
     * @param matches matches!
     * @return array of results by team or something idk i want to go to sleep
     */
    public static double[] getBPR(int[][] data, ArrayList<Team> teams, ArrayList<Match> matches) {

        double[][][] mMatrices = convertDataToTeamMatrix(data, teams, matches);
        return getRegression(mMatrices[0],mMatrices[1]).regress().getParameterEstimates();
    }
}
