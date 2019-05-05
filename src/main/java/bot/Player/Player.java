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

    public int getCumulus() {
        return cumulus;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isSamePlayer(Player player) {
        return userId.equals(player.getUserId());
    }
}
