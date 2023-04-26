package net.theiceninja.crates.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;

public final class LocationUtility {

    public static void saveLocation(Location location, ConfigurationSection section) {
        section.set("worldName", location.getWorld().getName());
        section.set("x", location.getBlockX());
        section.set("y", location.getBlockY());
        section.set("z", location.getBlockZ());
        section.set("yaw", location.getYaw());
        section.set("pitch", location.getPitch());
    }

    public static Location readLocation(ConfigurationSection section) {
        if (Bukkit.getWorld(section.getString("worldName")) == null) {
            Bukkit.createWorld(new WorldCreator(section.getString("worldName")));
        }

        return new Location(
                Bukkit.getWorld(section.getString("worldName")),
                section.getInt("x"),
                section.getInt("y"),
                section.getInt("z"),
                (float) section.getDouble("yaw"),
                (float) section.getDouble("pitch")
        );
    }
}