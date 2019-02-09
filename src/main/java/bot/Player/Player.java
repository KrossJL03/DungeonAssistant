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

    public boolean isSamePlayer(Player player) {
        return this.userId.equals(player.getUserId());
    }

    private String getUserId() {
        return this.userId;
    }
}
