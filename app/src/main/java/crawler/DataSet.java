package crawler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import crawler.dataModels.ChannelData;
import crawler.dataModels.ExternalData;
import crawler.dataModels.AllExternalData;
import crawler.dataModels.MessageData;
import net.dv8tion.jda.api.entities.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class DataSet {

    private HashMap<String, ChannelData> dataSet;
    private AllExternalData externalData = new AllExternalData();
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
    public void addExternalChannel(String channelId,String channelName,String channelCategoryName) {
        ExternalData tmp = new ExternalData();
        tmp.title = channelName;
        tmp.subtitle = channelCategoryName;
        tmp.value = channelId;
        externalData.channels.add(tmp);

    }
    public void addExternalUser(String userId,String userName,String usersubname) {
        ExternalData tmp = new ExternalData();
        tmp.title = userName;
        tmp.subtitle = usersubname;
        tmp.value = userId;
        externalData.users.add(tmp);
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
    public void saveExternalData() throws IOException {

        String jsonString = objectMapper.writeValueAsString(externalData);
        FileWriter writer = new FileWriter(dataFile);
        writer.write(jsonString);
        writer.close();
    }
}