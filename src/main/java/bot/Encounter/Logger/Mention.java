package bot.Encounter.Logger;

import net.dv8tion.jda.core.entities.Role;

public class Mention {

    private String value;

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
