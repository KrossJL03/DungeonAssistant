package bot.Battle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExplorerAgilityComparatorTest
{
    @Test
    @DisplayName("Compare: 2 explorers with different agility stats")
    void compareTest1()
    {
        ExplorerAgilityComparator comparator = new ExplorerAgilityComparator();
        CombatExplorer            explorer1  = this.mockCombatExplorer(10);
        CombatExplorer            explorer2  = this.mockCombatExplorer(9);

        assertEquals(-1, comparator.compare(explorer1, explorer2));
        assertEquals(1, comparator.compare(explorer2, explorer1));
    }

    @Test
    @DisplayName("Compare: 2 explorers with the same agility stat")
    void compareTest2()
    {
        ExplorerAgilityComparator comparator = new ExplorerAgilityComparator();
        CombatExplorer            explorer1  = this.mockCombatExplorer(10);
        CombatExplorer            explorer2  = this.mockCombatExplorer(10);

        assertEquals(0, comparator.compare(explorer1, explorer2));
        assertEquals(0, comparator.compare(explorer2, explorer1));
    }

    /**
     * Mock CombatExplorer
     *
     * @return CombatExplorer
     */
    private CombatExplorer mockCombatExplorer(int agility)
    {
        CombatExplorer mock = mock(CombatExplorer.class);
        when(mock.getAgility()).thenReturn(agility);

        return mock;
    }
}
