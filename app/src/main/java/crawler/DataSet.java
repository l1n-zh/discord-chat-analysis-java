package crawler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawler.dataModels.ChannelData;
import crawler.dataModels.MessageData;
import net.dv8tion.jda.api.entities.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class DataSet {

    private HashMap<String, ChannelData> dataSet;
    private File dataFile;
    private static ObjectMapper objectMapper = new ObjectMapper();



    DataSet(File dataFile) throws IOException {
        this.dataFile = dataFile;

        if (dataFile.exists()) {
            try {
                this.dataSet = objectMapper.readValue(dataFile, new TypeReference<HashMap<String, ChannelData>>() { });
            } catch (Exception e) {
                this.dataSet = new HashMap<>();
            }
        } else {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
            this.dataSet = new HashMap<>();
        }
    }

    public void addMessage(Message message) {
        String channelID = message.getChannelId();
        if (!dataSet.containsKey(channelID))
            dataSet.put(channelID, new ChannelData());
        MessageData messageData = new MessageData();
        messageData.authorID = message.getAuthor().getId();
        messageData.charCount = message.getContentDisplay().length();
        dataSet.get(channelID).messages.add(messageData);
        dataSet.get(channelID).lastUpdateMessageID = message.getId();
    }

    public ChannelData getChannelData(String channelID) {
        return dataSet.getOrDefault(channelID, new ChannelData());
    }

    public void save() throws IOException {
        String jsonString = objectMapper.writeValueAsString(dataSet);
        FileWriter writer = new FileWriter(dataFile);
        writer.write(jsonString);
        writer.close();
    }
}