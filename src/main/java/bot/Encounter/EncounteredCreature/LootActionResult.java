package bot.Encounter.EncounteredCreature;

import bot.Encounter.EncounterCreatureInterface;
import bot.Encounter.Logger.Mention;
import bot.Encounter.LootActionResultInterface;
import bot.Encounter.LootRollInterface;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LootActionResult implements LootActionResultInterface
{
    private ArrayList<EncounterCreatureInterface> finalBlows;
    private ArrayList<LootRollInterface>           lootRolls;
    private Player                                 owner;
    private String                                 name;
    private int                                    finalBlowBonus;
    private int                                    lootDie;
    private boolean                                isRolled;

    /**
     * LootActionResult constructor
     *
     * @param name  Name
     * @param owner Owner
     */
    @NotNull LootActionResult(@NotNull String name, @NotNull Player owner)
    {
        this.finalBlows = new ArrayList<>();
        this.isRolled = false;
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
     * @param kills          Kills
     * @param lootDie        Loot die used to roll loot
     * @param finalBlowBonus Bonus cumulus for final blows
     */
    @NotNull LootActionResult(
        @NotNull String name,
        @NotNull Player owner,
        @NotNull ArrayList<LootRollInterface> lootRolls,
        @NotNull ArrayList<EncounterCreatureInterface> kills,
        int lootDie,
        int finalBlowBonus
    )
    {
        this.finalBlowBonus = finalBlowBonus;
        this.finalBlows = kills;
        this.isRolled = true;
        this.lootDie = lootDie;
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
        for (EncounterCreatureInterface finalBlow : finalBlows) {
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
        return lootRolls.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLootDie()
    {
        return lootDie;
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