package bot.Encounter.EncounterData;

import bot.Hostile.Hostile;

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
        return this.hostile.getAttackDice();
    }

    public int getAttackRoll() {
        return this.attackRoll;
    }

    public int getCurrentHP() {
        return this.currentHp;
    }

    public Hostile getHostile() {
        return this.hostile;
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

    public int healPoints(int hitpoints) {
        if (this.isSlain()) {
            this.slayer = null;
        }
        if (this.currentHp + hitpoints > this.getMaxHP()) {
            hitpoints = this.getMaxHP() - this.currentHp;
            this.currentHp = this.getMaxHP();
        } else {
            this.currentHp += hitpoints;
        }
        return hitpoints;
    }

    public int healPercent(float percent) {
        if (this.isSlain()) {
            this.slayer = null;
        }
        int hitpointsHealed = (int) Math.floor(this.getMaxHP() * percent);
        if (this.currentHp + hitpointsHealed > this.getMaxHP()) {
            hitpointsHealed = this.getMaxHP() - this.currentHp;
            this.currentHp = this.getMaxHP();
        } else {
            this.currentHp += hitpointsHealed;
        }
        return hitpointsHealed;
    }

    public void hurt(int hitpoints) {
        this.currentHp -= hitpoints;
        if (this.currentHp < 0) {
            this.currentHp = 0;
        }
    }

    public boolean isName(String name) {
        return this.name.toLowerCase().equals(name.toLowerCase());
    }

    public boolean isSlain() {
        return this.currentHp < 1;
    }

    public int rollDamage() {
        return (int) Math.floor(Math.random() * this.getAttackDice()) + 1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int takeDamage(EncounterDataInterface attacker, int damage) {
        if (this.currentHp > 0 && this.currentHp - damage < 1) {
            this.slayer = attacker;
            this.currentHp = 0;
        } else {
            this.currentHp -= damage;
        }
        return damage;
    }
}
