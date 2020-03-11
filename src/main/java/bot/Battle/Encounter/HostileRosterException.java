package bot.Battle.Encounter;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

class HostileRosterException extends CustomException
{
    /**
     * HostileRosterException constructor
     *
     * @param message Message
     */
    private @NotNull HostileRosterException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "hostile not found"
     *
     * @param name Hostile name
     *
     * @return EncounteredCreatureNotFoundException
     */
    static @NotNull HostileRosterException createHostileNotFound(@NotNull String name)
    {
        return new HostileRosterException(
            String.format("I don't see any hostiles named %s in this encounter", name)
        );
    }

    /**
     * Factory method of "is slain"
     *
     * @param hostileName Nickname
     * @param slayerName  Slayer name
     *
     * @return HostileRosterException
     */
    static @NotNull HostileRosterException createIsSlain(@NotNull String hostileName, @NotNull String slayerName)
    {
        return new HostileRosterException(String.format("%s was slain by %s", hostileName, slayerName));
    }

    /**
     * Factory method of "nickname in use"
     *
     * @param nickname Nickname
     *
     * @return HostileRosterException
     */
    static @NotNull HostileRosterException createNicknameInUse(@NotNull String nickname)
    {
        return new HostileRosterException(String.format("There's already a hostile named %s in this battle", nickname));
    }

    /**
     * Factory method of "is null"
     *
     * @return HostileRosterException
     */
    static @NotNull HostileRosterException createNullRoster()
    {
        return new HostileRosterException(
            "This type of battle does not have hostiles. To play with hostiles create a different type of battle."
        );
    }
}
