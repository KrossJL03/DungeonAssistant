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

    public int attack() {
        return (int) Math.floor(Math.random() * this.attackDice) + 1;
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

    public String print() {
        String newLine    = System.getProperty("line.separator");
        int    nameBuffer = (int) Math.floor(15 + this.species.length() / 2);
        String print      = newLine;
        print += "```prolog";
        print += newLine;
        print += (nameBuffer < 29 ? String.format("%" + nameBuffer + "s", this.species) : this.species) + newLine;
        print += "*****************************" + newLine;
        print += "       HP     |     ATK      " + newLine;
        print += String.format("%9s     |     %2s", this.hitpoints, this.attackDice) + newLine;
        print += "```";
        return print;
    }

    public String toString() {
        return this.species;
    }
}
