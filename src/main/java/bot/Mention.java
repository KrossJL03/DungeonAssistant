package bot;

import org.jetbrains.annotations.NotNull;

public class Mention
{

    private String value;

    /**
     * Mention constructor
     *
     * @param value Mention as string
     */
    private Mention(@NotNull String value)
    {
        this.value = value;
    }

    /**
     * Factory method for everyone
     */
    public static Mention createForEveryone()
    {
        return new Mention("@everyone");
    }

    /**
     * Factory method for player
     *
     * @param userId Player user id
     */
    public static Mention createForPlayer(@NotNull String userId)
    {
        return new Mention(String.format("<@%s>", userId));
    }

    /**
     * Factory method for role
     *
     * @param id Role id
     */
    public static Mention createForRole(@NotNull String id)
    {
        return new Mention(String.format("<@&%s>", id));
    }

    /**
     * Get mention string
     *
     * @return String
     */
    public String getValue()
    {
        return value;
    }
}
