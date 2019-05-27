package bot.Encounter.Logger;

import bot.Player.Player;
import net.dv8tion.jda.core.entities.Role;

public class Mention {

    private String value;

    /**
     * Mention constructor
     *
     * @param value Mention as string
     */
    private Mention(String value) {
        this.value = value;
    }

    /**
     * Factory method for player
     *
     * @param userId Player user id
     */
    public static Mention createForPlayer(String userId) {
        return new Mention(String.format("<@%s>", userId));
    }

    /**
     * Factory method for role
     *
     * @param id Role id
     */
    public static Mention createForRole(String id) {
        return new Mention(String.format("<@&%s>", id));
    }

    /**
     * Get mention string
     *
     * @return String
     */
    public String getValue() {
        return value;
    }
}
