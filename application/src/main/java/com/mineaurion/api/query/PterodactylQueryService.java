package com.mineaurion.api.query;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mineaurion.api.query.pterodactyl.schedule.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class PterodactylQueryService {

    @Autowired
    private Environment env;

    Logger logger = LoggerFactory.getLogger(PterodactylQueryService.class);

    @Cacheable(cacheNames = {"nextRebootSchedule"}, key = "#uuid")
    public Optional<Date> getNextRebootSchedule(UUID uuid){
        Optional<Date> nextSchedule = Optional.empty();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/api/client/servers/%s/schedules", env.getProperty("pterodactyl.api_url"), uuid)))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + env.getProperty("pterodactyl.api_token"))
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200){
                JsonArray data = JsonParser.parseString(response.body()).getAsJsonObject().get("data").getAsJsonArray();
                Type listOfSchedule =  new TypeToken<ArrayList<Schedule>>(){}.getType();
                List<Schedule> serverSchedules = new Gson().fromJson(data, listOfSchedule);
                NavigableSet<Date> nextSchedules = new TreeSet<>();
                if(!serverSchedules.isEmpty()){
                    for (Schedule schedule: serverSchedules) {
                        if(schedule.getAttributes().getName().toLowerCase().contains("reboot")){
                            nextSchedules.add(schedule.getAttributes().getNext_run_at());
                        }
                    }
                    Date now = new Date();
                    nextSchedule = Optional.ofNullable(nextSchedules.higher(now));
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return nextSchedule;
    }
}
