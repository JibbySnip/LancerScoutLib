package com.finnbrenner.LancerScoutLib.EventStructures;

import java.util.ArrayList;

public class Alliance {
    public enum AllianceColor {
        RED,
        BLUE
    }
    private ArrayList<Team> teams = new ArrayList<>();
    private int numTeams;
    private AllianceColor color;

    public Alliance(ArrayList<Team> teams, AllianceColor color) {
        this.teams = teams;
        this.numTeams = teams.size();
        this.color = color;

    }
}
