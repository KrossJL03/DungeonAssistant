package bot.Battle.EncounteredCreature;

import bot.Constant;
import bot.Battle.DodgeResultInterface;
import bot.Battle.EncounteredCreatureInterface;
import bot.Battle.EncounteredExplorerInterface;
import bot.Battle.EncounteredHostileInterface;
import bot.Battle.GuardActionResultInterface;
import bot.Battle.GuardResultInterface;
import bot.Battle.HealActionResultInterface;
import bot.Battle.HurtActionResultInterface;
import bot.Battle.LootRollInterface;
import bot.Battle.ModifyStatActionResultInterface;
import bot.Battle.ProtectActionResultInterface;
import bot.Explorer.Explorer;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class EncounteredExplorer implements EncounteredExplorerInterface
{
    private static int FINAL_BLOW_BONUS = 300;

    private int                                     agility;
    private int                                     currentActions;
    private int                                     currentHp;
    private int                                     defense;
    private boolean                                 hasProtect;
    private boolean                                 isPresent;
    private LootActionResult                        loot;
    private int                                     maxHp;
    private String                                  name;
    private ArrayList<EncounteredCreatureInterface> opponents;
    private Player                                  owner;
    private Slayer                                  slayer;
    private int                                     strength;
    private int                                     wisdom;

    /**
     * EncounteredExplorerInterface constructor
     *
     * @param explorer Explorer
     * @param nickname Optional nickname
     */
    public @NotNull EncounteredExplorer(@NotNull Explorer explorer, @Nullable String nickname)
    {
        this.agility = explorer.getAgility();
        this.currentActions = 0;
        this.currentHp = explorer.getHitpoints();
        this.defense = explorer.getDefense();
        this.hasProtect = true;
        this.isPresent = true;
        this.maxHp = explorer.getHitpoints();
        this.name = nickname != null ? nickname : explorer.getName();
        this.opponents = new ArrayList<>();
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
    public void addOpponent(@NotNull EncounteredCreatureInterface opponent) throws EncounteredExplorerException
    {
        if (!isActive()) {
            throw EncounteredExplorerException.createFailedToAddOpponent(name, opponent.getName());
        } else if (!opponents.contains(opponent)) {
            opponents.add(opponent);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull AttackActionResult attack(@NotNull EncounteredCreatureInterface target)
        throws EncounteredExplorerException
    {
        if (!hasActions()) {
            throw EncounteredExplorerException.createHasNoActions(name);
        } else if (target.isSlain()) {
            throw EncounteredCreatureException.createIsSlain(target.getName(), target.getSlayer().getName());
        }

        HitRoll hitRoll    = rollToHit();
        int     damageRoll = 0;
        if (hitRoll.isHit()) {
            damageRoll = hitRoll.isCrit() ? getCritDamage() : rollDamage();
            target.takeDamage(this, damageRoll);
        }

        useAction();

        return new AttackActionResult(
            name,
            target.getName(),
            hitRoll,
            getAttackDice(),
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
    public int compareTo(@NotNull EncounteredExplorerInterface encounteredExplorer)
    {
        return encounteredExplorer.getAgility() - this.agility;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull DodgeActionResult dodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
    {
        if (!hasActions()) {
            throw EncounteredExplorerException.createHasNoActions(name);
        }

        ArrayList<DodgeResultInterface> dodgeResults = new ArrayList<>();
        for (EncounteredHostileInterface encounteredHostile : encounteredHostiles) {
            int       damageResisted    = 0;
            int       hostileDamageRoll = encounteredHostile.getAttackRoll();
            DodgeRoll dodgeRoll         = rollToDodge();

            if (dodgeRoll.isFail() && !isSlain()) {
                damageResisted = hostileDamageRoll - takeDamage(encounteredHostile, hostileDamageRoll);
            }

            DodgeResult result = new DodgeResult(
                encounteredHostile.getName(),
                dodgeRoll,
                hostileDamageRoll,
                damageResisted
            );

            dodgeResults.add(result);
        }

        useAction();

        return new DodgeActionResult(
            name,
            dodgeResults,
            getDodgeDice(),
            currentHp,
            maxHp,
            slayer,
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull DodgeActionResult failToDodge(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
    {
        if (!hasActions()) {
            throw EncounteredExplorerException.createHasNoActions(name);
        }

        ArrayList<DodgeResultInterface> dodgeResults = new ArrayList<>();
        for (EncounteredHostileInterface encounteredHostile : encounteredHostiles) {
            int hostileDamageRoll = encounteredHostile.getAttackRoll();
            int damageResisted    = hostileDamageRoll - takeDamage(encounteredHostile, hostileDamageRoll);

            DodgeResult result = new DodgeResult(
                encounteredHostile.getName(),
                new DodgeRoll(0),
                hostileDamageRoll,
                damageResisted
            );

            dodgeResults.add(result);
        }

        useAction();

        return new DodgeActionResult(
            name,
            dodgeResults,
            getDodgeDice(),
            currentHp,
            maxHp,
            slayer,
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAgility()
    {
        return agility;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttackDice()
    {
        return strength + 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentHP()
    {
        return currentHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDefense()
    {
        return defense;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDodgeDice()
    {
        return ((int) Math.floor(agility / 2)) + 10;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull LootActionResult getLoot()
    {
        return loot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxActions()
    {
        return (int) Math.floor(agility / 10) + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxHP()
    {
        return maxHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinCrit()
    {
        return 20 - ((int) Math.floor(wisdom / 4));
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
    public int getStatPoints()
    {
        return strength + defense + agility + wisdom +
               ((maxHp - Constant.EXPLORER_MIN_HITPOINTS) / Constant.EXPLORER_HITPOINT_STAT_MULTIPLIER);
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

    @Override
    public @NotNull GuardActionResultInterface guard(@NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles)
        throws EncounteredExplorerException
    {
        if (!hasActions()) {
            throw EncounteredExplorerException.createHasNoActions(name);
        }

        ArrayList<GuardResultInterface> guardResults = new ArrayList<>();
        for (EncounteredHostileInterface encounteredHostile : encounteredHostiles) {
            int hostileDamageRoll = encounteredHostile.getAttackRoll();
            int damageResisted    = hostileDamageRoll - takeGuardedDamage(encounteredHostile, hostileDamageRoll);

            GuardResultInterface result = new GuardResult(
                encounteredHostile.getName(),
                hostileDamageRoll,
                damageResisted
            );

            guardResults.add(result);
        }

        useAction();

        return new GuardActionResult(
            name,
            guardResults,
            getGuardEndurance(),
            currentHp,
            maxHp,
            slayer
        );
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
    public @NotNull HealActionResultInterface healPercent(float percent)
    {
        return healPoints((int) Math.floor(maxHp * percent));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HealActionResultInterface healPoints(int hitpoints)
    {
        boolean wasRevived = false;
        if (isSlain()) {
            slayer = new Slayer();
            wasRevived = true;
        }

        int healedHp;
        if (currentHp + hitpoints > maxHp) {
            healedHp = maxHp - currentHp;
            currentHp = maxHp;
        } else {
            healedHp = hitpoints;
            currentHp += hitpoints;
        }

        return new HealActionResult(name, healedHp, currentHp, maxHp, wasRevived);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HurtActionResultInterface hurt(int hitpoints) throws EncounteredCreatureException
    {
        if (isSlain()) {
            throw EncounteredCreatureException.createIsSlain(name, slayer.getName());
        }

        int hurtHp;
        if (currentHp - hitpoints < 0) {
            hurtHp = currentHp;
            currentHp = 0;
        } else {
            hurtHp = hitpoints;
            currentHp -= hitpoints;
        }

        return new HurtActionResult(name, hurtHp, currentHp, maxHp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive()
    {
        return !isSlain() && isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBloodied()
    {
        return currentHp < (maxHp / 4);
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
        return owner.isSamePlayer(player);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPresent()
    {
        return isPresent;
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
    public void leave() throws EncounteredExplorerException
    {
        if (!isPresent()) {
            throw EncounteredExplorerException.createHasAleadyLeft(owner);
        }
        isPresent = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ModifyStatActionResultInterface modifyStat(@NotNull String statName, int statModifier)
    {
        switch (statName.toLowerCase()) {
            case Constant.EXPLORER_STAT_AGILITY:
                return modifyAgility(statModifier);
            case Constant.EXPLORER_STAT_DEFENSE:
                return modifyDefense(statModifier);
            case Constant.EXPLORER_STAT_HITPOINTS:
                return modifyHitpoints(statModifier);
            case Constant.EXPLORER_STAT_STRENGTH:
                return modifyStrength(statModifier);
            case Constant.EXPLORER_STAT_WISDOM:
                return modifyWisdom(statModifier);
            case Constant.EXPLORER_STAT_AGILITY_SHORT:
                return modifyAgility(statModifier);
            case Constant.EXPLORER_STAT_DEFENSE_SHORT:
                return modifyDefense(statModifier);
            case Constant.EXPLORER_STAT_HITPOINTS_SHORT:
                return modifyHitpoints(statModifier);
            case Constant.EXPLORER_STAT_STRENGTH_SHORT:
                return modifyStrength(statModifier);
            case Constant.EXPLORER_STAT_WISDOM_SHORT:
                return modifyWisdom(statModifier);
            default:
                throw EncounteredExplorerException.createInvalidStatName(statName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ProtectActionResultInterface protect(
        @NotNull EncounteredExplorerInterface recipient,
        @NotNull ArrayList<EncounteredHostileInterface> encounteredHostiles
    ) throws EncounteredExplorerException
    {
        if (!hasActions()) {
            throw EncounteredExplorerException.createHasNoActions(name);
        } else if (!hasProtect) {
            throw EncounteredExplorerException.createProtectAlreadyUsed();
        } else if (equals(recipient)) {
            throw EncounteredExplorerException.createProtectYourself();
        } else if (recipient.isSlain()) {
            throw EncounteredExplorerException.createProtectSlainExplorer(recipient.getName());
        } else if (!recipient.hasActions()) {
            throw EncounteredExplorerException.createProtectActionlessExplorer(recipient.getName());
        }

        int damageDealt    = 0;
        int damageResisted = 0;

        for (EncounteredHostileInterface encounteredHostile : encounteredHostiles) {
            int damage = takeDamage(encounteredHostile, encounteredHostile.getAttackRoll());
            damageDealt += damage;
            damageResisted += encounteredHostile.getAttackRoll() - damage;
        }

        useAction();
        useProtect();
        recipient.useAction();

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
    public void rejoin() throws EncounteredExplorerException
    {
        if (isPresent()) {
            throw EncounteredExplorerException.createCannotRejoinIfPresent(owner);
        }
        this.isPresent = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeOpponent(@NotNull EncounteredCreatureInterface opponent) throws EncounteredExplorerException
    {
        opponents.remove(opponent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetActions(boolean singleAction)
    {
        this.currentActions = singleAction ? 1 : getMaxActions();
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
    public void rollKillLoot()
    {
        ArrayList<EncounteredCreatureInterface> finalBlows = new ArrayList<>();
        ArrayList<LootRollInterface>            lootRolls  = new ArrayList<>();
        for (EncounteredCreatureInterface kill : opponents) {
            lootRolls.addAll(kill.rollLoot());
            if (kill.getSlayer().isSlayer(this)) {
                finalBlows.add(kill);
            }
        }
        loot = new LootActionResult(
            name,
            owner,
            lootRolls,
            finalBlows,
            opponents.size(),
            finalBlows.size() * FINAL_BLOW_BONUS
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<LootRollInterface> rollLoot() throws EncounteredCreatureException
    {
        if (!isSlain()) {
            throw EncounteredCreatureException.createLootWhenNotSlain(name);
        }

        return new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ModifyStatActionResultInterface setStat(@NotNull String statName, int statValue)
    {
        switch (statName.toLowerCase()) {
            case Constant.EXPLORER_STAT_AGILITY:
                return modifyAgility(statValue - agility);
            case Constant.EXPLORER_STAT_DEFENSE:
                return modifyDefense(statValue - defense);
            case Constant.EXPLORER_STAT_HITPOINTS:
                return modifyHitpoints(statValue - maxHp);
            case Constant.EXPLORER_STAT_STRENGTH:
                return modifyStrength(statValue - strength);
            case Constant.EXPLORER_STAT_WISDOM:
                return modifyWisdom(statValue - wisdom);
            case Constant.EXPLORER_STAT_AGILITY_SHORT:
                return modifyAgility(statValue - agility);
            case Constant.EXPLORER_STAT_DEFENSE_SHORT:
                return modifyDefense(statValue - defense);
            case Constant.EXPLORER_STAT_HITPOINTS_SHORT:
                return modifyHitpoints(statValue - maxHp);
            case Constant.EXPLORER_STAT_STRENGTH_SHORT:
                return modifyStrength(statValue - strength);
            case Constant.EXPLORER_STAT_WISDOM_SHORT:
                return modifyWisdom(statValue - wisdom);
            default:
                throw EncounteredExplorerException.createInvalidStatName(statName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int takeDamage(@NotNull EncounteredCreatureInterface attacker, int damage)
        throws EncounteredCreatureException
    {
        if (isSlain()) {
            throw EncounteredCreatureException.createIsSlain(name, slayer.getName());
        }

        damage = damage - this.getEndurance();
        damage = Math.max(1, damage);
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
     * Get endurance
     *
     * @return int
     */
    private int getGuardEndurance()
    {
        return (int) Math.floor(this.defense * .75);
    }

    /**
     * Modify agility
     *
     * @param statModifier Agility modifier
     *
     * @return ModifyStatActionResultInterface
     */
    private @NotNull ModifyStatActionResultInterface modifyAgility(int statModifier)
    {
        int moddedStatValue = agility + statModifier;
        if (moddedStatValue > Constant.EXPLORER_MAX_AGILITY ||
            moddedStatValue < Constant.EXPLORER_MIN_AGILITY) {
            throw EncounteredCreatureException.createStatOutOfBounds(
                name,
                Constant.EXPLORER_STAT_AGILITY,
                Constant.EXPLORER_MIN_AGILITY,
                Constant.EXPLORER_MAX_AGILITY
            );
        }
        agility += statModifier;

        return new ModifyStatActionResult(name, Constant.EXPLORER_STAT_AGILITY, statModifier, agility);
    }

    /**
     * Modify defense
     *
     * @param statModifier Defense modifier
     *
     * @return ModifyStatActionResultInterface
     */
    private @NotNull ModifyStatActionResultInterface modifyDefense(int statModifier)
    {
        int moddedStatValue = defense + statModifier;
        if (moddedStatValue > Constant.EXPLORER_MAX_DEFENSE ||
            moddedStatValue < Constant.EXPLORER_MIN_DEFENSE) {
            throw EncounteredCreatureException.createStatOutOfBounds(
                name,
                Constant.EXPLORER_STAT_DEFENSE,
                Constant.EXPLORER_MIN_DEFENSE,
                Constant.EXPLORER_MAX_DEFENSE
            );
        }
        defense += statModifier;

        return new ModifyStatActionResult(name, Constant.EXPLORER_STAT_DEFENSE, statModifier, defense);
    }

    /**
     * Modify hitpoints
     *
     * @param statModifier Hitpoints modifier
     *
     * @return ModifyStatActionResultInterface
     */
    private @NotNull ModifyStatActionResultInterface modifyHitpoints(int statModifier)
    {
        int moddedStatValue = maxHp + statModifier;
        if (moddedStatValue > Constant.EXPLORER_MAX_HITPOINTS ||
            moddedStatValue < Constant.EXPLORER_MIN_HITPOINTS) {
            throw EncounteredCreatureException.createStatOutOfBounds(
                name,
                Constant.EXPLORER_STAT_HITPOINTS,
                Constant.EXPLORER_MIN_HITPOINTS,
                Constant.EXPLORER_MAX_HITPOINTS
            );
        }
        maxHp += statModifier;
        if (statModifier > 0) {
            currentHp += statModifier;
        }
        if (currentHp > maxHp) {
            currentHp = maxHp;
        }

        return new ModifyStatActionResult(name, Constant.EXPLORER_STAT_HITPOINTS, statModifier, maxHp);
    }

    /**
     * Modify strength
     *
     * @param statModifier Strength modifier
     *
     * @return ModifyStatActionResultInterface
     */
    private @NotNull ModifyStatActionResultInterface modifyStrength(int statModifier)
    {
        int moddedStatValue = strength + statModifier;
        if (moddedStatValue > Constant.EXPLORER_MAX_STRENGTH ||
            moddedStatValue < Constant.EXPLORER_MIN_STRENGTH) {
            throw EncounteredCreatureException.createStatOutOfBounds(
                name,
                Constant.EXPLORER_STAT_STRENGTH,
                Constant.EXPLORER_MIN_STRENGTH,
                Constant.EXPLORER_MAX_STRENGTH
            );
        }
        strength += statModifier;

        return new ModifyStatActionResult(name, Constant.EXPLORER_STAT_STRENGTH, statModifier, strength);
    }

    /**
     * Modify wisdom
     *
     * @param statModifier Wisdom modifier
     *
     * @return ModifyStatActionResultInterface
     */
    private @NotNull ModifyStatActionResultInterface modifyWisdom(int statModifier)
    {
        int moddedStatValue = wisdom + statModifier;
        if (moddedStatValue > Constant.EXPLORER_MAX_WISDOM ||
            moddedStatValue < Constant.EXPLORER_MIN_WISDOM) {
            throw EncounteredCreatureException.createStatOutOfBounds(
                name,
                Constant.EXPLORER_STAT_WISDOM,
                Constant.EXPLORER_MIN_WISDOM,
                Constant.EXPLORER_MAX_WISDOM
            );
        }
        wisdom += statModifier;

        return new ModifyStatActionResult(name, Constant.EXPLORER_STAT_WISDOM, statModifier, wisdom);
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
     * Take guarded damage
     *
     * @return int Damage taken
     */
    private int takeGuardedDamage(@NotNull EncounteredCreatureInterface attacker, int damage)
    {
        damage = damage - getGuardEndurance();
        damage = Math.max(1, damage);
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
     * Use protect
     */
    private void useProtect()
    {
        this.hasProtect = false;
    }
}