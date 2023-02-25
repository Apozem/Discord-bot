package me.jarenandrews;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class botListeners extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.getAuthor().isBot()) {
            if(!event.getMessage().getAttachments().isEmpty()) {
                try {
                    Message.Attachment att = event.getMessage().getAttachments().get(0);
                    if(att.getSize() > 10485760) {
                        return;
                    }
                    File temp = new File("draw.png");
                    att.downloadToFile(temp).get(); //dont know current way, oh well
                    BufferedImage img = ImageIO.read(temp);
                    Graphics2D g = img.createGraphics();
                    //draw block
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLUE);
                    g.drawRect(10, 10, img.getWidth() - 20, img.getHeight() - 20);
                    //draw block
                    ImageIO.write(img, "png", temp);
                    event.getChannel().sendFiles(FileUpload.fromData(temp)).queue();
                    att.close(); //not sure that this helps
                    if(!temp.delete()) { //cant always delete?
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
