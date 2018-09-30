package bot;

import java.awt.*;

import bot.Exception.ContextChannelNotSetException;
import bot.Exception.OutOfBoundsStatExecption;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

    private CommandManager commandManager;

    public CommandListener(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            this.processMessage(event);
        }
    }

    private void processMessage(MessageReceivedEvent event) {
        Message        message = event.getMessage();
        MessageChannel channel = event.getChannel();
        String         input   = message.getContentRaw();

        try {
            // todo remove testing cases
            if (input.startsWith("(")) {
                String[] splitArray = input.split("\\s+");
                if (splitArray[0].startsWith("(roll")) {
                    int    die    = Integer.parseInt(splitArray[1].trim());
                    int    roll   = (int) Math.ceil(Math.random() * die);
                    String output = event.getAuthor().getAsMention() + " rolled a " + roll;
                    channel.sendMessage(output).queue();
                }
            } else if (input.startsWith("$testing")) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Froyo");
                embed.setAuthor(event.getAuthor().getName(), null, event.getAuthor().getAvatarUrl());
                embed.setColor(new Color(0x62c4f9));
                embed.setImage(
                    "https://orig00.deviantart.net/b126/f/2018/252/3/2/se__froyo_equipment_and_stats_by_jksketchy-dchajyr.png");
                embed.addField("STATS", "4 | 3 | 4 | 13 | 170", true);
                channel.sendMessage(embed.build()).queue();
            } else if (input.startsWith("$populate")) {
                this.commandManager.populate(event.getAuthor());
                channel.sendMessage("Test data has been populated").queue();
            } else if (input.startsWith("$")) {
                String[] splitArray = input.split("\\s+");

                switch (splitArray[0].toLowerCase()) {
                    case "$attack":
                        this.commandManager.attackCommand(event);
                        break;
                    case "$createcharacter":
                        this.commandManager.createCharacterCommand(event);
                        break;
                    case "$createhostile":
                        this.commandManager.createHostile(event);
                        break;
                    case "$dm":
                        this.processDungeonMasterCommand(event);
                        break;
                    case "$dodge":
                        this.commandManager.dodgeCommand(event);
                        break;
                    case "$help":
                        this.commandManager.helpCommand(event);
                        break;
                    case "$join":
                        this.commandManager.joinEncounter(event);
                        break;
                    case "$leave":
                        this.commandManager.leaveCommand(event);
                        break;
                    case "$loot":
                        this.commandManager.lootCommand(event);
                        break;
                    case "$protect":
                        this.commandManager.protectCommand(event);
                        break;
                    case "$return":
                        this.commandManager.returnCommand(event);
                        break;
                    case "$view":
                        this.processViewCommand(event);
                        break;
                    default:
                        channel.sendMessage("Sorry, did you say something? I don't know that command").queue();
                        break;
                }
            }
        } catch (ContextChannelNotSetException e) {
            channel.sendMessage("I'm not sure which channel to talk in...").queue();
        } catch (ArrayIndexOutOfBoundsException e) {
            channel.sendMessage("Could you say that again? I think I'm missing something... Check `$help`").queue();
        } catch (NumberFormatException e) {
            channel.sendMessage("Hey could you say that again? Sorry, I'm bad with numbers.").queue();
        } catch (OutOfBoundsStatExecption e) {
            channel.sendMessage(e.getMessage()).queue();
        } catch (Throwable e) {
            channel.sendMessage("Something went wrong, but I'm not sure what...").queue();
            System.out.println(e.toString());
        }

    }

    private void processDungeonMasterCommand(MessageReceivedEvent event) {
        Message        message    = event.getMessage();
        MessageChannel channel    = event.getChannel();
        String         input      = message.getContentRaw();
        String[]       splitArray = input.split("\\s+");

        switch (splitArray[1].toLowerCase()) {
            case "addhostile":
                this.commandManager.addHostile(event);
                break;
            case "attackturn":
                this.commandManager.startAttackPhase(event);
                break;
            case "create":
                this.commandManager.createEncounter(event);
                break;
            case "dodgeturn":
                this.commandManager.startDodgePhase(event);
                break;
            case "healhostile":
                this.commandManager.healHostile(event);
                break;
            case "healplayer":
                this.commandManager.healPlayer(event);
                break;
            case "hurthostile":
                this.commandManager.hurtHostile(event);
                break;
            case "hurtplayer":
                this.commandManager.hurtPlayer(event);
                break;
            case "maxplayers":
                this.commandManager.setMaxPlayers(event);
                break;
            case "removehostile":
                this.commandManager.removeHostile(event);
                break;
            case "removeplayer":
                this.commandManager.removePlayerCharacter(event);
                break;
            case "skip":
                this.commandManager.skipCommand(event);
                break;
            case "start":
                this.commandManager.startEncounter(event);
                break;
            default:
                channel.sendMessage("What was that you wanted to do for an encounter?").queue();
                break;
        }
    }

    private void processViewCommand(MessageReceivedEvent event) {
        Message        message    = event.getMessage();
        MessageChannel channel    = event.getChannel();
        String         input      = message.getContentRaw();
        String[]       splitArray = input.split("\\s+");

        switch (splitArray[1].toLowerCase()) {
            case "characters":
                this.commandManager.viewCharacters(event);
                break;
            case "hostiles":
                this.commandManager.viewHostiles(event);
                break;
            case "loot":
                this.commandManager.viewHostileLoot(event);
                break;
            case "summary":
                this.commandManager.viewEncounterSummary();
                break;
            default:
                channel.sendMessage("What was it you wanted to view?").queue();
                break;
        }
    }
}