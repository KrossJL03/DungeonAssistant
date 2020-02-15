package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.AdditionalCommandInterface;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.Encounter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Player.Player;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UseItemCommand extends EncounterCommand implements AdditionalCommandInterface
{
    /**
     * UseItemCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     */
    UseItemCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            logger,
            dmChecker,
            "rp!use",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("ItemName", true));
                    add(new CommandParameter("Amount", true));
                }
            },
            "Use an item through rp!bot. The DM will be pinged to activate the item.",
            false
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        Encounter encounter = getHostileEncounter();

        if (!encounter.isOver()) {
            Player player = getPlayerFromEvent(event);
            encounter.useItemAction(player);

            getLogger().pingDmItemUsed(player);
        }
    }
}
