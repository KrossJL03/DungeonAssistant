package bot.Lottery.Mofongo;

import bot.Command;
import bot.CommandParameter;
import bot.ProcessManager;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

class MofongoRollCommand extends Command
{
    final static private int RARITY_DIE = 100;

    private MofongoLogger logger;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param logger         Logger
     */
    MofongoRollCommand(@NotNull ProcessManager processManager, @NotNull MofongoLogger logger)
    {
        super(
            processManager,
            "roll mofongo",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("PetSpecies", false));
                }
            },
            "Roll mofongo. Include a pet species to roll for a pet."
        );
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        MessageChannel channel    = event.getChannel();
        String[]       parameters = getParametersFromEvent(event);

        if (parameters.length > 0) {
            rollForPet(channel, parameters[0]);
        } else {
            rollForExplorer(channel);
        }

    }

    /**
     * Get rarity by roll
     *
     * @param rarityRoll Rarity roll
     *
     * @return String
     */
    private @Nullable String getRarityByRoll(int rarityRoll)
    {
        String result;
        if (rarityRoll > 95) {
            result = MofongoItem.RARITY_ITEM_ULTRA_RARE;
        } else if (rarityRoll > 85) {
            result = MofongoItem.RARITY_ITEM_RARE;
        } else if (rarityRoll > 60) {
            result = MofongoItem.RARITY_ITEM_UNCOMMON;
        } else if (rarityRoll > 20) {
            result = MofongoItem.RARITY_ITEM_COMMON;
        } else {
            result = null;
        }

        return result;
    }

    /**
     * Roll for explorer
     *
     * @param channel Channel to log to
     */
    private void rollForExplorer(@NotNull MessageChannel channel)
    {
        int         rarityRoll = rollRarity();
        String      rarityName = getRarityByRoll(rarityRoll);
        int         itemDie    = 0;
        MofongoItem item       = null;

        if (rarityName != null) {
            itemDie = MofongoItemRegistry.getItemCountByRarity(rarityName);
            item = MofongoItemRegistry.getItemByRarityAndRoll(rarityName, rollItemDie(itemDie));
        }

        MofongoExplorerRollResult result = new MofongoExplorerRollResult(
            item,
            rarityName,
            itemDie,
            RARITY_DIE,
            rarityRoll
        );

        logger.logExplorerItemRolled(channel, result);
    }

    /**
     * Roll for pet by species name
     *
     * @param channel     Channel to log to
     * @param speciesName Pet species name
     */
    private void rollForPet(@NotNull MessageChannel channel, @NotNull String speciesName)
    {
        ArrayList<MofongoItem> items   = new ArrayList<>();
        int                    itemDie = MofongoItemRegistry.getItemCountByRarity(speciesName);
        int                    rolls   = MofongoPetStatRegistry.getRollCountBySpeciesName(speciesName);
        while (rolls > 0) {
            items.add(MofongoItemRegistry.getItemByRarityAndRoll(speciesName, rollItemDie(itemDie)));
            rolls--;
        }

        MofongoPetRollResult result = new MofongoPetRollResult(items, itemDie);

        logger.logPetItemRolled(channel, result);
    }

    /**
     * Roll item by rarity
     *
     * @param itemDie Item die
     *
     * @return int
     */
    private int rollItemDie(int itemDie)
    {
        return (int) Math.floor(Math.random() * itemDie) + 1;
    }

    /**
     * Roll rarity
     *
     * @return int
     */
    private int rollRarity()
    {
        return (int) Math.floor(Math.random() * RARITY_DIE) + 1;
    }
}
