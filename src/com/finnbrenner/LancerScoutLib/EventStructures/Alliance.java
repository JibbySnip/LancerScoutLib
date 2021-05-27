package com.finnbrenner.LancerScoutLib.EventStructures;

import java.io.Serializable;
import java.util.ArrayList;

public class Alliance implements Serializable {
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
    private int numTeams;
    private AllianceColor color;

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
