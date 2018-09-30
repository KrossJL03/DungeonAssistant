package bot.Player;

public class Player {

    private int id;
    private String userId;
    private String name;
    private int cumulus;

    public Player(int id, String userId, String name, int cumulus) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.cumulus = cumulus;
    }

    public int getCumulus() {
        return cumulus;
    }

    public String getAsMention() {
        return String.format("<@%s>", this.userId);
    }

    public String getName() {
        return name;
    }
}
