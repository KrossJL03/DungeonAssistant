package bot.Encounter;

/**
 * Null Object Pattern implementation of Encounter
 */
public class NullEncounter implements EncounterInterface
{
    @Override
    public boolean isNull()
    {
        return true;
    }

    @Override
    public boolean isOver()
    {
        return false;
    }
}
