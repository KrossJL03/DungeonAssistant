package bot;

import net.dv8tion.jda.core.entities.User;

class PrivateLogger {

    private static String NEWLINE = System.getProperty("line.separator");

    static void showHelpPage(User user, boolean isAdmin) {
        StringBuilder output = new StringBuilder();

        output.append("**DUNGEON ASSISTANT COMMANDS:**");
        output.append(PrivateLogger.NEWLINE);
        output.append(PrivateLogger.NEWLINE);
        output.append("General Commands:");
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        if (isAdmin) {
            output.append(
                String.format(
                    "   %-16s%s %-16s",
                    CommandListener.COMMAND_KEY,
                    "createCharacter",
                    "[Name] [STR] [DEF] [AGI] [WIS] [HP]"
                )
            );
            output.append(PrivateLogger.NEWLINE);
//            output.append(String.format("   %-16s %-16s", "$createHostile", "[Name] [HP] [AttackDice]"));
//            output.append(PrivateLogger.NEWLINE);
        } else {
            output.append(
                String.format(
                    "   %-16s%s %-16s",
                    CommandListener.COMMAND_KEY,
                    "createCharacter",
                    "[Name] [STR] [DEF] [AGI] [WIS] [HP]"
                )
            );
            output.append(PrivateLogger.NEWLINE);
        }
        output.append("```");
        output.append(PrivateLogger.NEWLINE);
        output.append("Encounter Commands:");
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %-16s%s %-16s %s",
            CommandListener.COMMAND_KEY,
            "attack",
            "[HostileName]",
            "Attack a hostile during the attack turn"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %-16s%s %-16s %s",
            CommandListener.COMMAND_KEY,
            "dodge",
            "",
            "Attempt to dodge enemy attacks during the dodge turn"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %-16s%s %-16s %s",
            CommandListener.COMMAND_KEY,
            "loot",
            "",
            "Collect materials and rewards at the end of an encounter"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(
            String.format(
                "   %-16s%s %-16s %s",
                CommandListener.COMMAND_KEY,
                "join",
                "[Name]",
                "Add a character to an encounter"
            )
        );
        output.append(PrivateLogger.NEWLINE);
        output.append(
            String.format(
                "   %-16s%s %-16s %s",
                CommandListener.COMMAND_KEY,
                "protect",
                "[Name]",
                "Protect a teammate during the dodge turn"
            )
        );
        output.append(PrivateLogger.NEWLINE);
        output.append(
            String.format(
                "   %-16s%s %-16s %s",
                CommandListener.COMMAND_KEY,
                "use",
                "[ItemName] (RecipientName)",
                "Use an item. Optional recipient name, leave blank to use on yourself."
            )
        );
        output.append("```");

        if (isAdmin) {
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("Dungeon Master Commands [%sdm + COMMAND]:", CommandListener.COMMAND_KEY));
            output.append(PrivateLogger.NEWLINE);
            output.append("```ini");
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %-16s %-16s %s",
                "addHostile",
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
        output.append(String.format("View Commands [%sview + COMMAND]:", CommandListener.COMMAND_KEY));
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        if (isAdmin) {
            output.append(String.format(
                "   %-16s %-16s %s",
                "characters",
                "",
                "View all of your registered characters"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "hostiles", "", "View all hostiles registered"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "loot [Species]", "", "View loot for a given hostile"));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "summary", "", "View encounter summary"));
        } else {
            output.append(String.format(
                "   %-16s %-16s %s",
                "characters",
                "",
                "View all of your registered characters"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format("   %-16s %-16s %s", "summary", "", "View encounter summary"));
        }
        output.append("```");

        PrivateLogger.sendPrivateMessage(user, output.toString());
    }

    private static void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue());
    }
}
