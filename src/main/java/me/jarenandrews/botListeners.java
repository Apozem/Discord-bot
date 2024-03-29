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
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class botListeners extends ListenerAdapter {
    protected String channelName;

    public botListeners(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(!event.getAuthor().isBot() && event.getChannel().getName().equals(channelName)) {
            if(!event.getMessage().getAttachments().isEmpty()) {
                try {
                    //log request
                    System.out.println("Request from user: " + event.getAuthor().getName());
                    //tell the user to stop being dumb and send an image in the channel instead of whatever file they just sent, and log it to standard output
                    if(!event.getMessage().getAttachments().get(0).isImage()) {
                        event.getChannel().sendMessage("Attached file has to be an image, idiot. " + event.getMessage().getAuthor().getAsMention()).queue();
                        System.out.println("User " + event.getAuthor().getName() + " tried to upload a file that was not an image. shame.");
                        return;
                    }
                    //exit if too large, avoids clogging my system with a huge file, only a problem if user has nitro I think
                    if(event.getMessage().getAttachments().get(0).getSize() > 10485760) {
                        event.getChannel().sendMessage("Attached file is too large, idiot. " + event.getMessage().getAuthor().getAsMention()).queue();
                        System.out.println("User " + event.getAuthor().getName() + " tried to upload a file that was too large. shame.");
                        return;
                    }
                    //get message
                    String input = event.getMessage().getContentRaw();
                    String[] messageArr = input.split(",");
                    //error if array is not size 4 and list colors
                    if(messageArr.length != 4) {
                        event.getChannel().sendMessage("Message structure should be *Message*, *font*, *font size*, *color* with the commas between, idiot. " + event.getMessage().getAuthor().getAsMention()).queue();
                        System.out.println("User " + event.getAuthor().getName() + " used incorrect syntax when trying to use the bot. shame.");
                        return;
                    }
                    try {
                        //split parameters
                        String message = messageArr[0];
                        String font = messageArr[1].strip();
                        int fontSize = Integer.parseInt(messageArr[2].strip());
                        String color = messageArr[3].strip();
                        //change color, error if color is not available

                        System.out.println("Message to write: " + message);
                        //get image file
                        Message.Attachment att = event.getMessage().getAttachments().get(0);
                        //create temp file to draw on and send
                        File temp = new File("draw.png");
                        //noinspection deprecation
                        att.downloadToFile(temp).get(); //don't know current way, oh well
                        BufferedImage img = ImageIO.read(temp);
                        Graphics2D g = img.createGraphics();
                        //caption block - color coming soon
                        Font meme = new Font(font, Font.PLAIN, fontSize);
                        g.setFont(meme);
                        //set color
                        if(setColor(color, g) == 1) {
                            event.getChannel().sendMessage("Invalid color used, idiot. " + event.getMessage().getAuthor().getAsMention()
                                    + "\nAvailable colors are: \nblack\nblue\ncyan\ndarkgray\ngray\ngreen\nlightgray\nmagenta\norange\npink\nred\nwhite\nyellow").queue();
                            System.out.println("User " + event.getAuthor().getName() + " used incorrect color when trying to use the bot. shame.");
                            return;
                        }
                        FontRenderContext frc = g.getFontRenderContext();
                        float messageWidth = (float)meme.getStringBounds(message, frc).getWidth();
                        g.drawString(message, (img.getWidth()-messageWidth)/2, img.getHeight()-(int)(img.getHeight()/6.0));
                        //end caption block
                        //write the changes to the image
                        ImageIO.write(img, "png", temp);
                        //send image to the channel
                        event.getChannel().sendFiles(AttachedFile.fromData(temp)).queue();
                        att.close(); //not sure that this helps
                        //attempt deletion
                        if(!temp.delete()) { //can't always delete? overwrites anyway so no big deal
                            System.out.println("Unable to delete " + temp.getName());
                        }
                    } catch (NumberFormatException e) {
                        event.getChannel().sendMessage("Font size was not a number, idiot. " + event.getMessage().getAuthor().getAsMention()).queue();
                        System.out.println("User " + event.getAuthor().getName() + " did not send a number when trying to use the bot. shame.");
                    }
                } catch (IOException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int setColor(String color, Graphics2D g) {
        switch (color.toLowerCase(Locale.ROOT)){
            case "black":
                g.setColor(Color.BLACK);
                return 0;
            case "blue":
                g.setColor(Color.BLUE);
                return 0;
            case "cyan":
                g.setColor(Color.CYAN);
                return 0;
            case "darkgray":
                g.setColor(Color.darkGray);
                return 0;
            case "gray":
                g.setColor(Color.gray);
                return 0;
            case "green":
                g.setColor(Color.green);
                return 0;
            case "lightgray":
                g.setColor(Color.LIGHT_GRAY);
                return 0;
            case "magenta":
                g.setColor(Color.magenta);
                return 0;
            case "orange":
                g.setColor(Color.ORANGE);
                return 0;
            case "pink":
                g.setColor(Color.PINK);
                return 0;
            case "red":
                g.setColor(Color.red);
                return 0;
            case "white":
                g.setColor(Color.WHITE);
                return 0;
            case "yellow":
                g.setColor(Color.YELLOW);
                return 0;
        }
        return 1;
    }
}
