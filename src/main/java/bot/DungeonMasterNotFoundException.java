package bot;

class DungeonMasterNotFoundException extends RuntimeException implements CustomExceptionInterface {

    DungeonMasterNotFoundException() {
        super("How can we play without a DungeonMaster? I don't see that role anywhere...");
    }

}
