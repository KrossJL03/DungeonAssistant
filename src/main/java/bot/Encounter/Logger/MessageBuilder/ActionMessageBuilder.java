package bot.Encounter.Logger.MessageBuilder;

import bot.Encounter.*;
import bot.Encounter.Logger.Mention;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActionMessageBuilder
{

    private ActionMessageFormatter formatter;

    /**
     * Action MessageBuilder constructor
     */
    public @NotNull ActionMessageBuilder()
    {
        this.formatter = new ActionMessageFormatter();
    }

    /**
     * Build message from attack action result
     *
     * @param result    Attack action result
     * @param dmMention Dungeon master mention
     *
     * @return String
     */
    public @NotNull String buildActionMessage(@NotNull AttackActionResultInterface result, Mention dmMention)
    {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                textBlockLines = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        codeBlockLines.add(String.format(
            "%s attacks %s!",
            formatter.makeYellow(result.getAttackerName()),
            formatter.makeRed(result.getTargetName())
        ));
        codeBlockLines.add(String.format(
            "d20 %s rolled %d [%s]",
            formatter.makeCyan("hit dice"),
            result.getHitRoll(),
            formatter.makeYellow(result.getHitTypeString()).toUpperCase()
        ));

        if (result.isHit()) {
            if (result.isCrit()) {
                codeBlockLines.add(String.format("MAX DAMAGE %d!!", result.getDamageRoll()));
            } else {
                codeBlockLines.add(String.format(
                    "d%d %s rolled %d",
                    result.getDamageDie(),
                    formatter.makeCyan("dmg dice"),
                    result.getDamageRoll()
                ));
            }
        }

        codeBlockLines.add(MessageConstants.BREAK);

        if (result.isFail()) {
            codeBlockLines.add(String.format("well... that's %s", formatter.makeRed("unfortunate")));
            textBlockLines.add(String.format("Sit tight while me and %s discuss your fate", dmMention.getValue()));
        } else {
            if (result.getDamageDealt() > 0) {
                codeBlockLines.add(getDamageDealtLine(result, false));
            }
            codeBlockLines.add(getTargetStatusLine(result));
        }

        blocks.add(new CodeBlock(codeBlockLines, formatter.getStyle()));
        blocks.add(new TextBlock(textBlockLines));

        Message message = new Message(blocks);
        return message.getPrintout();
    }

    /**
     * Build message from dodge action result
     *
     * @param result Dodge action result
     *
     * @return String
     */
    public @NotNull String buildActionMessage(@NotNull DodgeActionResultInterface result)
    {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        if (result.isForceFail()) {
            codeBlockLines.add(String.format(
                "%s was distracted, they %s to %s the attacks!",
                formatter.makeYellow(result.getTargetName()),
                formatter.makeYellow("failed"),
                formatter.makeYellow("dodge")
            ));
            codeBlockLines.add(getHitForFullDamageLine(result.getTargetName()));
        } else {
            codeBlockLines.add(String.format(
                "%s attempts to %s %d attacks!",
                formatter.makeYellow(result.getTargetName()),
                formatter.makeYellow("dodge"),
                result.getAttackCount()
            ));
            codeBlockLines.add(String.format(
                "d%d %s %s",
                result.getTargetDodgeDie(),
                formatter.makeCyan("dodge dice"),
                formatter.makeGray(String.format("success = %d", result.getMinSucessDodgeRoll()))
            ));
            codeBlockLines.add(MessageConstants.BREAK);
            for (DodgeResultInterface subActionData : result.getDodgeResults()) {
                codeBlockLines.add(getDodgeResultLine(subActionData));
            }
        }

        if (result.getDamageResisted() > 0) {
            codeBlockLines.add(MessageConstants.BREAK);
            codeBlockLines.add(getDamageResistedLine(result.getDamageResisted()));
        }

        codeBlockLines.add(MessageConstants.BREAK);
        codeBlockLines.add(getDamageDealtLine(result, true));
        codeBlockLines.add(getTargetStatusLine(result));

        blocks.add(new CodeBlock(codeBlockLines, formatter.getStyle()));

        Message message = new Message(blocks);
        return message.getPrintout();
    }

    /**
     * Build message from loot action result
     *
     * @param actionData Loot action result
     *
     * @return String
     */
    public @NotNull String buildActionMessage(LootActionResultInterface actionData)
    {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                textBlockLines = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        textBlockLines.add(actionData.getMention().getValue());

        if (actionData.noLoot()) {
            throw LoggerException.createNoLoot(actionData.getName());
        }

        codeBlockLines.add(String.format(
            "%s helped slay %d hostile%s!",
            formatter.makeYellow(actionData.getName()),
            actionData.getKillCount(),
            actionData.getKillCount() > 1 ? "s" : ""
        ));
        codeBlockLines.add(String.format(
            "%dd%d %s",
            actionData.getKillCount(),
            actionData.getLootDie(),
            formatter.makeCyan("loot dice")
        ));

        codeBlockLines.add(MessageConstants.BREAK);

        for (LootRollInterface subAction : actionData.getLootRolls()) {
            codeBlockLines.add(getLootRollLine(subAction));
        }

        codeBlockLines.add(MessageConstants.BREAK);

        if (actionData.hasFinalBlows()) {
            ArrayList<String> formattedKills = new ArrayList<>();
            for (String finalBlowName : actionData.getFinalBlowNames()) {
                formattedKills.add(formatter.makeRed(finalBlowName));
            }
            codeBlockLines.add(String.format(
                "ALSO they earned %dc for landing the final blow%s on %s!",
                actionData.getFinalBlowBonus(),
                actionData.getFinalBlowNames().size() > 1 ? "s" : "",
                String.join(",  ", formattedKills)
            ));
        }
        codeBlockLines.add("Congratulations!");

        blocks.add(new TextBlock(textBlockLines));
        blocks.add(new CodeBlock(codeBlockLines, formatter.getStyle()));

        Message message = new Message(blocks);
        return message.getPrintout();
    }

    /**
     * Build message from protect action result
     *
     * @param result Protect action result
     *
     * @return String
     */
    public @NotNull String buildActionMessage(ProtectActionResultInterface result)
    {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                textBlockLines = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        codeBlockLines.add(String.format(
            "%s shields %s from the attacks!",
            result.getTargetName(),
            result.getProtectedName()
        ));
        codeBlockLines.add(getHitForFullDamageLine(result.getTargetName()));
        codeBlockLines.add(MessageConstants.BREAK);

        if (result.getDamageResisted() > 0) {
            codeBlockLines.add(getDamageResistedLine(result.getDamageResisted()));
            codeBlockLines.add(MessageConstants.BREAK);
        }

        codeBlockLines.add(getDamageDealtLine(result, true));
        codeBlockLines.add(getTargetStatusLine(result));

        textBlockLines.add(String.format(
            "%s, %s has been protected. They take no damage this round.",
            result.getProtectedOwnerMention().getValue(),
            result.getProtectedName()
        ));

        blocks.add(new CodeBlock(codeBlockLines, formatter.getStyle()));
        blocks.add(new TextBlock(textBlockLines));

        Message message = new Message(blocks);
        return message.getPrintout();
    }

    /**
     * Get damage dealt line
     *
     * @param result            Action result
     * @param isMultipleAttacks Were multiple attacks inflicted
     *
     * @return String
     */
    @NotNull
    private String getDamageDealtLine(CombatActionResultInterface result, boolean isMultipleAttacks)
    {
        String targetName = result.isTargetExplorer()
                            ? formatter.makeYellow(result.getTargetName())
                            : formatter.makeRed(result.getTargetName());
        return String.format(
            "%s takes %d damage%s!",
            targetName,
            result.getDamageDealt(),
            isMultipleAttacks ? " total" : ""
        );
    }


    /**
     * Get damage resisted line
     *
     * @param damageResisted Damage resisted
     *
     * @return String
     */
    @NotNull
    private String getDamageResistedLine(int damageResisted)
    {
        return String.format("Resisted %d dmg through sheer might!", damageResisted);
    }

    /**
     * Get dodge result line
     *
     * @param result Dodge result
     *
     * @return String
     */
    @NotNull
    private String getDodgeResultLine(DodgeResultInterface result)
    {
        StringBuilder output    = new StringBuilder();
        int           dodgeRoll = result.getTargetDodgeRoll();

        output.append(String.format("%2d %s ", dodgeRoll, MessageConstants.DOUBLE_ARROW));

        if (result.isSuccess()) {
            output.append(String.format(
                "%s! %s",
                formatter.makeYellow("DODGED"),
                formatter.makeGray(String.format("no dmg from %s", result.getAttackerName()))
            ));
        } else {
            output.append(String.format(
                "%s! %2d dmg from '%s'",
                formatter.makeRed("FAIL"),
                result.getAttackerDamageRoll(),
                result.getAttackerName()
            ));
        }

        return output.toString();
    }

    /**
     * Get "hit for full damage" line
     *
     * @param name Target name
     *
     * @return String
     */
    @NotNull
    private String getHitForFullDamageLine(String name)
    {
        return String.format(
            "%s is hit for %s damage!",
            formatter.makeYellow(name),
            formatter.makeRed("full")
        );
    }

    /**
     * Get loot roll line
     *
     * @param roll Loot roll
     *
     * @return String
     */
    @NotNull
    private String getLootRollLine(LootRollInterface roll)
    {
        StringBuilder output = new StringBuilder();
        Loot          loot   = roll.getLoot();

        output.append(String.format("%2d %s ", roll.getLootRoll(), MessageConstants.DOUBLE_ARROW));

        if (loot.getItem() == null || loot.getItem().equals("null")) {
            output.append(formatter.makeGray(String.format("nothing from %s", roll.getKillName())));
        } else {
            output.append(String.format(
                "x%d %s from %s",
                loot.getQuantity(),
                formatter.makeYellow(loot.getItem()),
                formatter.makeRed(roll.getKillName())
            ));
        }

        return output.toString();
    }

    /**
     * Get target status line
     *
     * @param result Action result
     *
     * @return String
     */
    @NotNull
    private String getTargetStatusLine(CombatActionResultInterface result)
    {
        if (result.isTargetSlain()) {
            return String.format(
                "%s was %s%s!!",
                result.getTargetName(),
                formatter.makeRed(result.isTargetExplorer() ? "knocked out" : "slain"),
                result.isTargetSlain() ? String.format(" by %s", result.getTargetSlayer().getName()) : ""
            );
        } else {
            return String.format(
                "%d/%d health remaining",
                result.getTargetCurrentHp(),
                result.getTargetMaxHp()
            );
        }
    }
}
