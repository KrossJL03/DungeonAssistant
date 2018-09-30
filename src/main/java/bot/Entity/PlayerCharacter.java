package bot.Entity;

import bot.Exception.OutOfBoundsStatExecption;

import net.dv8tion.jda.core.entities.User;


public class PlayerCharacter {

    private User   owner;
    private String name;
    private int    strength;
    private int    defense;
    private int    agility;
    private int    wisdom;
    private int    hitpoints;

    public PlayerCharacter(User owner, String name, int strength, int defense, int agility, int wisdom, int hitpoints) {
        if (strength > 20) {
            throw new OutOfBoundsStatExecption(
                String.format("%s, %s is too strong! %d/20", owner.getAsMention(), name, strength)
            );
        } else if (defense > 20) {
            throw new OutOfBoundsStatExecption(
                String.format("%s, %s is too durable! %d/20", owner.getAsMention(), name, defense)
            );
        } else if (agility > 20) {
            throw new OutOfBoundsStatExecption(
                String.format("%s, %s is too fast! %d/20", owner.getAsMention(), name, agility)
            );
        } else if (wisdom > 20) {
            throw new OutOfBoundsStatExecption(
                String.format("%s, %s is too wise! %d/20", owner.getAsMention(), name, wisdom)
            );
        } else if (hitpoints > 240) {
            throw new OutOfBoundsStatExecption(
                String.format("%s, %s is too healthy! %d/240", owner.getAsMention(), name, hitpoints)
            );
        } else if (hitpoints < 40) {
            throw new OutOfBoundsStatExecption(
                String.format("%s, %s is too weak! They should have at last 40 HP!", owner.getAsMention(), name)
            );
        }

        this.owner = owner;
        this.name = name;
        this.strength = strength;
        this.defense = defense;
        this.agility = agility;
        this.wisdom = wisdom;
        this.hitpoints = hitpoints;
    }


    public int getAgility() {
        return this.agility;
    }

    public int getDefense() {
        return this.defense;
    }

    public int getStrength() {
        return this.strength;
    }

    public int getWisdom() {
        return this.wisdom;
    }

    public int getHitpoints() {
        return this.hitpoints;
    }

    public String getName() {
        return this.name;
    }

    public User getOwner() {
        return this.owner;
    }

    /**
     * @return String
     */
    public String toString() {
        return this.name;
    }
}
