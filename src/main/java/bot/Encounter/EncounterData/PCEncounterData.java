package bot.Encounter.EncounterData;

import bot.Constant;
import bot.Encounter.Exception.PCEncounterDataException;
import bot.Encounter.Exception.PlayerCharacterSlainException;
import bot.Player.Player;
import bot.PlayerCharacter.PlayerCharacter;
import org.jetbrains.annotations.NotNull;

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

    public boolean isAbleToProtect() {
        return this.hasProtect;
    }

    public boolean isActive() {
        return !this.isSlain() && this.isPresent();
    }

    public boolean isCrit(int rollToHit) {
        return rollToHit >= this.getMinCrit();
    }

    public boolean isName(String name) {
        return this.name.toLowerCase().equals(name.toLowerCase());
    }

    public boolean isOwner(Player player) {
        return this.owner.isSamePlayer(player);
    }

    public boolean isPresent() {
        return this.isPresent;
    }

    public boolean isSlain() {
        return currentHp < 1;
    }

    public boolean isStatModifiable(String statName, int boost) {
        statName = statName.toLowerCase();
        if (!Constant.isStatName(statName)) {
            throw PCEncounterDataException.invalidStatName(statName);
        }
        int newStat = statName.equals(Constant.STAT_MAX_HP)
                      ? this.maxHp + (boost * Constant.HP_STAT_MULTIPLIER)
                      : this.getStat(statName) + boost;

        return !(newStat > Constant.getStatMax(statName) || newStat < Constant.getStatMin(statName));
    }

    public void leave() {
        this.useAllActions();
        this.isPresent = false;
    }

    public void modifyStat(String statName, int statBoost) {
        if (!this.isStatModifiable(statName, statBoost)) {
            throw PCEncounterDataException.createStatOutOfBounds(this.name, statName);
        }
        switch (statName) {
            case Constant.STAT_AGILITY:
                this.agility += statBoost;
                return;
            case Constant.STAT_DEFENSE:
                this.defense += statBoost;
                return;
            case Constant.STAT_MAX_HP:
                this.maxHp += statBoost;
                this.currentHp += statBoost;
                if (this.currentHp > this.maxHp) {
                    this.currentHp = this.maxHp;
                } else if (this.currentHp < Constant.MIN_MAX_HP) {
                    this.currentHp = Constant.MIN_MAX_HP;
                }
                return;
            case Constant.STAT_STRENGTH:
                this.strength += statBoost;
                return;
            case Constant.STAT_WISDOM:
                this.wisdom += statBoost;
                return;
            default:
                throw PCEncounterDataException.invalidStatName(statName);
        }
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

    public int compareTo(@NotNull PCEncounterData playerCharacter) {
        return playerCharacter.agility - this.agility;
    }
}
