package bot.Encounter;

import bot.Encounter.EncounterData.DodgeActionResult;
import bot.Encounter.EncounterData.DodgeResult;
import bot.Encounter.Logger.Message.DodgeActionDataInterface;
import bot.Encounter.Logger.Message.DodgeSubActionDataInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgeActionAdapter implements DodgeActionDataInterface {

    private DodgeActionResult result;
    private boolean           isForceFail;

    /**
     * DodgeActionAdapter constructor
     *
     * @param actionResult Dodge action result
     * @param isForcedFail Was the dodge action a forced fail
     */
    DodgeActionAdapter(DodgeActionResult actionResult, boolean isForcedFail) {
        this.result = actionResult;
        this.isForceFail = isForcedFail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttackCount() {
        return result.getDodgeResults().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamage() {
        int totalDamageDealt = 0;
        for (DodgeResult result : this.result.getDodgeResults()) {
            totalDamageDealt += result.getDamageDealt();
        }
        return totalDamageDealt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDodgeDie() {
        return this.result.getTargetDodgeDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinSucessDodgeRoll() {
        return this.result.getMinSucessDodgeRoll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getResistedDamage() {
        int totalDamageResisted = 0;
        for (DodgeResult result : this.result.getDodgeResults()) {
            totalDamageResisted += result.getDamageResisted();
        }
        return totalDamageResisted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ArrayList<DodgeSubActionDataInterface> getSubActionData() {
        ArrayList<DodgeSubActionDataInterface> subActionData = new ArrayList<>();
        for (DodgeResult result : this.result.getDodgeResults()) {
            subActionData.add(new DodgeSubActionAdapter(result));
        }
        return subActionData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetCurrentHp() {
        return this.result.getTargetCurrentHp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetMaxHp() {
        return this.result.getTargetMaxHp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getTargetName() {
        return this.result.getTargetName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isForceFail() {
        return this.isForceFail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetSlain() {
        return this.getTargetCurrentHp() <= 0;
    }
}
