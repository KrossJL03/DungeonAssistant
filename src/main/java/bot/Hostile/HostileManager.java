package bot.Hostile;

import bot.Hostile.Exception.HostileNotFoundException;

import java.util.ArrayList;

public class HostileManager {

    public HostileManager() {}

    public static void createHostile(String species, int dangerLevel, int hitpoints, int attackDice) {
        Hostile existingHostile = HostileRepository.getHostile(species);
        if (existingHostile != null) {
            System.out.println("i failed you");
            return;
            // todo that hostile species already exists. Use `$updateHostile` to update their information
        }
        HostileRepository.insertHostile(species, dangerLevel, hitpoints, attackDice);
        int hostileId = HostileRepository.getHostileId(species);
        LootRepository.initializeLoot(hostileId);
    }

    public static Hostile getHostile(String species) {
        Hostile hostile = HostileRepository.getHostile(species);
        if (hostile == null) {
            throw HostileNotFoundException.createNotInDatabase(species);
        }
        return hostile;
    }

    static void setLoot(String hostileSpecies, int diceRoll, String itemName, int quantity) {
        int hostileId = HostileRepository.getHostileId(hostileSpecies);
        if (hostileId < 0) {
            throw HostileNotFoundException.createNotInDatabase(hostileSpecies);
        }
        LootRepository.insertLoot(hostileId, diceRoll, itemName, quantity);
    }

    public static void updateHostile(String species, int dangerLevel, int hitpoints, int attackDice) {
        Hostile existingHostile = HostileRepository.getHostile(species);
        if (existingHostile == null) {
            System.out.println("i failed you");
            return;
            //todo hostile does not exist, use the `$create` command instead
        }
        HostileRepository.updateHostile(species, dangerLevel, hitpoints, attackDice);
    }

    public static void main(String[] args) {
        ArrayList<Loot> lootList = LootRepository.getLootList("Slime");
    }

}
