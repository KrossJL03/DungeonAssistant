package bot.Encounter.EncounteredCreature;

import bot.Constant;
import bot.Encounter.*;
import bot.Hostile.Loot;
import bot.Player.Player;
import bot.Explorer.Explorer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class EncounteredExplorer implements EncounteredExplorerInterface
{
    private static int FINAL_BLOW_BONUS = 300;
    private static int LOOT_DIE         = 10;

    private Player                                owner;
    private Slayer                                slayer;
    private ArrayList<EncounterCreatureInterface> kills;
    private LootActionResult                      loot;
    private String                                name;
    private int                                   agility;
    private int                                   currentActions;
    private int                                   currentHp;
    private int                                   defense;
    private int                                   maxHp;
    private int                                   strength;
    private int                                   wisdom;
    private boolean                               hasProtect;
    private boolean                               isPresent;

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
    public void addKill(@NotNull EncounterCreatureInterface kill) throws EncounteredExplorerException
    {
        if (!isActive()) {
            throw EncounteredExplorerException.createFailedToAddKill(name, kill.getName());
        }
        kills.add(kill);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull AttackActionResult attack(@NotNull EncounterCreatureInterface target)
        throws EncounteredExplorerException
    {
        if (!hasActions()) {
            throw EncounteredExplorerException.createHasNoActions(name);
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

            if (dodgeRoll.isFail()) {
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

        useAllActions();

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

        useAllActions();

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
    public @NotNull Loot getLoot(int roll)
    {
        //todo
        return new Loot(roll, "Bloody fur patch", 1);
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
        return strength + defense + agility + wisdom + ((maxHp - Constant.MIN_MAX_HP)/Constant.HP_STAT_MULTIPLIER);
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
    public @NotNull HealActionResultInterface healPoints(int hitpoints)
    {
        if (isSlain()) {
            slayer = null;
        }

        int healedHp;
        if (currentHp + hitpoints > maxHp) {
            healedHp = maxHp - currentHp;
            currentHp = maxHp;
        } else {
            healedHp = hitpoints;
            currentHp += hitpoints;
        }

        return new HealActionResult(name, healedHp, currentHp, maxHp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int healPercent(float percent)
    {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull HurtActionResultInterface hurt(int hitpoints)
    {
        int hurtHp;
        if (currentHp - hitpoints < 0) {
            hurtHp = currentHp - hitpoints;
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
        useAllActions();
        isPresent = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ModifyStatActionResultInterface modifyStat(@NotNull String statName, int statModifier)
    {
        statName = statName.toLowerCase();
        int statValue = getStat(statName);
        int moddedStatValue = statName.equals(Constant.STAT_MAX_HP)
                              ? statValue + (statModifier * Constant.HP_STAT_MULTIPLIER)
                              : statValue + statModifier;

        if (moddedStatValue > Constant.getStatMax(statName) || moddedStatValue < Constant.getStatMin(statName)) {
            throw EncounteredExplorerException.createStatOutOfBounds(name, statName);
        }

        if (statName.equals(Constant.STAT_MAX_HP)) {
            currentHp += statModifier;
            if (currentHp > maxHp) {
                currentHp = maxHp;
            } else if (currentHp < Constant.MIN_MAX_HP) {
                currentHp = Constant.MIN_MAX_HP;
            }
        }

        return new ModifyStatActionResult(name, statName, statModifier, statValue);
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

        useAllActions();
        useProtect();
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
    public void resetActions()
    {
        this.currentActions = getMaxActions();
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
        ArrayList<EncounterCreatureInterface> finalBlows = new ArrayList<>();
        ArrayList<LootRollInterface>          lootRolls  = new ArrayList<>();
        for (EncounterCreatureInterface kill : kills) {
            int roll = (int) Math.floor(Math.random() * LOOT_DIE) + 1;
            lootRolls.add(new LootRoll(roll, kill.getName(), kill.getLoot(roll)));
            if (kill.getSlayer().isSlayer(this)) {
                finalBlows.add(kill);
            }
        }
        loot = new LootActionResult(
            name,
            owner,
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
     * Get stat
     *
     * @param statName Name of stat
     *
     * @return int
     *
     * @throws EncounteredExplorerException If stat name is invalid
     */
    private int getStat(String statName) throws EncounteredExplorerException
    {
        switch (statName) {
            case Constant.STAT_AGILITY:
                return agility;
            case Constant.STAT_DEFENSE:
                return defense;
            case Constant.STAT_MAX_HP:
                return maxHp;
            case Constant.STAT_STRENGTH:
                return strength;
            case Constant.STAT_WISDOM:
                return wisdom;
            default:
                throw EncounteredExplorerException.invalidStatName(statName);
        }
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
