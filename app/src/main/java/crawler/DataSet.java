package crawler;

import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.xml.crypto.Data;

public class DataSet {

    private HashMap<String, ChannelData> dataSet;
    private File dataFile;
    private static ObjectMapper objectMapper = new ObjectMapper();

    class MessageData {
        String authorID;
        int charCount;

        public String getAuthorID() {
            return authorID;
        }

        public void setAuthorID(String authorID) {
            this.authorID = authorID;
        }

        public int getCharCount() {
            return charCount;
        }

        public void setCharCount(int charCount) {
            this.charCount = charCount;
        }
    }

    class ChannelData {
        String lastUpdateMessageID = "";
        ArrayList<MessageData> messages = new ArrayList<>();

        public String getLastUpdateMessageID() {
            return lastUpdateMessageID;
        }

        public void setLastUpdateMessageID(String lastUpdateMessageID) {
            this.lastUpdateMessageID = lastUpdateMessageID;
        }

        public ArrayList<MessageData> getMessages() {
            return messages;
        }

        public void setMessages(ArrayList<MessageData> messages) {
            this.messages = messages;
        }
    }

    DataSet(File dataFile) throws IOException {
        this.dataFile = dataFile;

        if (dataFile.exists()) {
            try {
                this.dataSet = objectMapper.readValue(dataFile, new TypeReference<HashMap<String, ChannelData>>() {
                });
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
        return dataSet.get(channelID);
    }

    public void save() throws IOException {
        String jsonString = objectMapper.writeValueAsString(dataSet);
        FileWriter writer = new FileWriter(dataFile);
        writer.write(jsonString);
        writer.close();
    }
}