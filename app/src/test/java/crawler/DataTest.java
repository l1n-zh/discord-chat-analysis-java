package crawler;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import crawler.Crawler;

public class DataTest {

    Message message = Mockito.mock(Message.class);
    User user = Mockito.mock(User.class);

    {
        Mockito.when(user.getId()).thenReturn("336902195786858109");
        Mockito.when(message.getContentDisplay()).thenReturn("test message :D");
        Mockito.when(message.getId()).thenReturn("1251514414415414510");
        Mockito.when(message.getChannelId()).thenReturn("934146182061111452");
        Mockito.when(message.getAuthor()).thenReturn(user);
    }

    @Test
    public void canCreateDataSetWhenJsonFileNotExist() {
        File file = new File("src/test/resources/dataSetTest1.json");
        try {
            File dataFile = new File("src/test/java/crawler/data/000000000000000000.json");
            dataFile.deleteOnExit();
            DataSet dataSet = new DataSet(dataFile);
            dataSet.addMessage(message);
            dataSet.save();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void test() {
        try {
            DataSet dataset = new DataSet(new File("src/test/java/crawler/data/221801148530144231.json"));
            dataset.addMessage(message);
            dataset.save();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }
}
