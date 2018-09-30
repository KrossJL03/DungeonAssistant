import bot.*;
import bot.Entity.Hostile;
import bot.Repository.HostileRepository;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] arguments) throws Exception {
        EncounterContext       encounterContext       = new EncounterContext();
        EncounterLoggerContext encounterLoggerContext = new EncounterLoggerContext();
        HostileRepository      hostileRepository      = new HostileRepository();

        EncounterLogger encounterLogger = new EncounterLogger(encounterLoggerContext);

        EncounterManager encountermanager = new EncounterManager(
            encounterContext,
            encounterLogger,
            encounterLoggerContext,
            hostileRepository
        );

        CommandManager commandManager = new CommandManager(
            encountermanager,
            new EncyclopediaLogger(),
            new PrivateLogger(),
            hostileRepository
        );

        CommandListener commandListener = new CommandListener(commandManager);

        Main.populateTestData(hostileRepository);

        Properties properties = new Properties();
        properties.load(new FileInputStream("config.properties"));
        JDA api = new JDABuilder(properties.getProperty("token")).build();
        api.addEventListener(commandListener);
    }

    private static void populateTestData(HostileRepository hostileRepository) {
        Hostile culebratu = new Hostile("Culebratu", 100, 30);
        Hostile shaman    = new Hostile("CulebratuShaman", 250, 30);
        Hostile volpup    = new Hostile("Volpup", 100, 15);
        Hostile volpire   = new Hostile("Volpire", 200, 30);

        culebratu.setLoot(2, "Culebratu Feeler", 2);
        culebratu.setLoot(3, "Culebratu Feeler", 2);
        culebratu.setLoot(4, "Culebratu Feeler", 2);
        culebratu.setLoot(5, "Culebratu Claw", 1);
        culebratu.setLoot(6, "Culebratu Claw", 1);
        culebratu.setLoot(7, "Culebratu Scale", 1);
        culebratu.setLoot(8, "Culebratu Scale", 1);
        culebratu.setLoot(9, "Culebratu Venom", 1);
        culebratu.setLoot(10, "Culebratu Venom", 1);

        volpup.setLoot(7, "Leather Hide", 1);
        volpup.setLoot(8, "Leather Hide", 1);
        volpup.setLoot(9, "Leather Hide", 1);
        volpup.setLoot(10, "Beast Fang", 1);

        shaman.setLoot(1, "Culebratu Feeler", 4);
        shaman.setLoot(2, "Culebratu Feeler", 4);
        shaman.setLoot(3, "Culebratu Feeler", 4);
        shaman.setLoot(4, "Culebratu Claw", 2);
        shaman.setLoot(5, "Culebratu Claw", 2);
        shaman.setLoot(6, "Culebratu Scale", 2);
        shaman.setLoot(7, "Culebratu Scale", 2);
        shaman.setLoot(8, "Culebratu Venom", 2);
        shaman.setLoot(9, "Culebratu Venom", 2);
        shaman.setLoot(10, "Shaman Skull", 1);

        volpire.setLoot(1, "Leather Hide", 5);
        volpire.setLoot(2, "Leather Hide", 5);
        volpire.setLoot(3, "Leather Hide", 5);
        volpire.setLoot(4, "Silver Necklace", 1);
        volpire.setLoot(5, "Silver Necklace", 1);
        volpire.setLoot(6, "Silver Necklace", 1);
        volpire.setLoot(7, "King's Crown", 1);
        volpire.setLoot(8, "King's Crown", 1);
        volpire.setLoot(9, "King's Crown", 1);
        volpire.setLoot(10, "Beast Fang", 2);

        hostileRepository.addHostile(culebratu);
        hostileRepository.addHostile(shaman);
        hostileRepository.addHostile(volpup);
        hostileRepository.addHostile(volpire);
    }
}