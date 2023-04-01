package me.jarenandrews;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.AttachedFile;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class botListeners extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.getAuthor().isBot()) {
            if(!event.getMessage().getAttachments().isEmpty()) {
                try {
                    String input = event.getMessage().getContentRaw();
                    String[] messageArr = input.split(",");
                    String message = messageArr[0];
                    System.out.println("Message to write: " + message);
                    String font = messageArr[1].strip();
                    int fontSize = Integer.parseInt(messageArr[2].strip());
                    Message.Attachment att = event.getMessage().getAttachments().get(0);
                    if(att.getSize() > 10485760) {
                        return;
                    }
                    File temp = new File("draw.png");
                    att.downloadToFile(temp).get(); //don't know current way, oh well
                    BufferedImage img = ImageIO.read(temp);
                    Graphics2D g = img.createGraphics();
                    //caption block - color coming soon
                    Font meme = new Font(font, Font.PLAIN, fontSize);
                    g.setFont(meme);
                    FontRenderContext frc = g.getFontRenderContext();
                    float messageWidth = (float)meme.getStringBounds(message, frc).getWidth();
                    g.drawString(message, (img.getWidth()-messageWidth)/2, img.getHeight()-(img.getHeight()/6));
                    //draw block
                    ImageIO.write(img, "png", temp);
                    event.getChannel().sendFiles(AttachedFile.fromData(temp)).queue();
                    att.close(); //not sure that this helps
                    if(!temp.delete()) { //can't always delete? overwrites anyway so no big deal
                        System.out.println("Unable to delete " + temp.getName());
                    }
                } catch (IOException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else { //echos message
                String message = event.getMessage().getContentRaw();
                event.getChannel().sendMessage("This was sent: " + message).queue();
            }
        }
    }

}
