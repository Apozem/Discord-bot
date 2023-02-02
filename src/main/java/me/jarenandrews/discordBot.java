package me.jarenandrews;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class discordBot {
    public static void main(String[] args) {
        JDA bot = JDABuilder.createDefault(args[0])
                .setActivity(Activity.playing("Modern Warfare 2: No Russian"))
                .build();
    }
}
