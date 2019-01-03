package bot.Encounter.EncounterData;

import bot.Constant;
import bot.Encounter.Exception.PCEncounterDataException;
import bot.Encounter.Exception.PlayerCharacterSlainException;
import bot.Player.Player;
import bot.PlayerCharacter.PlayerCharacter;

import java.util.ArrayList;
import java.util.Hashtable;

public class PCEncounterData implements EncounterDataInterface {

    private Player                          owner;
    private EncounterDataInterface          slayer;
    private ArrayList<HostileEncounterData> kills;
    private Hashtable<String, Integer>      lootRolls;
    private String                          name;
    private int                             agility;
    private int                             currentActions;
    private int                             currentHp;
    private int                             defense;
    private int                             maxHp;
    private int                             strength;
    private int                             wisdom;
    private boolean                         hasProtect;
    private boolean                         isPresent;

    public PCEncounterData(PlayerCharacter playerCharacter) {
        this.agility = playerCharacter.getAgility();
        this.currentActions = 0;
        this.currentHp = playerCharacter.getHitpoints();
        this.defense = playerCharacter.getDefense();
        this.hasProtect = true;
        this.isPresent = true;
        this.kills = new ArrayList<>();
        this.lootRolls = new Hashtable<>();
        this.maxHp = playerCharacter.getHitpoints();
        this.name = playerCharacter.getName();
        this.owner = playerCharacter.getOwner();
        this.strength = playerCharacter.getStrength();
        this.wisdom = playerCharacter.getWisdom();
    }

    public void addKill(HostileEncounterData hostile) {
        if (this.isSlain()) {
            throw PlayerCharacterSlainException.createFailedToAddKill(
                this.name,
                this.slayer.getName(),
                hostile.getName()
            );
        }
        this.kills.add(hostile);
    }

    public int getAgility() {
        return this.agility;
    }

    public int getAttackDice() {
        return this.strength + 10;
    }

    public int getCritDamage() {
        return (int) Math.floor(this.getAttackDice() * 1.5);
    }

    public int getCurrentHP() {
        return this.currentHp;
    }

    public int getDefense() {
        return this.defense;
    }

    public int getDodgeDice() {
        return ((int) Math.floor(this.agility / 2)) + 10;
    }

    public int getEndurance() {
        return (int) Math.floor(this.defense / 2);
    }

    public ArrayList<HostileEncounterData> getKills() {
        return this.kills;
    }

    public int getLootRoll(String hostileName) {
        return this.lootRolls.get(hostileName);
    }

    public int getMaxActions() {
        return (int) Math.floor(this.agility / 10) + 1;
    }

    public int getMaxHP() {
        return this.maxHp;
    }

    public int getMinCrit() {
        return 20 - ((int) Math.floor(this.wisdom / 4));
    }

    public String getName() {
        return this.name;
    }

    public Player getOwner() {
        return this.owner;
    }

    public int getRemainingActions() {
        return this.currentActions;
    }

    public EncounterDataInterface getSlayer() {
        return this.slayer;
    }

    public int getStat(String statName) {
        switch (statName) {
            case Constant.STAT_AGILITY:
                return this.agility;
            case Constant.STAT_DEFENSE:
                return this.defense;
            case Constant.STAT_MAX_HP:
                return this.maxHp;
            case Constant.STAT_STRENGTH:
                return this.strength;
            case Constant.STAT_WISDOM:
                return this.wisdom;
            default:
                throw PCEncounterDataException.invalidStatName(statName);
        }
    }

    public int getStrength() {
        return this.strength;
    }

    public int getWisdom() {
        return this.wisdom;
    }

    public boolean hasActions() {
        return this.currentActions > 0;
    }

    public boolean hasKills() {
        return this.kills.size() > 0;
    }

    public boolean hasLoot() {
        return this.lootRolls.size() > 0;
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

    public void increaseStat(String statName, int boost) {
        if (!this.isStatBoostable(statName, boost)) {
            throw PCEncounterDataException.createStatOutOfBounds(this.name, statName);
        }
        switch (statName) {
            case Constant.STAT_AGILITY:
                this.agility += boost;
                break;
            case Constant.STAT_DEFENSE:
                this.defense += boost;
                break;
            case Constant.STAT_MAX_HP:
                this.maxHp += (boost * Constant.HP_STAT_MULTIPLIER);
                break;
            case Constant.STAT_STRENGTH:
                this.strength += boost;
                break;
            case Constant.STAT_WISDOM:
                this.wisdom += boost;
                break;
            default:
                throw PCEncounterDataException.invalidStatName(statName);
        }
    }

    public boolean isAbleToProtect() {
        return this.hasProtect;
    }

    public boolean isCrit(int rollToHit) {
        return rollToHit >= this.getMinCrit();
    }

    public boolean isName(String name) {
        return this.name.toLowerCase().equals(name.toLowerCase());
    }

    public boolean isOwner(String userId) {
        return this.owner.isSamePlayer(userId);
    }

    public boolean isPresent() {
        return this.isPresent;
    }

    public boolean isSlain() {
        return currentHp < 1;
    }

    public boolean isStatBoostable(String statName, int boost) {
        switch (statName) {
            case Constant.STAT_AGILITY:
                return !((this.agility + boost) > Constant.MAX_AGILITY);
            case Constant.STAT_DEFENSE:
                return !((this.defense + boost) > Constant.MAX_DEFENSE);
            case Constant.STAT_MAX_HP:
                return !((this.maxHp + (boost * Constant.HP_STAT_MULTIPLIER)) > Constant.MAX_MAX_HP);
            case Constant.STAT_STRENGTH:
                return !((this.strength + boost) > Constant.MAX_STRENGTH);
            case Constant.STAT_WISDOM:
                return !((this.wisdom + boost) > Constant.MAX_WISDOM);
            default:
                throw PCEncounterDataException.invalidStatName(statName);
        }
    }

    public void leave() {
        this.isPresent = false;
    }

    public void rejoin() {
        this.isPresent = true;
    }

    public void resetActions(boolean isAttackPhase) {
        this.currentActions = isAttackPhase ? this.getMaxActions() : 1;
    }

    public int rollDamage() {
        return (int) Math.floor(Math.random() * this.getAttackDice()) + 1;
    }

    public int rollDodge() {
        return (int) Math.floor(Math.random() * this.getDodgeDice()) + 1;
    }

    public void rollLoot() {
        for (HostileEncounterData hostile : this.kills) {
            this.lootRolls.put(hostile.getName(), (int) Math.floor(Math.random() * 10) + 1);
        }
    }

    public int rollToHit() {
        return (int) Math.floor(Math.random() * 20) + 1;
    }

    public int takeDamage(EncounterDataInterface attacker, int damage) {
        damage = damage - this.getEndurance();
        damage = damage < 1 ? 1 : damage;
        if (this.currentHp > 0 && this.currentHp - damage < 0) {
            this.slayer = attacker;
        }
        this.currentHp -= damage;
        if (this.currentHp < 0) {
            this.currentHp = 0;
        }
        return damage;
    }

    public void useAction() {
        this.currentActions--;
    }

    public void useAllActions() {
        this.currentActions = 0;
    }

    public void useProtect() {
        this.hasProtect = false;
    }
}
