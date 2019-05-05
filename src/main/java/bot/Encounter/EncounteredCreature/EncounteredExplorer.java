package bot.Encounter.EncounteredCreature;

import bot.Constant;
import bot.Encounter.*;
import bot.Encounter.Exception.EncounterExplorerException;
import bot.Encounter.Exception.ExplorerSlainException;
import bot.Encounter.Exception.ExplorerUnableToProtectException;
import bot.Encounter.Exception.ProtectedExplorerException;
import bot.Player.Player;
import bot.Explorer.Explorer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounteredExplorer implements EncounteredExplorerInterface
{
    private static int FINAL_BLOW_BONUS = 300;
    private static int LOOT_DIE         = 10;

    private Player                                 owner;
    private Slayer                                 slayer;
    private ArrayList<EncounteredHostileInterface> kills;
    private LootActionResult                       loot;
    private String                                 name;
    private int                                    agility;
    private int                                    currentActions;
    private int                                    currentHp;
    private int                                    defense;
    private int                                    maxHp;
    private int                                    strength;
    private int                                    wisdom;
    private boolean                                hasProtect;
    private boolean                                isPresent;

    /**
     * EncounteredExplorerInterface constructor
     *
     * @param explorer Explorer
     */
    public @NotNull EncounteredExplorer(@NotNull Explorer explorer)
    {
        this.agility = explorer.getAgility();
        this.currentActions = 0;
        this.currentHp = explorer.getHitpoints();
        this.defense = explorer.getDefense();
        this.hasProtect = true;
        this.isPresent = true;
        this.kills = new ArrayList<>();
        this.maxHp = explorer.getHitpoints();
        this.name = explorer.getName();
        this.owner = explorer.getOwner();
        this.slayer = new Slayer();
        this.strength = explorer.getStrength();
        this.wisdom = explorer.getWisdom();

        this.loot = new LootActionResult(name, owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addKill(@NotNull EncounteredHostileInterface encounteredHostile)
    {
        if (this.isSlain()) {
            throw ExplorerSlainException.createFailedToAddKill(
                this.name,
                this.slayer.getName(),
                encounteredHostile.getName()
            );
        }
        this.kills.add(encounteredHostile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull AttackActionResult attack(@NotNull EncounterCreatureInterface target)
    {
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
     * {@inheritDoc}
     */
    @Override
    public @NotNull DodgeActionResult dodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
    {
        ArrayList<DodgeResultInterface> dodgeResults = new ArrayList<>();
        for (EncounteredHostileInterface encounteredHostile : encounteredHostiles) {
            int       damageResisted    = 0;
            int       hostileDamageRoll = encounteredHostile.getAttackRoll();
            DodgeRoll dodgeRoll         = this.rollToDodge();

            if (dodgeRoll.isFail()) {
                damageResisted = hostileDamageRoll - this.takeDamage(encounteredHostile, hostileDamageRoll);
            }

            DodgeResult result = new DodgeResult(
                encounteredHostile.getName(),
                dodgeRoll,
                hostileDamageRoll,
                damageResisted
            );

            dodgeResults.add(result);
        }

        this.useAction();

        return new DodgeActionResult(
            this.name,
            dodgeResults,
            this.getDodgeDice(),
            this.currentHp,
            this.maxHp,
            this.slayer,
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull DodgeActionResult failToDodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
    {
        ArrayList<DodgeResultInterface> dodgeResults = new ArrayList<>();
        for (EncounteredHostileInterface encounteredHostile : encounteredHostiles) {
            int hostileDamageRoll = encounteredHostile.getAttackRoll();
            int damageResisted    = hostileDamageRoll - this.takeDamage(encounteredHostile, hostileDamageRoll);

            DodgeResult result = new DodgeResult(
                encounteredHostile.getName(),
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
            this.slayer,
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAgility()
    {
        return this.agility;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttackDice()
    {
        return this.strength + 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentHP()
    {
        return this.currentHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDefense()
    {
        return this.defense;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDodgeDice()
    {
        return ((int) Math.floor(this.agility / 2)) + 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull LootActionResult getLoot()
    {
        return this.loot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxActions()
    {
        return (int) Math.floor(this.agility / 10) + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxHP()
    {
        return this.maxHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinCrit()
    {
        return 20 - ((int) Math.floor(this.wisdom / 4));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName()
    {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Player getOwner()
    {
        return this.owner; // todo try to refactor to only return owner id
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRemainingActions()
    {
        return this.currentActions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Slayer getSlayer()
    {
        return this.slayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStat(String statName)
    {
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
                throw EncounterExplorerException.invalidStatName(statName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStrength()
    {
        return this.strength;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWisdom()
    {
        return this.wisdom;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasActions()
    {
        return this.currentActions > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int healPoints(int hitpoints)
    {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int healPercent(float percent)
    {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int hurt(int hitpoints)
    {
        if (this.currentHp - hitpoints < 0) {
            hitpoints = this.currentHp - hitpoints;
            this.currentHp = 0;
        } else {
            this.currentHp -= hitpoints;
        }
        return hitpoints;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive()
    {
        return !this.isSlain() && this.isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isName(@NotNull String name)
    {
        return this.name.toLowerCase().equals(name.toLowerCase());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOwner(@NotNull Player player)
    {
        return this.owner.isSamePlayer(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPresent()
    {
        return this.isPresent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSlain()
    {
        return currentHp < 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave()
    {
        this.useAllActions();
        this.isPresent = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void modifyStat(@NotNull String statName, int statBoost)
    {
        if (!this.isStatModifiable(statName, statBoost)) {
            throw EncounterExplorerException.createStatOutOfBounds(this.name, statName);
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
                throw EncounterExplorerException.invalidStatName(statName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ProtectActionResultInterface protect(
        @NotNull EncounteredExplorerInterface recipient,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    )
    {
        if (!this.hasProtect) {
            throw ExplorerUnableToProtectException.createProtectAlreadyUsed();
        } else if (this.equals(recipient)) {
            throw ProtectedExplorerException.createProtectYourself();
        } else if (recipient.isSlain()) {
            throw ProtectedExplorerException.createIsSlain(recipient.getName());
        } else if (!recipient.hasActions()) {
            throw ProtectedExplorerException.createTurnHasPassed(recipient.getName());
        }

        int damageDealt    = 0;
        int damageResisted = 0;

        for (EncounteredHostileInterface encounteredHostile : encounteredHostiles) {
            int damage = this.takeDamage(encounteredHostile, encounteredHostile.getAttackRoll());
            damageDealt += damage;
            damageResisted += encounteredHostile.getAttackRoll() - damage;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejoin()
    {
        this.isPresent = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetActions(boolean setToMax)
    {
        this.currentActions = setToMax ? this.getMaxActions() : 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int rollDamage()
    {
        return (int) Math.floor(Math.random() * this.getAttackDice()) + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollLoot()
    {
        ArrayList<EncounteredHostileInterface> finalBlows = new ArrayList<>();
        ArrayList<LootRollInterface>  lootRolls  = new ArrayList<>();
        for (EncounteredHostileInterface hostile : this.kills) {
            int roll = (int) Math.floor(Math.random() * LOOT_DIE) + 1;
            lootRolls.add(new LootRoll(roll, hostile.getName(), hostile.getLoot(roll)));
            if (hostile.getSlayer().isSlayer(this)) {
                finalBlows.add(hostile);
            }
        }
        this.loot = new LootActionResult(
            this.name,
            this.owner,
            lootRolls,
            finalBlows,
            LOOT_DIE,
            finalBlows.size() * FINAL_BLOW_BONUS
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int takeDamage(@NotNull EncounterCreatureInterface attacker, int damage)
    {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAction()
    {
        this.currentActions--;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAllActions()
    {
        this.currentActions = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        return encounteredExplorer.getAgility() - this.agility;
    }

    /**
     * Get crit damage
     *
     * @return int
     */
    private int getCritDamage()
    {
        return (int) Math.floor(this.getAttackDice() * 1.5);
    }

    /**
     * Get endurance
     *
     * @return int
     */
    private int getEndurance()
    {
        return (int) Math.floor(this.defense / 2);
    }

    /**
     * Is stat modifiable
     *
     * @return boolean
     */
    private boolean isStatModifiable(String statName, int boost)
    {
        statName = statName.toLowerCase();
        if (!Constant.isStatName(statName)) {
            throw EncounterExplorerException.invalidStatName(statName);
        }
        int newStat = statName.equals(Constant.STAT_MAX_HP)
                      ? this.maxHp + (boost * Constant.HP_STAT_MULTIPLIER)
                      : this.getStat(statName) + boost;

        return !(newStat > Constant.getStatMax(statName) || newStat < Constant.getStatMin(statName));
    }

    /**
     * Roll to dodge
     *
     * @return DodgeRoll
     */
    private @NotNull DodgeRoll rollToDodge()
    {
        int roll = (int) Math.floor(Math.random() * this.getDodgeDice()) + 1;
        return new DodgeRoll(roll);
    }

    /**
     * Roll to hit
     *
     * @return HitRoll
     */
    private @NotNull HitRoll rollToHit()
    {
        int roll = (int) Math.floor(Math.random() * 20) + 1;
        return new HitRoll(roll, this.getMinCrit());
    }

    /**
     * Use protect
     */
    private void useProtect()
    {
        this.hasProtect = false;
    }
}
