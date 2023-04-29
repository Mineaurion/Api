package com.mineaurion.api.query;

import com.mineaurion.api.query.pterodactyl.schedule.Schedule;
import com.mineaurion.api.query.pterodactyl.schedule.ScheduleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class PterodactylQueryService {

    @Autowired
    private Environment env;

    Logger logger = LoggerFactory.getLogger(PterodactylQueryService.class);

    @Cacheable(cacheNames = {"nextRebootSchedule"}, key = "#uuid")
    public Optional<Date> getNextRebootSchedule(UUID uuid){
        WebClient client = WebClient.builder()
                .baseUrl(env.getProperty("pterodactyl.api_url"))
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Authorization", "Bearer " + env.getProperty("pterodactyl.api_token"))
                .build();
        Optional<Date> nextSchedule = Optional.empty();
        try {
            ScheduleResponse scheduleResponse = client
                    .get()
                    .uri("/api/client/servers/" + uuid + "/schedules")
                    .retrieve()
                    .bodyToMono(ScheduleResponse.class)
                    .block()
                    ;
            if(scheduleResponse != null){
                List<Schedule> serverSchedules = scheduleResponse.getData();
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
        } catch (Exception e) {
            logger.error("Error when calling pterodactyl api", e);
        }
        return nextSchedule;
    }
}
