package bot;

import net.dv8tion.jda.core.entities.User;

public class PrivateLogger {

    private static String NEWLINE = System.getProperty("line.separator");

    public PrivateLogger() {}

    void showHelpPage(User user, boolean isAdmin) {
        StringBuilder output = new StringBuilder();

        output.append("**DUNGEON ASSISTANT COMMANDS:**");
        output.append(PrivateLogger.NEWLINE);
        output.append(PrivateLogger.NEWLINE);
        output.append("General Commands:");
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        if (isAdmin) {
            output.append(String.format("   %-16s %-16s", "$createCharacter", "[Name] [STR] [DEF] [AGI] [WIS] [HP]"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s", "$createHostile", "[Name] [HP] [AttackDice]"));
            output.append(PrivateLogger.NEWLINE);
        } else {
            output.append(String.format("   %-16s %-16s", "$createCharacter", "[Name] [STR] [DEF] [AGI] [WIS] [HP]"));
            output.append(PrivateLogger.NEWLINE);
        }
        output.append("```");
        output.append(PrivateLogger.NEWLINE);
        output.append("Encounter Commands:");
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %-16s %-16s %s",
            "$attack",
            "[HostileName]",
            "Attack a hostile during the attack turn"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %-16s %-16s %s",
            "$dodge",
            "",
            "Attempt to dodge enemy attacks during the dodge turn"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %-16s %-16s %s",
            "$loot",
            "",
            "Collect materials and rewards at the end of an encounter"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "$join", "[Name]", "Add a character to an encounter"));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %-16s %-16s %s",
            "$protect",
            "[Name]",
            "Protect a teammate during the dodge turn"
        ));
        output.append("```");

        if (isAdmin) {
            output.append(PrivateLogger.NEWLINE);
            output.append("Dungeon Master Commands [$dm + COMMAND]:");
            output.append(PrivateLogger.NEWLINE);
            output.append("```ini");
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %-16s %-16s %s",
                "createHostile",
                "[Species] (Name)",
                "Add a hostile, Name is optional"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "attackTurn", "", "Start attack turn"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "create", "", "Begin creating a new encounter"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "dodgeTurn", "", "Start dodge turn"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %-16s %-16s %s",
                "healHostile",
                "[Name] [HP]",
                "Heal a hostile by X hitpoints"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %-16s %-16s %s",
                "healPlayer",
                "[Name] [HP]",
                "Heal a player character by X hitpoints"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %-16s %-16s %s",
                "hurtHostile",
                "[Name] [HP]",
                "Deal X damage to a hostile"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %-16s %-16s %s",
                "hurtPlayer",
                "[Name] [HP]",
                "Deal X damage to a player character"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %-16s %-16s %s",
                "maxPlayers",
                "[PlayerCount]",
                "Set number of players permitted"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "removeHostile", "[Name]", "Remove a hostile"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "removePlayer", "[Name]", "Remove a player"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "skip", "", "Skip the current player's turn"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "start", "", "Start the encounter"));
            output.append("```");
        }
        output.append(PrivateLogger.NEWLINE);
        output.append("View Commands [$view + COMMAND]:");
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        if (isAdmin) {
            output.append(String.format("   %-16s %-16s %s", "characters", "", "View all characters registered"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "hostiles", "", "View all hostiles registered"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "loot", "", "View all hostiles registered"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "summary", "", "View encounter summary"));
        } else {
            output.append(String.format("   %-16s %-16s %s", "players", "", "View all characters registered"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "summary", "", "View encounter summary"));
        }
        output.append("```");

        this.sendPrivateMessage(user, output.toString());
    }

    private void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue());
    }
}
