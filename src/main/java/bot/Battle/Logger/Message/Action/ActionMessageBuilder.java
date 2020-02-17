package bot.Battle.Logger.Message.Action;

import bot.Battle.ActionResultInterface;
import bot.Battle.AttackActionResult;
import bot.Battle.HostileEncounter.DodgeActionResult;
import bot.Battle.HostileEncounter.GuardActionResult;
import bot.Battle.HealActionResult;
import bot.Battle.HurtActionResult;
import bot.Battle.HostileEncounter.LootActionResult;
import bot.Battle.ModifyStatActionResult;
import bot.Battle.HostileEncounter.ProtectActionResult;
import bot.Battle.JoinActionResult;
import bot.Battle.Logger.Mention;
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
        if (result instanceof AttackActionResult) {
            message = attackActionMessageFactory.createMessage((AttackActionResult) result, dmMention);
        } else if (result instanceof DodgeActionResult) {
            message = dodgeActionMessageFactory.createMessage((DodgeActionResult) result);
        } else if (result instanceof GuardActionResult) {
            message = guardActionMessageFactory.createMessage((GuardActionResult) result);
        } else if (result instanceof HealActionResult) {
            message = healActionMessageFactory.createMessage((HealActionResult) result);
        } else if (result instanceof HurtActionResult) {
            message = hurtActionMessageFactory.createMessage((HurtActionResult) result);
        } else if (result instanceof JoinActionResult) {
            message = joinActionMessageFactory.createMessage((JoinActionResult) result);
        } else if (result instanceof LootActionResult) {
            message = lootActionMessageFactory.createMessage((LootActionResult) result);
        } else if (result instanceof ModifyStatActionResult) {
            message = modifyStatActionMessageFactory.createMessage((ModifyStatActionResult) result);
        } else if (result instanceof ProtectActionResult) {
            message = protectActionMessageFactory.createMessage((ProtectActionResult) result);
        } else {
            throw ActionMessageBuilderException.createActionNotSet();
        }

        return message.getAsString();
    }
}
