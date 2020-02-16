package bot.Encounter.EncounteredCreature;

import bot.CustomException;
import bot.Encounter.AttackActionResultInterface;
import org.jetbrains.annotations.NotNull;

public class AttackActionResult implements AttackActionResultInterface
{
    private String  attackerName;
    private int     damageDie;
    private int     damageRoll;
    private HitRoll hitRoll;
    private int     targetCurrentHp;
    private int     targetMaxHp;
    private String  targetName;
    private Slayer  targetSlayer;

    /**
     * AttackActionResult constructor
     *
     * @param attackerName    Attacker name
     * @param targetName      Target name
     * @param hitRoll         Roll to hit encountered hostile
     * @param damageDie       Damage die rolled by encountered explorer
     * @param damageRoll      Damage dealt to encountered hostile
     * @param targetCurrentHp Target's current hitpoints
     * @param targetMaxHp     Target's max hitpoints
     * @param targetSlayer    Target's slayer
     */
    AttackActionResult(
        @NotNull String attackerName,
        @NotNull String targetName,
        @NotNull HitRoll hitRoll,
        int damageDie,
        int damageRoll,
        int targetCurrentHp,
        int targetMaxHp,
        @NotNull Slayer targetSlayer
    )
    {
        this.attackerName = attackerName;
        this.damageDie = damageDie;
        this.damageRoll = damageRoll;
        this.hitRoll = hitRoll;
        this.targetCurrentHp = targetCurrentHp;
        this.targetMaxHp = targetMaxHp;
        this.targetName = targetName;
        this.targetSlayer = targetSlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getAttackerName()
    {
        return attackerName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDealt()
    {
        return damageRoll; // todo update for PVP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDie()
    {
        return damageDie;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageResisted()
    {
        return 0; // todo update for PVP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageRoll()
    {
        return damageRoll;
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
    public int getHitDie()
    {
        return hitRoll.getDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHitRoll()
    {
        return hitRoll.getRoll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getHitTypeString()
    {
        return isHit() ? (isCrit() ? "crit" : "hit") : (isFail() ? "fail" : "miss");
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
    public boolean isCrit()
    {
        return hitRoll.isCrit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFail()
    {
        return hitRoll.isFail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHit()
    {
        return hitRoll.isHit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetExplorer()
    {
        return false; // todo update for PVP
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
}
