package me.jarenandrews;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class botListeners extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if(!event.getAuthor().isBot()) {
            String message = event.getMessage().getContentRaw();
            event.getChannel().sendMessage("This was sent: " + message).queue();
        }
    }
}
