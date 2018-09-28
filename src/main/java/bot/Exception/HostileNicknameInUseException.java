package bot.Exception;

public class HostileNicknameInUseException extends RuntimeException {

    private String nickname;

    public HostileNicknameInUseException(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
