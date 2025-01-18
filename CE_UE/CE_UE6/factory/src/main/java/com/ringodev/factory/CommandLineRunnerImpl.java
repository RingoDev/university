package com.ringodev.factory;

import com.ringodev.factory.data.Restriction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(CommandLineRunnerImpl.class);

    RestrictionRepository restrictionRepository;
    OptionRepository optionRepository;

    @Autowired
    CommandLineRunnerImpl(OptionRepository optionRepository, RestrictionRepository restrictionRepository) {
        this.restrictionRepository = restrictionRepository;
        this.optionRepository = optionRepository;
    }

    @Override
    public void run(String... args) {

        logger.info("Running CommandLineRunnerImpl");

        if (restrictionRepository.count() == 0) {
            logger.info("Adding Constriants");
            addConstraints();
        }
        if (optionRepository.count() == 0) {
            logger.info("Adding Options");
            addOptions();
        }

        logger.info("Exiting CommandLineRunnerImpl");
    }

    private void addOptions() {
        this.optionRepository.save(new Option(Part.handlebarType, List.of("Flatbarlenker", "Rennradlenker", "Bullhornlenker")));
        this.optionRepository.save(new Option(Part.handlebarMaterial, List.of("Aluminium", "Stahl", "Kunststoff")));
        this.optionRepository.save(new Option(Part.handlebarGearshift, List.of("Kettenschaltung", "Nabenschaltung", "Tretlagerschaltung")));
        this.optionRepository.save(new Option(Part.handleType, List.of("Ledergriff", "Schaumstoffgriff", "Kunststoffgriff")));
    }

    private void addConstraints() {


        // Bedingung 1
        this.restrictionRepository.save(new Restriction("Flatbarlenker", "Kunststoff"));
        this.restrictionRepository.save(new Restriction("Rennradlenker", "Kunststoff"));

        // Bedingung 3
        this.restrictionRepository.save(new Restriction("Stahl", "Nabenschaltung"));
        this.restrictionRepository.save(new Restriction("Stahl", "Tretlagerschaltung"));

        // Bedingung 4
        this.restrictionRepository.save(new Restriction("Kunststoffgriff", "Stahl"));
        this.restrictionRepository.save(new Restriction("Kunststoffgriff", "Aluminium"));
        this.restrictionRepository.save(new Restriction("Ledergriff", "Flatbarlenker"));
        this.restrictionRepository.save(new Restriction("Ledergriff", "Bullhornlenker"));

    }

}
