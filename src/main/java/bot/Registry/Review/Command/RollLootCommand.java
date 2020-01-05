package bot.Registry.Review.Command;

import bot.Command;
import bot.CommandParameter;
import bot.Encounter.Logger.Message.Action.LootRollLineFactory;
import bot.Encounter.LootRollInterface;
import bot.Hostile.Hostile;
import bot.Hostile.HostileRepository;
import bot.ProcessManager;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RollLootCommand extends Command
{
    final private static int LIMIT = 40;

    /**
     * Constructor
     *
     * @param processManager Process manager
     */
    RollLootCommand(@NotNull ProcessManager processManager)
    {
        super(
            processManager,
            "roll loot",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("HostileSpecies", true));
                    add(new CommandParameter("RollCount", false));
                }
            },
            String.format(
                "Manually roll loot for a hostile. Include roll count to roll multiple times, max is %s.",
                LIMIT
            )
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        MessageChannel               channel = event.getChannel();
        String[]                     parameters = getParametersFromEvent(event);
        String                       species = parameters[0];
        int                          rollCount = parameters.length > 1 ? Integer.parseInt(parameters[1]) : 1;
        Hostile                      hostile = HostileRepository.getHostile(species);
        ArrayList<LootRollInterface> rolls = new ArrayList<>();
        LootRollLineFactory          factory = new LootRollLineFactory();

        // need a limit for discord character count limit
        if (rollCount > LIMIT) {
            rollCount = LIMIT;
        }

        for (int i = 0; i < rollCount; i++) {
            rolls.addAll(hostile.rollLoot());
        }

        channel.sendMessage(factory.getLootRollsMessage(rolls)).queue();
    }
}