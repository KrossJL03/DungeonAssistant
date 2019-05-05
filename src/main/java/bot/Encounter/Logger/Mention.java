package bot.Encounter.Logger;

import net.dv8tion.jda.core.entities.Role;

public class Mention {

    private String value;

    /**
     * Mention constructor
     *
     * @param userId User id
     */
    public Mention(String userId) {
        this.value = String.format("<@%s>", userId);
    }

    /**
     * Mention constructor
     *
     * @param role Role
     */
    Mention(Role role) {
        this.value = role.getAsMention();
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
