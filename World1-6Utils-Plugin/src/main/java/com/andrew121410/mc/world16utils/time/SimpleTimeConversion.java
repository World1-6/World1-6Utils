package com.andrew121410.mc.world16utils.time;

import com.andrew121410.mc.world16utils.utils.Utils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTimeConversion {

    private String letter;
    private Long number;

    private long seconds;

    public SimpleTimeConversion() {
    }

    public SimpleTimeConversion(long seconds) {
        this.seconds = seconds;
    }

    public boolean string(String convert) {
        Pattern pattern = Pattern.compile("(?<number>\\d{1,3})(?<letter>[a-z]{1,3})");
        Matcher matcher = pattern.matcher(convert);
        while (matcher.find()) {
            this.number = Utils.asLongOrElse(matcher.group("number"), null);
            this.letter = matcher.group("letter");
        }
        if (this.number != null && this.letter != null) {
            convertTimeToSeconds();
            return true;
        }
        return false;
    }

    private void convertTimeToSeconds() {
        if (this.letter == null) return;
        long ticks;
        if (this.letter.equalsIgnoreCase("s")) {
            ticks = this.number * 20;
        } else if (this.letter.equalsIgnoreCase("m")) {
            ticks = this.number * 1200;
        } else if (this.letter.equalsIgnoreCase("h")) {
            ticks = this.number * 72000;
        } else if (this.letter.equalsIgnoreCase("d")) {
            ticks = this.number * 1728000;
        } else return;
        this.seconds = ticks / 20;
    }

    public String getFancyDate() {
        int days = (int) TimeUnit.SECONDS.toDays(this.seconds);
        int hours = (int) TimeUnit.SECONDS.toHours(this.seconds);
        int minutes = (int) TimeUnit.SECONDS.toMinutes(this.seconds);
        int seconds = (int) TimeUnit.SECONDS.toSeconds(this.seconds);

        StringBuilder stringBuilder = new StringBuilder();
        if (days >= 1) {
            stringBuilder.append(days);
            stringBuilder.append(" day");
            if (days > 1) stringBuilder.append("s");
            stringBuilder.append(" ");
        }
        if (hours >= 1) {
            stringBuilder.append(hours);
            stringBuilder.append(" hour");
            if (hours > 1) stringBuilder.append("s");
            stringBuilder.append(" ");
        }
        if (minutes >= 1) {
            stringBuilder.append(minutes);
            stringBuilder.append(" minute");
            if (minutes > 1) stringBuilder.append("s");
            stringBuilder.append(" ");
        }
        if (seconds >= 1) {
            stringBuilder.append(seconds);
            stringBuilder.append(" second");
            if (seconds > 1) stringBuilder.append("s");
        }
        return stringBuilder.toString();
    }

    public int getSeconds() {
        return (int) this.seconds;
    }
}
