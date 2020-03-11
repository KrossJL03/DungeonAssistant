package bot.Battle;

import bot.Battle.Encounter.EncounteredExplorerException;
import bot.Constant;
import bot.CustomException;
import bot.Explorer.Explorer;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;

public class CombatExplorer extends CombatCreature
{
    private static int DIE_DEATH_SAVE = 100;
    private static int DIE_HIT        = 20;

    private int           agility;
    private int           currentActions;
    private int           defense;
    private boolean       isPresent;
    private ZonedDateTime joinedAt;
    private Player        owner;
    private Slayer        slayer;
    private int           strength;
    private int           wisdom;

    /**
     * Constructor.
     *
     * @param explorer Explorer
     * @param nickname Optional nickname
     */
    public CombatExplorer(@NotNull Explorer explorer, @Nullable String nickname)
    {
        super(explorer.getHitpoints(), nickname != null ? nickname : explorer.getName());

        this.agility = explorer.getAgility();
        this.currentActions = 0;
        this.defense = explorer.getDefense();
        this.isPresent = true;
        this.joinedAt = ZonedDateTime.now();
        this.owner = explorer.getOwner();
        this.slayer = new Slayer();
        this.strength = explorer.getStrength();
        this.wisdom = explorer.getWisdom();
    }

    /**
     * Attack a target
     *
     * @param target Encountered creature that is being targeted by the attack
     *
     * @return AttackActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    public @NotNull AttackActionResult attack(@NotNull CombatCreature target) throws EncounteredExplorerException
    {
        assertHasActions();

        HitRoll    hitRoll    = rollToHit();
        DamageRoll damageRoll = hitRoll.isHit() ? rollDamage(hitRoll, target) : null;

        useAction();

        return new AttackActionResult(getName(), target, hitRoll, damageRoll);
    }

    /**
     * Get defense
     *
     * @return int
     */
    public int getAttackDice()
    {
        return strength + 10;
    }

    /**
     * Get dodge dice
     *
     * @return int
     */
    public int getDodgeDice()
    {
        return ((int) Math.floor(agility / 2)) + 10;
    }

    /**
     * Get owner
     *
     * @return Player
     */
    public @NotNull Player getOwner()
    {
        return owner; // todo try to refactor to only return owner id
    }

