package bot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface HelpMessageBuilderInterface
{
    /**
     * Build help message for admin
     *
     * @param commands List of commands
     *
     * @return String
     */
    @NotNull String buildAdminCommandsMessage(@NotNull ArrayList<CommandInterface> commands);

    /**
     * Build help message description
     *
     * @return HelpMessage
     */
    @NotNull String buildDescriptionMessage();

    /**
     * Build help message for member
     *
     * @param commands List of commands
     *
     * @return String
     */
    @NotNull String buildMemberCommandsMessage(@NotNull ArrayList<CommandInterface> commands);
}
