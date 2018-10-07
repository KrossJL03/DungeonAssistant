package bot.Hostile;

import java.util.HashMap;

public class Hostile {

    private HashMap<Integer, Loot> lootList;
    private String                 species;
    private int                    dangerLevel;
    private int                    attackDice;
    private int                    hitpoints;
    private int                    id;

    public Hostile(
        int id,
        String species,
        int dangerLevel,
        int hitpoints,
        int attackDice,
        HashMap<Integer, Loot> lootList
    ) {
        this.attackDice = attackDice;
        this.dangerLevel = dangerLevel;
        this.hitpoints = hitpoints;
        this.id = id;
        this.lootList = lootList;
        this.species = species;
    }

    public int getAttackDice() {
        return this.attackDice;
    }

    public int getDangerLevel() {
        return this.dangerLevel;
    }

    public int getHitpoints() {
        return this.hitpoints;
    }

    public int getId() {
        return this.id;
    }

    public Loot getLoot(int roll) {
        return this.lootList.get(roll);
    }

    public String getSpecies() {
        return this.species;
    }

    public String toString() {
        return this.species;
    }
}
