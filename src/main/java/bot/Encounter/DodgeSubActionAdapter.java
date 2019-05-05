package bot.Encounter;

import bot.Encounter.EncounterData.DodgeResult;
import bot.Encounter.Logger.MessageBuilder.DodgeSubActionDataInterface;
import org.jetbrains.annotations.NotNull;

public class DodgeSubActionAdapter implements DodgeSubActionDataInterface {

    private DodgeResult result;

    /**
     * DodgeSubActionAdapter constructor
     *
     * @param result Dodge result
     */
    DodgeSubActionAdapter(@NotNull DodgeResult result) {
        this.result = result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getAttackerName() {
        return this.result.getAttackerName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageRoll() {
        return this.result.getAttackerDamageRoll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDodgeRoll() {
        return this.result.getDodgeRoll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSuccess() {
        return this.result.isSuccess();
    }
}
