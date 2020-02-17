package bot.Battle.HostileEncounter;

import bot.Battle.CombatCreature;
import bot.Battle.CombatCreatureException;
import bot.Battle.ModifyStatActionResult;
import bot.Battle.Slayer;
import bot.Constant;
import bot.Hostile.Hostile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class EncounteredHostile extends CombatCreature
{
    private int               attack;
    private int               attackRoll;
    private boolean           hasNickname;
    private Hostile           hostile;
    private ArrayList<Slayer> slayers;
    private String            species;

    /**
     * Constructor.
     *
     * @param hostile Hostile
     * @param name    Name
     */
    EncounteredHostile(@NotNull Hostile hostile, @Nullable String name)
    {
        super(hostile.getHitpoints(), name != null ? name : hostile.getSpecies());

        this.attack = hostile.getAttack();
        this.attackRoll = 0;
        this.hasNickname = name != null;
        this.hostile = hostile;
        this.slayers = new ArrayList<>();
        this.species = hostile.getSpecies();
    }

    /**
     * Attack
     */
    public void attack()
    {
        attackRoll = rollDamage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttackDice()
    {
        return attack;
    }

    /**
     * Get attack roll
     *
     * @return int
     */
    public int getAttackRoll()
    {
        return attackRoll;
    }

    /**
     * Get hostile
     *
     * @return Hostile
     */
    public @NotNull Hostile getHostile()
    {
        return hostile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Slayer getSlayer()
    {
        return slayers.isEmpty() ? new Slayer() : slayers.get(slayers.size() - 1);
    }

    /**
     * Get species
     *
     * @return String
     */
    public @NotNull String getSpecies()
    {
        return species;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isActive()
    {
        return !isSlain();
    }

    /**
     * Roll loot
     *
     * @return ArrayList<LootRoll>
     *
     * @throws CombatCreatureException When attempting to loot and not slain
     */
    public @NotNull ArrayList<LootRoll> loot() throws CombatCreatureException
    {
        if (!isSlain()) {
            throw CombatCreatureException.createLootWhenNotSlain(getName());
        }

        ArrayList<LootRoll> lootRolls = new ArrayList<>();
        for (LootRoll lootRoll : hostile.rollLoot()) {
            lootRolls.add(new LootRoll(getName(), lootRoll.getLoot(), lootRoll.getDie(), lootRoll.getRoll()));
        }

        return lootRolls;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ModifyStatActionResult modifyStat(@NotNull String statName, int statModifier)
    {
        switch (statName.toLowerCase()) {
            case Constant.HOSTILE_STAT_ATTACK:
                return modifyAttack(statModifier);
            case Constant.HOSTILE_STAT_HITPOINTS:
                return modifyHitpoints(statModifier);
            case Constant.HOSTILE_STAT_ATTACK_SHORT:
                return modifyAttack(statModifier);
            case Constant.HOSTILE_STAT_HITPOINTS_SHORT:
                return modifyHitpoints(statModifier);
            default:
                throw CombatCreatureException.createInvalidStatName(statName, "hostile");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ModifyStatActionResult setStat(@NotNull String statName, int statValue)
    {
        switch (statName.toLowerCase()) {
            case Constant.HOSTILE_STAT_ATTACK:
                return modifyAttack(statValue - attack);
            case Constant.HOSTILE_STAT_HITPOINTS:
                return modifyHitpoints(statValue - getMaxHP());
            case Constant.HOSTILE_STAT_ATTACK_SHORT:
                return modifyAttack(statValue - attack);
            case Constant.HOSTILE_STAT_HITPOINTS_SHORT:
                return modifyHitpoints(statValue - getMaxHP());
            default:
                throw CombatCreatureException.createInvalidStatName(statName, "hostile");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wasSlainBy(@NotNull CombatCreature creature)
    {
        for (Slayer slayer : slayers) {
            if (slayer.isSlayer(creature)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Does this hostile have a nickname
     *
     * @return boolean
     */
    boolean hasNickname()
    {
        return hasNickname;
    }

    /**
     * {@inheritDoc}
     * Does not add slayer if slayer with the same name already exists
     */
    @Override
    protected void addSlayer(@NotNull Slayer newSlayer)
    {
        for (Slayer slayer : slayers) {
            if (slayer.isSlayer(slayer)) {
                return;
            }
        }

        slayers.add(newSlayer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getEndurance()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHeal()
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    protected void preModifyHitpoints(int statModifier)
    {
        int moddedStatValue = getMaxHP() + statModifier;
        if (moddedStatValue < Constant.HOSTILE_MIN_HITPOINTS) {
            throw CombatCreatureException.createStatLessThanMin(
                getName(),
                Constant.HOSTILE_STAT_HITPOINTS,
                Constant.HOSTILE_MIN_HITPOINTS
            );
        }
    }

    /**
     * Modify attack
     *
     * @param statModifier Attack modifier
     *
     * @return ModifyStatActionResult
     */
    private @NotNull ModifyStatActionResult modifyAttack(int statModifier)
    {
        int moddedStatValue = attack + statModifier;
        if (moddedStatValue < Constant.HOSTILE_MIN_ATTACK) {
            throw CombatCreatureException.createStatLessThanMin(
                getName(),
                Constant.HOSTILE_STAT_ATTACK,
                Constant.HOSTILE_MIN_ATTACK
            );
        }
        attack = moddedStatValue;

        return new ModifyStatActionResult(getName(), Constant.HOSTILE_STAT_ATTACK, statModifier, attack);
    }

    /**
     * Roll damage
     *
     * @return int
     */
    private int rollDamage()
    {
        return (int) Math.floor(Math.random() * getAttackDice()) + 1;
    }
}
