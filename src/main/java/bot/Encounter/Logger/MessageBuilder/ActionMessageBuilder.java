package bot.Encounter.Logger.MessageBuilder;

import bot.Encounter.Logger.Mention;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActionMessageBuilder {

    private ActionMessageFormatter formatter;

    /**
     * Action MessageBuilder constructor
     */
    public @NotNull ActionMessageBuilder() {
        this.formatter = new ActionMessageFormatter();
    }

    /**
     * Build message from attack action result
     *
     * @param actionData Attack action result
     * @param dmMention  Dungeon master mention
     *
     * @return String
     */
    public @NotNull String buildActionMessage(@NotNull AttackActionDataInterface actionData, Mention dmMention) {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                textBlockLines = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        codeBlockLines.add(String.format(
            "%s attacks %s!",
            formatter.makeYellow(actionData.getAttackerName()),
            formatter.makeRed(actionData.getTargetName())
        ));
        codeBlockLines.add(String.format(
            "d20 %s rolled %d [%s]",
            formatter.makeCyan("hit dice"),
            actionData.getHitRoll(),
            formatter.makeYellow(actionData.getHitType()).toUpperCase()
        ));

        if (actionData.isHit()) {
            if (actionData.isCrit()) {
                codeBlockLines.add(String.format("MAX DAMAGE %d!!", actionData.getDamageRoll()));
            } else {
                codeBlockLines.add(String.format(
                    "d%d %s rolled %d",
                    actionData.getDamageDie(),
                    formatter.makeCyan("dmg dice"),
                    actionData.getDamageRoll()
                ));
            }
        }

        codeBlockLines.add(MessageConstants.BREAK);

        if (actionData.isFail()) {
            codeBlockLines.add(String.format("well... that's %s", formatter.makeRed("unfortunate")));
            textBlockLines.add(String.format("Sit tight while me and %s discuss your fate", dmMention.getValue()));
        } else {
            if (actionData.getDamageDealt() > 0) {
                codeBlockLines.add(getDamageDealtLine(actionData, false));
            }
            codeBlockLines.add(getTargetStatusLine(actionData));
        }

        blocks.add(new CodeBlock(codeBlockLines, formatter.getStyle()));
        blocks.add(new TextBlock(textBlockLines));

        Message message = new Message(blocks);
        return message.getPrintout();
    }

    /**
     * Build message from dodge action result
     *
     * @param actionData Dodge action result
     *
     * @return String
     */
    public @NotNull String buildActionMessage(@NotNull DodgeActionDataInterface actionData) {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        if (actionData.isForceFail()) {
            codeBlockLines.add(String.format(
                "%s was distracted, they %s to %s the attacks!",
                formatter.makeYellow(actionData.getTargetName()),
                formatter.makeYellow("failed"),
                formatter.makeYellow("dodge")
            ));
            codeBlockLines.add(getHitForFullDamageLine(actionData.getTargetName()));
        } else {
            codeBlockLines.add(String.format(
                "%s attempts to %s %d attacks!",
                formatter.makeYellow(actionData.getTargetName()),
                formatter.makeYellow("dodge"),
                actionData.getAttackCount()
            ));
            codeBlockLines.add(String.format(
                "d%d %s %s",
                actionData.getDodgeDie(),
                formatter.makeCyan("dodge dice"),
                formatter.makeGray(String.format("success = %d", actionData.getMinSucessDodgeRoll()))
            ));
            codeBlockLines.add(MessageConstants.BREAK);
            for (DodgeSubActionDataInterface subActionData : actionData.getSubActionData()) {
                codeBlockLines.add(getDodgeSubActionLine(subActionData));
            }
        }

        if (actionData.getDamageResisted() > 0) {
            codeBlockLines.add(MessageConstants.BREAK);
            codeBlockLines.add(getDamageResistedLine(actionData.getDamageResisted()));
        }

        codeBlockLines.add(MessageConstants.BREAK);
        codeBlockLines.add(getDamageDealtLine(actionData, true));
        codeBlockLines.add(getTargetStatusLine(actionData));

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
    public @NotNull String buildActionMessage(LootActionDataInterface actionData) {
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

        for (LootSubActionDataInterface subAction : actionData.getSubActions()) {
            codeBlockLines.add(getLootSubActionline(subAction));
        }

        codeBlockLines.add(MessageConstants.BREAK);

        if (actionData.hasFinalBlows()) {
            ArrayList<String> formattedKills = new ArrayList<>();
            for (String finalBlowName : actionData.getFinalBlows()) {
                formattedKills.add(formatter.makeRed(finalBlowName));
            }
            codeBlockLines.add(String.format(
                "ALSO they earned %dc for landing the final blow%s on %s!",
                actionData.getFinalBlowBonus(),
                actionData.getFinalBlows().size() > 1 ? "s" : "",
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
     * @param actionData Protect action result
     *
     * @return String
     */
    public @NotNull String buildActionMessage(ProtectActionDataInterface actionData) {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                textBlockLines = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        codeBlockLines.add(String.format(
            "%s shields %s from the attacks!",
            actionData.getTargetName(),
            actionData.getProtectedName()
        ));
        codeBlockLines.add(getHitForFullDamageLine(actionData.getTargetName()));
        codeBlockLines.add(MessageConstants.BREAK);

        if (actionData.getDamageResisted() > 0) {
            codeBlockLines.add(getDamageResistedLine(actionData.getDamageResisted()));
            codeBlockLines.add(MessageConstants.BREAK);
        }

        codeBlockLines.add(getDamageDealtLine(actionData, true));
        codeBlockLines.add(getTargetStatusLine(actionData));

        textBlockLines.add(String.format(
            "%s, %s has been protected. They take no damage this round.",
            actionData.getProtectedOwnerMention().getValue(),
            actionData.getProtectedName()
        ));

        blocks.add(new CodeBlock(codeBlockLines, formatter.getStyle()));
        blocks.add(new TextBlock(textBlockLines));

        Message message = new Message(blocks);
        return message.getPrintout();
    }

    /**
     * Get damage dealt line
     *
     * @param actionData        Action data
     * @param isMultipleAttacks Were multiple attacks inflicted
     *
     * @return String
     */
    @NotNull
    private String getDamageDealtLine(CombatActionDataInterface actionData, boolean isMultipleAttacks) {
        String targetName = actionData.isTargetExplorer()
                            ? formatter.makeYellow(actionData.getTargetName())
                            : formatter.makeRed(actionData.getTargetName());
        return String.format(
            "%s takes %d damage%s!",
            targetName,
            actionData.getDamageDealt(),
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
    private String getDamageResistedLine(int damageResisted) {
        return String.format("Resisted %d dmg through sheer might!", damageResisted);
    }

    /**
     * Get dodge sub action line
     *
     * @param actionData Dodge sub action data
     *
     * @return String
     */
    @NotNull
    private String getDodgeSubActionLine(DodgeSubActionDataInterface actionData) {
        StringBuilder output    = new StringBuilder();
        int           dodgeRoll = actionData.getDodgeRoll();

        output.append(String.format("%2d %s ", dodgeRoll, MessageConstants.DOUBLE_ARROW));

        if (actionData.isSuccess()) {
            output.append(String.format(
                "%s! %s",
                formatter.makeYellow("DODGED"),
                formatter.makeGray(String.format("no dmg from %s", actionData.getAttackerName()))
            ));
        } else {
            output.append(String.format(
                "%s! %2d dmg from '%s'",
                formatter.makeRed("FAIL"),
                actionData.getDamageRoll(),
                actionData.getAttackerName()
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
    private String getHitForFullDamageLine(String name) {
        return String.format(
            "%s is hit for %s damage!",
            formatter.makeYellow(name),
            formatter.makeRed("full")
        );
    }

    /**
     * Get loot sub action line
     *
     * @param actionData Loot sub action data
     *
     * @return String
     */
    @NotNull
    private String getLootSubActionline(LootSubActionDataInterface actionData) {
        StringBuilder output = new StringBuilder();
        Loot          loot   = actionData.getLoot();

        output.append(String.format("%2d %s ", actionData.getLootRoll(), MessageConstants.DOUBLE_ARROW));

        if (loot.getItem() == null || loot.getItem().equals("null")) {
            output.append(formatter.makeGray(String.format("nothing from %s", actionData.getKillName())));
        } else {
            output.append(String.format(
                "x%d %s from %s",
                loot.getQuantity(),
                formatter.makeYellow(loot.getItem()),
                formatter.makeRed(actionData.getKillName())
            ));
        }

        return output.toString();
    }

    /**
     * Get target status line
     *
     * @param actionData Action data
     *
     * @return String
     */
    @NotNull
    private String getTargetStatusLine(CombatActionDataInterface actionData) {
        if (actionData.isTargetSlain()) {
            return String.format(
                "%s was %s%s!!",
                actionData.getTargetName(),
                formatter.makeRed(actionData.isTargetExplorer() ? "knocked out" : "slain"),
                actionData.isTargetSlain() ? String.format(" by %s", actionData.getTargetSlayer().getName()) : ""
            );
        } else {
            return String.format(
                "%d/%d health remaining",
                actionData.getTargetCurrentHp(),
                actionData.getTargetMaxHp()
            );
        }
    }
}
