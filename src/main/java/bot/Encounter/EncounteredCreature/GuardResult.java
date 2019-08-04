package bot.Encounter.EncounteredCreature;

import bot.Encounter.GuardResultInterface;
import org.jetbrains.annotations.NotNull;

public class GuardResult implements GuardResultInterface
{
    private String    attackerName;
    private int       attackerDamageRoll;
    private int       damageResisted;

    /**
     * GuardResult constructor
     *
     * @param attackerName       Attacker name
     * @param attackerDamageRoll Attack damage roll
     * @param damageResisted     Damage resisted
     */
    GuardResult(
        @NotNull String attackerName,
        int attackerDamageRoll,
        int damageResisted
    ) {
        this.attackerDamageRoll = attackerDamageRoll;
        this.attackerName = attackerName;
        this.damageResisted = damageResisted;
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
        return attackerDamageRoll - damageResisted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageResisted() {
        return damageResisted;
    }
}
