package bot;

import bot.Entity.*;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public class EncounterLogger {

    private static String NEWLINE           = System.getProperty("line.separator");
    private static String FULL_HEALTH_ICON  = "█";
    private static String EMPTY_HEALTH_ICON = "─";

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
            output.append(String.format("%s was slain by %s!!!", hostile.getName(), pcName));
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
                EncounterLogger.NEWLINE +
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
            output.append(String.format("%s was slain by %s!!!", hostile.getName(), playerCharacter.getName()));
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
            output.append(String.format("%2d » ", dodgeRoll));
            if (dodgeRoll < 10) {
                output.append(String.format("HIT! %2d dmg from '%s'", hostile.getAttackRoll(), hostile.getName()));
            } else {
                output.append(String.format("(* Successfully dodged %s! *)", hostile.getName()));
            }
            output.append(EncounterLogger.NEWLINE);
        }

        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("Resisted %d dmg through sheer might!", totalDefended));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%s takes %d dmg total!", playerCharacter.getName(), totalDamage));
        output.append(EncounterLogger.NEWLINE);
        if (playerCharacter.isSlain()) {
            output.append(String.format("%s has been knocked out!!", playerCharacter.getName()));
        } else {
            output.append(String.format(
                "%d/%d health remaining",
                playerCharacter.getCurrentHP(),
                playerCharacter.getMaxHP()
            ));
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
            output.append(String.format("%s has been knocked out!!", playerCharacter.getName()));
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
            "%s jumps in front of %s and shields them from the attacks!",
            protectorCharacter.getName(),
            protectedCharacter.getName()
        ));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("Resisted %d dmg through sheer might!", totalDefended));
        output.append(EncounterLogger.NEWLINE);
        output.append(EncounterLogger.NEWLINE);
        output.append(String.format("%s takes %d dmg total!", protectorCharacter.getName(), totalDamage));
        output.append(EncounterLogger.NEWLINE);
        if (protectorCharacter.isSlain()) {
            output.append(String.format("%s has been knocked out!!", protectorCharacter.getName()));
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
            "**DODGE TURN IS OVER!** %s " +
                EncounterLogger.NEWLINE +
                "You may take this time to RP amoungst yourselves. The ATTACK turn will begin shortly."
        );
        this.logEncounterSummary(playerCharacters, hostiles);
    }

    void logEndEncounter(
        ArrayList<PCEncounterData> playerCharacters,
        ArrayList<HostileEncounterData> hostiles,
        boolean win
    ) {
        StringBuilder output = new StringBuilder();
        this.logEncounterSummary(playerCharacters, hostiles);
        output.append(EncounterLogger.NEWLINE);
        output.append("***THE BATTLE IS OVER!!!***");
        output.append(EncounterLogger.NEWLINE);
        if (win) {
            output.append(
                String.format(
                    "Great job eveyone! Please wait patiently while %s and I discuss your prizes!",
                    this.context.getDungeonMasterMention()
                )
            );
            output.append("Feel free to RP amoungst yourselves in the meantime.");
        } else {
            output.append("Well... hmm... sorry guys. Looks like the hostiles were too much for you this time around.");
        }
        this.logMessage(output.toString());
    }

    void logStartAttackPhase(ArrayList<PCEncounterData> playerCharacters, ArrayList<HostileEncounterData> hostiles) {
        this.logMessage(
            "**ATTACK TURN!**" +
                EncounterLogger.NEWLINE +
                "Please use `$attack [HostileName]` to attack. Ex: `$attack Stanely`"
        );
        this.logEncounterSummary(playerCharacters, hostiles);
    }

    void logStartDodgePhase(ArrayList<PCEncounterData> playerCharacters, ArrayList<HostileEncounterData> hostiles) {
        StringBuilder output      = new StringBuilder();
        int           totalDamage = 0;
        output.append("**DODGE TURN!**");
        output.append(EncounterLogger.NEWLINE);
        output.append(
            "Please `$dodge` to try to avoid the attack, " +
                "or `$protect [CharacterName]` to sacrifice yourself to save someone else. " +
                "Ex: `$protect Cocoa`"
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
                    "(d%d) '%s' attacks... %10d dmg!",
                    hostile.getAttackDice(),
                    hostile.getName(),
                    hostile.getAttackRoll()
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
//            mentionRole.getAsMention() + todo uncomment after testing
            "everyone" +
                EncounterLogger.NEWLINE +
                "**BATTLE TIME!**" +
                EncounterLogger.NEWLINE +
                "To bring a character to battle, use `$join [CharacterName]`." +
                EncounterLogger.NEWLINE +
                "Make sure your character has already been registered using the `$createCharacter`." +
                EncounterLogger.NEWLINE +
                "You may join a battle at any time for as long as it's running, and as long as there are slots open!" +
                EncounterLogger.NEWLINE +
                String.format("This dungeon has a max capacity of **%d** players. ", maxPlayers)
        );
    }

    void logExceptionCharacterUnableToProtect() {
        this.logMessage("You've already used your `$protect` for this encounter");
    }

    void logExceptionDungeonMasterNotFound() {
        this.logMessage("How can we play without a DungeonMaster? I don't see that role anywhere...");
    }

    void logExceptionEmptyDungeon() {
        this.logMessage("Well uh... this is awkward. Is seems we don't have any players...");
    }

    void logExceptionEncounterInProgress() {
        this.logMessage("Hold your Capra! This encounter is already in progress.");
    }

    void logExceptionEncounterIsOver() {
        this.logMessage("This encounter is over. If you'd like to start a new one use the `$createEnc` command");
    }

    void logExceptionEncounterNotStarted() {
        this.logMessage("Hold your Rudi! This encounter hasn't even started yet.");
    }

    void logExceptionFullDungeon(User player) {
        this.logMessage(
            String.format(
                "Uh oh, looks like the dungeon is full. Sorry %s.", player.getAsMention()
            )
        );
    }

    void logExceptionHostileNicknameInUse(String name) {
        this.logMessage(String.format("There's already a hostile named %s in this battle", name));
    }

    void logExceptionHostileSlain(String hostileName, String slayerName) {
        this.logMessage(String.format("%s was slain by %s", hostileName, slayerName));
    }

    void logExceptionMultiplePlayerCharacters(User player, String name) {
        this.logMessage(
            String.format(
                "%s, you have already joined this encounter with %s. " +
                    "If you'd like to switch please talk to the DungeonMaster",
                player.getAsMention(),
                name
            )
        );
    }

    void logExceptionMaxZeroPlayers() {
        this.logMessage("Wait! I don't know how many players to have! Tell me using `$setMaxPlayers`.");
    }

    void logExceptionNoCharacterInEncounter(Throwable e) {
        this.logMessage(e.getMessage());
    }

    void logExceptionNoHostileFound(String species) {
        this.logMessage(
            String.format(
                "I'm not familiar with %s, could you describe them for me using the `$createHostile` command?",
                species
            )
        );
    }

    void logExceptionNoHostileInEncounter(String hostileName) {
        this.logMessage(String.format("I don't see any hostiles named %s", hostileName));
    }

    void logExceptionNoHostiles() {
        this.logMessage("Uh, wait. Who are we fighting again? Tell me using `$addHostile NAME HP ATK`.");
    }

    void logExceptionNotInInitiative() {
        this.logMessage("There is no turn order currently, so there is no current player to be skipped.");
    }

    void logExceptionNotYourTurn() {
        this.logMessage("Hey! Wait your turn!");
    }

    void logExceptionPlayerCharacterAlreadyLeft() {
        this.logMessage("You have already left");
    }

    void logExceptionProtectedCharacterIsSlain(String name) {
        this.logMessage(String.format("%s has already been slain. They can not be protected.", name));
    }

    void logExceptionProtectedCharactersTurnHasPassed(String name) {
        this.logMessage(String.format("%s's turn has already passed. They can not be protected.", name));
    }

    void logExceptionProtectYourself() {
        this.logMessage("You can't protect yourself.");
    }

    void logExceptionStartCurrentPhase(String phase) {
        this.logMessage(String.format("The %s turn is already in progress", phase));
    }

    void logExceptionWrongPhase(String phase, String commandName) {
        this.logMessage(String.format("You can only `$%s` during the %s turn", commandName, phase.toUpperCase()));
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
        this.logMessage(
            String.format(
                "*DUNGEON FULL!* %s Please wait for DungeonMaster to initiate the attack turn",
                EncounterLogger.NEWLINE
            )
        );
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

    void logDungeonMasterSlay(String name) {
        this.logMessage(String.format("%s was slain", name));
    }

    void logSetMaxPlayers(int maxPlayerCount) {
        this.logMessage(String.format("Max player count has been set to %d", maxPlayerCount));
    }

    void logLeftEncounter(PCEncounterData playerCharacter) {
        this.logMessage(String.format("%s has left the encounter", playerCharacter.getName()));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private String getHealthBar(EncounterDataInterface creature) {
        StringBuilder output    = new StringBuilder();
        int           currentHP = creature.getCurrentHP();
        int           maxHP     = creature.getMaxHP();
        if (creature.isSlain()) {
            EncounterDataInterface slayer = creature.getSlayer();
            output.append(String.format("--- %s was slain", creature.getName()));
            if (slayer != null) {
                output.append(String.format(" by %s", slayer.getName()));
            }
        } else {
            output.append(creature.getName());
            output.append(EncounterLogger.NEWLINE);
            output.append(String.format("%-2s", currentHP > maxHP / 4 ? "+" : "-"));
            output.append(String.format("[%3d/%3d] ", currentHP, maxHP));
            int fullHealthBlocks  = (int) Math.ceil((double) currentHP / 10);
            int emptyHealthBlocks = maxHP / 10 - fullHealthBlocks;
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