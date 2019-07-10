package bot.Encounter.Logger;

import bot.CommandListener;
import bot.Encounter.*;
import bot.Encounter.Logger.MessageBuilder.*;
import bot.Encounter.Tier.Tier;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounterLogger
{

    private static String NEWLINE      = System.getProperty("line.separator");
    private static String DOUBLE_ARROW = "»";

    private ActionMessageBuilder  actionMessageBuilder;
    private SummaryMessageBuilder summaryMessageBuilder;
    private MessageChannel        channel;
    private Mention               dmMention;
    private Mention               everyoneMention;

    /**
     * EncounterLogger constructor
     */
    public @NotNull EncounterLogger(
        @NotNull ActionMessageBuilder actionMessageBuilder,
        @NotNull SummaryMessageBuilder summaryMessageBuilder
    )
    {
        this.actionMessageBuilder = actionMessageBuilder;
        this.summaryMessageBuilder = summaryMessageBuilder;
    }

    /**
     * Log action result
     *
     * @param result Action result
     */
    public void logAction(@NotNull ActionResultInterface result)
    {
        // todo cleanup
        if (result instanceof AttackActionResultInterface) {
            sendMessage(actionMessageBuilder.buildActionMessage(((AttackActionResultInterface) result), dmMention));
        } else if (result instanceof DodgeActionResultInterface) {
            sendMessage(actionMessageBuilder.buildActionMessage((DodgeActionResultInterface) result));
        } else if (result instanceof HealActionResultInterface) {
            sendMessage(String.format(
                "%s heals %d points! [%d/%d]",
                ((HealActionResultInterface) result).getName(),
                ((HealActionResultInterface) result).getHealedHp(),
                ((HealActionResultInterface) result).getCurrentHp(),
                ((HealActionResultInterface) result).getMaxHp()
            ));
        } else if (result instanceof HurtActionResultInterface) {
            sendMessage(String.format(
                "%s takes %d dmg! [%d/%d]",
                ((HurtActionResultInterface) result).getName(),
                ((HurtActionResultInterface) result).getHealedHp(),
                ((HurtActionResultInterface) result).getCurrentHp(),
                ((HurtActionResultInterface) result).getMaxHp()
            ));
            if (((HurtActionResultInterface) result).isSlain()) {
                sendMessage(String.format("%s was slain", ((HurtActionResultInterface) result).getName()));
            }
        } else if (result instanceof JoinActionResultInterface) {
            EncounteredExplorerInterface encounteredExplorer = ((JoinActionResultInterface) result).getExplorer();
            sendMessage(
                String.format(
                    "%s: %s has been added! %s",
                    (Mention.createForPlayer(encounteredExplorer.getOwner().getUserId())).getValue(),
                    encounteredExplorer.getName(),
                    getExplorerPrintout(encounteredExplorer)
                )
            );
            if (((JoinActionResultInterface) result).isRosterFull()) {
                sendMessage("***THE ROSTER IS NOW FULL!***");
            }
        } else if (result instanceof LootActionResultInterface) {
            sendMessage(actionMessageBuilder.buildActionMessage(((LootActionResultInterface) result)));
        } else if (result instanceof ModifyStatActionResultInterface) {
            sendMessage(
                String.format(
                    "%s's %s has gone %s by %d, making it %d!",
                    ((ModifyStatActionResultInterface) result).getCreatureName(),
                    ((ModifyStatActionResultInterface) result).getStatName(),
                    ((ModifyStatActionResultInterface) result).getStatMod() > 0 ? "up" : "down",
                    ((ModifyStatActionResultInterface) result).getStatMod(),
                    ((ModifyStatActionResultInterface) result).getStatValue()
                )
            );
        } else if (result instanceof ProtectActionResultInterface) {
            sendMessage(actionMessageBuilder.buildActionMessage(((ProtectActionResultInterface) result)));
        } else {
            throw LoggerException.createActionNotSet();
        }
    }

    /**
     * Log phase change
     *
     * @param result Phase change result
     */
    public void logPhaseChange(PhaseChangeResult result)
    {
        // todo move to PhaseChangeMessageBuilder
        if (result.getNextPhase().isLootPhase()) {
            logEndEncounterWin(result.getExplorers(), result.getHostiles());
        } else if (result.getNextPhase().isEndPhase()) {
            logEndEncounterLose(result.getExplorers(), result.getHostiles());
            // todo?
//            logEndEncounterForced(result.getExplorers(), result.getHostiles());
        } else if (result.getNextPhase().isJoinPhase()) {
            logStartEncounter(result.getMaxPlayerCount(), result.getTier());
        } else if (result.getNextPhase().isAttackPhase()) {
            logStartAttackPhase(result.getExplorers(), result.getHostiles());
        } else if (result.getNextPhase().isDodgePhase()) {
            logStartDodgePhase(result.getExplorers(), result.getHostiles());
        } else if (result.getNextPhase().isRpPhase()) {
            if (result.getPreviousPhase().isAttackPhase()) {
                logEndAttackPhase(result.getExplorers(), result.getHostiles());
            } else if (result.getPreviousPhase().isDodgePhase()) {
                logEndDodgePhase(result.getExplorers(), result.getHostiles());
            }
        }
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
     * @param name      Name of explorer
     * @param currentHp Current hp of explorer
     * @param maxHp     Max hp of explorer
     */
    public void logActionDodgePass(@NotNull String name, int currentHp, int maxHp)
    {
        sendMessage(
            "```ml" +
            EncounterLogger.NEWLINE +
            String.format("%s successfully Dodges all attacks!", name) +
            EncounterLogger.NEWLINE +
            EncounterLogger.NEWLINE +
            String.format("%s takes 0 dmg total!", name) +
            EncounterLogger.NEWLINE +
            String.format("%d/%d health remaining", currentHp, maxHp) +
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
    public void logAddedHostile(@NotNull EncounteredHostileInterface hostile)
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
     * Log create encounter
     */
    public void logCreateEncounter()
    {
        sendMessage("Encounter creation has started!");
    }

    /**
     * Log end of encounter (forced)
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     */
    public void logEndEncounterForced(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        logSummary(encounteredExplorers, encounteredHostiles);
        sendMessage("***THE BATTLE IS OVER!!!***");
        sendMessage("Game over everyone, the DM commands it! Thanks for playing!");
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
            NEWLINE +
            String.format("%s has been healed %d HP and has earned the \"Zombie\" title.", name, hitpoints) +
            "```"
        );
    }

    /**
     * Log kicked player
     *
     * @param player Kicked player
     */
    public void logKickedPlayer(@NotNull Player player)
    {
        sendMessage(String.format(
            "%s has been kicked and may not rejoin the battle.",
            (Mention.createForPlayer(player.getUserId())).getValue()
        ));
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
        sendMessage(String.format("Explorer %s has been removed", name));
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
     * Tier has been set
     *
     * @param tier Tier
     */
    public void logSetTier(@NotNull Tier tier)
    {
        sendMessage(String.format(
            "%s tier has been set! [Stat Point Range: %d - %d]",
            tier.getName(),
            tier.getMinStatPointTotal(),
            tier.getMaxStatPointTotal()
        ));
    }

    /**
     * Log encounter summary
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     */
    public void logSummary(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        sendMessage(summaryMessageBuilder.buildSummary(encounteredExplorers, encounteredHostiles));
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
                dmMention.getValue(),
                (Mention.createForPlayer(player.getUserId())).getValue()
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
                dmMention.getValue(),
                (Mention.createForPlayer(player.getUserId())).getValue()
            )
        );
    }

    /**
     * Ping player that it is their turn
     *
     * @param explorer Explorer
     */
    public void pingPlayerTurn(@NotNull EncounteredExplorerInterface explorer)
    {
        sendMessage(
            String.format(
                "%s, it's %s's turn!",
                (Mention.createForPlayer(explorer.getOwner().getUserId())).getValue(),
                explorer.getName()
            )
        );
    }

    /**
     * Set logger channel
     *
     * @param channel Channel
     */
    public void setChannel(@NotNull MessageChannel channel)
    {
        this.channel = channel;
    }

    /**
     * Set dungeon master mention
     *
     * @param dmMention Dungeon master mention
     */
    public void setDmMention(@NotNull Mention dmMention)
    {
        this.dmMention = dmMention;
    }

    /**
     * Set everyone mention
     *
     * @param everyoneMention Everyone mention
     */
    public void setEveryoneMention(@NotNull Mention everyoneMention)
    {
        this.everyoneMention = everyoneMention;
    }

    /**
     * Get explorer printout
     *
     * @param explorer Explorer
     *
     * @return String
     */
    private @NotNull String getExplorerPrintout(@NotNull EncounteredExplorerInterface explorer)
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
    private @NotNull String getHostilePrintout(@NotNull EncounteredHostileInterface hostile)
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
     * Log end of attack phase
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     */
    private void logEndAttackPhase(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        sendMessage(
            "**ATTACK TURN IS OVER!**" +
            EncounterLogger.NEWLINE +
            "You may take this time to RP amongst yourselves. The DODGE turn will begin shortly."
        );
        logSummary(encounteredExplorers, encounteredHostiles);
    }

    /**
     * Log end of dodge phase
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     */
    private void logEndDodgePhase(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        sendMessage(
            "**DODGE TURN IS OVER!**" +
            EncounterLogger.NEWLINE +
            "You may take this time to RP amongst yourselves. The ATTACK turn will begin shortly."
        );
        logSummary(encounteredExplorers, encounteredHostiles);
    }

    /**
     * Log end of encounter (lose)
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     */
    private void logEndEncounterLose(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        logSummary(encounteredExplorers, encounteredHostiles);
        sendMessage("***THE BATTLE IS OVER!!!***");
        sendMessage("Well... sorry guys. Looks like the hostiles were too much for you this time around.");
    }

    /**
     * Log end of encounter (win)
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     */
    private void logEndEncounterWin(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        logSummary(encounteredExplorers, encounteredHostiles);
        sendMessage("***THE BATTLE IS OVER!!!***");
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
    }

    /**
     * Log start attack phase
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     */
    private void logStartAttackPhase(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
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
        logSummary(encounteredExplorers, encounteredHostiles);
    }

    /**
     * Log start dodge phase
     *
     * @param encounteredExplorers Encountered explorers
     * @param encounteredHostiles  Encountered hostiles
     */
    private void logStartDodgePhase(
        @NotNull ArrayList<EncounteredExplorerInterface> encounteredExplorers,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
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
        for (EncounteredHostileInterface hostile : encounteredHostiles) {
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
        logSummary(encounteredExplorers, encounteredHostiles);
    }

    /**
     * Log start encounter
     *
     * @param maxPlayers Max number of players
     * @param tier       Tier
     */
    private void logStartEncounter(int maxPlayers, Tier tier)
    {
        sendMessage(
//            everyoneMention.getValue() +
//            EncounterLogger.NEWLINE +
            "**BATTLE TIME!**" +
            EncounterLogger.NEWLINE +
            String.format(
                "To bring an explorer in to battle, use `%sjoin [CharacterName]`.",
                CommandListener.COMMAND_KEY
            ) +
            EncounterLogger.NEWLINE +
            "You may join a battle at any time before the batle has ended and as long as there are slots open!" +
            EncounterLogger.NEWLINE +
            EncounterLogger.NEWLINE +
            String.format("This dungeon has a max capacity of **%d** players. ", maxPlayers) +
            EncounterLogger.NEWLINE +
            String.format(
                "Tier is set to **%s**! All explorers must have a stat point total between **%d** and **%d**",
                tier.getName(),
                tier.getMinStatPointTotal(),
                tier.getMaxStatPointTotal()
            )
        );
    }

    /**
     * Send message
     *
     * @param message Message
     */
    private void sendMessage(@NotNull String message)
    {
        channel.sendMessage(message).queue();
    }
}