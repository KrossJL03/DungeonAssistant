package bot.Item;

import bot.CustomException;
import net.dv8tion.jda.core.entities.Message.Attachment;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class AttachmentReader
{
    private File      file;
    private CSVParser parser;

    /**
     * Constructor.
     *
     * @param attachment Attachment to read
     */
    AttachmentReader(@NotNull Attachment attachment)
    {
        init(attachment);
    }

    /**
     * Read attachment
     *
     * @return List
     *
     * @throws CustomException If records cannot be read
     */
    List<Map<String, String>> read() throws CustomException
    {
        List<Map<String, String>> result = new ArrayList<>();
        int                       count  = 0;

        try {
            for (CSVRecord record : parser) {
                result.add(record.toMap());
            }
        } catch (Throwable e) {
            throw new CustomException(String.format(
                "I wasn't able to read the file, I had an issue with line %d",
                count
            ));
        } finally {
            delete();
        }

        return result;
    }

    /**
     * Delete file
     *
     * @throws CustomException if file fails to be deleted
     */
    private void delete() throws CustomException
    {
        boolean deleted = false;
        try {
            if (parser != null) {
                parser.close();
            }
            if (file != null) {
                deleted = file.delete();
            }
        } catch (Throwable e) {
            throw new CustomException("I saved this file but I couldn't read it. I'll need help removing it later.");
        }

        if (!deleted) {
            throw new CustomException("I saved this file but I couldn't read it. I'll need help removing it later.");
        }
    }

    /**
     * Initialize
     *
     * @param attachment Attachment
     *
     * @throws CustomException If file is not a CSV
     *                         If error it thrown while building BufferedReader
     *                         If error is thrown while building CSVParser
     */
    private void init(@NotNull Attachment attachment) throws CustomException
    {
        String fileName = attachment.getFileName();
        if (!fileName.endsWith("csv")) {
            throw new CustomException("Please submit as csv file instead.");
        }

        file = new File(fileName);
        CSVFormat format = CSVFormat.EXCEL.withHeader();
        attachment.download(file);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            parser = new CSVParser(reader, format);
        } catch (Throwable e) {
            delete();
            throw new CustomException("I screwed up somewhere, I don't know what you can do...");
        }
    }
}
