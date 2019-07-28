package bot.Hostile;

import bot.Hostile.Exception.HostileNotFoundException;

public class HostileManager {

    public HostileManager() {}

    static void createHostile(String species, int dangerLevel, int hitpoints, int attackDice) {
        HostileRepository.insertHostile(species, dangerLevel, hitpoints, attackDice);
        int hostileId = HostileRepository.getHostileId(species);
        LootRepository.initializeLoot(hostileId);
    }

    public static Hostile getHostile(String species) {
        return HostileRepository.getHostile(species);
    }

    static void setLoot(String hostileSpecies, int diceRoll, String itemName, int quantity) {
        int hostileId = HostileRepository.getHostileId(hostileSpecies);
        if (hostileId < 0) {
            throw HostileNotFoundException.createNotInDatabase(hostileSpecies);
        }
        LootRepository.insertLoot(hostileId, diceRoll, itemName, quantity);
    }

}
