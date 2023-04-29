package com.mineaurion.api;

import com.mineaurion.api.query.pterodactyl.schedule.Attributes;
import com.mineaurion.api.query.pterodactyl.schedule.Schedule;
import com.mineaurion.api.query.pterodactyl.schedule.ScheduleResponse;
import com.mineaurion.api.server.model.Server;
import com.mineaurion.api.server.model.embeddable.Access;
import com.mineaurion.api.server.model.embeddable.Address;
import com.mineaurion.api.server.model.embeddable.Administration;
import com.mineaurion.api.server.model.embeddable.Version;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Faker {

    public static net.datafaker.Faker faker = new net.datafaker.Faker(new Locale("fr", "FR"));

    public static Server server(){
        return new Server(
                faker.random().nextLong(),
                faker.name().title(),
                new Version(semver(), semver()),
                faker.options().option("overworld", "skyblock"),
                new Access(faker.random().nextBoolean(), faker.random().nextBoolean(), faker.random().nextBoolean()),
                faker.internet().domainName(),
                new Administration(
                        faker.random().nextBoolean(),
                        faker.random().nextBoolean(),
                        new Address(faker.internet().ipV4Address(), faker.internet().port()),
                        new Address(faker.internet().ipV4Address(), faker.internet().port()),
                        UUID.randomUUID()
                ),
                false // à voir pour un random ici
        );
    }

    /**
     * @param   lowerDateIndex the index where we want to be the closest next schedule.
     */
    public static List<Schedule> listSchedule(int lowerDateIndex) {
        long oneMinuteInMs = 60000;
        long currentTime = new Date().getTime();
        Date next5Min = new Date(currentTime + (5 * oneMinuteInMs));
        List<Schedule> schedules = IntStream.rangeClosed(1, 10).mapToObj( i -> schedule(faker.random().nextBoolean())).collect(Collectors.toList());
        schedules.get(lowerDateIndex).getAttributes().setNext_run_at(next5Min).setName("reboot");
        return schedules;
    }

    public static Schedule schedule(boolean isReboot){
        return  new Schedule()
                .setObject("list")
                .setAttributes(
                        new Attributes()
                                .setName(isReboot ? "reboot "+ faker.lorem().word() : faker.lorem().word())
                                .setNext_run_at(new Date())
                );
    }

    /**
     * @param   lowerDateIndex the index where we want to be the closest next schedule.
     */
    public static ScheduleResponse scheduleResponse(int lowerDateIndex){
        return new ScheduleResponse().setObject("list").setAttributes(listSchedule(lowerDateIndex));
    }

    public static Server hiddenServer(){
        Server server = server();
        server().setHidden(true);
        return server;
    }

    public static List<Server> listServer(){
        return IntStream.rangeClosed(1, 10).mapToObj( i -> server()).collect(Collectors.toList());
    }

    public static String semver(){
        return faker.random().nextInt(1, 10) + "." + faker.random().nextInt(1, 10) + "." + faker.random().nextInt(1, 10);
    }

    public static com.mineaurion.api.library.model.query.Server queryServer(){
        return server().toQueryServer();
    }

    public static List<com.mineaurion.api.library.model.query.Server> listQueryServer() {
        return IntStream.rangeClosed(1, 10).mapToObj( i -> server().toQueryServer()).collect(Collectors.toList());
    }

    public static List<com.mineaurion.api.library.model.query.Server> listQueryServer(List<Server> servers) {
        return servers.stream().map(Server::toQueryServer).toList();
    }
}
