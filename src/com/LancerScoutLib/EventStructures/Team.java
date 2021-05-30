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
import java.util.HashMap;

/**
 * The idea is that this team class can be serialized and reused over a season. This should allow plotting of changes in scoring
 * over time and such to be visible. Futher qualitative description categories are
 * recommended, as well as a picture, classification as a defense bot, etc.
 */
public abstract class Team implements Serializable {
    private final int teamNumber;
    private final String teamName;
    private final ArrayList<LancerEvent> seasonEvents = new ArrayList<>();
    private final ArrayList<Match> matches = new ArrayList<>();
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

    /**
     * Adds a new match to the team's match list
     * @param match match to add
     */
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
