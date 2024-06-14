package crawler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.annotation.Nonnull;

import java.util.EnumSet;

public class Crawler {
    public Crawler(String token) {
        EnumSet<GatewayIntent> intents = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS);
        JDA jda = JDABuilder.createDefault(token).enableIntents(intents).build();
        jda.addEventListener(new ReadyListener());
    }
}

class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        System.out.println("hello world");
    }
}