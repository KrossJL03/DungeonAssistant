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
                }
            },
            "Manually roll loot for a hostile."
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        MessageChannel               channel    = event.getChannel();
        String[]                     parameters = getParametersFromEvent(event);
        String                       species    = parameters[0];
        Hostile                      hostile    = HostileRepository.getHostile(species);
        ArrayList<LootRollInterface> rolls      = hostile.rollLoot();
        LootRollLineFactory          factory    = new LootRollLineFactory();

        channel.sendMessage(factory.getLootRollsMessage(rolls)).queue();
    }
}