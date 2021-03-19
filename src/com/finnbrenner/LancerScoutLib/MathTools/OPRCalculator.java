package com.finnbrenner.LancerScoutLib.MathTools;

import com.finnbrenner.LancerScoutLib.EventStructures.Alliance;
import com.finnbrenner.LancerScoutLib.EventStructures.Match;
import com.finnbrenner.LancerScoutLib.EventStructures.Team;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;

public class OPRCalculator {


    private static double[][][] convertDataToTeamMatrix(double[][] data, ArrayList<Team> teams, ArrayList<Match> matches, boolean isOPR) {
        var numTeams = teams.size();
        var numMatches = matches.size();
        double[][] teamMatrix = new double[numMatches*2][numTeams];
        double[][] scoreMatrix = new double[numMatches*2][1];
        var scoreDone = false;

        for (int i = 0; i < numMatches*2; i++) { // Cycling along y-axis (first bracket set) by match
            scoreDone=false;
            for (int j = 0; j < numTeams; j++) { // Cycling along x-axis (second bracket set) by team
                var containsTeam = matches.get((int) Math.floor(i/2)).containsTeam(teams.get(j), (i % 2 == 0) ? Alliance.AllianceColor.RED : Alliance.AllianceColor.BLUE);
                teamMatrix[i][j] = containsTeam ? 1 : 0;
                if (!scoreDone) {
                    if (isOPR) {
                        scoreMatrix[i][1] = matches.get((int) Math.floor(i / 2)).getScoreFromTeam(teams.get(j));
                    } else {
                        scoreMatrix[i][1] = matches.get((int) Math.floor(i / 2)).getOpposingScoreFromTeam(teams.get(j));

                    }
                    scoreDone = true;
                }
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

    public static double[] getOPR(double[][] data, ArrayList<Team> teams, ArrayList<Match> matches, boolean isOPR) {

        var mMatrices = convertDataToTeamMatrix(data, teams, matches, isOPR);
        return getRegression(mMatrices[0],mMatrices[1]).regress().getParameterEstimates();
    }
}
