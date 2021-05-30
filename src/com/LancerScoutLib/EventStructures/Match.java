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

import com.LancerScoutLib.Utils.FrcAPIHandler;

import java.io.Serializable;

/**
 * Model for a match. I would recommend adding a HashMap with per-season match attributes that can be synchronized using FRC
 * API {@link FrcAPIHandler}. Someone more motivated might want to migrate a lot of these
 * models to a SQL database for better backwards-referencability (I know there's an actual word for this but I can't remember it) and general organization.
 */
public abstract class Match implements Serializable {
    public enum MatchRanking {
        SEED,
        SEMIFINALS,
        FINALS
    }
    public int matchNumber;
    private final Alliance[] alliances; // goes Red, Blue
    private final int[] scores = new int[2];
    private Alliance winner;
    private final LancerEvent event;

    public Match(int matchNumber, Alliance allianceRed, Alliance allianceBlue, LancerEvent event) {
        alliances = new Alliance[]{allianceRed, allianceBlue};
        this.matchNumber = matchNumber;
        this.event = event;
        for (Alliance a : alliances) {
            for (Team t : a.getTeams()) {
                t.addMatch(this);
            }
        }
    }

    /**
     * Set the scores for a match
     *
     * @param allianceRedScore  Overall score for the red team
     * @param allianceBlueScore Overall score for the blue team
     */
    public void setScores(int allianceRedScore, int allianceBlueScore) {
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
        Alliance a = alliances[(allianceColor == Alliance.AllianceColor.RED) ? 0 : 1];
        return a.getTeams().contains(t);
    }

    public int getRedScore() {
        return scores[0];
    }
    public int getBlueScore() {
        return scores[1];
    }

    Alliance.AllianceColor teamColor(Team t) {
        if (containsTeam(t, Alliance.AllianceColor.RED)) return Alliance.AllianceColor.RED;
        else if (containsTeam(t, Alliance.AllianceColor.BLUE)) return Alliance.AllianceColor.BLUE;
        else return null;
    }

}
