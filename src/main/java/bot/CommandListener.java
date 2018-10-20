package bot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

    public static String COMMAND_KEY = "?";

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
            if (input.startsWith(CommandListener.COMMAND_KEY)) {
                String[] splitArray = input.split("\\s+");

                switch (splitArray[0].substring(1).toLowerCase()) {
                    case "addhostile":
                        this.commandManager.addHostile(event);
                        break;
                    case "attack":
                        this.commandManager.attackCommand(event);
                        break;
                    case "attackturn":
                        this.commandManager.startAttackPhase(event);
                        break;
                    case "create":
                        this.processCreateCommand(event);
                        break;
                    case "deletecharacter":
                        this.commandManager.deleteCharacterCommand(event);
                        break;
                    case "dodge":
                        this.commandManager.dodgeCommand(event);
                        break;
                    case "dodgeturn":
                        this.commandManager.startDodgePhase(event);
                        break;
                    case "heal":
                        this.commandManager.healCommand(event);
                        break;
                    case "hello":
                        this.commandManager.helloCommand(event);
                        break;
                    case "help":
                        this.commandManager.helpCommand(event);
                        break;
                    case "hurt":
                        this.commandManager.hurtCommand(event);
                        break;
                    case "join":
                        this.commandManager.joinCommand(event);
                        break;
                    case "kick":
                        this.commandManager.removePlayerCharacter(event);
                        break;
                    case "leave":
                        this.commandManager.leaveCommand(event);
                        break;
                    case "loot":
                        this.commandManager.lootCommand(event);
                        break;
                    case "maxplayers":
                        this.commandManager.setMaxPlayers(event);
                        break;
                    case "protect":
                        this.commandManager.protectCommand(event);
                        break;
                    case "removehostile":
                        this.commandManager.removeHostile(event);
                        break;
                    case "return":
                        this.commandManager.returnCommand(event);
                        break;
                    case "skip":
                        this.commandManager.skipCommand(event);
                        break;
                    case "start":
                        this.commandManager.startEncounter(event);
                        break;
                    case "use":
                        this.commandManager.useItemCommand(event);
                        break;
                    case "view":
                        this.processViewCommand(event);
                        break;
                    default:
                        channel.sendMessage("Sorry, did you say something? I don't know that command").queue();
                        break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            channel.sendMessage(
                String.format(
                    "Could you say that again? I think I'm missing something... Check `%shelp`",
                    CommandListener.COMMAND_KEY
                )
            ).queue();
        } catch (NumberFormatException e) {
            channel.sendMessage("Hey could you say that again? Sorry, I'm bad with numbers.").queue();
        } catch (Throwable e) {
            if (e instanceof CustomExceptionInterface) {
                channel.sendMessage(e.getMessage()).queue();
            } else {
                channel.sendMessage("Something went wrong, but I'm not sure what...").queue();
                System.out.println(e.toString());
            }
        }

    }

    private void processCreateCommand(MessageReceivedEvent event) {
        Message        message    = event.getMessage();
        MessageChannel channel    = event.getChannel();
        String         input      = message.getContentRaw();
        String[]       splitArray = input.split("\\s+");

        switch (splitArray[1].toLowerCase()) {
            case "character":
                this.commandManager.createCharacterCommand(event);
                break;
            case "encounter":
                this.commandManager.createEncounter(event);
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
            case "allcharacters":
                this.commandManager.viewAllCharacters(event);
                break;
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