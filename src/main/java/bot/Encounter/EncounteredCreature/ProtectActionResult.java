package bot.Encounter.EncounteredCreature;

import bot.Encounter.Logger.Mention;
import bot.Encounter.ProtectActionResultInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProtectActionResult implements ProtectActionResultInterface
{
    private int           damageDealt;
    private int           damageResisted;
    private DeathSaveRoll deathSaveRoll;
    private String        protectedName;
    private String        protectedOwnerId;
    private int           targetCurrentHp;
    private int           targetMaxHp;
    private String        targetName;
    private Slayer        targetSlayer;

    /**
     * ProtectActionResult constructor
     *
     * @param targetName       Target name
     * @param protectedName    Name of protected explorer
     * @param protectedOwnerId User id of protected owner
     * @param damageDealt      Damage dealt to target
     * @param damageResisted   Damage resisted by target
     * @param targetCurrentHp  Target current hp
     * @param targetMaxHp      Target max hp
     * @param targetSlayer     Target slayer
     * @param deathSaveRoll    Death save roll
     */
    @NotNull ProtectActionResult(
        @NotNull String targetName,
        @NotNull String protectedName,
        @NotNull String protectedOwnerId,
        int damageDealt,
        int damageResisted,
        int targetCurrentHp,
        int targetMaxHp,
        @NotNull Slayer targetSlayer,
        @Nullable DeathSaveRoll deathSaveRoll
    )
    {
        this.damageDealt = damageDealt;
        this.damageResisted = damageResisted;
        this.deathSaveRoll = deathSaveRoll;
        this.protectedName = protectedName;
        this.protectedOwnerId = protectedOwnerId;
        this.targetCurrentHp = targetCurrentHp;
        this.targetMaxHp = targetMaxHp;
        this.targetName = targetName;
        this.targetSlayer = targetSlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDealt()
    {
        return damageDealt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageResisted()
    {
        return damageResisted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathMinSaveRoll()
    {
        return rolledDeathSave() ? deathSaveRoll.getMinSaveRoll() : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathSaveDie()
    {
        return rolledDeathSave() ? deathSaveRoll.getDie() : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathSaveRoll()
    {
        return rolledDeathSave() ? deathSaveRoll.getRoll() : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getProtectedName()
    {
        return protectedName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Mention getProtectedOwnerMention()
    {
        return Mention.createForPlayer(protectedOwnerId);
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
        return true;
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
        return deathSaveRoll != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean survivedDeathSave()
    {
        return rolledDeathSave() && deathSaveRoll.survived();
    }
}
