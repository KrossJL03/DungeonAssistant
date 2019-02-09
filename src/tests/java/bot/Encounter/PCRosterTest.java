package bot.Encounter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import bot.Encounter.EncounterData.PCEncounterData;
import bot.Encounter.Exception.*;
import bot.Player.Player;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class PCRosterTest {

    private int pcCounter = 0;

    @Test
    @DisplayName("Add PC")
    void addPcTest() {
        PCRoster                   pcRoster     = new PCRoster();
        PCEncounterData            pc1          = this.mockPCEncounterData();
        PCEncounterData            pc2          = this.mockPCEncounterData();
        PCEncounterData            pc3          = this.mockPCEncounterData();
        PCEncounterData            pc4          = this.mockPCEncounterData();
        PCEncounterData            pc5          = this.mockPCEncounterData(pc1.getOwner());
        ArrayList<PCEncounterData> expectedList = new ArrayList<>();

        // max count is not set /////////////////////////
        assertThrows(MaxZeroPlayersException.class, () -> pcRoster.addPC(pc1));
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////

        // add 1 PC when max count is set ///////////////
        pcRoster.setMaxPlayerCount(3);
        pcRoster.addPC(pc1);
        assertEquals(pc1, pcRoster.getPC(pc1.getName()));
        expectedList.add(pc1);
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////

        // add max amount of PCs ////////////////////////
        pcRoster.addPC(pc2);
        assertEquals(pc2, pcRoster.getPC(pc2.getName()));
        expectedList.add(pc2);
        assertEquals(expectedList, pcRoster.getAllPCs());

        pcRoster.addPC(pc3);
        assertEquals(pc3, pcRoster.getPC(pc3.getName()));
        expectedList.add(pc3);
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////

        // add over max amount of PCs ///////////////////
        assertThrows(PCRosterException.class, () -> pcRoster.addPC(pc4));
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.getPC(pc4.getName()));
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.getPC(pc4.getOwner()));
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////

        // add PC when a PC leaves //////////////////////
        when(pc3.isPresent()).thenReturn(false);
        pcRoster.addPC(pc4);
        assertEquals(pc4, pcRoster.getPC(pc4.getName()));
        expectedList.add(pc4);
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////

        // add the same PC twice ////////////////////////
        pcRoster.setMaxPlayerCount(5);
        assertThrows(MultiplePlayerCharactersException.class, () -> pcRoster.addPC(pc1));
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////

        // add the same PC twice ////////////////////////
        assertThrows(MultiplePlayerCharactersException.class, () -> pcRoster.addPC(pc5));
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Contains PC")
    void containsPCTest() {
        PCRoster                   pcRoster     = new PCRoster();
        PCEncounterData            pc1          = this.mockPCEncounterData();
        PCEncounterData            pc2          = this.mockPCEncounterData();

        pcRoster.setMaxPlayerCount(2);

        // does empty roster contain PC /////////////////
        assertFalse(pcRoster.containsPC(pc1.getName()));
        /////////////////////////////////////////////////

        // does roster with PC contain PC ///////////////
        pcRoster.addPC(pc1);
        assertTrue(pcRoster.containsPC(pc1.getName()));
        /////////////////////////////////////////////////

        // does roster without PC contain PC ////////////
        assertFalse(pcRoster.containsPC(pc2.getName()));
        /////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Get Active PCs")
    void getActivePCsTest() {
        PCRoster                   pcRoster     = new PCRoster();
        PCEncounterData            pc1          = this.mockPCEncounterData();
        PCEncounterData            pc2          = this.mockPCEncounterData();
        PCEncounterData            pc3          = this.mockPCEncounterData();
        ArrayList<PCEncounterData> expectedList = new ArrayList<>();

        pcRoster.setMaxPlayerCount(3);
        pcRoster.addPC(pc1);
        pcRoster.addPC(pc2);
        pcRoster.addPC(pc3);

        // all PCs are active //////////////////////////////
        when(pc1.isActive()).thenReturn(true);
        when(pc2.isActive()).thenReturn(true);
        when(pc3.isActive()).thenReturn(true);
        expectedList.add(pc1);
        expectedList.add(pc2);
        expectedList.add(pc3);
        assertEquals(expectedList, pcRoster.getActivePCs());
        assertTrue(pcRoster.hasActivePCs());
        ////////////////////////////////////////////////////

        // some PCs are active /////////////////////////////
        when(pc1.isActive()).thenReturn(false);
        when(pc2.isActive()).thenReturn(false);
        when(pc3.isActive()).thenReturn(true);
        expectedList.remove(pc1);
        expectedList.remove(pc2);
        assertEquals(expectedList, pcRoster.getActivePCs());
        assertTrue(pcRoster.hasActivePCs());
        ////////////////////////////////////////////////////

        // no PCs are active ///////////////////////////////
        when(pc3.isActive()).thenReturn(false);
        expectedList.remove(pc3);
        assertEquals(expectedList, pcRoster.getActivePCs());
        assertFalse(pcRoster.hasActivePCs());
        ////////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Get All PCs")
    void getAllPCsTest() {
        PCRoster                   pcRoster     = new PCRoster();
        PCEncounterData            pc1          = this.mockPCEncounterData();
        PCEncounterData            pc2          = this.mockPCEncounterData();
        PCEncounterData            pc3          = this.mockPCEncounterData();
        ArrayList<PCEncounterData> expectedList = new ArrayList<>();

        pcRoster.setMaxPlayerCount(3);

        pcRoster.addPC(pc1);
        expectedList.add(pc1);
        assertEquals(expectedList, pcRoster.getAllPCs());

        pcRoster.addPC(pc2);
        expectedList.add(pc2);
        assertEquals(expectedList, pcRoster.getAllPCs());

        pcRoster.addPC(pc3);
        expectedList.add(pc3);
        assertEquals(expectedList, pcRoster.getAllPCs());
    }

    @Test
    @DisplayName("Get Max Player Count")
    void getMaxPlayerCountTest() {
        PCRoster pcRoster = new PCRoster();

        pcRoster.setMaxPlayerCount(3);
        assertEquals(3, pcRoster.getMaxPlayerCount());

        pcRoster.setMaxPlayerCount(10);
        assertEquals(10, pcRoster.getMaxPlayerCount());

        pcRoster.setMaxPlayerCount(2);
        assertEquals(2, pcRoster.getMaxPlayerCount());
    }

    @Test
    @DisplayName("Get PC")
    void getPcTest() {
        PCRoster                   pcRoster     = new PCRoster();
        PCEncounterData            pc1          = this.mockPCEncounterData();
        PCEncounterData            pc2          = this.mockPCEncounterData();
        ArrayList<PCEncounterData> expectedList = new ArrayList<>();

        // get a PC from an empty roster ////////////////
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.getPC(pc1.getName()));
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.getPC(pc1.getOwner()));
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////

        // get a PC that is in the roster ///////////////
        pcRoster.setMaxPlayerCount(1);
        pcRoster.addPC(pc1);
        assertEquals(pc1, pcRoster.getPC(pc1.getName()));
        expectedList.add(pc1);
        assertEquals(expectedList, pcRoster.getAllPCs());

        assertEquals(pc1, pcRoster.getPC(pc1.getName()));
        assertEquals(pc1, pcRoster.getPC(pc1.getOwner()));
        /////////////////////////////////////////////////

        // get a PC that is not in the roster ////////////
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.getPC(pc2.getName()));
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.getPC(pc2.getOwner()));
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Player Has Left")
    void playerHasLeftTest() {
        PCRoster        pcRoster = new PCRoster();
        PCEncounterData pc       = this.mockPCEncounterData();
        Player          p        = pc.getOwner();

        pcRoster.setMaxPlayerCount(3);

        // player attempts to leave that was not in the roster //
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.playerHasLeft(p));
        /////////////////////////////////////////////////////////

        // player attempts to leave that was not present //
        pcRoster.addPC(pc);
        when(pc.isPresent()).thenReturn(false);
        assertThrows(PlayerCharacterPresentException.class, () -> pcRoster.playerHasLeft(p));
        ///////////////////////////////////////////////////

        // player attempts to leave that was present //
        when(pc.isPresent()).thenReturn(true);
        pcRoster.playerHasLeft(p);
        verify(pc, times(1)).leave();
        ///////////////////////////////////////////////
    }

    @Test
    @DisplayName("Player Has Rejoined")
    void playerHasRejoinedTest() {
        PCRoster        pcRoster = new PCRoster();
        PCEncounterData pc1       = this.mockPCEncounterData();
        PCEncounterData pc2       = this.mockPCEncounterData();
        Player          p        = pc1.getOwner();

        pcRoster.setMaxPlayerCount(1);

        // player attempts to rejoin that was not in the roster //
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.playerHasRejoined(p));
        //////////////////////////////////////////////////////////

        // player attempts to rejoin that was present //
        pcRoster.addPC(pc1);
        when(pc1.isPresent()).thenReturn(true);
        assertThrows(PlayerCharacterPresentException.class, () -> pcRoster.playerHasRejoined(p));
        ////////////////////////////////////////////////

        // player attempts to rejoin when roster is full //
        when(pc1.isPresent()).thenReturn(false);
        pcRoster.addPC(pc2);
        when(pc2.isPresent()).thenReturn(true);
        assertThrows(PCRosterException.class, () -> pcRoster.playerHasRejoined(p));
        ///////////////////////////////////////////////////

        // player attempts to rejoin //
        when(pc2.isPresent()).thenReturn(false);
        pcRoster.playerHasRejoined(p);
        verify(pc1, times(1)).rejoin();
        ///////////////////////////////////////////////
    }

    @Test
    @DisplayName("Set Max Player Count")
    void setMaxPlayerCountTest() {
        PCRoster        pcRoster = new PCRoster();
        PCEncounterData pc1      = this.mockPCEncounterData();
        PCEncounterData pc2      = this.mockPCEncounterData();
        PCEncounterData pc3      = this.mockPCEncounterData();


        // set max player count ///////////////////////////////
        pcRoster.setMaxPlayerCount(3);
        assertEquals(3, pcRoster.getMaxPlayerCount());
        ///////////////////////////////////////////////////////

        // set max player count to less than 1 ////////////////
        assertThrows(PCRosterException.class, () -> pcRoster.setMaxPlayerCount(0));
        ///////////////////////////////////////////////////////

        // set max player count to more than current active players //
        pcRoster.addPC(pc1);
        pcRoster.addPC(pc2);
        pcRoster.addPC(pc3);
        pcRoster.setMaxPlayerCount(5);
        assertEquals(5, pcRoster.getMaxPlayerCount());
        //////////////////////////////////////////////////////////////

        // set max player count to less than current active players //
        assertThrows(PCRosterException.class, () -> pcRoster.setMaxPlayerCount(2));
        //////////////////////////////////////////////////////////////
    }

    @Test
    @DisplayName("Sort")
    void sortTest() {
        PCRoster                   pcRoster     = new PCRoster();
        PCEncounterData            pc1          = this.mockPCEncounterData();
        PCEncounterData            pc2          = this.mockPCEncounterData();
        PCEncounterData            pc3          = this.mockPCEncounterData();
        ArrayList<PCEncounterData> expectedList = new ArrayList<>();

        pcRoster.setMaxPlayerCount(5);

        pcRoster.addPC(pc1);
        pcRoster.addPC(pc2);
        pcRoster.addPC(pc3);
        expectedList.add(pc1);
        expectedList.add(pc2);
        expectedList.add(pc3);
        assertEquals(expectedList, pcRoster.getAllPCs());

        when(pc1.getAgility()).thenReturn(5);
        when(pc2.getAgility()).thenReturn(5);
        when(pc3.getAgility()).thenReturn(15);
        expectedList.sort(new PCAgilityComparator());

        pcRoster.sort();
        assertEquals(expectedList, pcRoster.getAllPCs());
    }

    @Test
    @DisplayName("Remove PC")
    void removePcTest() {
        PCRoster                   pcRoster     = new PCRoster();
        PCEncounterData            pc           = this.mockPCEncounterData();
        ArrayList<PCEncounterData> expectedList = new ArrayList<>();

        pcRoster.setMaxPlayerCount(2);

        // remove a PC that is not in the roster ////////
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.remove(pc));
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////

        // remove a PC that is in the roster ////////////
        pcRoster.addPC(pc);
        assertEquals(pc, pcRoster.getPC(pc.getName()));
        expectedList.add(pc);
        assertEquals(expectedList, pcRoster.getAllPCs());

        pcRoster.remove(pc);
        expectedList.remove(pc);
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////

        // remove a PC twice ////////////////////////////
        assertThrows(EncounterDataNotFoundException.class, () -> pcRoster.remove(pc));
        assertEquals(expectedList, pcRoster.getAllPCs());
        /////////////////////////////////////////////////
    }

    /**
     * Mock PCEncounterData
     *
     * @return PCEncounterData
     */
    @NotNull
    private PCEncounterData mockPCEncounterData() {
        PCEncounterData mock      = mock(PCEncounterData.class);
        Player          mockOwner = mock(Player.class);
        String          name      = "Name" + pcCounter;
        when(mock.getName()).thenReturn(name);
        when(mock.isName(name)).thenReturn(true);
        when(mock.getOwner()).thenReturn(mockOwner);
        when(mock.isOwner(mockOwner)).thenReturn(true);
        when(mock.isPresent()).thenReturn(true);
        pcCounter++;
        return mock;
    }

    /**
     * Mock PCEncounterData
     *
     * @return PCEncounterData
     */
    @NotNull
    private PCEncounterData mockPCEncounterData(@NotNull Player owner) {
        PCEncounterData mock = mock(PCEncounterData.class);
        String          name = "Name" + pcCounter;
        when(mock.getName()).thenReturn(name);
        when(mock.getOwner()).thenReturn(owner);
        when(mock.isOwner(owner)).thenReturn(true);
        pcCounter++;
        return mock;
    }
}