package com.robintegg.demo.bfs.utils;

import java.time.Duration;

public class DurationFormatter {

    public static String formatDuration( Duration duration) {
        long days = duration.toDaysPart();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        long millis = duration.toMillisPart();

        StringBuilder sb = new StringBuilder();

        if (days > 0) {
            sb.append(days).append(" day").append(days > 1 ? "s" : "").append(" ");
        }
        if (hours > 0) {
            sb.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ");
        }
        if (seconds > 0) {
            sb.append(seconds).append(" second").append(seconds > 1 ? "s" : "").append(" ");
        }
        if (millis > 0 && days==0 && hours == 0 && minutes == 0 && seconds == 0) {
            sb.append(millis).append(" millisecond").append(millis > 1 ? "s" : "").append(" ");
        }

        if (sb.length() == 0) {
            return "0 seconds"; // Or "0 milliseconds" if you prefer
        }

        return sb.toString().trim();
    }

    public static void main(String[] args) {
        Duration duration1 = Duration.ofDays(1).plusHours(2).plusMinutes(30).plusSeconds(15);
        Duration duration2 = Duration.ofMinutes(5).plusSeconds(45);
        Duration duration3 = Duration.ofMillis(500);
        Duration duration4 = Duration.ofSeconds(0);

        System.out.println(formatDuration(duration1)); // Output: 1 day 2 hours 30 minutes 15 seconds
        System.out.println(formatDuration(duration2)); // Output: 5 minutes 45 seconds
        System.out.println(formatDuration(duration3)); // Output: 500 milliseconds
        System.out.println(formatDuration(duration4)); // Output: 0 seconds
    }
}