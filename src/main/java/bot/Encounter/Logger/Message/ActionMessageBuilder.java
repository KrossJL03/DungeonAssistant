package bot.Encounter.Logger.Message;

import bot.Encounter.Logger.Mention;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActionMessageBuilder {

    private ActionMessageFormatter formatter;
    private Mention                dungeonMasterMention;

    /**
     * Action MessageBuilder constuctor
     *
     * @param dungeonMasterMention Dungeon master mention
     */
    @NotNull
    public ActionMessageBuilder(Mention dungeonMasterMention) {
        // todo make mention not nullable
        this.dungeonMasterMention = dungeonMasterMention;
        this.formatter = new ActionMessageFormatter();
    }

    /**
     * Build message from attack action result
     *
     * @param actionData Attack action result
     *
     * @return String
     */
    @NotNull
    public String buildAttackActionMessage(@NotNull AttackActionDataInterface actionData) {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                textBlockLines = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        codeBlockLines.add(String.format(
            "%s attacks %s!",
            this.formatter.makeYellow(actionData.getAttackerName()),
            this.formatter.makeRed(actionData.getTargetName())
        ));
        codeBlockLines.add(String.format(
            "d20 %s rolled %d [%s]",
            this.formatter.makeCyan("hit dice"),
            actionData.getHitRoll(),
            this.formatter.makeYellow(actionData.getHitType()).toUpperCase()
        ));

        if (actionData.isHit()) {
            if (actionData.isCrit()) {
                codeBlockLines.add(String.format("MAX DAMAGE %d!!", actionData.getDamageRoll()));
            } else {
                codeBlockLines.add(String.format(
                    "d%d %s rolled %d",
                    actionData.getDamageDie(),
                    this.formatter.makeCyan("dmg dice"),
                    actionData.getDamageRoll()
                ));
            }
            codeBlockLines.add(MessageConstants.BREAK);
            codeBlockLines.add(String.format(
                "%s takes %d damage!",
                this.formatter.makeRed(actionData.getTargetName()),
                actionData.getDamageRoll()
            ));
        }

        if (actionData.isFail()) {
            codeBlockLines.add(String.format("well... that's %s", this.formatter.makeRed("unfortunate")));
            textBlockLines.add(String.format(
                "Sit tight while me and %s discuss your fate",
                this.dungeonMasterMention.getValue()
            ));
        } else {
            codeBlockLines.add(this.getTargetStatusMessage(actionData));
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
    @NotNull
    public String buildDodgeActionMessage(@NotNull DodgeActionDataInterface actionData) {
        ArrayList<MessageBlockInterface> blocks           = new ArrayList<>();
        ArrayList<String>                codeMessageLines = new ArrayList<>();

        if (actionData.isForceFail()) {
            codeMessageLines.add(String.format(
                "%s was distracted, they %s to %s the attacks!",
                this.formatter.makeYellow(actionData.getTargetName()),
                this.formatter.makeYellow("failed"),
                this.formatter.makeYellow("dodge")
            ));
            codeMessageLines.add(String.format(
                "%s is hit for %s damage!",
                this.formatter.makeYellow(actionData.getTargetName()),
                this.formatter.makeRed("full")
            ));
        } else {
            codeMessageLines.add(String.format(
                "%s attempts to %s %d attacks!",
                this.formatter.makeYellow(actionData.getTargetName()),
                this.formatter.makeYellow("dodge"),
                actionData.getAttackCount()
            ));
            codeMessageLines.add(String.format(
                "d%d %s %s",
                actionData.getDodgeDie(),
                this.formatter.makeCyan("dodge dice"),
                this.formatter.makeGrey(String.format("success = %d", actionData.getMinSucessDodgeRoll()))
            ));
            codeMessageLines.add(MessageConstants.BREAK);
            for (DodgeSubActionDataInterface subActionData : actionData.getSubActionData()) {
                codeMessageLines.add(this.getDodgeSubActionMessage(subActionData));
                codeMessageLines.add(MessageConstants.BREAK);
            }
        }

        if (actionData.getResistedDamage() > 0) {
            codeMessageLines.add(String.format(
                "Resisted %d dmg through sheer might!",
                actionData.getResistedDamage()
            ));
            codeMessageLines.add(MessageConstants.BREAK);
        }

        codeMessageLines.add(String.format(
            "%s takes %d dmg total!",
            this.formatter.makeYellow(actionData.getTargetName()),
            actionData.getDamage()
        ));

        codeMessageLines.add(this.getTargetStatusMessage(actionData));

        blocks.add(new CodeBlock(codeMessageLines, this.formatter.getStyle()));

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
    @NotNull
    public String buildLootActionMessage(LootActionDataInterface actionData) {
        ArrayList<MessageBlockInterface> blocks         = new ArrayList<>();
        ArrayList<String>                textBlockLines = new ArrayList<>();
        ArrayList<String>                codeBlockLines = new ArrayList<>();

        textBlockLines.add(actionData.getMention());

        if (actionData.noLoot()) {
            return "Looks like you don't have any loot. That's a problem"; // todo
        }

        codeBlockLines.add(String.format(
            "%s helped slay %d hostile%s!",
            this.formatter.makeYellow(actionData.getName()),
            actionData.getKillCount(),
            actionData.getKillCount() > 1 ? "s" : ""
        ));
        codeBlockLines.add(String.format(
            "%dd%d %s",
            actionData.getKillCount(),
            actionData.getLootDie(),
            this.formatter.makeCyan("loot dice")
        ));

        codeBlockLines.add(MessageConstants.BREAK);

        for (LootSubActionDataInterface subAction : actionData.getSubActions()) {
            codeBlockLines.add(this.getLootSubActionMessage(subAction));
        }

        codeBlockLines.add(MessageConstants.BREAK);

        if (actionData.hasFinalBlows()) {
            codeBlockLines.add(String.format(
                "ALSO they earned %dc for landing the final blow%s on %s!",
                actionData.getFinalBlowBonus(),
                actionData.getFinalBlows().size() > 1 ? "s" : "",
                this.formatter.makeRed(String.join("', '", actionData.getFinalBlows()))
            ));
        }
        codeBlockLines.add("Congratulations!");

        blocks.add(new TextBlock(textBlockLines));
        blocks.add(new CodeBlock(codeBlockLines, formatter.getStyle()));

        Message message = new Message(blocks);
        return message.getPrintout();
    }

    /**
     * Get dodge sub action message
     *
     * @param actionData Dodge sub action data
     *
     * @return String
     */
    @NotNull
    private String getDodgeSubActionMessage(DodgeSubActionDataInterface actionData) {
        StringBuilder output    = new StringBuilder();
        int           dodgeRoll = actionData.getDodgeRoll();

        output.append(String.format("%2d %s ", dodgeRoll, MessageConstants.DOUBLE_ARROW));

        if (actionData.isSuccess()) {
            output.append(String.format(
                "%s! %s",
                this.formatter.makeYellow("DODGED"),
                this.formatter.makeGrey(String.format("no dmg from %s", actionData.getAttackerName()))
            ));
        } else {
            output.append(String.format(
                "%s! %2d dmg from '%s'",
                this.formatter.makeRed("FAIL"),
                actionData.getDamageRoll(),
                actionData.getAttackerName()
            ));
        }

        return output.toString();
    }

    /**
     * Get loot sub action message
     *
     * @param actionData Loot sub action data
     *
     * @return String
     */
    @NotNull
    private String getLootSubActionMessage(LootSubActionDataInterface actionData) {
        StringBuilder output = new StringBuilder();
        Loot          loot   = actionData.getLoot();

        output.append(String.format("%2d %s ", actionData.getLootRoll(), MessageConstants.DOUBLE_ARROW));

        if (loot.getItem() == null || loot.getItem().equals("null")) {
            output.append(this.formatter.makeGrey(String.format("nothing from %s", actionData.getKillName())));
        } else {
            output.append(String.format(
                "x%d %s from '%s'",
                loot.getQuantity(),
                this.formatter.makeYellow(loot.getItem()),
                this.formatter.makeRed(actionData.getKillName())
            ));
        }

        return output.toString();
    }

    /**
     * Get remainign health message
     *
     * @param actionData Combat action data
     *
     * @return String
     */
    @NotNull
    private String getRemainingHealthMessage(CombatActionDataInterface actionData) {
        return String.format(
            "%d/%d health remaining",
            actionData.getTargetCurrentHp(),
            actionData.getTargetMaxHp()
        );
    }

    /**
     * Get target status mesage
     *
     * @param actionData Attack action data
     *
     * @return String
     */
    @NotNull
    private String getTargetStatusMessage(AttackActionDataInterface actionData) {
        if (actionData.isTargetSlain()) {
            return String.format(
                "%s was slain by %s!!!",
                this.formatter.makeRed(actionData.getTargetName()),
                this.formatter.makeYellow(actionData.getAttackerName())
            );
        } else {
            return this.getRemainingHealthMessage(actionData);
        }
    }

    /**
     * Get target status message
     *
     * @param actionData Dodge action data
     *
     * @return String
     */
    @NotNull
    private String getTargetStatusMessage(DodgeActionDataInterface actionData) {
        if (actionData.isTargetSlain()) {
            return String.format(
                "%s has been %s!!",
                actionData.getTargetName(),
                this.formatter.makeRed("knocked out")
            );
        } else {
            return this.getRemainingHealthMessage(actionData);
        }
    }
}
