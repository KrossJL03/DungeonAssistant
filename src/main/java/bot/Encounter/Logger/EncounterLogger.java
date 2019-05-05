package bot.Encounter.Logger;

import bot.CommandListener;
import bot.Encounter.EncounterData.*;
import bot.Encounter.Logger.MessageBuilder.*;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounterLogger
{

    private static String NEWLINE      = System.getProperty("line.separator");
    private static String DOUBLE_ARROW = "»";

    private ActionMessageBuilder   actionMessageBuilder;
    private SummaryMessageBuilder  summaryMessageBuilder;
    private EncounterLoggerContext context;

    /**
     * EncounterLogger constructor
     *
     * @param context Encounter logger context
     */
    public @NotNull EncounterLogger(@NotNull EncounterLoggerContext context)
    {
        this.context = context;
        this.actionMessageBuilder = new ActionMessageBuilder();
        this.summaryMessageBuilder = new SummaryMessageBuilder();
    }

    /**
     * Log attack action
     *
     * @param actionData Action data
     */
    public void logAction(@NotNull AttackActionDataInterface actionData)
    {
        sendMessage(actionMessageBuilder.buildActionMessage(actionData, context.getDungeonMasterMention()));
    }

    /**
     * Log dodge action
     *
     * @param actionData Action data
     */
    public void logAction(@NotNull DodgeActionDataInterface actionData)
    {
        sendMessage(actionMessageBuilder.buildActionMessage(actionData));
    }

    /**
     * Log loot action
     *
     * @param actionData Action data
     */
    public void logAction(@NotNull LootActionDataInterface actionData)
    {
        sendMessage(actionMessageBuilder.buildActionMessage(actionData));
    }

    /**
     * Log protect action
     *
     * @param actionData Action data
     */
    public void logAction(@NotNull ProtectActionDataInterface actionData)
    {
        sendMessage(actionMessageBuilder.buildActionMessage(actionData));
    }

    /**
     * Log skipped attack action
     *
     * @param name Character name
     */
    public void logActionAttackSkipped(@NotNull String name)
    {
        sendMessage(String.format("%s's turn has been skipped", name));
    }

    /**
     * Log dodged action passed
     *
     * @param explorer Explorer that passed dodge action
     */
    public void logActionDodgePass(@NotNull PCEncounterData explorer)
    {
        sendMessage(
            "```ml" +
            EncounterLogger.NEWLINE +
            String.format("%s successfully Dodges all attacks!", explorer.getName()) +
            EncounterLogger.NEWLINE +
            EncounterLogger.NEWLINE +
            String.format("%s takes 0 dmg total!", explorer.getName()) +
            EncounterLogger.NEWLINE +
            String.format("%d/%d health remaining", explorer.getCurrentHP(), explorer.getMaxHP()) +
            "```"
        );
    }

    /**
     * Log number of actions remaining for player
     *
     * @param name             Explorer name
     * @param actionsRemaining Number of actions remaining
     */
    public void logActionsRemaining(@NotNull String name, int actionsRemaining)
    {
        sendMessage(
            String.format("%s has %d action%s remaining", name, actionsRemaining, actionsRemaining > 1 ? "s" : "")
        );
    }

    /**
     * Log added hostile
     *
     * @param hostile Hostile
     */
    public void logAddedHostile(@NotNull HostileEncounterData hostile)
    {
        sendMessage(
            String.format(
                "Hostile %s has been added to the encounter %s",
                hostile.getName(),
                getHostilePrintout(hostile)
            )
        );
    }

    /**
     * Log added explorer
     *
     * @param explorer Explorer
     */
    public void logAddedExplorer(@NotNull PCEncounterData explorer)
    {
        sendMessage(
            String.format(
                "%s: %s has been added! %s",
                (new Mention(explorer.getOwner().getUserId())).getValue(),
                explorer.getName(),
                getExplorerPrintout(explorer)
            )
        );
    }

    /**
     * Log create encounter
     */
    public void logCreateEncounter()
    {
        sendMessage("Encounter creation has started!");
    }

    /**
     * Log dungeon is full
     */
    public void logDungeonIsFull()
    {
        sendMessage("***THE DUNGEON IS NOW FULL!***");
    }

    /**
     * Log dungeon master heal
     *
     * @param name      Name of the creature that was healed
     * @param hitpoints Number of hitpoints healed
     * @param currentHP Current HP of the creature
     * @param maxHP     Max HP of the creature
     */
    public void logDungeonMasterHeal(@NotNull String name, int hitpoints, int currentHP, int maxHP)
    {
        sendMessage(String.format("%s heals %d points! [%d/%d]", name, hitpoints, currentHP, maxHP));
    }

    /**
     * Log dungeon master hurt
     *
     * @param name      Name of the creature that were hurt
     * @param hitpoints Number of hitpoints hurt
     * @param currentHP Current HP of the creature
     * @param maxHP     Max HP of the creature
     */
    public void logDungeonMasterHurt(@NotNull String name, int hitpoints, int currentHP, int maxHP)
    {
        sendMessage(String.format("%s takes %d dmg! [%d/%d]", name, hitpoints, currentHP, maxHP));
    }

    /**
     * Log dungeon master stat modification
     *
     * @param name         Name of the creature with modified stats
     * @param statName     Name of modified stat
     * @param statMod      Amount that was changed
     * @param newStatTotal New stat total
     */
    public void logDungeonMasterStatMod(@NotNull String name, String statName, int statMod, int newStatTotal)
    {
        sendMessage(
            String.format(
                "%s's %s has gone %s by %d, making it %d!",
                name,
                statName,
                statMod > 0 ? "up" : "down",
                statMod,
                newStatTotal
            )
        );
    }

    /**
     * Log dungeon master slay
     *
     * @param name Name of slain creature
     */
    public void logDungeonMasterSlay(@NotNull String name)
    {
        sendMessage(String.format("%s was slain", name));
    }

    /**
     * Log end of attack phase
     *
     * @param explorers Explorers
     * @param hostiles  Hostiles
     */
    public void logEndAttackPhase(
        @NotNull ArrayList<PCEncounterData> explorers,
        @NotNull ArrayList<HostileEncounterData> hostiles
    )
    {
        sendMessage(
            "**ATTACK TURN IS OVER!**" +
            EncounterLogger.NEWLINE +
            "You may take this time to RP amongst yourselves. The DODGE turn will begin shortly."
        );
        logSummary(explorers, hostiles);
    }

    /**
     * Log end of dodge phase
     *
     * @param explorers Explorers
     * @param hostiles  Hostiles
     */
    public void logEndDodgePhase(
        @NotNull ArrayList<PCEncounterData> explorers,
        @NotNull ArrayList<HostileEncounterData> hostiles
    )
    {
        sendMessage(
            "**DODGE TURN IS OVER!**" +
            EncounterLogger.NEWLINE +
            "You may take this time to RP amongst yourselves. The ATTACK turn will begin shortly."
        );
        logSummary(explorers, hostiles);
    }

    /**
     * Log end of encounter
     *
     * @param explorers Explorers
     * @param hostiles  Hostiles
     * @param win       Did the players win
     */
    public void logEndEncounter(
        @NotNull ArrayList<PCEncounterData> explorers,
        @NotNull ArrayList<HostileEncounterData> hostiles,
        boolean win
    )
    {
        logSummary(explorers, hostiles);
        sendMessage("***THE BATTLE IS OVER!!!***");
        if (win) {
            sendMessage(
                "Great work everyone! You did it!" +
                EncounterLogger.NEWLINE +
                EncounterLogger.NEWLINE +
                "**LOOT TURN!**" +
                EncounterLogger.NEWLINE +
                String.format(
                    "Please use `%sloot` to harvest materials from the hostiles.",
                    CommandListener.COMMAND_KEY
                ) +
                EncounterLogger.NEWLINE +
                "There is no turn order and if you are unable to roll now you may do so later."
            );
        } else {
            sendMessage("Well... sorry guys. Looks like the hostiles were too much for you this time around.");
        }
    }

    /**
     * Log first death revival
     *
     * @param name      Name of fallen explorer
     * @param hitpoints Amount of hitpoints healed
     */
    public void logFirstDeathRevived(@NotNull String name, int hitpoints)
    {
        sendMessage(
            "```ml" +
            NEWLINE +
            String.format("The guild leader in charge takes a phoenix feather out of their bag, reviving %s!", name) +
            NEWLINE +
            String.format(" %s has been healed %d HP and has earned the \"Zombie\" title.", name, hitpoints) +
            "```"
        );
    }


    /**
     * Log left encounter
     *
     * @param name Name of explorer that left
     */
    public void logLeftEncounter(@NotNull String name)
    {
        sendMessage(String.format("%s has left the encounter", name));
    }

    /**
     * Log rejoined encounter
     *
     * @param name Name of explorer that rejoined
     */
    public void logRejoinEncounter(@NotNull String name)
    {
        sendMessage(String.format("%s has rejoined the encounter!", name));
    }

    /**
     * Log removed explorer
     *
     * @param name Name of explorer removed
     */
    public void logRemovedExplorer(@NotNull String name)
    {
        sendMessage(String.format("PlayerCharacter %s has been removed", name));
    }

    /**
     * Log removed hostile
     *
     * @param name Name of hostile removed
     */
    public void logRemovedHostile(@NotNull String name)
    {
        sendMessage(String.format("Hostile %s has been removed", name));
    }

    /**
     * Log set max players
     *
     * @param maxPlayerCount Max player count
     */
    public void logSetMaxPlayers(int maxPlayerCount)
    {
        sendMessage(String.format("Max player count has been set to %d", maxPlayerCount));
    }

    /**
     * Log encounter summary
     *
     * @param explorers Explorers
     * @param hostiles  Hostiles
     */
    public void logSummary(
        @NotNull ArrayList<PCEncounterData> explorers,
        @NotNull ArrayList<HostileEncounterData> hostiles
    )
    {
        sendMessage(summaryMessageBuilder.buildSummary(explorers, hostiles));
    }

    /**
     * Log start attack phase
     *
     * @param explorers Explorers
     * @param hostiles  Hostiles
     */
    public void logStartAttackPhase(
        @NotNull ArrayList<PCEncounterData> explorers,
        @NotNull ArrayList<HostileEncounterData> hostiles
    )
    {
        sendMessage(
            "**ATTACK TURN!**" +
            EncounterLogger.NEWLINE +
            String.format(
                "Please use `%sattack [HostileName]` to attack. Ex: `%sattack Stanley`",
                CommandListener.COMMAND_KEY,
                CommandListener.COMMAND_KEY
            )
        );
        logSummary(explorers, hostiles);
    }

    /**
     * Log start dodge phase
     *
     * @param explorers Explorers
     * @param hostiles  Hostiles
     */
    public void logStartDodgePhase(
        @NotNull ArrayList<PCEncounterData> explorers,
        @NotNull ArrayList<HostileEncounterData> hostiles
    )
    {
        StringBuilder output      = new StringBuilder();
        int           totalDamage = 0;
        output.append("**DODGE TURN!**");
        output.append(EncounterLogger.NEWLINE);
        output.append(
            String.format(
                "Please `%sdodge` to try to avoid the attack, " +
                "or `%sprotect [CharacterName]` to sacrifice yourself to save someone else. " +
                "Ex: `%sprotect Cocoa`",
                CommandListener.COMMAND_KEY,
                CommandListener.COMMAND_KEY,
                CommandListener.COMMAND_KEY
            )
        );
        output.append(EncounterLogger.NEWLINE);
        output.append("```ml");
        output.append(EncounterLogger.NEWLINE);
        output.append("'Hostiles' attack the party!");
        output.append(EncounterLogger.NEWLINE);
        output.append("\"dmg dice\"");
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        for (HostileEncounterData hostile : hostiles) {
            if (!hostile.isSlain()) {
                totalDamage += hostile.getAttackRoll();
                output.append(String.format(
                    "d%d %s %2d dmg from '%s'!",
                    hostile.getAttackDice(),
                    EncounterLogger.DOUBLE_ARROW,
                    hostile.getAttackRoll(),
                    hostile.getName()
                ));
                output.append(EncounterLogger.NEWLINE);
            }
        }
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("combined attacks add up to %d dmg!!", totalDamage));
        output.append("```");
        sendMessage(output.toString());
        logSummary(explorers, hostiles);
    }

    /**
     * Log start encounter
     *
     * @param mentionRole Role to mention players
     * @param maxPlayers  Max number of players
     */
    public void logStartEncounter(@NotNull Role mentionRole, int maxPlayers)
    {
        sendMessage(
            mentionRole.getAsMention() +
            EncounterLogger.NEWLINE +
            "**BATTLE TIME!**" +
            EncounterLogger.NEWLINE +
            String.format(
                "To bring a character to battle, use `%sjoin [CharacterName]`.",
                CommandListener.COMMAND_KEY
            ) +
            EncounterLogger.NEWLINE +
            String.format(
                "Make sure your character has already been registered using the `%screate character`.",
                CommandListener.COMMAND_KEY
            ) +
            EncounterLogger.NEWLINE +
            "You may join a battle at any time for as long as it's running, and as long as there are slots open!" +
            EncounterLogger.NEWLINE +
            String.format("This dungeon has a max capacity of **%d** players. ", maxPlayers)
        );
    }

    /**
     * Ping the DM to request a dodge pass
     *
     * @param player Player
     */
    public void pingDmDodgePass(@NotNull Player player)
    {
        sendMessage(
            String.format(
                "%s, %s wants to successfully pass their dodge turn. Is this ok? If so please use the command DM.",
                context.getDungeonMasterMention(),
                (new Mention(player.getUserId())).getValue()
            )
        );
    }

    /**
     * Ping DM to request item use
     *
     * @param player Player
     */
    public void pingDmItemUsed(@NotNull Player player)
    {
        sendMessage(
            String.format(
                "%s, %s has used an item. Could you tell me what to do?",
                context.getDungeonMasterMention(),
                (new Mention(player.getUserId())).getValue()
            )
        );
    }

    /**
     * Ping player that it is their turn
     *
     * @param explorer Explorer
     */
    public void pingPlayerTurn(@NotNull PCEncounterData explorer)
    {
        sendMessage(
            String.format(
                "%s, it's %s's turn!",
                (new Mention(explorer.getOwner().getUserId())).getValue(),
                explorer.getName()
            )
        );
    }

    /**
     * Get explorer printout
     *
     * @param explorer Explorer
     *
     * @return String
     */
    private @NotNull String getExplorerPrintout(@NotNull PCEncounterData explorer)
    {
        int    nameBuffer = (int) Math.floor(15 + explorer.getName().length() / 2);
        String output     = "";
        output += "```md";
        output += EncounterLogger.NEWLINE;
        output += nameBuffer < 29 ?
                  String.format("%" + nameBuffer + "s", explorer.getName()) :
                  explorer.getName();
        output += EncounterLogger.NEWLINE;
        output += "=============================";
        output += EncounterLogger.NEWLINE;
        output += "  HP | STR | DEF | AGI | WIS ";
        output += EncounterLogger.NEWLINE;
        output += String.format(
            "%4s | %2s  | %2s  | %2s  | %2s",
            explorer.getMaxHP(),
            explorer.getStrength(),
            explorer.getDefense(),
            explorer.getAgility(),
            explorer.getWisdom()
        );
        output += EncounterLogger.NEWLINE;
        output += "=============================";
        output += EncounterLogger.NEWLINE;
        output += String.format("ATK Dice:  %2d  ", explorer.getAttackDice());
        output += String.format("Min Crit:   %2d", explorer.getMinCrit());
        output += EncounterLogger.NEWLINE;
        output += String.format("DOD Dice:  %2d  ", explorer.getDodgeDice());
        output += String.format("# of Turns: %2d", explorer.getMaxActions());
        output += EncounterLogger.NEWLINE;
        output += "```";
        return output;
    }

    /**
     * Get hostile printout
     *
     * @param hostile Hostile
     *
     * @return String
     */
    private @NotNull String getHostilePrintout(@NotNull HostileEncounterData hostile)
    {
        int    nameBuffer = (int) Math.floor(15 + hostile.getName().length() / 2);
        String output     = "";
        output += "```prolog";
        output += EncounterLogger.NEWLINE;
        output += (nameBuffer < 29 ? String.format("%" + nameBuffer + "s", hostile.getName()) : hostile.getName());
        output += EncounterLogger.NEWLINE;
        output += "*****************************";
        output += EncounterLogger.NEWLINE;
        output += "       HP     |     ATK      ";
        output += EncounterLogger.NEWLINE;
        output += String.format("%9s     |     %2s", hostile.getMaxHP(), hostile.getAttackDice());
        output += EncounterLogger.NEWLINE;
        output += "```";
        return output;
    }

    /**
     * Send message
     *
     * @param message Message
     */
    private void sendMessage(@NotNull String message)
    {
        context.getChannel().sendMessage(message).queue();
    }
}