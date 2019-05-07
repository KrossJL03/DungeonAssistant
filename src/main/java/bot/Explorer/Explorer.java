package bot.Explorer;

import bot.Constant;
import bot.Player.Player;
import bot.Player.Exception.OutOfBoundsStatExecption;

public class Explorer
{
    private String name;
    private Player owner;
    private int    strength;
    private int    defense;
    private int    agility;
    private int    wisdom;
    private int    hitpoints;
    private String appLink;
    private String statsLink;

    Explorer(
        String name,
        Player owner,
        int hitpoints,
        int strength,
        int defense,
        int agility,
        int wisdom,
        String appLink,
        String statsLink
    ) {
        Explorer.validateStats(name, hitpoints, strength, defense, agility, wisdom);
        this.owner = owner;
        this.name = name;
        this.strength = strength;
        this.defense = defense;
        this.agility = agility;
        this.wisdom = wisdom;
        this.hitpoints = hitpoints;
        this.appLink = appLink;
        this.statsLink = statsLink;
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

    boolean isOwner(Player player) {
        return this.owner.isSamePlayer(player);
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
        if (strength > Constant.MAX_STRENGTH) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too strong! %d/%d", name, strength, Constant.MAX_STRENGTH)
            );
        } else if (defense > Constant.MAX_DEFENSE) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too durable! %d/%d", name, defense, Constant.MAX_DEFENSE)
            );
        } else if (agility > Constant.MAX_AGILITY) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too fast! %d/%d", name, agility, Constant.MAX_AGILITY)
            );
        } else if (wisdom > Constant.MAX_WISDOM) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too wise! %d/%d", name, wisdom, Constant.MAX_WISDOM)
            );
        } else if (hitpoints > Constant.MAX_MAX_HP) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too healthy! %d/%d", name, hitpoints, Constant.MAX_MAX_HP)
            );
        } else if (hitpoints < Constant.MIN_MAX_HP) {
            throw new OutOfBoundsStatExecption(
                String.format("%s is too weak! They should have at least %d HP!", name, Constant.MIN_MAX_HP)
            );
        }
    }
}
