package bot.Entity;

import java.util.ArrayList;

public class Hostile {

    private ArrayList<Loot> lootList;
    private String species;
    private int    attackDice;
    private int    hitpoints;


    public Hostile(String species, int hitpoints, int attackDice) {
        this.attackDice = attackDice;
        this.hitpoints  = hitpoints;
        this.lootList = new ArrayList<>();
        this.species = species;
        for (int i = 0; i < 11; i++) {
            this.lootList.add(null);
        }
    }

    public int getAttackDice() {
        return this.attackDice;
    }

    public int getHitpoints() {
        return this.hitpoints;
    }

    public Loot getLoot(int roll) {
        return this.lootList.get(roll);
    }

    public String getSpecies() {
        return this.species;
    }

    public void removeLoot(int roll) {
        if (roll < 1 || roll > 10) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.lootList.set(roll, null);
    }

    public void setLoot(int roll, String item, int quantity) {
        if (roll < 1 || roll > 10) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.lootList.add(roll, new Loot(item, quantity));
    }

    public String toString() {
        return this.species;
    }
}
