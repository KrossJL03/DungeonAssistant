package bot.Item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class Equipment extends ItemAbstract
{
    final static String ITEM_TYPE = "EQUIPMENT";

    static String FIELD_ARMOR_SET_NAME = "armorSetName";
    static String FIELD_STAT_VALUE_AGI = "agiStatValue";
    static String FIELD_STAT_VALUE_DEF = "defStatValue";
    static String FIELD_STAT_VALUE_HP  = "hpStatValue";
    static String FIELD_STAT_VALUE_STR = "strStatValue";
    static String FIELD_STAT_VALUE_WIS = "wisStatValue";

    private int    agiStatValue;
    private String armorSetName;
    private int    defStatValue;
    private int    hpStatValue;
    private int    strStatValue;
    private int    wisStatValue;

    /**
     * Types of equipment
     */
    enum EquipmentType
    {
        EYEWEAR,
        MASK,
        HEADGEAR,
        EARRINGS,
        SHOULDER_PIECE,
        RING,
        PENDANT,
        CHEST_PIECE,
        BELT,
        PANTS,
        FOOTWEAR,
        GLOVES,
        CAPE,
        REAR_GUARD,
        SHIELD,
        WEAPON
    }

    /**
     * Constructor.
     *
     * @param name             Name
     * @param subtype          Equipment type
     * @param imageUrl         Image url
     * @param buyValue         Buy value
     * @param sellValue        Sell value
     * @param isBuyable        Is the item buyable
     * @param isCraftable      Is the item craftable
     * @param isEventExclusive Is the item event exclusive
     * @param isSeasonal       Is the item seasonal
     * @param armorSetName     Armor set name
     * @param hpStatValue      HP stat value
     * @param strStatValue     STR stat value
     * @param wisStatValue     WIS stat value
     * @param agiStatValue     AGI stat value
     * @param defStatValue     DEF stat value
     */
    Equipment(
        @NotNull String name,
        @NotNull EquipmentType subtype,
        @NotNull String imageUrl,
        int buyValue,
        int sellValue,
        boolean isBuyable,
        boolean isCraftable,
        boolean isEventExclusive,
        boolean isSeasonal,
        @Nullable String armorSetName,
        int hpStatValue,
        int strStatValue,
        int wisStatValue,
        int agiStatValue,
        int defStatValue
    )
    {
        super(
            name,
            ITEM_TYPE,
            subtype.toString(),
            imageUrl,
            buyValue,
            sellValue,
            isBuyable,
            isCraftable,
            isEventExclusive,
            isSeasonal
        );
        this.agiStatValue = agiStatValue;
        this.armorSetName = armorSetName;
        this.defStatValue = defStatValue;
        this.hpStatValue = hpStatValue;
        this.strStatValue = strStatValue;
        this.wisStatValue = wisStatValue;
    }

    /**
     * Get agi stat value
     *
     * @return int
     */
    int getAgiStatValue()
    {
        return agiStatValue;
    }

    /**
     * Get armor set name
     *
     * @return String
     */
    @Nullable String getArmorSetName()
    {
        return armorSetName;
    }

    /**
     * Get def stat value
     *
     * @return int
     */
    int getDefStatValue()
    {
        return defStatValue;
    }

    /**
     * Get hp stat value
     *
     * @return int
     */
    int getHpStatValue()
    {
        return hpStatValue;
    }

    /**
     * Get str stat value
     *
     * @return int
     */
    int getStrStatValue()
    {
        return strStatValue;
    }

    /**
     * Get wis stat value
     *
     * @return int
     */
    int getWisStatValue()
    {
        return wisStatValue;
    }
}
