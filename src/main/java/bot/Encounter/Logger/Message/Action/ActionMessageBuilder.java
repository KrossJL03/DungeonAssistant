package bot.Encounter.Logger.Message.Action;

import bot.Encounter.*;
import bot.Encounter.Logger.Mention;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

public class ActionMessageBuilder
{
    private AttackActionMessageFactory     attackActionMessageFactory;
    private DodgeActionMessageFactory      dodgeActionMessageFactory;
    private GuardActionMessageFactory      guardActionMessageFactory;
    private HealActionMessageFactory       healActionMessageFactory;
    private HurtActionMessageFactory       hurtActionMessageFactory;
    private JoinActionMessageFactory       joinActionMessageFactory;
    private LootActionMessageFactory       lootActionMessageFactory;
    private ModifyStatActionMessageFactory modifyStatActionMessageFactory;
    private ProtectActionMessageFactory    protectActionMessageFactory;

    /**
     * ActionMessageBuilder constructor
     */
    public @NotNull ActionMessageBuilder()
    {
        this.attackActionMessageFactory = new AttackActionMessageFactory();
        this.dodgeActionMessageFactory = new DodgeActionMessageFactory();
        this.guardActionMessageFactory = new GuardActionMessageFactory();
        this.healActionMessageFactory = new HealActionMessageFactory();
        this.hurtActionMessageFactory = new HurtActionMessageFactory();
        this.joinActionMessageFactory = new JoinActionMessageFactory();
        this.lootActionMessageFactory = new LootActionMessageFactory();
        this.modifyStatActionMessageFactory = new ModifyStatActionMessageFactory();
        this.protectActionMessageFactory = new ProtectActionMessageFactory();
    }

    /**
     * Build action message
     *
     * @param result    Action result
     * @param dmMention Dm mention
     *
     * @return String
     *
     * @throws ActionMessageBuilderException If action builder does not exist for action result
     */
    public String buildActionMessage(ActionResultInterface result, Mention dmMention)
        throws ActionMessageBuilderException
    {
        // todo make more SOLID
        MessageInterface message;
        if (result instanceof AttackActionResultInterface) {
            message = attackActionMessageFactory.createMessage((AttackActionResultInterface) result, dmMention);
        } else if (result instanceof DodgeActionResultInterface) {
            message = dodgeActionMessageFactory.createMessage((DodgeActionResultInterface) result);
        } else if (result instanceof GuardActionResultInterface) {
            message = guardActionMessageFactory.createMessage((GuardActionResultInterface) result);
        } else if (result instanceof HealActionResultInterface) {
            message = healActionMessageFactory.createMessage((HealActionResultInterface) result);
        } else if (result instanceof HurtActionResultInterface) {
            message = hurtActionMessageFactory.createMessage((HurtActionResultInterface) result);
        } else if (result instanceof JoinActionResultInterface) {
            message = joinActionMessageFactory.createMessage((JoinActionResultInterface) result);
        } else if (result instanceof LootActionResultInterface) {
            message = lootActionMessageFactory.createMessage((LootActionResultInterface) result);
        } else if (result instanceof ModifyStatActionResultInterface) {
            message = modifyStatActionMessageFactory.createMessage((ModifyStatActionResultInterface) result);
        } else if (result instanceof ProtectActionResultInterface) {
            message = protectActionMessageFactory.createMessage((ProtectActionResultInterface) result);
        } else {
            throw ActionMessageBuilderException.createActionNotSet();
        }

        return message.getAsString();
    }
}
