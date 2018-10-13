package bot.Player;

public class Player {

    private String userId;
    private String name;
    private int cumulus;

    public Player(String userId, String name, int cumulus) {
        this.userId = userId;
        this.name = name;
        this.cumulus = cumulus;
    }

    public String getAsMention() {
        return String.format("<@%s>", this.userId);
    }

    public int getCumulus() {
        return cumulus;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return this.userId;
    }

    public boolean isSamePlayer(String userId) {
        return this.userId.equals(userId);
    }
}
