package com.finnbrenner.LancerScoutLib.EventStructures;

public class Match {
    public enum MatchRanking {
        SEED,
        SEMIFINALS,
        FINALS
    }

    private Alliance[] alliances; // goes Red, Blue
    private int[] scores = new int[2];
    private Alliance winner;
    private final LancerEvent event;

    public Match(Alliance allianceRed, Alliance allianceBlue, LancerEvent event) {
        alliances = new Alliance[]{allianceRed, allianceBlue};
        this.event = event;
    }

    /**
     * Set the scores for a match
     *
     * @param allianceRedScore  Overall score for the red team
     * @param allianceBlueScore Overall score for the blue team
     */
    public void setScores(int allianceRedScore, int allianceBlueScore) { //Todo: migrate to detailed scoring system.
        scores[0] = allianceRedScore;
        scores[1] = allianceBlueScore;

    }

    public Alliance getWinner() {
        return winner;
    }

    public int getScoreFromTeam(Team t) {
        for (int a = 0; a < alliances.length; a++) {
            if (alliances[a].getTeams().contains(t)) {
                return scores[a];
            }
        }
        return -1;
    }
    public int getOpposingScoreFromTeam(Team t) {
        for (int a = 0; a < alliances.length; a++) {
            if (alliances[a].getTeams().contains(t)) {
                return scores[a == 0 ? 1 : 0];
            }
        }
        return -1;
    }

    public boolean containsTeam(Team t, Alliance.AllianceColor allianceColor) {
        var a = alliances[(allianceColor == Alliance.AllianceColor.RED) ? 0 : 1];
        if (a.getTeams().contains(t)) {
                return true;
        }
        return false;
    }

    Alliance.AllianceColor teamColor(Team t) {
        if (containsTeam(t, Alliance.AllianceColor.RED)) return Alliance.AllianceColor.RED;
        else if (containsTeam(t, Alliance.AllianceColor.BLUE)) return Alliance.AllianceColor.BLUE;
        else return null;
    }

}
