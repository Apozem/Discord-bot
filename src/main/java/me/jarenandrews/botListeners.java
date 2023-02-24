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
                //handle image
                try {

                    Message.Attachment att = event.getMessage().getAttachments().get(0);
                    File temp = new File("filetosend.png");
                    att.downloadToFile(temp).get();
                    BufferedImage img = ImageIO.read(temp);
                    Graphics2D g = img.createGraphics();
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.BLUE);
                    g.drawRect(10, 10, img.getWidth() - 20, img.getHeight() - 20);
                    File out = new File("send.png");
                    ImageIO.write(img, "png", out);
                    Collection<File> imageList = new ArrayList<>();
                    imageList.add(out);
                    event.getChannel().sendFiles(FileUpload.fromData(out)).queue();
                } catch (IOException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                String message = event.getMessage().getContentRaw();
                event.getChannel().sendMessage("This was sent: " + message).queue();
            }
        }
    }

}
