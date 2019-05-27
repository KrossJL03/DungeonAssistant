package bot.Encounter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import bot.Player.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ExplorerRosterTest
{
    private int explorerCounter = 0;

    @Test
    @DisplayName("Add Explorer")
    void addExplorerTest()
    {
        ExplorerRoster                          explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface            explorer1      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer2      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer3      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer4      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer5      = this.mockEncounteredExplorerInterface(explorer1.getOwner());
        ArrayList<EncounteredExplorerInterface> expectedList   = new ArrayList<>();

        // max count is not set /////////////////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.addExplorer(explorer1));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // add 1 explorer when max count is set ///////////////
        explorerRoster.setMaxPlayerCount(3);
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
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.getExplorer(explorer4.getName()));
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.getExplorer(explorer4.getOwner()));
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
        explorerRoster.setMaxPlayerCount(5);
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.addExplorer(explorer1));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // add the same explore twice ////////////////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.addExplorer(explorer5));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Contains Explorer")
    void containsExplorersTest()
    {
        ExplorerRoster               explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface explorer1      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface explorer2      = this.mockEncounteredExplorerInterface();

        explorerRoster.setMaxPlayerCount(2);

        // does empty roster contain explorer /////////////////
        assertFalse(explorerRoster.containsExplorer(explorer1.getName()));
        /////////////////////////////////////////////////

        // does roster with explorer contain explorer ///////////////
        explorerRoster.addExplorer(explorer1);
        assertTrue(explorerRoster.containsExplorer(explorer1.getName()));
        /////////////////////////////////////////////////

        // does roster without explorer contain explorer ////////////
        assertFalse(explorerRoster.containsExplorer(explorer2.getName()));
        /////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Get Active Explorers")
    void getActiveExplorersTest()
    {
        ExplorerRoster                          explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface            explorer1      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer2      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer3      = this.mockEncounteredExplorerInterface();
        ArrayList<EncounteredExplorerInterface> expectedList   = new ArrayList<>();

        explorerRoster.setMaxPlayerCount(3);
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
        assertTrue(explorerRoster.hasActiveExplorers());
        ////////////////////////////////////////////////////

        // some exlorers are active /////////////////////////////
        when(explorer1.isActive()).thenReturn(false);
        when(explorer2.isActive()).thenReturn(false);
        when(explorer3.isActive()).thenReturn(true);
        expectedList.remove(explorer1);
        expectedList.remove(explorer2);
        assertEquals(expectedList, explorerRoster.getActiveExplorers());
        assertTrue(explorerRoster.hasActiveExplorers());
        ////////////////////////////////////////////////////

        // no explorers are active ///////////////////////////////
        when(explorer3.isActive()).thenReturn(false);
        expectedList.remove(explorer3);
        assertEquals(expectedList, explorerRoster.getActiveExplorers());
        assertFalse(explorerRoster.hasActiveExplorers());
        ////////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Get All Explorers")
    void getAllExplorersTest()
    {
        ExplorerRoster                          explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface            explorer1      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer2      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer3      = this.mockEncounteredExplorerInterface();
        ArrayList<EncounteredExplorerInterface> expectedList   = new ArrayList<>();

        explorerRoster.setMaxPlayerCount(3);

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
    @DisplayName("Get Max Player Count")
    void getMaxPlayerCountTest()
    {
        ExplorerRoster explorerRoster = new ExplorerRoster();

        explorerRoster.setMaxPlayerCount(3);
        assertEquals(3, explorerRoster.getMaxPlayerCount());

        explorerRoster.setMaxPlayerCount(10);
        assertEquals(10, explorerRoster.getMaxPlayerCount());

        explorerRoster.setMaxPlayerCount(2);
        assertEquals(2, explorerRoster.getMaxPlayerCount());
    }

    @Test
    @DisplayName("Get Explorer")
    void getExplorerTest()
    {
        ExplorerRoster                          explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface            explorer1      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer2      = this.mockEncounteredExplorerInterface();
        ArrayList<EncounteredExplorerInterface> expectedList   = new ArrayList<>();

        // get an explorer from an empty roster ////////////////
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.getExplorer(explorer1.getName()));
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.getExplorer(explorer1.getOwner()));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////

        // get an explorer that is in the roster ///////////////
        explorerRoster.setMaxPlayerCount(1);
        explorerRoster.addExplorer(explorer1);
        assertEquals(explorer1, explorerRoster.getExplorer(explorer1.getName()));
        expectedList.add(explorer1);
        assertEquals(expectedList, explorerRoster.getAllExplorers());

        assertEquals(explorer1, explorerRoster.getExplorer(explorer1.getName()));
        assertEquals(explorer1, explorerRoster.getExplorer(explorer1.getOwner()));
        /////////////////////////////////////////////////

        // get an explorer that is not in the roster ////////////
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.getExplorer(explorer2.getName()));
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.getExplorer(explorer2.getOwner()));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Player Has Left")
    void playerHasLeftTest()
    {
        ExplorerRoster               explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface explorer       = this.mockEncounteredExplorerInterface();
        Player                       p              = explorer.getOwner();

        explorerRoster.setMaxPlayerCount(3);

        // player attempts to leave that was not in the roster //
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.leave(p));
        /////////////////////////////////////////////////////////

        // player attempts to leave that was not present //
        explorerRoster.addExplorer(explorer);
        when(explorer.isPresent()).thenReturn(false);
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.leave(p));
        ///////////////////////////////////////////////////

        // player attempts to leave that was present //
        when(explorer.isPresent()).thenReturn(true);
        explorerRoster.leave(p);
        verify(explorer, times(1)).leave();
        ///////////////////////////////////////////////
    }

    @Test
    @DisplayName("Player Has Rejoined")
    void playerHasRejoinedTest()
    {
        ExplorerRoster               explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface explorer1      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface explorer2      = this.mockEncounteredExplorerInterface();
        Player                       p              = explorer1.getOwner();

        explorerRoster.setMaxPlayerCount(1);

        // player attempts to rejoin that was not in the roster //
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.rejoin(p));
        //////////////////////////////////////////////////////////

        // player attempts to rejoin that was present //
        explorerRoster.addExplorer(explorer1);
        when(explorer1.isPresent()).thenReturn(true);
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.rejoin(p));
        ////////////////////////////////////////////////

        // player attempts to rejoin when roster is full //
        when(explorer1.isPresent()).thenReturn(false);
        explorerRoster.addExplorer(explorer2);
        when(explorer2.isPresent()).thenReturn(true);
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.rejoin(p));
        ///////////////////////////////////////////////////

        // player attempts to rejoin //
        when(explorer2.isPresent()).thenReturn(false);
        explorerRoster.rejoin(p);
        verify(explorer1, times(1)).rejoin();
        ///////////////////////////////////////////////
    }

    @Test
    @DisplayName("Set Max Player Count")
    void setMaxPlayerCountTest()
    {
        ExplorerRoster               explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface explorer1      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface explorer2      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface explorer3      = this.mockEncounteredExplorerInterface();


        // set max player count ///////////////////////////////
        explorerRoster.setMaxPlayerCount(3);
        assertEquals(3, explorerRoster.getMaxPlayerCount());
        ///////////////////////////////////////////////////////

        // set max player count to less than 1 ////////////////
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.setMaxPlayerCount(0));
        ///////////////////////////////////////////////////////

        // set max player count to more than current active players //
        explorerRoster.addExplorer(explorer1);
        explorerRoster.addExplorer(explorer2);
        explorerRoster.addExplorer(explorer3);
        explorerRoster.setMaxPlayerCount(5);
        assertEquals(5, explorerRoster.getMaxPlayerCount());
        //////////////////////////////////////////////////////////////

        // set max player count to less than current active players //
        assertThrows(ExplorerRosterException.class, () -> explorerRoster.setMaxPlayerCount(2));
        //////////////////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Sort")
    void sortTest()
    {
        ExplorerRoster                          explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface            explorer1      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer2      = this.mockEncounteredExplorerInterface();
        EncounteredExplorerInterface            explorer3      = this.mockEncounteredExplorerInterface();
        ArrayList<EncounteredExplorerInterface> expectedList   = new ArrayList<>();

        explorerRoster.setMaxPlayerCount(5);

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
        expectedList.sort(new ExplorerAgilityComparator());

        explorerRoster.sort();
        assertEquals(expectedList, explorerRoster.getAllExplorers());
    }

    @Test
    @DisplayName("Remove Explorer")
    void removeExplorerTest()
    {
        ExplorerRoster                          explorerRoster = new ExplorerRoster();
        EncounteredExplorerInterface            explorer       = this.mockEncounteredExplorerInterface();
        ArrayList<EncounteredExplorerInterface> expectedList   = new ArrayList<>();

        explorerRoster.setMaxPlayerCount(2);

        // remove an explorer that is not in the roster ////////
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.remove(explorer));
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
        assertThrows(EncounteredCreatureNotFoundException.class, () -> explorerRoster.remove(explorer));
        assertEquals(expectedList, explorerRoster.getAllExplorers());
        /////////////////////////////////////////////////
    }

    /**
     * Mock EncounteredExplorerInterface
     *
     * @return EncounteredExplorerInterface
     */
    @NotNull
    private EncounteredExplorerInterface mockEncounteredExplorerInterface()
    {
        EncounteredExplorerInterface mock      = mock(EncounteredExplorerInterface.class);
        Player                       mockOwner = mock(Player.class);
        String                       name      = "Name" + explorerCounter;
        when(mock.getName()).thenReturn(name);
        when(mock.isName(name)).thenReturn(true);
        when(mock.getOwner()).thenReturn(mockOwner);
        when(mock.isOwner(mockOwner)).thenReturn(true);
        when(mock.isPresent()).thenReturn(true);
        explorerCounter++;
        return mock;
    }

    /**
     * Mock EncounteredExplorerInterface
     *
     * @return EncounteredExplorerInterface
     */
    @NotNull
    private EncounteredExplorerInterface mockEncounteredExplorerInterface(@NotNull Player owner)
    {
        EncounteredExplorerInterface mock = mock(EncounteredExplorerInterface.class);
        String                       name = "Name" + explorerCounter;
        when(mock.getName()).thenReturn(name);
        when(mock.getOwner()).thenReturn(owner);
        when(mock.isOwner(owner)).thenReturn(true);
        explorerCounter++;
        return mock;
    }
}