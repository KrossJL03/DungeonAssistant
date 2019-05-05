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
        output.append(
            String.format(
                "   %s%-16s %-16s",
                CommandListener.COMMAND_KEY,
                "create character",
                "[Name] [HP] [STR] [DEF] [AGI] [WIS] [AppLink] [StatsLink]"
            )
        );
        output.append(PrivateLogger.NEWLINE);
        output.append(
            String.format(
                "   %s%-16s %-16s",
                CommandListener.COMMAND_KEY,
                "deletecharacter",
                "[Name]"
            )
        );
        output.append(PrivateLogger.NEWLINE);
        output.append("```");
        output.append(PrivateLogger.NEWLINE);
        output.append("Encounter Commands:");
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %s%-16s %-16s %s",
            CommandListener.COMMAND_KEY,
            "attack",
            "[HostileName]",
            "Attack a hostile during the attack turn"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %s%-16s %-16s %s",
            CommandListener.COMMAND_KEY,
            "dodge",
            "",
            "Attempt to dodge enemy attacks during the dodge turn"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %s%-16s %-16s %s",
            CommandListener.COMMAND_KEY,
            "dodgePass",
            "",
            "Auto dodge all attacks. Use for smoke bombs, special abilities, or being protected. DM will be pinged to confirm."
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(
            String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "join",
                "[Name]",
                "Add a character to an encounter"
            )
        );
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %s%-16s %-16s %s",
            CommandListener.COMMAND_KEY,
            "leave",
            "",
            "Leave the encounter"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %s%-16s %-16s %s",
            CommandListener.COMMAND_KEY,
            "loot",
            "",
            "Collect materials and rewards at the end of an encounter"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(
            String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "protect",
                "[Name]",
                "Protect a teammate during the dodge turn"
            )
        );
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %s%-16s %-16s %s",
            CommandListener.COMMAND_KEY,
            "rejoin",
            "",
            "Rejoin the encounter"
        ));
        // todo remove once inventory is implemented
        output.append(PrivateLogger.NEWLINE);
        output.append(
            String.format(
                "   %-16s %-16s %s",
                "rp!use",
                "\"item Name\" [quantity]",
                "Use an item through rp!bot. The DM will be pinged to activate the item."
            )
        );
        output.append("```");
        PrivateLogger.sendPrivateMessage(user, output.toString());

        output = new StringBuilder();
        if (isAdmin) {
            output.append(PrivateLogger.NEWLINE);
            output.append("Dungeon Master Commands:");
            output.append(PrivateLogger.NEWLINE);
            output.append("```ini");
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "addHostile",
                "[Species] (Name)",
                "Add a hostile, Name is optional"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "attackTurn",
                "",
                "Start attack turn"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "create encounter",
                "",
                "Begin creating a new encounter"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "dodgeTurn",
                "",
                "Start dodge turn"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "dodgePass",
                "",
                "The current character successfully dodges all attacks this round"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "endAction",
                "",
                "End a single action of a player. No ill side effects."
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "endEncounter",
                "",
                "End the encounter"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "endTurn",
                "",
                "End a players turn. No ill side effects."
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "heal",
                "[Name] [HP]",
                "Heal a hostile or PC by X hitpoints"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "healAllHostiles",
                "[HP]",
                "Heal all hostiles by X hitpoints"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "healAllPCs",
                "[HP]",
                "Heal all PCs by X hitpoints"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "hurt",
                "[Name] [HP]",
                "Deal X damage to a PC or hostile"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "hurtAllHostiles",
                "[HP]",
                "Deal X damage to all hostiles"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "hurtAllPCs",
                "[HP]",
                "Deal X damage to all PCs"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "kick",
                "[Name]",
                "Forcibly remove a character from an encounter. " +
                "The rejoin command cannot be used by the player to return."
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "maxPlayers",
                "[PlayerCount]",
                "Set number of players permitted"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "removeHostile",
                "[Name]",
                "Remove a hostile"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "skip",
                "",
                "Skip the current player's turn"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "start",
                "",
                "Start the encounter"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "statBoost",
                "[CharacterName] [StatName] [StatBoost]",
                "Increase a characters stat by the boost amount for the current encounter"
            ));
            output.append(PrivateLogger.NEWLINE);
            output.append(String.format(
                "   %s%-16s %-16s %s",
                CommandListener.COMMAND_KEY,
                "statDrop",
                "[CharacterName] [StatName] [StatDrop]",
                "Reduce a characters stat by the drop amount for the current encounter"
            ));
            output.append("```");
            PrivateLogger.sendPrivateMessage(user, output.toString());
            output = new StringBuilder();
        }

        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("View Commands [%sview + COMMAND]:", CommandListener.COMMAND_KEY));
        output.append(PrivateLogger.NEWLINE);
        output.append("```ini");
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %-16s %-16s %s",
            "allcharacters",
            "",
            "View all registered characters"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format(
            "   %-16s %-16s %s",
            "characters",
            "",
            "View your registered characters"
        ));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "hostiles", "", "View all hostiles registered"));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "item", "[Name]", "View detailed item entry"));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "items", "", "View all items"));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "loot", "[Species]", "View loot for a given hostile"));
        output.append(PrivateLogger.NEWLINE);
        output.append(String.format("   %-16s %-16s %s", "summary", "", "View encounter summary"));
        output.append("```");

        PrivateLogger.sendPrivateMessage(user, output.toString());
    }

    private static void sendPrivateMessage(User user, String content) {
        user.openPrivateChannel().queue((channel) -> channel.sendMessage(content).queue());
    }
}
