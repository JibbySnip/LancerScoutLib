package com.finnbrenner.LancerScoutLib.MathTools;

import com.finnbrenner.LancerScoutLib.EventStructures.Alliance;
import com.finnbrenner.LancerScoutLib.EventStructures.Match;
import com.finnbrenner.LancerScoutLib.EventStructures.Team;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.ArrayList;

/**
 * BPR is breakdown power rating. It refers to any sort of OPR-like metric solved using linear algebra. This can be used for component OPR as well.
 */
public class BPRCalculator {
    /**
     *
     * @param data Make sure this always goes red, blue
     * @param teams
     * @param matches
     * @return
     */
    private static double[][][] convertDataToTeamMatrix(int[][] data, ArrayList<Team> teams, ArrayList<Match> matches) {

        int numTeams = teams.size();
        int numMatches = matches.size();
        double[][] teamMatrix = new double[numMatches*2][numTeams];
        double[][] scoreMatrix = new double[numMatches*2][1];

        for (int i = 0; i < numMatches; i++) { // Cycling along y-axis (first bracket set) by match
            for (int j = 0; j < numTeams; j++) { // Cycling along x-axis (second bracket set) by team
                boolean containsTeamRed = matches.get(i).containsTeam(teams.get(j), Alliance.AllianceColor.RED);
                boolean containsTeamBlue = matches.get(i).containsTeam(teams.get(j), Alliance.AllianceColor.BLUE);
                teamMatrix[i*2][j] = containsTeamRed ? 1 : 0;
                teamMatrix[i*2+1][j] = containsTeamBlue ? 1 : 0;
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

    public static double[] getBPR(int[][] data, ArrayList<Team> teams, ArrayList<Match> matches) {

        double[][][] mMatrices = convertDataToTeamMatrix(data, teams, matches);
        return getRegression(mMatrices[0],mMatrices[1]).regress().getParameterEstimates();
    }
}
