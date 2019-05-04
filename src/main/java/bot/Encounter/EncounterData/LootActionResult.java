package bot.Encounter.EncounterData;

import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LootActionResult implements ActionResultInterface {

    private ArrayList<HostileEncounterData> finalBlows;
    private ArrayList<LootRoll>             lootRolls;
    private Player                          owner;
    private String                          name;
    private boolean                         isRolled;

    /**
     * LootActionResult constructor
     *
     * @param name  Name
     * @param owner Owner
     */
    LootActionResult(String name, Player owner) {
        this.finalBlows = new ArrayList<>();
        this.isRolled = false;
        this.lootRolls = new ArrayList<>();
        this.name = name;
        this.owner = owner;
    }

    /**
     * LootActionResult constructor
     *
     * @param name      Name
     * @param owner     Owner
     * @param lootRolls Loot rolls
     * @param kills     Kills
     */
    LootActionResult(String name, Player owner, ArrayList<LootRoll> lootRolls, ArrayList<HostileEncounterData> kills) {
        this.finalBlows = kills;
        this.isRolled = true;
        this.lootRolls = lootRolls;
        this.name = name;
        this.owner = owner;
    }

    /**
     * Get final blows
     *
     * @return ArrayList
     */
    @NotNull
    public ArrayList<HostileEncounterData> getFinalBlows() {
        return this.finalBlows;
    }

    /**
     * Get loot rolls
     *
     * @return ArrayList
     */
    @NotNull
    public ArrayList<LootRoll> getLootRolls() {
        return this.lootRolls;
    }

    /**
     * Get name
     *
     * @return String
     */
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Get owner
     *
     * @return Player
     */
    @NotNull
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Has rolled loot
     *
     * @return boolean
     */
    public boolean hasRolledLoot() {
        return this.isRolled;
    }
}
