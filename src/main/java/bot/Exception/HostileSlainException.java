package bot.Exception;

public class HostileSlainException extends RuntimeException {

    private String hostileName;
    private String slayerName;

    public HostileSlainException(String hostileName, String slayerName) {
        this.hostileName = hostileName;
        this.slayerName = slayerName;
    }

    public String getHostileName() {
        return hostileName;
    }

    public String getSlayerName() {
        return slayerName;
    }
}
