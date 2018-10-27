package bot.Exception;

import bot.CustomExceptionInterface;

public class DungeonMasterNotFoundException extends RuntimeException implements CustomExceptionInterface {

    public DungeonMasterNotFoundException() {
        super("How can we play without a DungeonMaster? I don't see that role anywhere...");
    }

}
