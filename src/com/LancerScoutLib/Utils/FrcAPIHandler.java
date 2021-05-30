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

package com.LancerScoutLib.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is fairly simple:
 * it has a basic utility for getting a match breakdown,
 * which with a little finagling can be used to get component BPRS
 * @see BPRCalculator
 *
 * Feel free to add your own API calls here- I've only done one because I'm lazy.
 * Someone smarter or more motivated could make a method that takes the output from
 * {@link #getMatchBreakdown(String, String, String)}
 * and formats it into an array for {@link BPRCalculator#convertDataToTeamMatrix(int[][], ArrayList, ArrayList)}
 *
 * I didn't add a handler for the TBA API because there's tons of libs already out there for that. No points for redundancy.
 */
public class FrcAPIHandler {
    private final String key;
    private final int season;
    private final String username;

    /**
     * Initialize an instance of the API handler. Take a look at https://frc-events.firstinspires.org/services/API for more info.
     * @param username See link above
     * @param authKey See link above
     * @param season Just the starting year of the season.
     */
    public FrcAPIHandler(String username, String authKey, int season) {
        this.key= authKey;
        this.username = username;
        this.season=season;
    }


    /**
     * Get a per-alliance and per-component breakdown of scoring for a match. Great for component OPR.
     * @param matchNumber Match number in an event
     * @param eventCode The code for the event. Try to find it with another API call (maybe check TBA API?)
     * @param tournamentLevel For example, "Playoff". I'm sure there's a way to find this from another API call, so try to do that.
     * @return An Arraylist of two Maps, one for each alliance, holding all the keys for the season's scoring (see https://frc-api-docs.firstinspires.org/#58da7b76-4b47-4ee3-903d-1571897e0a09)
     * @throws IOException
     * @throws UnirestException
     */
    public ArrayList<Map<String, String>> getMatchBreakdown(String matchNumber, String tournamentLevel, String eventCode) throws IOException, UnirestException {

        ArrayList<Map<String, String>> matchScores = new ArrayList<>();

        String url = String.format("https://frc-api.firstinspires.org/v2.0/%1$s/scores/%2$s/%3$s", season, eventCode, tournamentLevel);
        String charset = "UTF-8";

        String query = String.format("matchNumber=%1s",
            URLEncoder.encode(matchNumber, charset));

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get(url + "?" + query)
            .header("Authorization", "Basic "+ username+":"+key)
            .header("If-Modified-Since", "")
            .asString();

        if (response.getStatus() == 200) {
            JsonObject root = JsonParser.parseString(response.getBody()).getAsJsonObject();
            JsonArray scores = root.getAsJsonArray("MatchScores");
            Map<String, String> red = new HashMap<>();
            Map<String, String> blue = new HashMap<>();
            matchScores.add(red);
            matchScores.add(blue);
            for (int i = 0; i < 2; i++) {
                Set<Map.Entry<String, JsonElement>> entrySet = scores.get(i).getAsJsonObject().entrySet();
                int finalI = i;
                entrySet.forEach(entry -> {
                    matchScores.get(finalI).put(entry.getKey(), entry.getValue().getAsString());
                });
            }
        }
        else {
                throw new HTTPException(0);
        }
        return matchScores;
    }

}
