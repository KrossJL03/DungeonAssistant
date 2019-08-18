package bot.Encounter.EncounteredCreature;

import bot.Encounter.EncounteredCreatureInterface;
import bot.Encounter.Logger.Mention;
import bot.Encounter.LootActionResultInterface;
import bot.Encounter.LootRollInterface;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LootActionResult implements LootActionResultInterface
{
    private ArrayList<EncounteredCreatureInterface> finalBlows;
    private ArrayList<LootRollInterface>            lootRolls;
    private Player                                  owner;
    private String                                  name;
    private int                                     finalBlowBonus;
    private int                                     killCount;
    private boolean                                 isRolled;

    /**
     * LootActionResult constructor
     *
     * @param name  Name
     * @param owner Owner
     */
    @NotNull LootActionResult(@NotNull String name, @NotNull Player owner)
    {
        this.finalBlowBonus = 0;
        this.finalBlows = new ArrayList<>();
        this.isRolled = false;
        this.killCount = 0;
        this.lootRolls = new ArrayList<>();
        this.name = name;
        this.owner = owner;
    }

    /**
     * LootActionResult constructor
     *
     * @param name           Name
     * @param owner          Owner
     * @param lootRolls      Loot rolls
     * @param finalBlows     Kills
     * @param killCount      Number of kills present for
     * @param finalBlowBonus Bonus cumulus for final blows
     */
    @NotNull LootActionResult(
        @NotNull String name,
        @NotNull Player owner,
        @NotNull ArrayList<LootRollInterface> lootRolls,
        @NotNull ArrayList<EncounteredCreatureInterface> finalBlows,
        int killCount,
        int finalBlowBonus
    )
    {
        this.finalBlowBonus = finalBlowBonus;
        this.finalBlows = finalBlows;
        this.isRolled = true;
        this.killCount = killCount;
        this.lootRolls = lootRolls;
        this.name = name;
        this.owner = owner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFinalBlowBonus()
    {
        return finalBlowBonus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<String> getFinalBlowNames()
    {
        ArrayList<String> finalBlowNames = new ArrayList<>();
        for (EncounteredCreatureInterface finalBlow : finalBlows) {
            finalBlowNames.add(finalBlow.getName());
        }
        return finalBlowNames;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getKillCount()
    {
        return killCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLootRollCount()
    {
        return lootRolls.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<LootRollInterface> getLootRolls()
    {
        return lootRolls;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Mention getMention()
    {
        return Mention.createForPlayer(owner.getUserId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasFinalBlows()
    {
        return finalBlows.size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean noLoot()
    {
        return !isRolled;
    }
}
