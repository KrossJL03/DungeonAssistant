package bot.Encounter;

import bot.Encounter.EncounterData.ProtectActionResult;
import bot.Encounter.EncounterData.Slayer;
import bot.Encounter.Logger.Mention;
import bot.Encounter.Logger.Message.ProtectActionDataInterface;
import org.jetbrains.annotations.NotNull;

public class ProtectActionAdapter implements ProtectActionDataInterface {

    private ProtectActionResult result;

    /**
     * ProtectActionAdapter constructor
     *
     * @param result Protect action result
     */
    ProtectActionAdapter(ProtectActionResult result) {
        this.result = result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDealt() {
        return result.getDamageDealt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageResisted() {
        return result.getDamageResisted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getProtectedName() {
        return result.getProtectedName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Mention getProtectedOwnerMention() {
        return new Mention(result.getProtectedOwnerId());
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
    public @NotNull String getTargetName() {
        return result.getTargetName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Slayer getTargetSlayer() {
        return result.getTargetSlayer();
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
        return result.getTargetSlayer().exists();
    }
}
