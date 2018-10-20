package bot.Encounter.EncounterData;

import bot.Encounter.Exception.PlayerCharacterSlainException;
import bot.Player.Player;
import bot.PlayerCharacter.PlayerCharacter;

import java.util.ArrayList;
import java.util.Hashtable;

public class PCEncounterData implements EncounterDataInterface {

    private PlayerCharacter                 playerCharacter;
    private EncounterDataInterface          slayer;
    private ArrayList<HostileEncounterData> kills;
    private Hashtable<String, Integer>      lootRolls;
    private int                             currentHp;
    private int                             currentActions;
    private boolean                         hasProtect;

    public PCEncounterData(PlayerCharacter playerCharacter) {
        this.currentActions = 0;
        this.currentHp = playerCharacter.getHitpoints();
        this.hasProtect = true;
        this.kills = new ArrayList<>();
        this.lootRolls = new Hashtable<>();
        this.playerCharacter = playerCharacter;
    }

    public void addKill(HostileEncounterData hostile) {
        if (this.isSlain()) {
            throw PlayerCharacterSlainException.createFailedToAddKill(
                this.playerCharacter.getName(),
                this.slayer.getName(),
                hostile.getName()
            );
        }
        this.kills.add(hostile);
    }

    public int getAgility() {
        return this.playerCharacter.getAgility();
    }

    public int getAttackDice() {
        return this.playerCharacter.getStrength() + 10;
    }

    public int getCritDamage() {
        return (int) Math.floor(this.getAttackDice() * 1.5);
    }

    public int getCurrentHP() {
        return this.currentHp;
    }

    public int getDefense() {
        return this.playerCharacter.getDefense();
    }

    public int getDodgeDice() {
        return ((int) Math.floor(this.playerCharacter.getAgility() / 2)) + 10;
    }

    public int getEndurance() {
        return (int) Math.floor(this.playerCharacter.getDefense() / 2);
    }

    public ArrayList<HostileEncounterData> getKills() {
        return this.kills;
    }

    public int getLootRoll(String hostileName) {
        return this.lootRolls.get(hostileName);
    }

    public int getMaxActions() {
        return (int) Math.floor(this.playerCharacter.getAgility() / 10) + 1;
    }

    public int getMaxHP() {
        return this.playerCharacter.getHitpoints();
    }

    public int getMinCrit() {
        return 20 - ((int) Math.floor(this.playerCharacter.getWisdom() / 4));
    }

    public String getName() {
        return this.playerCharacter.getName();
    }

    public Player getOwner() {
        return this.playerCharacter.getOwner();
    }

    public int getRemainingActions() {
        return this.currentActions;
    }

    public EncounterDataInterface getSlayer() {
        return this.slayer;
    }

    public int getStrength() {
        return this.playerCharacter.getStrength();
    }

    public int getWisdom() {
        return this.playerCharacter.getWisdom();
    }

    public boolean hasActions() {
        return this.currentActions > 0;
    }

    public boolean hasLoot() {
        return this.lootRolls.size() > 0;
    }

    public int heal(int hitpoints) {
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

    public void hurt(int hitpoints) {
        this.currentHp -= hitpoints;
        if (this.currentHp < 0) {
            this.currentHp = 0;
        }
    }

    public int heal(float percent) {
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

    public boolean isAbleToProtect() {
        return this.hasProtect;
    }

    public boolean isCrit(int rollToHit) {
        return rollToHit >= this.getMinCrit();
    }

    public boolean isOwner(String userId) {
        return this.playerCharacter.getOwner().isSamePlayer(userId);
    }

    public boolean isSlain() {
        return currentHp < 1;
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
        damage = (int) Math.floor(damage - this.getEndurance());
        damage = damage < 1 ? 1 : damage;
        if (this.currentHp > 0 && this.currentHp - damage < 0) {
            this.slayer = attacker;
            this.currentHp = 0;
        }
        this.currentHp -= damage;
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
