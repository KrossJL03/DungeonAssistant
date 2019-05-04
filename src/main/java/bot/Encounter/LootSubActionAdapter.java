package bot.Encounter;

import bot.Encounter.EncounterData.LootRoll;
import bot.Encounter.Logger.Message.LootSubActionDataInterface;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

public class LootSubActionAdapter implements LootSubActionDataInterface {

    private LootRoll result;

    /**
     * LootSubActionAdapter constructor
     *
     * @param result Loot roll
     */
    LootSubActionAdapter(LootRoll result) {
        this.result = result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String getKillName() {
        return this.result.getHostileName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Loot getLoot() {
        return this.result.getLoot();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLootRoll() {
        return this.result.getRoll();
    }
}
