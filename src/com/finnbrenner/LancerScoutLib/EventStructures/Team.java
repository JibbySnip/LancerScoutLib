package com.finnbrenner.LancerScoutLib.EventStructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Team implements Serializable {
    private int teamNumber;
    private String teamName;
    private ArrayList<LancerEvent> seasonEvents = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();
    private final HashMap<LancerEvent, Double> eventOPRs = new HashMap<>();
    private final HashMap<LancerEvent, Double> eventDPRs = new HashMap<>();
    private final HashMap<LancerEvent, Double> eventCCWMs = new HashMap<>();


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

    public void addMatch(Match match) {
        matches.add(match);
    }

    public void setEventOPR(LancerEvent e, double opr) {
        eventOPRs.put(e, opr);
    }

    public void setEventDPR(LancerEvent e, double dpr) {
        eventOPRs.put(e, dpr);
    }

    public void updateCCWM(LancerEvent e) {
        eventCCWMs.put(e, eventOPRs.get(e)-eventDPRs.get(e));
    }


}
