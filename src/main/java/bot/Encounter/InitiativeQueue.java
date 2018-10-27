package bot.Encounter;

import bot.Encounter.EncounterData.PCEncounterData;

import java.util.ArrayList;
import java.util.LinkedList;

class InitiativeQueue {

    private LinkedList<PCEncounterData> initiative;

    InitiativeQueue() {
        this.initiative = new LinkedList<>();
    }

    InitiativeQueue(ArrayList<PCEncounterData> playerCharacters) {
        this.initiative = new LinkedList<>();
        this.initiative.addAll(playerCharacters);
    }

    void add(PCEncounterData playerCharacter) {
        this.initiative.add(playerCharacter);
    }

    boolean contains(PCEncounterData playerCharacter) {
        return this.initiative.contains(playerCharacter);
    }

    PCEncounterData getCurrentPlayerCharacter() {
        return this.initiative.peek();
    }

    PCEncounterData getNextPlayerCharacter() {
        PCEncounterData nextPlayer = this.initiative.peek();
        while (nextPlayer != null && (nextPlayer.isSlain() || !nextPlayer.isPresent() || !nextPlayer.hasActions())) {
            this.initiative.pop();
            nextPlayer = this.initiative.peek();
        }
        return nextPlayer;
    }

    void remove(PCEncounterData playerCharacter) {
        this.initiative.remove(playerCharacter);
    }
}
