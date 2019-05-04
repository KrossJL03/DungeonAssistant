package bot.Encounter;

import bot.Encounter.EncounterData.AttackActionResult;
import bot.Encounter.Logger.Message.AttackActionDataInterface;
import org.jetbrains.annotations.NotNull;

public class AttackActionAdapter implements AttackActionDataInterface {

    private AttackActionResult result;

    /**
     * AttackActionAdapter constructor
     *
     * @param result Attack action result
     */
    AttackActionAdapter(@NotNull AttackActionResult result) {
        this.result = result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getAttackerName() {
        return result.getAttackerName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDie() {
        return result.getDamageDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageRoll() {
        return result.getDamageRoll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHitRoll() {
        return result.getHitRoll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getHitType() {
        return result.isHit() ? (result.isCrit() ? "crit" : "hit") : (result.isFail() ? "fail" : "miss");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetCurrentHp() {
        return result.getTargetCurrentHp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetMaxHp() {
        return result.getTargetMaxHp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getTargetName() {
        return result.getTargetName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCrit() {
        return result.isCrit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFail() {
        return result.isFail();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHit() {
        return result.isHit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetSlain() {
        return result.getTargetCurrentHp() < 1;
    }
}
