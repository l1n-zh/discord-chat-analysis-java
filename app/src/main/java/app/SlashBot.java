package app;


import javax.annotation.Nonnull;

import crawler.Crawler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import static net.dv8tion.jda.api.interactions.commands.OptionType.*;



public class SlashBot extends ListenerAdapter {
    public SlashBot(String token) throws Exception {
        EnumSet<GatewayIntent> intents = GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS);
        JDA jda = JDABuilder.createDefault(token, intents).addEventListeners(new SlashCommandListener()).build();
        jda.awaitReady();
        CommandListUpdateAction commands = jda.getGuildById("1250711874111148062").updateCommands();

        // add clawer
        commands.addCommands(
                Commands.slash("crawler", "Scraping Chat Room Data")
                        .setGuildOnly(true) // This way the command can only be executed from a guild, and not the DMs
        );

        commands.addCommands(
                Commands.slash("say", "Makes the bot say what you tell it to")
                        .addOption(STRING, "content", "What the bot should say", true) // you can add required options like this too
        );
        commands.addCommands(
                Commands.slash("server", "open server to show data")
                        .setGuildOnly(true) // you can add required options like this too
        );
        commands.queue();
    }
}
    
class SlashCommandListener extends ListenerAdapter{
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event)
    {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;

        switch (event.getName())
        {
        case "say":
            say(event, event.getOption("content").getAsString()); // content is required so no null-check here
            break;
        case "crawler":
            Crawler crawler = new Crawler(event.getJDA());
            crawler.crawl(event.getGuild().getId());
            break;
        case "server":
            try{
                int port = CustomHttpServer.OpenServer();
                event.reply("http://localhost:" + port + "/").setEphemeral(true).queue();
                System.out.println("success");
            }catch(Exception e){
                event.reply("error").setEphemeral(true).queue();
                System.out.println("error");
            }
            break;
        default:
            event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }
    public void say(SlashCommandInteractionEvent event, String content)
    {
        event.reply(content).queue(); // This requires no permissions!
    }
    
}
