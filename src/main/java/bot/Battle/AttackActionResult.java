package bot.Battle;

import bot.Battle.Encounter.EncounteredExplorer;
import bot.CustomException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AttackActionResult implements CombatActionResultInterface
{
    private String     attackerName;
    private DamageRoll damageRoll;
    private HitRoll    hitRoll;
    private boolean    isTargetExplorer;
    private int        targetCurrentHp;
    private int        targetMaxHp;
    private String     targetName;
    private Slayer     targetSlayer;

    /**
     * AttackActionResult constructor
     *
     * @param attackerName Attacker name
     * @param target       Target
     * @param hitRoll      Roll to hit target
     * @param damageRoll   Damage rolled by attacker
     */
    AttackActionResult(
        @NotNull String attackerName,
        @NotNull CombatCreature target,
        @NotNull HitRoll hitRoll,
        @Nullable DamageRoll damageRoll
    )
    {
        this.attackerName = attackerName;
        this.damageRoll = damageRoll;
        this.hitRoll = hitRoll;
        this.isTargetExplorer = target instanceof EncounteredExplorer;
        this.targetCurrentHp = target.getCurrentHP();
        this.targetMaxHp = target.getMaxHP();
        this.targetName = target.getName();
        this.targetSlayer = target.getSlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDealt()
    {
        return hasDamageRoll() ? damageRoll.getDamageDealt() : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageResisted()
    {
        return hasDamageRoll() ? damageRoll.getDamageResisted() : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathMinSaveRoll()
    {
        throw new CustomException("Death saves are not rolled on the attack turn");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathSaveDie()
    {
        throw new CustomException("Death saves are not rolled on the attack turn");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathSaveRoll()
    {
        throw new CustomException("Death saves are not rolled on the attack turn");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetCurrentHp()
    {
        return targetCurrentHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetMaxHp()
    {
        return targetMaxHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getTargetName()
    {
        return targetName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Slayer getTargetSlayer()
    {
        return targetSlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetExplorer()
    {
        return isTargetExplorer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetSlain()
    {
        return targetCurrentHp < 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean rolledDeathSave()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean survivedDeathSave()
    {
        throw new CustomException("Death saves are not rolled on the attack turn");
    }

    /**
     * Get attacker name
     *
     * @return String
     */
    @NotNull String getAttackerName()
    {
        return attackerName;
    }

    /**
     * Get damage die
     *
     * @return int
     */
    int getDamageDie()
    {
        return hasDamageRoll() ? damageRoll.getDie() : -1;
    }

    /**
     * Get damage roll
     *
     * @return int
     */
    int getDamageRoll()
    {
        return hasDamageRoll() ? damageRoll.getRoll() : -1;
    }

    /**
     * Get hit die
     *
     * @return int
     */
    int getHitDie()
    {
        return hitRoll.getDie();
    }

    /**
     * Get hit roll
     *
     * @return int
     */
    int getHitRoll()
    {
        return hitRoll.getRoll();
    }

    /**
     * Get hit type as string
     *
     * @return String
     */
    @NotNull String getHitTypeString()
    {
        return isHit() ? (isCrit() ? "crit" : "hit") : (isFail() ? "fail" : "miss");
    }

    /**
     * Is hit roll crit
     *
     * @return int
     */
    boolean isCrit()
    {
        return hitRoll.isCrit();
    }

    /**
     * Is hit roll fail
     *
     * @return int
     */
    boolean isFail()
    {
        return hitRoll.isFail();
    }

    /**
     * Is hit roll hit
     *
     * @return int
     */
    boolean isHit()
    {
        return hitRoll.isHit();
    }

    /**
     * Has damage roll
     *
     * @return boolean
     */
    private boolean hasDamageRoll()
    {
        return damageRoll != null;
    }
}
