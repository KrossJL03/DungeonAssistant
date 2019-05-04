package bot.Encounter;

import bot.Encounter.EncounterData.HostileEncounterData;
import bot.Encounter.EncounterData.LootActionResult;
import bot.Encounter.EncounterData.LootRoll;
import bot.Encounter.Logger.Message.LootActionDataInterface;
import bot.Encounter.Logger.Message.LootSubActionDataInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LootActionAdapter implements LootActionDataInterface {

    private LootActionResult result;

    /**
     * LootActionAdapter constructor
     *
     * @param result Loot action result
     */
    LootActionAdapter(LootActionResult result) {
        this.result = result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFinalBlowBonus() {
        return 300; //todo
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ArrayList<String> getFinalBlows() {
        ArrayList<String> finalBlowNames = new ArrayList<>();
        for (HostileEncounterData hostile : this.result.getFinalBlows()) {
            finalBlowNames.add(hostile.getName());
        }
        return finalBlowNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getKillCount() {
        return this.result.getLootRolls().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLootDie() {
        return 10; // todo
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMention() {
        return this.result.getOwner().getAsMention();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.result.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<LootSubActionDataInterface> getSubActions() {
        ArrayList<LootSubActionDataInterface> subActionData = new ArrayList<>();
        for (LootRoll result : this.result.getLootRolls()) {
            subActionData.add(new LootSubActionAdapter(result));
        }
        return subActionData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFinalBlows() {
        return this.result.getFinalBlows().size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean noLoot() {
        return this.result.hasRolledLoot();
    }
}
