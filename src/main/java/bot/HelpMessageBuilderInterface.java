package bot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public interface HelpMessageBuilderInterface
{
    /**
     * Build help message for admin
     *
     * @param adminCommands  List of admin commands
     * @param memberCommands List of member commands
     *
     * @return String
     */
    @NotNull String buildAdminHelpMessage(
        @NotNull ArrayList<CommandInterface> adminCommands,
        @NotNull ArrayList<CommandInterface> memberCommands
    );

    /**
     * Build help message for member
     *
     * @param commands List of commands
     *
     * @return String
     */
    @NotNull String buildMemberHelpMessage(@NotNull ArrayList<CommandInterface> commands);
}
