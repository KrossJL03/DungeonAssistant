package bot.Encounter.EncounterData;

import bot.Constant;
import bot.Encounter.Exception.PCEncounterDataException;
import bot.Encounter.Exception.PlayerCharacterSlainException;
import bot.Encounter.Exception.PlayerCharacterUnableToProtectException;
import bot.Encounter.Exception.ProtectedCharacterException;
import bot.Player.Player;
import bot.PlayerCharacter.PlayerCharacter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PCEncounterData implements EncounterDataInterface {

    private Player                          owner;
    private Slayer                          slayer;
    private ArrayList<HostileEncounterData> kills;
    private LootActionResult                loot;
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
        this.loot = new LootActionResult(name, owner);
        this.maxHp = playerCharacter.getHitpoints();
        this.name = playerCharacter.getName();
        this.owner = playerCharacter.getOwner();
        this.slayer = new Slayer();
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

    /**
     * Attack a target
     *
     * @param target EncounterData that is being targets by the attack
     *
     * @return AttackActionResult
     */
    public AttackActionResult attack(EncounterDataInterface target) {

        HitRoll hitRoll    = this.rollToHit();
        int     damageRoll = 0;

        if (hitRoll.isHit()) {
            damageRoll = hitRoll.isCrit() ? this.getCritDamage() : this.rollDamage();
            target.takeDamage(this, damageRoll);
        }

        this.useAction();

        return new AttackActionResult(
            this.name,
            target.getName(),
            hitRoll,
            this.getAttackDice(),
            damageRoll,
            target.getCurrentHP(),
            target.getMaxHP(),
            target.getSlayer()
        );
    }

    /**
     * Dodge hostile attacks
     *
     * @param hostiles Hostiles to dodge
     *
     * @return DodgeActionResult
     */
    public DodgeActionResult dodge(@NotNull ArrayList<HostileEncounterData> hostiles) {

        ArrayList<DodgeResult> dodgeResults = new ArrayList<>();
        for (HostileEncounterData hostile : hostiles) {
            int       damageResisted    = 0;
            int       hostileDamageRoll = hostile.getAttackRoll();
            DodgeRoll dodgeRoll         = this.rollToDodge();
            if (dodgeRoll.isFail()) {
                damageResisted = hostileDamageRoll - this.takeDamage(hostile, hostileDamageRoll);
            }
            DodgeResult result = new DodgeResult(hostile.getName(), dodgeRoll, hostileDamageRoll, damageResisted);
            dodgeResults.add(result);
        }

        this.useAction();

        return new DodgeActionResult(
            this.name,
            dodgeResults,
            this.getDodgeDice(),
            this.currentHp,
            this.maxHp,
            this.slayer
        );
    }

    /**
     * Dodge hostile attacks
     *
     * @param hostiles Hostiles to dodge
     *
     * @return DodgeActionResult
     */
    public DodgeActionResult failToDodge(@NotNull ArrayList<HostileEncounterData> hostiles) {

        ArrayList<DodgeResult> dodgeResults = new ArrayList<>();
        for (HostileEncounterData hostile : hostiles) {
            int hostileDamageRoll = hostile.getAttackRoll();
            int damageResisted    = hostileDamageRoll - this.takeDamage(hostile, hostileDamageRoll);
            DodgeResult result = new DodgeResult(
                hostile.getName(),
                new DodgeRoll(0),
                hostileDamageRoll,
                damageResisted
            );
            dodgeResults.add(result);
        }

        this.useAllActions();

        return new DodgeActionResult(
            this.name,
            dodgeResults,
            this.getDodgeDice(),
            this.currentHp,
            this.maxHp,
            this.slayer
        );
    }

    public int getAgility() {
        return this.agility;
    }

    public int getAttackDice() {
        return this.strength + 10;
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

    public LootActionResult getLoot() {
        return this.loot;
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

    public Slayer getSlayer() {
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
        return this.slayer.exists();
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

    /**
     * Protect recipient from hostile attacks
     *
     * @param recipient PC being protected
     * @param hostiles  Hostiles to protect against
     *
     * @return ProtectActionResult
     */
    public @NotNull ProtectActionResult protect(
        @NotNull PCEncounterData recipient,
        @NotNull ArrayList<HostileEncounterData> hostiles
    ) {
        if (!this.hasProtect) {
            throw PlayerCharacterUnableToProtectException.createProtectAlreadyUsed();
        } else if (this.equals(recipient)) {
            throw ProtectedCharacterException.createProtectYourself();
        } else if (recipient.isSlain()) {
            throw ProtectedCharacterException.createIsSlain(recipient.getName());
        } else if (!recipient.hasActions()) {
            throw ProtectedCharacterException.createTurnHasPassed(recipient.getName());
        }

        int damageDealt    = 0;
        int damageResisted = 0;

        for (HostileEncounterData hostile : hostiles) {
            int damage = this.takeDamage(hostile, hostile.getAttackRoll());
            damageDealt += damage;
            damageResisted += hostile.getAttackRoll() - damage;
        }

        this.useAction();
        this.useProtect();
        recipient.useAllActions();

        return new ProtectActionResult(
            name,
            recipient.getName(),
            recipient.getOwner().getUserId(),
            damageDealt,
            damageResisted,
            currentHp,
            maxHp,
            slayer
        );
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

    public void rollLoot() {
        ArrayList<HostileEncounterData> finalBlows = new ArrayList<>();
        ArrayList<LootRoll>             lootRolls  = new ArrayList<>();
        for (HostileEncounterData hostile : this.kills) {
            int roll = (int) Math.floor(Math.random() * 10) + 1;
            lootRolls.add(new LootRoll(roll, hostile.getName(), hostile.getLoot(roll)));
            if (hostile.getSlayer().isSlayer(this)) {
                finalBlows.add(hostile);
            }
        }
        this.loot = new LootActionResult(this.name, this.owner, lootRolls, finalBlows);
    }

    public int takeDamage(EncounterDataInterface attacker, int damage) {
        damage = damage - this.getEndurance();
        damage = damage < 1 ? 1 : damage;
        if (this.currentHp > 0 && this.currentHp - damage < 0) {
            this.slayer = new Slayer(attacker.getName());
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

    public int compareTo(@NotNull PCEncounterData playerCharacter) {
        return playerCharacter.agility - this.agility;
    }

    private int getCritDamage() {
        return (int) Math.floor(this.getAttackDice() * 1.5);
    }

    private int getEndurance() {
        return (int) Math.floor(this.defense / 2);
    }

    private boolean isStatModifiable(String statName, int boost) {
        statName = statName.toLowerCase();
        if (!Constant.isStatName(statName)) {
            throw PCEncounterDataException.invalidStatName(statName);
        }
        int newStat = statName.equals(Constant.STAT_MAX_HP)
                      ? this.maxHp + (boost * Constant.HP_STAT_MULTIPLIER)
                      : this.getStat(statName) + boost;

        return !(newStat > Constant.getStatMax(statName) || newStat < Constant.getStatMin(statName));
    }

    private DodgeRoll rollToDodge() {
        int roll = (int) Math.floor(Math.random() * this.getDodgeDice()) + 1;
        return new DodgeRoll(roll);
    }

    private HitRoll rollToHit() {
        int roll = (int) Math.floor(Math.random() * 20) + 1;
        return new HitRoll(roll, this.getMinCrit());
    }

    private void useProtect() {
        this.hasProtect = false;
    }
}
