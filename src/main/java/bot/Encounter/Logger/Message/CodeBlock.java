package bot.Encounter.Logger.Message;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class CodeBlock implements MessageBlockInterface {

    private ArrayList<String> lines;
    private CodeBlockStyle    style;

    /**
     * CodeBlock constructor
     *
     * @param lines        Message lines
     * @param messageStyle Message style
     */
    CodeBlock(@NotNull ArrayList<String> lines, @NotNull CodeBlockStyle messageStyle) {
        this.lines = lines;
        this.style = messageStyle;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    public String getPrintout() {
        StringBuilder     output       = new StringBuilder();
        ArrayList<String> messageLines = this.lines;

        messageLines.add(0, style.getStyle());
        output.append(MessageConstants.CODE_BRACKET);
        for (String line : messageLines) {
            if (!line.equals(MessageConstants.BREAK)) {
                output.append(line);
            }
            output.append(MessageConstants.NEWLINE);
        }
        output.append(MessageConstants.CODE_BRACKET);
        return output.toString();
    }

}
