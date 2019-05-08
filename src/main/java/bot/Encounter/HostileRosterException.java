package bot.Encounter;

import org.jetbrains.annotations.NotNull;

class HostileRosterException extends RuntimeException implements EncounterExceptionInterface
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
     * Factory method of "is slain"
     *
     * @param hostileName Nickname
     * @param slayerName Slayer name
     *
     * @return HostileRosterException
     */
    static @NotNull HostileRosterException createIsSlain(@NotNull String hostileName, @NotNull String slayerName) {
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
}
