package bot.Battle.Encounter;

import bot.Battle.CombatCreature;
import bot.Battle.CombatCreatureException;
import bot.Battle.ModifyStatActionResult;
import bot.Battle.Slayer;
import bot.Constant;
import bot.CustomException;
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
     * {@inheritDoc}
     */
    @Override
    public int getAttackDice()
    {
        return attack;
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
     * {@inheritDoc}
     */
    @Override
    public boolean isActive()
    {
        return !isSlain();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ModifyStatActionResult modifyStat(@NotNull String statName, int statModifier)
    {
        switch (statName.toLowerCase()) {
            case Constant.HOSTILE_STAT_ATTACK:
            case Constant.HOSTILE_STAT_ATTACK_SHORT:
                return modifyAttack(statModifier);
            case Constant.CREATURE_STAT_HITPOINTS:
            case Constant.CREATURE_STAT_HITPOINTS_SHORT:
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
            case Constant.HOSTILE_STAT_ATTACK_SHORT:
                return modifyAttack(statValue - attack);
            case Constant.CREATURE_STAT_HITPOINTS:
            case Constant.CREATURE_STAT_HITPOINTS_SHORT:
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
     * Attack
     */
    void attack()
    {
        attackRoll = rollDamage();
    }

    /**
     * Get attack roll
     *
     * @return int
     */
    int getAttackRoll()
    {
        return attackRoll;
    }

    /**
     * Get species
     *
     * @return String
     */
    @NotNull String getSpecies()
    {
        return species;
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
     * Roll loot
     *
     * @return ArrayList<LootRoll>
     *
     * @throws CustomException When attempting to loot and not slain
     */
    @NotNull ArrayList<LootRoll> loot() throws CustomException
    {
        if (!isSlain()) {
            throw new CustomException(String.format("%s has not been slain, they cannot be looted!", getName()));
        }

        ArrayList<LootRoll> lootRolls = new ArrayList<>();
        for (LootRoll lootRoll : hostile.rollLoot()) {
            lootRolls.add(new LootRoll(getName(), lootRoll.getLoot(), lootRoll.getDie(), lootRoll.getRoll()));
        }

        return lootRolls;
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
    protected int getMaxHitpointStatValue()
    {
        return Constant.HOSTILE_MAX_HITPOINTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getMinHitpointStatValue()
    {
        return Constant.HOSTILE_MIN_HITPOINTS;
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
     * Modify attack
     *
     * @param statModifier Attack modifier
     *
     * @return ModifyStatActionResult
     */
    private @NotNull ModifyStatActionResult modifyAttack(int statModifier)
    {
        statModifier = validateStatMod(statModifier, attack, Constant.HOSTILE_MIN_ATTACK, Constant.HOSTILE_MAX_ATTACK);

        attack += statModifier;

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
