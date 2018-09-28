package bot.Exception;

import net.dv8tion.jda.core.entities.User;

public class MultiplePlayerCharactersException extends RuntimeException {

    private User   player;
    private String name;

    public MultiplePlayerCharactersException(User player, String name) {
        this.player = player;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public User getPlayer() {
        return player;
    }
}
