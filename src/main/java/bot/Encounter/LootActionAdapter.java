package bot.Encounter;

import bot.Encounter.EncounterData.HostileEncounterData;
import bot.Encounter.EncounterData.LootActionResult;
import bot.Encounter.EncounterData.LootRoll;
import bot.Encounter.Logger.Mention;
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
        return 300 * getFinalBlows().size(); //todo make constant
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ArrayList<String> getFinalBlows() {
        ArrayList<String> finalBlowNames = new ArrayList<>();
        for (HostileEncounterData hostile : result.getFinalBlows()) {
            finalBlowNames.add(hostile.getName());
        }
        return finalBlowNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getKillCount() {
        return result.getLootRolls().size();
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
    public Mention getMention() {
        return new Mention(result.getOwner().getUserId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return result.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<LootSubActionDataInterface> getSubActions() {
        ArrayList<LootSubActionDataInterface> subActionData = new ArrayList<>();
        for (LootRoll result : result.getLootRolls()) {
            subActionData.add(new LootSubActionAdapter(result));
        }
        return subActionData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFinalBlows() {
        return result.getFinalBlows().size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean noLoot() {
        return !result.hasRolledLoot();
    }
}
