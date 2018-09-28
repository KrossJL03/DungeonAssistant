package bot.Repository;

import bot.Entity.Hostile;
import bot.Exception.NoHostileFoundException;

import java.util.ArrayList;

public class HostileRepository {

    private ArrayList<Hostile> collection;

    public HostileRepository() {
        this.collection = new ArrayList<>();
    }

    public void addHostile(Hostile hostile) {
        this.collection.add(hostile);
    }

    public ArrayList<Hostile> getAllHostiles() {
        return this.collection;
    }

    public Hostile getHostile(String speciesName) {
        String speciesLower = speciesName.toLowerCase();
        for (Hostile hostile : collection) {
            if (hostile.getSpecies().toLowerCase().equals(speciesLower)) {
                return hostile;
            }
        }
        throw new NoHostileFoundException(speciesName);
    }
}
