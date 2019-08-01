package bot.Encounter.Command;

import bot.CommandException;
import org.jetbrains.annotations.NotNull;

class EncounterCommandException extends CommandException
{
    /**
     * EncounterCommandException constructor
     *
     * @param message Message
     */
    protected @NotNull EncounterCommandException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "dm command"
     *
     * @return EncounterException
     */
    static @NotNull EncounterCommandException createDmCommand(@NotNull String formattedCommand)
    {
        return new EncounterCommandException(String.format(
            "Only the Dungeon Master can use the `%s` command",
            formattedCommand
        ));
    }

    /**
     * Factory method for "dm not found"
     *
     * @return EncounterException
     */
    static @NotNull EncounterCommandException createDmNotFound()
    {
        return new EncounterCommandException(
            "How can we play without a DungeonMaster? I don't see that role anywhere..."
        );
    }

    /**
     * Factory method for "wrong encounter type"
     *
     * @param formattedCommand Formatted command
     * @param encounterType    Encounter type
     *
     * @return EncounterException
     */
    static @NotNull EncounterCommandException createWrongEncounterType(
        @NotNull String formattedCommand,
        @NotNull String encounterType
    )
    {
        return new EncounterCommandException(String.format(
            "That `%s` command cannot be used for a %s encounter",
            formattedCommand,
            encounterType
        ));
    }
}
