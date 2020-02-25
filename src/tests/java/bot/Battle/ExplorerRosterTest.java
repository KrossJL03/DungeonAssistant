package bot.Battle;

import bot.Battle.HostileEncounter.EncounteredExplorer;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExplorerRosterTest
{
    private int explorerCounter = 0;

    @Test
    @DisplayName("Add Explorer")
    void addExplorerTest()
    {
        ExplorerRoster            explorerRoster = new ExplorerRoster();
        CombatExplorer            explorer1      = mockCombatExplorer();
        CombatExplorer            explorer2      = mockCombatExplorer();
        CombatExplorer            explorer3      = mockCombatExplorer();
        CombatExplorer            explorer4      = mockCombatExplorer();
        CombatExplorer            explorer5      = this.mockCombatExplorer(explorer1.getOwner());
        ArrayList<CombatExplorer> expectedList   = new ArrayList<>();

        // max count is not set /////////////////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.addExplorer(explorer1));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // add 1 explorer when max count is set ///////////////
        explorerRoster.setMaxPartySize(3);
        explorerRoster.addExplorer(explorer1);
        assertEquals(explorer1, explorerRoster.getExplorer(explorer1.getName()));
        expectedList.add(explorer1);
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // add max amount of explorers ////////////////////////
        explorerRoster.addExplorer(explorer2);
        assertEquals(explorer2, explorerRoster.getExplorer(explorer2.getName()));
        expectedList.add(explorer2);
        assertEquals(expectedList, explorerRoster.getAllExplorers());

        explorerRoster.addExplorer(explorer3);
        assertEquals(explorer3, explorerRoster.getExplorer(explorer3.getName()));
        expectedList.add(explorer3);
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // add over max amount of explorers ///////////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.addExplorer(explorer4));
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.getExplorer(explorer4.getName()));
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.getExplorer(explorer4.getOwner()));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // add explorer when an explorer leaves //////////////////////
        when(explorer3.isPresent()).thenReturn(false);
        explorerRoster.addExplorer(explorer4);
        assertEquals(explorer4, explorerRoster.getExplorer(explorer4.getName()));
        expectedList.add(explorer4);
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // add the same explorer twice ////////////////////////
        explorerRoster.setMaxPartySize(5);
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.addExplorer(explorer1));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // add the same explore twice ////////////////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.addExplorer(explorer5));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Get Active Explorers")
    void getActiveExplorersTest()
    {
        ExplorerRoster            explorerRoster = new ExplorerRoster();
        CombatExplorer            explorer1      = mockCombatExplorer();
        CombatExplorer            explorer2      = mockCombatExplorer();
        CombatExplorer            explorer3      = mockCombatExplorer();
        ArrayList<CombatExplorer> expectedList   = new ArrayList<>();

        explorerRoster.setMaxPartySize(3);
        explorerRoster.addExplorer(explorer1);
        explorerRoster.addExplorer(explorer2);
        explorerRoster.addExplorer(explorer3);

        // all explorers are active //////////////////////////////
        when(explorer1.isActive()).thenReturn(true);
        when(explorer2.isActive()).thenReturn(true);
        when(explorer3.isActive()).thenReturn(true);
        expectedList.add(explorer1);
        expectedList.add(explorer2);
        expectedList.add(explorer3);
        assertEquals(expectedList, explorerRoster.getActiveExplorers());
        assertTrue(explorerRoster.hasAtLeastOneActiveExplorer());
        ////////////////////////////////////////////////////

        // some exlorers are active /////////////////////////////
        when(explorer1.isActive()).thenReturn(false);
        when(explorer2.isActive()).thenReturn(false);
        when(explorer3.isActive()).thenReturn(true);
        expectedList.remove(explorer1);
        expectedList.remove(explorer2);
        assertEquals(expectedList, explorerRoster.getActiveExplorers());
        assertTrue(explorerRoster.hasAtLeastOneActiveExplorer());
        ////////////////////////////////////////////////////

        // no explorers are active ///////////////////////////////
        when(explorer3.isActive()).thenReturn(false);
        expectedList.remove(explorer3);
        assertEquals(expectedList, explorerRoster.getActiveExplorers());
        assertFalse(explorerRoster.hasAtLeastOneActiveExplorer());
        ////////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Get All Explorers")
    void getAllExplorersTest()
    {
        ExplorerRoster            explorerRoster = new ExplorerRoster();
        CombatExplorer            explorer1      = mockCombatExplorer();
        CombatExplorer            explorer2      = mockCombatExplorer();
        CombatExplorer            explorer3      = mockCombatExplorer();
        ArrayList<CombatExplorer> expectedList   = new ArrayList<>();

        explorerRoster.setMaxPartySize(3);

        explorerRoster.addExplorer(explorer1);
        expectedList.add(explorer1);
        assertEquals(expectedList, explorerRoster.getAllExplorers());

        explorerRoster.addExplorer(explorer2);
        expectedList.add(explorer2);
        assertEquals(expectedList, explorerRoster.getAllExplorers());

        explorerRoster.addExplorer(explorer3);
        expectedList.add(explorer3);
        assertEquals(expectedList, explorerRoster.getAllExplorers());
    }

    @Test
    @DisplayName("Get Explorer")
    void getExplorerTest()
    {
        ExplorerRoster            explorerRoster = new ExplorerRoster();
        CombatExplorer            explorer1      = mockCombatExplorer();
        CombatExplorer            explorer2      = mockCombatExplorer();
        ArrayList<CombatExplorer> expectedList   = new ArrayList<>();

        // get an explorer from an empty roster ////////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.getExplorer(explorer1.getName()));
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.getExplorer(explorer1.getOwner()));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // get an explorer that is in the roster ///////////////
        explorerRoster.setMaxPartySize(1);
        explorerRoster.addExplorer(explorer1);
        assertEquals(explorer1, explorerRoster.getExplorer(explorer1.getName()));
        expectedList.add(explorer1);
        assertEquals(expectedList, explorerRoster.getAllExplorers());

        assertEquals(explorer1, explorerRoster.getExplorer(explorer1.getName()));
        assertEquals(explorer1, explorerRoster.getExplorer(explorer1.getOwner()));
        /////////////////////////////////////////////////

        // get an explorer that is not in the roster ////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.getExplorer(explorer2.getName()));
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.getExplorer(explorer2.getOwner()));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Get Max Player Count")
    void getMaxPlayerCountTest()
    {
        ExplorerRoster explorerRoster = new ExplorerRoster();

        explorerRoster.setMaxPartySize(3);
        assertEquals(3, explorerRoster.getMaxPartySize());

        explorerRoster.setMaxPartySize(10);
        assertEquals(10, explorerRoster.getMaxPartySize());

        explorerRoster.setMaxPartySize(2);
        assertEquals(2, explorerRoster.getMaxPartySize());
    }

    @Test
    @DisplayName("Player Has Left")
    void playerHasLeftTest()
    {
        ExplorerRoster explorerRoster = new ExplorerRoster();
        CombatExplorer explorer       = mockCombatExplorer();
        Player         p              = explorer.getOwner();

        explorerRoster.setMaxPartySize(3);

        // player attempts to leave that was not in the roster //
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.markAsLeft(p));
        /////////////////////////////////////////////////////////

        // player attempts to leave that was not present //
        explorerRoster.addExplorer(explorer);
        when(explorer.isPresent()).thenReturn(false);
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.markAsLeft(p));
        ///////////////////////////////////////////////////

        // player attempts to leave that was present //
        when(explorer.isPresent()).thenReturn(true);
        explorerRoster.markAsLeft(p);
        verify(explorer, times(1)).markAsNotPresent();
        ///////////////////////////////////////////////
    }

    @Test
    @DisplayName("Player Has Rejoined")
    void playerHasRejoinedTest()
    {
        ExplorerRoster explorerRoster = new ExplorerRoster();
        CombatExplorer explorer1      = mockCombatExplorer();
        CombatExplorer explorer2      = mockCombatExplorer();
        Player         p              = explorer1.getOwner();

        explorerRoster.setMaxPartySize(1);

        // player attempts to rejoin that was not in the roster //
//        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.rejoin(p));
        //////////////////////////////////////////////////////////

        // player attempts to rejoin that was present //
        explorerRoster.addExplorer(explorer1);
        when(explorer1.isPresent()).thenReturn(true);
//        assertThrows(ExplorerRosterException.class, () -> explorerRoster.rejoin(p));
        ////////////////////////////////////////////////

        // player attempts to rejoin when roster is full //
        when(explorer1.isPresent()).thenReturn(false);
        explorerRoster.addExplorer(explorer2);
        when(explorer2.isPresent()).thenReturn(true);
//        assertThrows(ExplorerRosterException.class, () -> explorerRoster.rejoin(p));
        ///////////////////////////////////////////////////

        // player attempts to rejoin //
        when(explorer2.isPresent()).thenReturn(false);
//        explorerRoster.rejoin(p);
        verify(explorer1, times(1)).markAsPresent();
        ///////////////////////////////////////////////
    }

    @Test
    @DisplayName("Remove Explorer")
    void removeExplorerTest()
    {
        ExplorerRoster            explorerRoster = new ExplorerRoster();
        CombatExplorer            explorer       = mockCombatExplorer();
        ArrayList<CombatExplorer> expectedList   = new ArrayList<>();

        explorerRoster.setMaxPartySize(2);

        // remove an explorer that is not in the roster ////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.remove(explorer));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // remove an explorer that is in the roster ////////////
        explorerRoster.addExplorer(explorer);
        assertEquals(explorer, explorerRoster.getExplorer(explorer.getName()));
        expectedList.add(explorer);
        assertEquals(expectedList, explorerRoster.getAllExplorers());

        explorerRoster.remove(explorer);
        expectedList.remove(explorer);
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // remove an explorer twice ////////////////////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.remove(explorer));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Set Max Player Count")
    void setMaxPlayerCountTest()
    {
        ExplorerRoster explorerRoster = new ExplorerRoster();
        CombatExplorer explorer1      = mockCombatExplorer();
        CombatExplorer explorer2      = mockCombatExplorer();
        CombatExplorer explorer3      = mockCombatExplorer();

        // set max player count ///////////////////////////////
        explorerRoster.setMaxPartySize(3);
        assertEquals(3, explorerRoster.getMaxPartySize());
        ///////////////////////////////////////////////////////

        // set max player count to less than 1 ////////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.setMaxPartySize(0));
        ///////////////////////////////////////////////////////

        // set max player count to more than current active players //
        explorerRoster.addExplorer(explorer1);
        explorerRoster.addExplorer(explorer2);
        explorerRoster.addExplorer(explorer3);
        explorerRoster.setMaxPartySize(5);
        assertEquals(5, explorerRoster.getMaxPartySize());
        //////////////////////////////////////////////////////////////

        // set max player count to less than current active players //
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.setMaxPartySize(2));
        //////////////////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Sort")
    void sortTest()
    {
        ExplorerRoster            explorerRoster = new ExplorerRoster();
        CombatExplorer            explorer1      = mockCombatExplorer();
        CombatExplorer            explorer2      = mockCombatExplorer();
        CombatExplorer            explorer3      = mockCombatExplorer();
        ArrayList<CombatExplorer> expectedList   = new ArrayList<>();

        explorerRoster.setMaxPartySize(5);

        explorerRoster.addExplorer(explorer1);
        explorerRoster.addExplorer(explorer2);
        explorerRoster.addExplorer(explorer3);
        expectedList.add(explorer1);
        expectedList.add(explorer2);
        expectedList.add(explorer3);
        assertEquals(expectedList, explorerRoster.getAllExplorers());

        when(explorer1.getAgility()).thenReturn(5);
        when(explorer2.getAgility()).thenReturn(5);
        when(explorer3.getAgility()).thenReturn(15);
        expectedList.sort(new ExplorerInitiativeComparator());

        explorerRoster.sort();
        assertEquals(expectedList, explorerRoster.getAllExplorers());
    }

    /**
     * Mock CombatExplorer
     *
     * @return CombatExplorer
     */
    @NotNull
    private CombatExplorer mockCombatExplorer()
    {
        CombatExplorer mock      = mock(CombatExplorer.class);
        Player         mockOwner = mock(Player.class);
        String         name      = "Name" + explorerCounter;
        when(mock.getName()).thenReturn(name);
        when(mock.isName(name)).thenReturn(true);
        when(mock.getOwner()).thenReturn(mockOwner);
        when(mock.isOwner(mockOwner)).thenReturn(true);
        when(mock.isPresent()).thenReturn(true);
        explorerCounter++;
        return mock;
    }

    /**
     * Mock EncounteredExplorer
     *
     * @return EncounteredExplorer
     */
    @NotNull
    private EncounteredExplorer mockCombatExplorer(@NotNull Player owner)
    {
        EncounteredExplorer mock = mock(EncounteredExplorer.class);
        String              name = "Name" + explorerCounter;
        when(mock.getName()).thenReturn(name);
        when(mock.getOwner()).thenReturn(owner);
        when(mock.isOwner(owner)).thenReturn(true);
        explorerCounter++;
        return mock;
    }
}