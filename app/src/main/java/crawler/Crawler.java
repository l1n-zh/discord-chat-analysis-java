package crawler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.annotation.Nonnull;

import crawler.dataModels.ChannelData;

import java.util.EnumSet;
import java.util.List;
import java.io.File;


public class Crawler {
    JDA jda;

    public Crawler(JDA jda) {
        this.jda = jda;
        jda.addEventListener(new ReadyListener());
    }

    public void crawl(String guildID){
        File dataFile = new File("src/main/java/crawler/data/"+guildID+".json");
        try{
            DataSet dataSet = new DataSet(dataFile);
            for (TextChannel channel : jda.getGuildById(guildID).getTextChannels()){
                String channelId = channel.getId();
                List<Message> messages;
                while(!(messages = channel.getHistoryAfter(dataSet.getChannelData(channelId).lastUpdateMessageID, 100).complete().getRetrievedHistory()).isEmpty()){
                    messages.reversed().forEach( m -> dataSet.addMessage(m));
                    dataSet.save();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("hello world");
    }
}