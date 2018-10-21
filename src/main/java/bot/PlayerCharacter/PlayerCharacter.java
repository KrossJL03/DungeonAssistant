package bot.PlayerCharacter;

import bot.Player.Player;
import bot.Player.Exception.OutOfBoundsStatExecption;

public class PlayerCharacter {

    private String name;
    private Player owner;
    private int    strength;
    private int    defense;
    private int    agility;
    private int    wisdom;
    private int    hitpoints;

    PlayerCharacter(
        String name,
        Player owner,
        int hitpoints,
        int strength,
        int defense,
        int agility,
        int wisdom
    ) {
        PlayerCharacter.validateStats(name, hitpoints, strength, defense, agility, wisdom);
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

    public Player getOwner() {
        return this.owner;
    }

    boolean isOwner(String userId) {
        return this.owner.isSamePlayer(userId);
    }

    /**
     * @return String
     */
    public String toString() {
        return this.name;
    }

    static void validateStats(
        String name,
        int hitpoints,
        int strength,
        int defense,
        int agility,
        int wisdom
    ) {
        if (strength > 20) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too strong! %d/20", name, strength)
            );
        } else if (defense > 20) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too durable! %d/20", name, defense)
            );
        } else if (agility > 20) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too fast! %d/20", name, agility)
            );
        } else if (wisdom > 20) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too wise! %d/20", name, wisdom)
            );
        } else if (hitpoints > 240) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too healthy! %d/240", name, hitpoints)
            );
        } else if (hitpoints < 40) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too weak! They should have at last 40 HP!", name)
            );
        }
    }
}
