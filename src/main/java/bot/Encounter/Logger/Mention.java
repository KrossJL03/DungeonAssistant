package bot.Encounter.Logger;

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
     * Get mention string
     *
     * @return String
     */
    public String getValue() {
        return value;
    }
}
