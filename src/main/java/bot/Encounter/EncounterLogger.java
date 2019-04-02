package bot.Encounter;

import bot.CommandListener;
import bot.Encounter.EncounterData.EncounterDataInterface;
import bot.Encounter.EncounterData.HostileEncounterData;
import bot.Encounter.EncounterData.PCEncounterData;
import bot.Encounter.Exception.ItemRecipientException;
import bot.Hostile.Loot;
import bot.Item.Consumable.ConsumableItem;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;

public class EncounterLogger {

    private static String NEWLINE           = System.getProperty("line.separator");
    private static String FULL_HEALTH_ICON  = "█";
    private static String EMPTY_HEALTH_ICON = "─";
    private static String DOUBLE_ARROW      = "»";

    private EncounterLoggerContext context;

    public EncounterLogger(EncounterLoggerContext context) {
        this.context = context;
    }

    void logActionAttackCrit(
        String pcName,
        HostileEncounterData hostile,
        int hitRoll,
        int damage
    ) {
        StringBuilder output = new StringBuilder();
        output.append("```ml");
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%s attacks %s!", pcName, hostile.getName()));
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("d20 \"hit dice\" rolled %d [CRIT]", hitRoll));
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("MAX DAMAGE %d!!", damage));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("'%s' takes %d damage!", hostile.getName(), damage));
        output.append(EncounterLogger.NEWLINE);
        if (hostile.isSlain()) {
            output.append(String.format("'%s' was slain by %s!!!", hostile.getName(), pcName));
        } else {
            output.append(String.format("%d/%d health remaining", hostile.getCurrentHP(), hostile.getMaxHP()));
        }
        output.append(EncounterLogger.NEWLINE);
        output.append("```");
        this.logMessage(output.toString());
    }

    void logActionAttackFail(String pcName, String hostileName) {
        this.logMessage(
            "```ml" +
            EncounterLogger.NEWLINE +
            String.format("%s attacks %s!", pcName, hostileName) +
            EncounterLogger.NEWLINE +
            "d20 \"hit dice\" rolled 1 [FAIL]" +
            EncounterLogger.NEWLINE + EncounterLogger.NEWLINE +
            "well... that's 'unfortunate'" +
            "```" +
            String.format("Sit tight while me and %s discuss your fate", this.context.getDungeonMasterMention())
        );
    }

    void logActionAttackHit(
        PCEncounterData playerCharacter,
        HostileEncounterData hostile,
        int hitRoll,
        int damage
    ) {
        StringBuilder output = new StringBuilder();
        output.append("```ml");
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%s attacks %s!", playerCharacter.getName(), hostile.getName()));
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("d20 \"hit dice\" rolled %d [Hit]", hitRoll));
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("d%d \"dmg dice\" rolled %d", playerCharacter.getAttackDice(), damage));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("'%s' takes %d damage!", hostile.getName(), damage));
        output.append(EncounterLogger.NEWLINE);
        if (hostile.isSlain()) {
            output.append(String.format("'%s' was slain by %s!!!", hostile.getName(), playerCharacter.getName()));
        } else {
            output.append(String.format("%d/%d health remaining", hostile.getCurrentHP(), hostile.getMaxHP()));
        }
        output.append(EncounterLogger.NEWLINE);
        output.append("```");
        this.logMessage(output.toString());
    }

    void logActionAttackMiss(HostileEncounterData hostile, String pcName, int hitRoll) {
        this.logMessage(
            "```ml" +
            EncounterLogger.NEWLINE +
            String.format("%s attacks %s!", pcName, hostile.getName()) +
            EncounterLogger.NEWLINE +
            String.format("d20 \"hit dice\" rolled %d [Miss]", hitRoll) +
            EncounterLogger.NEWLINE + EncounterLogger.NEWLINE +
            String.format(
                "'%s' is has %d/%d health remaining",
                hostile.getName(),
                hostile.getCurrentHP(),
                hostile.getMaxHP()
            ) +
            EncounterLogger.NEWLINE +
            "```"
        );
    }

    void logActionAttackSkipped(String name) {
        this.logMessage(String.format("%s's turn has been skipped", name));
    }

    void logActionDodge(
        PCEncounterData playerCharacter,
        ArrayList<HostileEncounterData> hostiles,
        ArrayList<Integer> dodgeRolls,
        int totalDamage,
        int totalDefended
    ) {
        if (hostiles.size() != dodgeRolls.size()) {
            throw new IllegalArgumentException();
        }
        StringBuilder output = new StringBuilder();
        output.append("```ml");
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%s attempts to Dodge %d attacks!", playerCharacter.getName(), dodgeRolls.size()));
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("d%d \"dodge dice\" (success = 10)", playerCharacter.getDodgeDice()));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        for (int i = 0; i < hostiles.size(); i++) {
            HostileEncounterData hostile   = hostiles.get(i);
            int                  dodgeRoll = dodgeRolls.get(i);
            output.append(String.format("%2d %s ", dodgeRoll, EncounterLogger.DOUBLE_ARROW));
            if (dodgeRoll < 10) {
                output.append(String.format("'FAIL'! %2d dmg from '%s'", hostile.getAttackRoll(), hostile.getName()));
            } else {
                output.append(String.format("DODGED! (* No dmg from %s! *)", hostile.getName()));
            }
            output.append(EncounterLogger.NEWLINE);
        }
        if (totalDefended > 0) {
            output.append(EncounterLogger.NEWLINE);
            output.append(String.format("Resisted %d dmg through sheer might!", totalDefended));
            output.append(EncounterLogger.NEWLINE);
        }
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%s takes %d dmg total!", playerCharacter.getName(), totalDamage));
        output.append(EncounterLogger.NEWLINE);
        if (playerCharacter.isSlain()) {
            output.append(String.format("%s has been 'knocked' 'out'!!", playerCharacter.getName()));
        } else {
            output.append(
                String.format(
                    "%d/%d health remaining",
                    playerCharacter.getCurrentHP(),
                    playerCharacter.getMaxHP()
                )
            );
        }
        output.append("```");
        this.logMessage(output.toString());
    }

    void logActionDodgeSkipped(PCEncounterData playerCharacter, int totalDamage, int totalDefended) {
        StringBuilder output = new StringBuilder();
        output.append("```ml");
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%s was distracted, they failed to dodge the attacks!", playerCharacter.getName()));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%s is hit for 'full' damage!", playerCharacter.getName()));
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("Resisted %d dmg through sheer might!", totalDefended));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%s takes %d dmg total!", playerCharacter.getName(), totalDamage));
        output.append(EncounterLogger.NEWLINE);
        if (playerCharacter.isSlain()) {
            output.append(String.format("%s has been 'knocked' 'out'!!", playerCharacter.getName()));
        } else {
            output.append(
                String.format(
                    "%d/%d health remaining",
                    playerCharacter.getCurrentHP(),
                    playerCharacter.getMaxHP()
                )
            );
        }
        output.append("```");
        this.logMessage(output.toString());
    }

    void logActionLoot(PCEncounterData playerCharacter) {
        ArrayList<HostileEncounterData> hostiles = playerCharacter.getKills();
        StringBuilder                   output   = new StringBuilder();
        ArrayList<String>               slain    = new ArrayList<>();
        for (HostileEncounterData hostile : hostiles) {
            EncounterDataInterface slayer = hostile.getSlayer();
            if (slayer instanceof PCEncounterData && slayer.equals(playerCharacter)) {
                slain.add(hostile.getName());
            }
        }
        output.append(playerCharacter.getOwner().getAsMention());
        output.append("```ml");
        output.append(EncounterLogger.NEWLINE);
        output.append(
            String.format(
                "%s helped slay %d hostiles!",
                playerCharacter.getName(),
                hostiles.size()
            )
        );
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%dd10 \"loot dice\"", hostiles.size()));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        for (HostileEncounterData hostile : hostiles) {
            int  lootRoll = playerCharacter.getLootRoll(hostile.getName());
            Loot loot     = hostile.getHostile().getLoot(lootRoll);
            output.append(String.format("%2d %s ", lootRoll, EncounterLogger.DOUBLE_ARROW));
            if (loot.getItem() == null) {
                output.append(String.format("(* nothing from %s *)", hostile.getName()));
            } else {
                output.append(String.format("x%d %s from '%s'", loot.getQuantity(), loot.getItem(), hostile.getName()));
            }
            output.append(EncounterLogger.NEWLINE);
        }
        output.append(EncounterLogger.NEWLINE);
        if (slain.size() > 0) {
            output.append(
                String.format(
                    "ALSO they earned %dc for landing the final blow%s on '%s'!",
                    slain.size() * 300,
                    slain.size() > 1 ? "s" : "",
                    String.join("', '", slain)
                )
            );
            output.append(EncounterLogger.NEWLINE);
        }
        output.append("Congratulations!");
        output.append("```");
        this.logMessage(output.toString());
    }

    void logActionProtect(
        PCEncounterData protectorCharacter,
        PCEncounterData protectedCharacter,
        int totalDamage,
        int totalDefended
    ) {
        StringBuilder output = new StringBuilder();
        output.append("```ml");
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format(
            "%s shields %s from the attacks!",
            protectorCharacter.getName(),
            protectedCharacter.getName()
        ));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        if (totalDefended > 0) {
            output.append(String.format("Resisted %d dmg through sheer might!", totalDefended));
            output.append(EncounterLogger.NEWLINE);
            output.append(EncounterLogger.NEWLINE);
        }
        output.append(String.format("%s takes %d dmg total!", protectorCharacter.getName(), totalDamage));
        output.append(EncounterLogger.NEWLINE);
        if (protectorCharacter.isSlain()) {
            output.append(String.format("%s has been 'knocked' 'out'!!", protectorCharacter.getName()));
        } else {
            output.append(String.format(
                "%d/%d health remaining",
                protectorCharacter.getCurrentHP(),
                protectorCharacter.getMaxHP()
            ));
        }
        output.append("```");
        output.append(String.format(
            "%s, %s has been protected. They take no damage this round.",
            protectedCharacter.getOwner().getAsMention(),
            protectedCharacter.getName()
        ));
        this.logMessage(output.toString());
    }

    void logActionsRemaining(String name, int actionsRemaining) {
        this.logMessage(
            String.format("%s has %d %s remaining", name, actionsRemaining, actionsRemaining > 1 ? "actions" : "action")
        );
    }

    void logEndAttackPhase(ArrayList<PCEncounterData> playerCharacters, ArrayList<HostileEncounterData> hostiles) {
        this.logMessage(
            "**ATTACK TURN IS OVER!**" +
            EncounterLogger.NEWLINE +
            "You may take this time to RP amoungst yourselves. The DODGE turn will begin shortly."
        );
        this.logEncounterSummary(playerCharacters, hostiles);
    }

    void logEndDodgePhase(ArrayList<PCEncounterData> playerCharacters, ArrayList<HostileEncounterData> hostiles) {
        this.logMessage(
            "**DODGE TURN IS OVER!**" +
            EncounterLogger.NEWLINE +
            "You may take this time to RP amoungst yourselves. The ATTACK turn will begin shortly."
        );
        this.logEncounterSummary(playerCharacters, hostiles);
    }

    void logEndEncounter(
        ArrayList<PCEncounterData> playerCharacters,
        ArrayList<HostileEncounterData> hostiles,
        int type
    ) {
        this.logEncounterSummary(playerCharacters, hostiles);
        this.logMessage("***THE BATTLE IS OVER!!!***");
        if (type == 1) {
            this.logMessage(
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
        } else if (type == -1) {
            this.logMessage("Well... sorry guys. Looks like the hostiles were too much for you this time around.");
        } else {
            this.logMessage("Game over everyone, the DM commands it! Thanks for playing!");
        }
    }

    void logStartAttackPhase(ArrayList<PCEncounterData> playerCharacters, ArrayList<HostileEncounterData> hostiles) {
        this.logMessage(
            "**ATTACK TURN!**" +
            EncounterLogger.NEWLINE +
            String.format(
                "Please use `%sattack [HostileName]` to attack. Ex: `%sattack Stanley`",
                CommandListener.COMMAND_KEY,
                CommandListener.COMMAND_KEY
            )
        );
        this.logEncounterSummary(playerCharacters, hostiles);
    }

    void logStartDodgePhase(ArrayList<PCEncounterData> playerCharacters, ArrayList<HostileEncounterData> hostiles) {
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
        this.logMessage(output.toString());
        this.logEncounterSummary(playerCharacters, hostiles);
    }

    void logStartEncounter(Role mentionRole, int maxPlayers) {
        this.logMessage(
            // todo uncomment after testing
            mentionRole.getAsMention() +
//            "everyone" +
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

    void pingPlayerTurn(PCEncounterData playerCharacter) {
        this.logMessage(
            String.format(
                "%s, it's %s's turn!",
                playerCharacter.getOwner().getAsMention(),
                playerCharacter.getName()
            )
        );
    }

    // TODO: organize
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    void logDungeonIsFull() {
        this.logMessage("***THE DUNGEON IS NOW FULL!***");
    }

    void logEncounterSummary(ArrayList<PCEncounterData> playerCharacters, ArrayList<HostileEncounterData> hostiles) {
        StringBuilder output = new StringBuilder();
        output.append("```diff");
        output.append(EncounterLogger.NEWLINE);
        output.append("ENCOUNTER SUMMARY");
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(EncounterLogger.NEWLINE);
        output.append("Hostiles");
        output.append(EncounterLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(EncounterLogger.NEWLINE);
        for (HostileEncounterData hostile : hostiles) {
            output.append(this.getHealthBar(hostile));
            output.append(EncounterLogger.NEWLINE);
        }
        output.append(EncounterLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(EncounterLogger.NEWLINE);
        output.append("Player Characters");
        output.append(EncounterLogger.NEWLINE);
        output.append("------------------------------------");
        output.append(EncounterLogger.NEWLINE);
        for (PCEncounterData playerCharacter : playerCharacters) {
            output.append(this.getHealthBar(playerCharacter));
            output.append(EncounterLogger.NEWLINE);
        }
        output.append("```");
        output.append(EncounterLogger.NEWLINE);
        this.logMessage(output.toString());
    }

    void logCreateEncounter() {
        this.logMessage("Encounter creation has started!");
    }

    void logAddedHostile(HostileEncounterData hostile) {
        this.logMessage(
            String.format(
                "Hostile %s has been added to the encounter %s",
                hostile.getName(),
                this.getHostilePrintout(hostile)
            )
        );
    }

    void logAddedPlayerCharacter(PCEncounterData playerCharacter) {
        this.logMessage(
            String.format(
                "%s: %s has been added! %s",
                playerCharacter.getOwner().getAsMention(),
                playerCharacter.getName(),
                this.getPlayerCharacterPrintout(playerCharacter)
            )
        );
    }

    void logRemovedHostile(String name) {
        this.logMessage(String.format("Hostile %s has been removed", name));
    }

    void logRemovedPlayerCharacter(String name) {
        this.logMessage(String.format("PlayerCharacter %s has been removed", name));
    }

    void logDungeonMasterHeal(String name, int hitpoints, int currentHP, int maxHP) {
        this.logMessage(String.format("%s heals %d points! [%d/%d]", name, hitpoints, currentHP, maxHP));
    }

    void logDungeonMasterHurt(String name, int hitpoints, int currentHP, int maxHP) {
        this.logMessage(String.format("%s takes %d dmg! [%d/%d]", name, hitpoints, currentHP, maxHP));
    }

    void logDungeonMasterStatMod(String name, String statName, int statMod, int newStatTotal) {
        this.logMessage(
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

    void logDungeonMasterSlay(String name) {
        this.logMessage(String.format("%s was slain", name));
    }

    void logSetMaxPlayers(int maxPlayerCount) {
        this.logMessage(String.format("Max player count has been set to %d", maxPlayerCount));
    }

    void logLeftEncounter(String name) {
        this.logMessage(String.format("%s has left the encounter", name));
    }

    void logRejoinEncounter(String name) {
        this.logMessage(String.format("%s has rejoined the encounter!", name));
    }

    void logUsedItem(
        PCEncounterData playerCharacter,
        EncounterDataInterface recipient,
        ConsumableItem item,
        int hitpointsHealed,
        int damage,
        String boostedStat,
        boolean isRevived
    ) {
        String  playerCharacterName = playerCharacter.getName();
        boolean usedOnSelf          = recipient == playerCharacter;
        String  recipientName       = recipient.getName();

        StringBuilder output = new StringBuilder();
        if (item.isDmPinged()) {
            output.append(this.context.getDungeonMasterMention());
            output.append(EncounterLogger.NEWLINE);
        }
        output.append("```ml");
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format(
            "%s uses a %s on %s!",
            playerCharacterName,
            item.getName(),
            usedOnSelf ? "themself" : recipient.getName()
        ));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        if (hitpointsHealed > 0) {
            if (!usedOnSelf && item.isUserHealed()) {
                output.append(String.format("%s regains %d HP!", playerCharacterName, hitpointsHealed));
                output.append(EncounterLogger.NEWLINE);
                output.append(String.format(
                    "%d/%d health remaining",
                    playerCharacter.getCurrentHP(),
                    playerCharacter.getMaxHP()
                ));
            } else {
                if (isRevived) {
                    output.append(String.format("%s has been revived and earned the \"Zombie\" Title", recipientName));
                    output.append(EncounterLogger.NEWLINE);
                }
                output.append(String.format("%s is healed %d HP!", recipientName, hitpointsHealed));
                output.append(EncounterLogger.NEWLINE);
                output.append(String.format("%d/%d health remaining", recipient.getCurrentHP(), recipient.getMaxHP()));
            }
            output.append(EncounterLogger.NEWLINE);
        }
        if (damage > 0) {
            output.append(String.format("%s takes %d dmg!", recipientName, damage));
            output.append(EncounterLogger.NEWLINE);
            if (recipient.isSlain()) {
                output.append(String.format("'%s' was slain by %s!!!", recipientName, playerCharacterName));
            } else {
                output.append(String.format("%d/%d health remaining", recipient.getCurrentHP(), recipient.getMaxHP()));
            }
        }
        if (item.isTempStatBoost()) {
            if (recipient instanceof PCEncounterData) {
                output.append(EncounterLogger.NEWLINE);
                output.append(
                    String.format(
                        "%s's %s is increased by %d! That brings it to %d!",
                        recipientName,
                        boostedStat,
                        item.getTempStatBoost(),
                        ((PCEncounterData) recipient).getStat(boostedStat)
                    )
                );
            } else {
                throw ItemRecipientException.createBoostHostile(recipient.getName());
            }
        }
        output.append(EncounterLogger.NEWLINE);
        output.append("```");
        this.logMessage(output.toString());

        if (item.isDodging()) {
            this.logActionDodgePass(playerCharacter);
        }
    }

    void logFirstDeathRevived(String name, int hitpoints) {
        this.logMessage(
            "```ml" +
            NEWLINE +
            String.format("The guild leader in charge takes a phoenix feather out of their bag, reviving %s!", name) +
            NEWLINE +
            String.format(" %s has been healed %d HP and has earned the \"Zombie\" title.", name, hitpoints) +
            NEWLINE +
            "```"
        );
    }

    void pingDmDodgePass(Player player) {
        this.logMessage(
            String.format(
                "%s, %s wants to successfully pass their dodge turn. Is this ok? If so please use the command DM.",
                this.context.getDungeonMasterMention(),
                player.getAsMention()
            )
        );
    }

    // todo remove once inventory is implemented
    void pingDmItemUsed(Player player) {
        this.logMessage(
            String.format(
                "%s, %s has used an item. Could you tell me what to do?",
                this.context.getDungeonMasterMention(),
                player.getAsMention()
            )
        );
    }

    void logActionDodgePass(PCEncounterData playerCharacter) {
        this.logMessage(
            "```ml" +
            EncounterLogger.NEWLINE +
            String.format("%s successfully Dodges all attacks!", playerCharacter.getName()) +
            EncounterLogger.NEWLINE +
            EncounterLogger.NEWLINE +
            String.format("%s takes 0 dmg total!", playerCharacter.getName()) +
            EncounterLogger.NEWLINE +
            String.format("%d/%d health remaining", playerCharacter.getCurrentHP(), playerCharacter.getMaxHP()) +
            "```"
        );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String getHealthBar(EncounterDataInterface creature) {
        StringBuilder output    = new StringBuilder();
        int           currentHP = creature.getCurrentHP();
        int           maxHP     = creature.getMaxHP();
        if (creature instanceof PCEncounterData && !((PCEncounterData) creature).isPresent()) {
            output.append(String.format("--- %s has left", creature.getName()));
        } else if (creature.isSlain()) {
            EncounterDataInterface slayer = creature.getSlayer();
            if (creature instanceof PCEncounterData) {
                output.append(String.format("--- %s was knocked out", creature.getName()));
            } else {
                output.append(String.format("--- %s was slain", creature.getName()));
            }
            if (slayer != null) {
                output.append(String.format(" by %s", slayer.getName()));
            }
        } else {
            output.append(creature.getName());
            if (creature instanceof PCEncounterData) {
                output.append(String.format(" [%s]", ((PCEncounterData) creature).getOwner().getName()));
            }
            output.append(EncounterLogger.NEWLINE);
            output.append(String.format("%-2s", currentHP > maxHP / 4 ? "+" : "-"));
            output.append(String.format("[%3d/%3d] ", currentHP, maxHP));
            int healthBlocks      = (int) Math.ceil(maxHP / 10) + 1;
            int emptyHealthBlocks = (int) Math.ceil((double) (maxHP - currentHP) / 10);
            int fullHealthBlocks  = healthBlocks - emptyHealthBlocks;
            output.append(this.repeatString(EncounterLogger.FULL_HEALTH_ICON, fullHealthBlocks));
            if (emptyHealthBlocks > 0) {
                output.append(this.repeatString(EncounterLogger.EMPTY_HEALTH_ICON, emptyHealthBlocks));
            }
        }
        return output.toString();
    }

    private String getHostilePrintout(HostileEncounterData hostile) {
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

    private String getPlayerCharacterPrintout(PCEncounterData playerCharacter) {
        int    nameBuffer = (int) Math.floor(15 + playerCharacter.getName().length() / 2);
        String output     = "";
        output += "```md";
        output += EncounterLogger.NEWLINE;
        output += nameBuffer < 29 ?
                  String.format("%" + nameBuffer + "s", playerCharacter.getName()) :
                  playerCharacter.getName();
        output += EncounterLogger.NEWLINE;
        output += "=============================";
        output += EncounterLogger.NEWLINE;
        output += "  HP | STR | DEF | AGI | WIS ";
        output += EncounterLogger.NEWLINE;
        output += String.format(
            "%4s | %2s  | %2s  | %2s  | %2s",
            playerCharacter.getMaxHP(),
            playerCharacter.getStrength(),
            playerCharacter.getDefense(),
            playerCharacter.getAgility(),
            playerCharacter.getWisdom()
        );
        output += EncounterLogger.NEWLINE;
        output += "=============================";
        output += EncounterLogger.NEWLINE;
        output += String.format("ATK Dice:  %2d  ", playerCharacter.getAttackDice());
        output += String.format("Min Crit:   %2d", playerCharacter.getMinCrit());
        output += EncounterLogger.NEWLINE;
        output += String.format("DOD Dice:  %2d  ", playerCharacter.getDodgeDice());
        output += String.format("# of Turns: %2d", playerCharacter.getMaxActions());
        output += EncounterLogger.NEWLINE;
        output += "```";
        return output;
    }

    private void logMessage(String message) {
        this.context.getChannel().sendMessage(message).queue();
    }

    private String repeatString(String stringToRepeat, int count) {
        return new String(new char[count]).replace("\0", stringToRepeat);
    }

}