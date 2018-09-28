package bot.Exception;

public class ProtectedCharacterIsSlain extends RuntimeException {

    private String name;

    public ProtectedCharacterIsSlain(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
