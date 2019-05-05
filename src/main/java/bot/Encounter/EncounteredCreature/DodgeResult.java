package bot.Encounter.EncounteredCreature;

import bot.Encounter.DodgeResultInterface;
import org.jetbrains.annotations.NotNull;

public class DodgeResult implements DodgeResultInterface
{
    private DodgeRoll dodgeRoll;
    private String    attackerName;
    private int       attackerDamageRoll;
    private int       damageResisted;

    /**
     * DodgeResult constructor
     *
     * @param attackerName       Attacker name
     * @param dodgeRoll          Dodge roll
     * @param attackerDamageRoll Attack damage roll
     * @param damageResisted     Damage resisted
     */
    DodgeResult(
        @NotNull String attackerName,
        @NotNull DodgeRoll dodgeRoll,
        int attackerDamageRoll,
        int damageResisted
    ) {
        this.attackerDamageRoll = attackerDamageRoll;
        this.attackerName = attackerName;
        this.damageResisted = damageResisted;
        this.dodgeRoll = dodgeRoll;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getAttackerName() {
        return attackerName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttackerDamageRoll() {
        return attackerDamageRoll;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDealt() {
        return isSuccess() ? 0 : attackerDamageRoll - damageResisted;
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
    public int getTargetDodgeRoll() {
        return dodgeRoll.getRoll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSuccess() {
        return !dodgeRoll.isFail();
    }
}
