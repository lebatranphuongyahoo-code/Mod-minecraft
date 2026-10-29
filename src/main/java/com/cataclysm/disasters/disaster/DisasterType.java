package com.cataclysm.disasters.disaster;

import java.util.Locale;

public enum DisasterType {
    EARTHQUAKE("Earthquake"), TORNADO("Tornado"), VOLCANO("Volcanic Eruption"), METEOR("Meteor Shower"),
    TSUNAMI("Tsunami / Flood"), WILDFIRE("Wildfire"), BLIZZARD("Blizzard"), THUNDERSTORM("Lightning Storm"), SANDSTORM("Sandstorm");
    private final String display;
    DisasterType(String display) { this.display = display; }
    public String display() { return display; }
    public static DisasterType parse(String value) {
        String normalized = value.toLowerCase(Locale.ROOT).replace('-', '_').replace(' ', '_');
        for (DisasterType type : values()) if (type.name().toLowerCase(Locale.ROOT).equals(normalized)) return type;
        throw new IllegalArgumentException("Unknown disaster: " + value);
    }
}
