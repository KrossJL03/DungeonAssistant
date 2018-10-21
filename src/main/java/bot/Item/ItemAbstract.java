package bot.Item;

import java.text.DateFormatSymbols;

public class ItemAbstract {

    private int    buyValue;
    private int    sellValue;
    private String description;
    private String image;
    private String name;
    private String shortDescription;
    private int    seasonalMonth;

    protected ItemAbstract(
        String name,
        String image,
        String description,
        String shortDescription,
        int buyValue,
        int sellValue,
        int seasonalMonth
    ) {
        this.buyValue = buyValue;
        this.description = description;
        this.image = image;
        this.name = name;
        this.sellValue = sellValue;
        this.seasonalMonth = seasonalMonth;
        this.shortDescription = shortDescription;
    }

    public int getBuyValue() {
        return buyValue;
    }

    public int getSellValue() {
        return sellValue;
    }

    public String getDescription() {
        return description;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getSeasonalMonth() {
        return this.seasonalMonth > 0 ? new DateFormatSymbols().getMonths()[this.seasonalMonth - 1] : "";
    }

    public boolean isBuyable() {
        return this.buyValue > 0;
    }

    public boolean isSeasonal() {
        return this.seasonalMonth != 0;
    }

}
