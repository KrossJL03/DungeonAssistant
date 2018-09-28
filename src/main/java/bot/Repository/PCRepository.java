package bot.Repository;

import bot.Entity.PlayerCharacter;
import bot.Exception.NoCharacterFoundException;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;

public class PCRepository {

    private ArrayList<PlayerCharacter> collection;

    public PCRepository() {
        this.collection = new ArrayList<>();
    }

    public void addPC(PlayerCharacter newPlayerCharacter) {
        for (PlayerCharacter playerCharacter : collection) {
            if (playerCharacter.getName().compareTo(newPlayerCharacter.getName()) > 0) {
                this.collection.add(this.collection.indexOf(playerCharacter), newPlayerCharacter);
                this.collection.indexOf(playerCharacter);
                return;
            }
        }
        this.collection.add(newPlayerCharacter);
    }

    public ArrayList<PlayerCharacter> getAllPC() {
        return this.collection;
    }

    public ArrayList<PlayerCharacter> getAllMyPCs(User owner) {
        ArrayList<PlayerCharacter> myCharacters = new ArrayList<>();
        for (PlayerCharacter playerCharacter : collection) {
            if (playerCharacter.getOwner().equals(owner)) {
                myCharacters.add(playerCharacter);
            }
        }
        return myCharacters;
    }

    public PlayerCharacter getMyPC(User owner, String name) {
        String nameLower = name.toLowerCase();
        for (PlayerCharacter playerCharacter : this.getAllMyPCs(owner)) {
            if (playerCharacter.getName().toLowerCase().equals(nameLower)) {
                return playerCharacter;
            }
        }
        throw new NoCharacterFoundException();
    }
}