    /**
     * Get remaining actions
     *
     * @return int
     */
    public int getRemainingActions()
    {
        return currentActions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Slayer getSlayer()
    {
        return slayer;
    }

    /**
     * Has actions
     *
     * @return boolean
     */
    public boolean hasActions()
    {
        return this.currentActions > 0;
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
     * Is given player the owner of this encountered explorer
     *
     * @param player Player
     *
     * @return boolean
     */
    public boolean isOwner(@NotNull Player player)
    {
        return owner.isSamePlayer(player);
    }

    /**
     * Leave the encounter
     *
     * @throws CombatCreatureException If explorer has already left
     */
    public void leave() throws CombatCreatureException
    {
        if (!isPresent()) {
            throw CombatCreatureException.createHasAlreadyLeft(owner);
        }

        isPresent = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ModifyStatActionResult modifyStat(@NotNull String statName, int statModifier)
    {
        switch (statName.toLowerCase()) {
            case Constant.EXPLORER_STAT_AGILITY:
                return modifyAgility(statModifier);
            case Constant.EXPLORER_STAT_DEFENSE:
                return modifyDefense(statModifier);
            case Constant.CREATURE_STAT_HITPOINTS:
                return modifyHitpoints(statModifier);
            case Constant.EXPLORER_STAT_STRENGTH:
                return modifyStrength(statModifier);
            case Constant.EXPLORER_STAT_WISDOM:
                return modifyWisdom(statModifier);
            case Constant.EXPLORER_STAT_AGILITY_SHORT:
                return modifyAgility(statModifier);
            case Constant.EXPLORER_STAT_DEFENSE_SHORT:
                return modifyDefense(statModifier);
            case Constant.CREATURE_STAT_HITPOINTS_SHORT:
                return modifyHitpoints(statModifier);
            case Constant.EXPLORER_STAT_STRENGTH_SHORT:
                return modifyStrength(statModifier);
            case Constant.EXPLORER_STAT_WISDOM_SHORT:
                return modifyWisdom(statModifier);
            default:
                throw CombatCreatureException.createInvalidStatName(statName, "explorer");
        }
    }

    /**
     * Reset actions
     *
     * @param singleAction Reset a single action only as opposed to all actions
     */
    public void resetActions(boolean singleAction)
    {
        this.currentActions = singleAction ? 1 : getMaxActions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ModifyStatActionResult setStat(@NotNull String statName, int statValue)
    {
        switch (statName.toLowerCase()) {
            case Constant.EXPLORER_STAT_AGILITY:
                return modifyAgility(statValue - agility);
            case Constant.EXPLORER_STAT_DEFENSE:
                return modifyDefense(statValue - defense);
            case Constant.CREATURE_STAT_HITPOINTS:
                return modifyHitpoints(statValue - getMaxHP());
            case Constant.EXPLORER_STAT_STRENGTH:
                return modifyStrength(statValue - strength);
            case Constant.EXPLORER_STAT_WISDOM:
                return modifyWisdom(statValue - wisdom);
            case Constant.EXPLORER_STAT_AGILITY_SHORT:
                return modifyAgility(statValue - agility);
            case Constant.EXPLORER_STAT_DEFENSE_SHORT:
                return modifyDefense(statValue - defense);
            case Constant.CREATURE_STAT_HITPOINTS_SHORT:
                return modifyHitpoints(statValue - getMaxHP());
            case Constant.EXPLORER_STAT_STRENGTH_SHORT:
                return modifyStrength(statValue - strength);
            case Constant.EXPLORER_STAT_WISDOM_SHORT:
                return modifyWisdom(statValue - wisdom);
            default:
                throw CombatCreatureException.createInvalidStatName(statName, "explorer");
        }
    }

    /**
     * Use one action
     */
    public void useAction()
    {
        currentActions--;
    }

    /**
     * Use all actions
     */
    public void useAllActions()
    {
        currentActions = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wasSlainBy(@NotNull CombatCreature creature)
    {
        return slayer.isSlayer(creature);
    }

    /**
     * Get agility
     *
     * @return int
     */
    int getAgility()
    {
        return agility;
    }

    /**
     * Get the time the explorer joined the battle
     *
     * @return ZonedDateTime
     */
    @NotNull ZonedDateTime getJoinedAt()
    {
        return joinedAt;
    }

    /**
     * Get max actions
     *
     * @return int
     */
    int getMaxActions()
    {
        return (int) Math.floor(agility / 10) + 1;
    }

    /**
     * Get min crit
     *
     * @return int
     */
    int getMinCrit()
    {
        return 20 - ((int) Math.floor(wisdom / 4));
    }

    /**
     * Get stat points
     *
     * @return int
     */
    int getStatPoints()
    {
        return strength + defense + agility + wisdom +
               ((getMaxHP() - Constant.EXPLORER_MIN_HITPOINTS) / Constant.EXPLORER_HITPOINT_STAT_MULTIPLIER);
    }

    /**
     * Get strength
     *
     * @return int
     */
    int getStrength()
    {
        return strength;
    }

    /**
     * Get wisdom
     *
     * @return int
     */
    int getWisdom()
    {
        return wisdom;
    }

    /**
     * Is present in the battle
     *
     * @return boolean
     */
    boolean isPresent()
    {
        return isPresent;
    }

    /**
     * Rejoin encounter
     *
     * @throws CombatCreatureException If explorer is already present
     */
    void markAsPresent() throws CombatCreatureException
    {
        if (isPresent()) {
            throw CombatCreatureException.createCannotRejoinIfPresent(owner);
        }

        isPresent = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addSlayer(@NotNull Slayer newSlayer)
    {
        slayer = newSlayer;
    }

    /**
     * Assert that explorer has actions
     *
     * @throws CombatCreatureException If explorer has no actions
     */
    final protected void assertHasActions() throws CombatCreatureException
    {
        if (!hasActions()) {
            throw CombatCreatureException.createHasNoActions(getName());
        }
    }

    /**
     * Get defense
     *
     * @return int
     */
    protected int getDefense()
    {
        return defense;
    }

    /**
     * Get endurance
     *
     * @return int
     */
    protected int getEndurance()
    {
        return (int) Math.floor(defense / 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getMaxHitpointStatValue()
    {
        return Constant.EXPLORER_MAX_HITPOINTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getMinHitpointStatValue()
    {
        return Constant.EXPLORER_MIN_HITPOINTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHeal()
    {
        if (slayer.exists() && !isSlain()) {
            slayer = new Slayer();
        }
    }

    /**
     * Roll death saving throw if applicable
     *
     * @return DeathSaveRoll
     */
    final protected @Nullable DeathSaveRoll rollDeathSaveIfApplicable()
    {
        DeathSaveRoll deathSaveRoll = null;
        if (isSlain()) {
            deathSaveRoll = rollDeathSave();
            if (deathSaveRoll.survived()) {
                healPoints(1);
            }
        }

        return deathSaveRoll;
    }

    /**
     * Get crit damage
     *
     * @return int
     */
    private int getCritDamage()
    {
        return (int) Math.floor(getAttackDice() * 1.5);
    }

    /**
     * Get min roll required to survive on a death save roll
     *
     * @return int
     */
    private int getMinDeathSave()
    {
        int minDeathSave = defense * 2;
        if (defense > 9) {
            minDeathSave += 5;
        }
        if (defense > 19) {
            minDeathSave += 5;
        }

        return DIE_DEATH_SAVE - minDeathSave;
    }

    /**
     * Modify agility
     *
     * @param statModifier Agility modifier
     *
     * @return ModifyStatActionResult
     */
    private @NotNull ModifyStatActionResult modifyAgility(int statModifier)
    {
        statModifier = validateStatMod(
            statModifier,
            agility,
            Constant.EXPLORER_MIN_AGILITY,
            Constant.EXPLORER_MAX_AGILITY
        );

        agility += statModifier;

        return new ModifyStatActionResult(getName(), Constant.EXPLORER_STAT_AGILITY, statModifier, agility);
    }

    /**
     * Modify defense
     *
     * @param statModifier Defense modifier
     *
     * @return ModifyStatActionResult
     */
    private @NotNull ModifyStatActionResult modifyDefense(int statModifier)
    {
        statModifier = validateStatMod(
            statModifier,
            defense,
            Constant.EXPLORER_MIN_DEFENSE,
            Constant.EXPLORER_MAX_DEFENSE
        );

        defense += statModifier;

        return new ModifyStatActionResult(getName(), Constant.EXPLORER_STAT_DEFENSE, statModifier, defense);
    }

    /**
     * Modify strength
     *
     * @param statModifier Strength modifier
     *
     * @return ModifyStatActionResult
     */
    private @NotNull ModifyStatActionResult modifyStrength(int statModifier)
    {
        statModifier = validateStatMod(
            statModifier,
            strength,
            Constant.EXPLORER_MIN_STRENGTH,
            Constant.EXPLORER_MAX_STRENGTH
        );

        strength += statModifier;

        return new ModifyStatActionResult(getName(), Constant.EXPLORER_STAT_STRENGTH, statModifier, strength);
    }

    /**
     * Modify wisdom
     *
     * @param statModifier Wisdom modifier
     *
     * @return ModifyStatActionResult
     */
    private @NotNull ModifyStatActionResult modifyWisdom(int statModifier)
    {
        statModifier = validateStatMod(
            statModifier,
            wisdom,
            Constant.EXPLORER_MIN_WISDOM,
            Constant.EXPLORER_MAX_WISDOM
        );

        wisdom += statModifier;

        return new ModifyStatActionResult(getName(), Constant.EXPLORER_STAT_WISDOM, statModifier, wisdom);
    }

    /**
     * Roll attack die
     *
     * @return int
     */
    private int rollAttackDie()
    {
        return roll(getAttackDice());
    }

    /**
     * Roll damage
     *
     * @return DamageRoll
     */
    private DamageRoll rollDamage(@NotNull HitRoll hitRoll, @NotNull CombatCreature target)
    {
        if (!hitRoll.isHit()) {
            throw new CustomException("Can't roll damage if you miss.");
        }

        int damageRoll     = hitRoll.isCrit() ? getCritDamage() : rollAttackDie();
        int damageDealt    = target.takeDamage(this, damageRoll);
        int damageResisted = damageRoll - damageDealt;

        return new DamageRoll(getAttackDice(), damageRoll, damageDealt, damageResisted);
    }

    /**
     * Roll death save
     */
    private @NotNull DeathSaveRoll rollDeathSave()
    {
        int roll = roll(DIE_DEATH_SAVE);

        return new DeathSaveRoll(roll, DIE_DEATH_SAVE, getMinDeathSave());
    }

    /**
     * Roll to hit
     *
     * @return HitRoll
     */
    private @NotNull HitRoll rollToHit()
    {
        return new HitRoll(roll(DIE_HIT), DIE_HIT, getMinCrit());
    }
}
