package bot.Encounter.Command;

import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LootCommand extends EncounterCommand
{
    /**
     * LootCommand constructor
     *
     * @param holder Encounter holder
     * @param logger Encounter logger
     */
    LootCommand(@NotNull EncounterHolder holder, @NotNull EncounterLogger logger)
    {
        super(
            holder,
            logger,
            "loot",
            new ArrayList<>(),
            "Collect materials and rewards at the end of an encounter.",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Player player = getPlayerFromEvent(event);

        getHostileEncounter().lootAction(player);
    }
}
