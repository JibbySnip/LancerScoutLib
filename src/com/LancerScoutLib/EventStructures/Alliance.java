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

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Just a container for teams and stuff. I'm sure you could do some good defense classification here or maybe in the Match class but that's left as an exercise for the reader ;).
 */
public abstract class Alliance implements Serializable {
    public enum AllianceColor {
        RED,
        BLUE;

        public AllianceColor getOpposingTeam() {
            if (this == RED) {
                return BLUE;
            } else {
                return RED;
            }
        }
    }
    private ArrayList<Team> teams = new ArrayList<>();
    private final int numTeams;
    private final AllianceColor color;

    public Alliance(ArrayList<Team> teams, AllianceColor color) {
        this.teams = teams;
        this.numTeams = teams.size();
        this.color = color;

    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public Team getTeam(int i){
        return teams.get(i);
    }
}
