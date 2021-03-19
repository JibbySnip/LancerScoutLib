package com.finnbrenner.LancerScoutLib.EventStructures;

import java.util.ArrayList;

public class Team {
    private int teamNumber;
    private String teamName;
    private ArrayList<LancerEvent> seasonEvents = new ArrayList<>();

    public Team(int teamNumber,String teamName) {
        this.teamNumber=teamNumber;
        this.teamName = teamName;
    }

    /**
     * Adds a new event to the team's event list
     * @param newEvent event to add
     */
    public void addEvent(LancerEvent newEvent) {
        seasonEvents.add(newEvent);
    }
}
