package bot.Entity;

public class Hostile {

    private int    attackDice;
    private int    hitpoints;
    private String species;

    public Hostile(String species, int hitpoints, int attackDice) {
        this.species = species;
        this.hitpoints  = hitpoints;
        this.attackDice = attackDice;
    }

    public int getAttackDice() {
        return this.attackDice;
    }

    public int getHitpoints() {
        return this.hitpoints;
    }

    public String getSpecies() {
        return this.species;
    }

    public String toString() {
        return this.species;
    }
}
