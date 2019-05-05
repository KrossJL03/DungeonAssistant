package bot.Encounter.EncounterData;

public class Slayer {

    private String name;

    /**
     * Slayer constructor
     *
     * @param name Slayer name
     */
    Slayer(String name) {
        this.name = name;
    }

    /**
     * Slayer constructor (empty)
     */
    Slayer() {
        this.name = "";
    }

    /**
     * Does a slayer exist
     *
     * @return boolean
     */
    public boolean exists() {
        return !name.isEmpty();
    }

    /**
     * Get name
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Does the supplied potential slayer match this slayer
     *
     * @param slayer Potential slayer
     *
     * @return boolean
     */
    boolean isSlayer(EncounterDataInterface slayer) {
        return name.equals(slayer.getName());
    }
}
