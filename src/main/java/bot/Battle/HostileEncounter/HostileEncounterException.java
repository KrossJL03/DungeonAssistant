package bot.Battle.HostileEncounter;

import bot.CustomException;
import bot.MyProperties;
import org.jetbrains.annotations.NotNull;

class HostileEncounterException extends CustomException
{
    /**
     * Constructor.
     *
     * @param message Message
     */
    private HostileEncounterException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "revive non-slain explorer"
     *
     * @param name Name of explorer
     *
     * @return EncounterException
     */
    static @NotNull HostileEncounterException createReviveNonSlainExplorer(String name)
    {
        return new HostileEncounterException(String.format(
            "%s looks fine to me, I don't think they need any phoenix down",
            name
        ));
    }

    /**
     * Factory method for "start without hostiles"
     *
     * @return BattlePhaseException
     */
    static @NotNull HostileEncounterException createStartWithoutHostiles()
    {
        return new HostileEncounterException(
            String.format(
                "Uh, wait. Who are we fighting again? Tell me using `%saddHostile [species]`.",
                MyProperties.COMMAND_PREFIX
            )
        );
    }

    /**
     * Factory method for "used phoenix down"
     *
     * @param name Name of slain explorer
     *
     * @return EncounterException
     */
    static @NotNull HostileEncounterException createUsedPhoenixDown(String name)
    {
        return new HostileEncounterException("The guild leaders do not have any more phoenix down, %s cannot be revived");
    }
}
