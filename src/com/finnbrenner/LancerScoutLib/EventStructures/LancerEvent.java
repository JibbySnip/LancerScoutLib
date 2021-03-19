package com.finnbrenner.LancerScoutLib.EventStructures;

import java.util.ArrayList;
import java.util.Date;

public class LancerEvent {
    private final int teamsPerAlliance;
    private final ArrayList<Team> teams = new ArrayList<>();
    private final ArrayList<Match> matches = new ArrayList<>();
    public final String eventName;
    public final Date eventStartDate;

    public LancerEvent(int teamsPerAlliance, String eventName, Date eventStartDate) {
        this.teamsPerAlliance = teamsPerAlliance;
        this.eventName = eventName;
        this.eventStartDate = eventStartDate;
    }


}
