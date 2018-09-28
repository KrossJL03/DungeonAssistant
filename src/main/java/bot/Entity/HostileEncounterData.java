package bot.Entity;

public class HostileEncounterData implements EncounterDataInterface {

    private Hostile                hostile;
    private int                    currentHp;
    private String                 name;
    private EncounterDataInterface slayer;
    private int                    attackRoll;

    public HostileEncounterData(Hostile hostile, String name) {
        this.currentHp = hostile.getHitpoints();
        this.hostile = hostile;
        this.slayer = null;
        this.attackRoll = 0;
        this.name = name;
    }

    public void attack() {
        this.attackRoll = this.rollDamage();
    }

    public int getAttackDice() {
        return this.hostile.getAttackDice() + 10;
    }

    public int getAttackRoll() {
        return this.attackRoll;
    }

    public int getCurrentHP() {
        return this.currentHp;
    }

    public int getMaxHP() {
        return this.hostile.getHitpoints();
    }

    public String getName() {
        return this.name;
    }

    public EncounterDataInterface getSlayer() {
        return this.slayer;
    }

    public String getSpecies() {
        return this.hostile.getSpecies();
    }

    public void heal(int hitpoints) {
        this.currentHp += hitpoints;
    }

    public void hurt(int hitpoints) {
        this.currentHp -= hitpoints;
    }

    public boolean isDead() {
        return this.currentHp < 1;
    }

    public String print() {
        String newLine    = System.getProperty("line.separator");
        int    nameBuffer = (int) Math.floor(15 + this.name.length() / 2);
        String print      = newLine;
        print += "```prolog";
        print += newLine;
        print += (nameBuffer < 29 ? String.format("%" + nameBuffer + "s", this.name) : this.name) + newLine;
        print += "*****************************" + newLine;
        print += "       HP     |     ATK      " + newLine;
        print += String.format("%9s     |     %2s", this.getMaxHP(), this.getAttackDice()) + newLine;
        print += "```";
        return print;
    }

    public int rollDamage() {
        return (int) Math.floor(Math.random() * this.getAttackDice()) + 1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int takeDamage(EncounterDataInterface attacker, int damage) {
        if (this.currentHp > 0 && this.currentHp - damage < 0) {
            this.slayer = attacker;
        }
        this.currentHp -= damage;
        return damage;
    }
}
