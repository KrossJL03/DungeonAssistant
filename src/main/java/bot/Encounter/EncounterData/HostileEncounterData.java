package bot.Encounter.EncounterData;

import bot.Hostile.Hostile;
import bot.Hostile.Loot;

public class HostileEncounterData implements EncounterDataInterface {

    private Hostile hostile;
    private int     currentHp;
    private String  name;
    private Slayer  slayer;
    private int     attackRoll;

    public HostileEncounterData(Hostile hostile, String name) {
        this.attackRoll = 0;
        this.currentHp = hostile.getHitpoints();
        this.hostile = hostile;
        this.name = name;
        this.slayer = new Slayer();
    }

    public void attack() {
        attackRoll = rollDamage();
    }

    public int getAttackDice() {
        return hostile.getAttackDice();
    }

    public int getAttackRoll() {
        return attackRoll;
    }

    public int getCurrentHP() {
        return currentHp;
    }

    public Hostile getHostile() {
        return hostile;
    }

    public int getMaxHP() {
        return hostile.getHitpoints();
    }

    public String getName() {
        return name;
    }

    public Slayer getSlayer() {
        return slayer;
    }

    public String getSpecies() {
        return hostile.getSpecies();
    }

    public int healPoints(int hitpoints) {
        if (isSlain()) {
            slayer = null;
        }
        if (currentHp + hitpoints > getMaxHP()) {
            hitpoints = getMaxHP() - currentHp;
            currentHp = getMaxHP();
        } else {
            currentHp += hitpoints;
        }
        return hitpoints;
    }

    public int healPercent(float percent) {
        if (isSlain()) {
            slayer = null;
        }
        int hitpointsHealed = (int) Math.floor(getMaxHP() * percent);
        if (currentHp + hitpointsHealed > getMaxHP()) {
            hitpointsHealed = getMaxHP() - currentHp;
            currentHp = getMaxHP();
        } else {
            currentHp += hitpointsHealed;
        }
        return hitpointsHealed;
    }

    public void hurt(int hitpoints) {
        currentHp -= hitpoints;
        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    public boolean isName(String name) {
        return this.name.toLowerCase().equals(name.toLowerCase());
    }

    public boolean isSlain() {
        return currentHp < 1;
    }

    public int rollDamage() {
        return (int) Math.floor(Math.random() * getAttackDice()) + 1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int takeDamage(EncounterDataInterface attacker, int damage) {
        if (currentHp > 0 && currentHp - damage < 1) {
            slayer = new Slayer(attacker.getName());
            currentHp = 0;
        } else {
            currentHp -= damage;
        }
        return damage;
    }

    Loot getLoot(int roll) {
        return hostile.getLoot(roll);
    }
}
