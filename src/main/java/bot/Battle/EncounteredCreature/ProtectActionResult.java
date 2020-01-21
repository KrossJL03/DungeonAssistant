package bot.Battle.EncounteredCreature;

import bot.Battle.Logger.Mention;
import bot.Battle.ProtectActionResultInterface;
import org.jetbrains.annotations.NotNull;

public class ProtectActionResult implements ProtectActionResultInterface
{
    private Slayer targetSlayer;
    private String protectedName;
    private String protectedOwnerId;
    private String targetName;
    private int    damageDealt;
    private int    damageResisted;
    private int    targetCurrentHp;
    private int    targetMaxHp;

    /**
     * ProtectActionResult constructor
     *
     * @param targetName      Target name
     * @param protectedName   Name of protected explorer
     * @param protectedOwnerId  User id of protected owner
     * @param damageDealt     Damage dealt to target
     * @param damageResisted  Damage resisted by target
     * @param targetCurrentHp Target current hp
     * @param targetMaxHp     Target max hp
     * @param targetSlayer    Target slayer
     */
    @NotNull ProtectActionResult(
        String targetName,
        @NotNull String protectedName,
        @NotNull String protectedOwnerId,
        int damageDealt,
        int damageResisted,
        int targetCurrentHp,
        int targetMaxHp,
        @NotNull Slayer targetSlayer
    ) {
        this.damageDealt = damageDealt;
        this.damageResisted = damageResisted;
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
    public int getDamageDealt() {
        return damageDealt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageResisted() {
        return damageResisted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getProtectedName() {
        return protectedName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Mention getProtectedOwnerMention() {
        return Mention.createForPlayer(protectedOwnerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetCurrentHp() {
        return targetCurrentHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetMaxHp() {
        return targetMaxHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getTargetName() {
        return targetName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Slayer getTargetSlayer() {
        return targetSlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetExplorer() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetSlain() {
        return targetCurrentHp < 1;
    }
}
