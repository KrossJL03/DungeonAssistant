package bot.Exception;

public class DungeonMasterNotFoundException extends RuntimeException implements EncounterException {
    public DungeonMasterNotFoundException() {
        super("How can we play without a DungeonMaster? I don't see that role anywhere...");
    }
}
