package bot.Entity;

public class UniqueItem implements ConsumableInterface{

    private String name;
    private String description;
    private String use;

    public UniqueItem(String name, String description, String use) {
        this.name = name;
        this.description = description;
        this.use = use;
    }

    public String consume(PCEncounterData owner, EncounterDataInterface consumer) {
        return use;
    }
}
