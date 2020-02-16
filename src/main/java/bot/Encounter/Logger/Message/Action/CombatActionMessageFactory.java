package bot.Encounter.Logger.Message.Action;

import bot.Constant;
import bot.Encounter.CombatActionResultInterface;
import org.jetbrains.annotations.NotNull;

abstract class CombatActionMessageFactory extends ActionMessageFactory
{
    /**
     * Add death saving throw text to message if applicable
     *
     * @param message Message
     * @param result  Action result
     */
    final protected void addDeathSaveIfApplicable(
        @NotNull ActionMessage message,
        @NotNull CombatActionResultInterface result
    )
    {
        if (result.rolledDeathSave()) {
            message.add(String.format(
                "%s takes %s damage & struggles not to %s!!",
                result.getTargetName(),
                codeFormatter.makeRed("lethal"),
                codeFormatter.makeRed("collapse")
            ));
            message.add(String.format(
                "d%d %s %s",
                result.getDeathSaveDie(),
                codeFormatter.makeRed("save die"),
                codeFormatter.makeGrey(String.format("success %d", result.getDeathMinSaveRoll()))
            ));
            message.addBreak();
            message.add(String.format(
                "%d %s %s!",
                result.getDeathSaveRoll(),
                ActionMessage.DOUBLE_ARROW,
                result.survivedDeathSave()
                ? "Success"
                : codeFormatter.makeRed("FAIL")
            ));
            message.addBreak();

            if (result.survivedDeathSave()) {
                message.add(String.format(
                    "%s remains %s through sheer might!!",
                    result.getTargetName(),
                    codeFormatter.makeCyan("standing")
                ));
            }
        }
    }

    /**
     * Get damage dealt line
     *
     * @param result            Action result
     * @param isMultipleAttacks Were multiple attacks inflicted
     *
     * @return String
     */
    final protected @NotNull String getDamageDealtLine(
        @NotNull CombatActionResultInterface result,
        boolean isMultipleAttacks
    )
    {
        String targetName = result.isTargetExplorer()
                            ? codeFormatter.makeYellow(result.getTargetName())
                            : codeFormatter.makeRed(result.getTargetName());
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
    final protected @NotNull String getDamageResistedLine(int damageResisted)
    {
        return String.format("Resisted %d dmg through sheer might!", damageResisted);
    }

    /**
     * Get "hit for full damage" line
     *
     * @param name Target name
     *
     * @return String
     */
    final protected @NotNull String getHitForFullDamageLine(@NotNull String name)
    {
        return String.format(
            "%s is hit for %s damage!",
            codeFormatter.makeYellow(name),
            codeFormatter.makeRed("full")
        );
    }

    /**
     * Get target status line
     *
     * @param result Action result
     *
     * @return String
     */
    final protected String getTargetStatusLine(@NotNull CombatActionResultInterface result)
    {
        if (result.isTargetSlain()) {
            return String.format(
                "%s was %s%s!!",
                result.getTargetName(),
                codeFormatter.makeRed(result.isTargetExplorer() ? "defeated" : "slain"),
                result.isTargetSlain() ? String.format(" by %s", result.getTargetSlayer().getName()) : ""
            );
        } else {
            int currentHp = result.getTargetCurrentHp();
            int maxHp     = result.getTargetMaxHp();
            return String.format(
                "%s/%s health remaining",
                currentHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : currentHp,
                maxHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : maxHp
            );
        }
    }
}
