package me.jarenandrews;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class discordBot extends ListenerAdapter {
    public static void main(String[] args) {
        JDA bot = JDABuilder.createDefault(args[0])
                .setActivity(Activity.listening("Garbage metal"))
                .addEventListeners(new discordBot())
                .build();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        System.out.println("A message has been sent");
    }
}
