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
            if (input.startsWith("$")) {
                String[] splitArray = input.split("\\s+");

                switch (splitArray[0].toLowerCase()) {
                    case "$attack":
                        this.commandManager.attackCommand(event);
                        break;
                    case "$createhostile":
                        channel.sendMessage("Temporarily disabled").queue();
//                        this.commandManager.createHostile(event);
                        break;
                    case "$createpc":
                        this.commandManager.createCharacterCommand(event);
                        break;
                    case "$deletehostile":
                        channel.sendMessage("Temporarily disabled").queue();
//                        this.commandManager.deleteHostile(event);
                        break;
                    case "$deletepc":
                        // todo make admin only
                        channel.sendMessage("Temporarily disabled").queue();
//                        this.commandManager.deleteCharacterCommand(event);
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
            case "pcs":
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