package bot;

import bot.Encounter.Exception.PCEncounterDataException;

import java.util.ArrayList;

public class Constant {

    public static final int MAX_AGILITY  = 20;
    public static final int MAX_DEFENSE  = 20;
    public static final int MAX_MAX_HP   = 240;
    public static final int MAX_STRENGTH = 20;
    public static final int MAX_WISDOM   = 20;

    public static final int MIN_MAX_HP = 50;

    public static final int HP_STAT_MULTIPLIER = 10;

    public static final String STAT_AGILITY  = "agility";
    public static final String STAT_DEFENSE  = "defense";
    public static final String STAT_MAX_HP   = "maxHp";
    public static final String STAT_STRENGTH = "strength";
    public static final String STAT_WISDOM   = "wisdom";

    public static int getStatMax(String statName) {
        switch (statName) {
            case Constant.STAT_AGILITY:
                return MAX_AGILITY;
            case Constant.STAT_DEFENSE:
                return MAX_DEFENSE;
            case Constant.STAT_MAX_HP:
                return MAX_MAX_HP;
            case Constant.STAT_STRENGTH:
                return MAX_STRENGTH;
            case Constant.STAT_WISDOM:
                return MAX_WISDOM;
            default:
                throw PCEncounterDataException.invalidStatName(statName);
        }
    }

    private static ArrayList<String> getStatNames() {
        return new ArrayList<String>() {{
            add(STAT_AGILITY);
            add(STAT_DEFENSE);
            add(STAT_MAX_HP);
            add(STAT_STRENGTH);
            add(STAT_WISDOM);
        }};
    }

    public static boolean isStatName(String name) {
        return getStatNames().contains(name);
    }
}