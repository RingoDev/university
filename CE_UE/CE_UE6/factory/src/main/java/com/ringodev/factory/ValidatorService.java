package com.ringodev.factory;

import com.ringodev.factory.data.Configuration;
import com.ringodev.factory.data.Restriction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ValidatorService {

    OptionRepository optionRepository;
    RestrictionRepository restrictionRepository;

    @Autowired
    ValidatorService(OptionRepository optionRepository,RestrictionRepository restrictionRepository) {
        this.restrictionRepository = restrictionRepository;
        this.optionRepository = optionRepository;
    }

    private Map<Part,List<String>> getOptions(){
        Map<Part,List<String>> map = new HashMap<>();
        this.optionRepository.findAll().forEach( o -> map.put(o.part,o.types));
        return map;
    }

    List<String> getPossibleHandlebarTypes() {
        return this.getOptions().get(Part.handlebarType);
    }

    List<String> getPossibleMaterials(String handlebarType) {
        List<String> result = new ArrayList<>(3);
        for (String material : getOptions().get(Part.handlebarMaterial)) {
            Configuration config = new Configuration();
            config.setHandlebarType(handlebarType);
            config.setHandlebarMaterial(material);
            if (validatePartialConfiguration(config)) result.add(material);
        }
        return result;
    }

    List<String> getPossibleGearshifts(String handlebarType, String handlebarMaterial) {
        List<String> result = new ArrayList<>(3);
        for (String gearshift : getOptions().get(Part.handlebarGearshift)) {
            Configuration config = new Configuration();
            config.setHandlebarType(handlebarType);
            config.setHandlebarMaterial(handlebarMaterial);
            config.setHandlebarGearshift(gearshift);
            if (validatePartialConfiguration(config)) result.add(gearshift);
        }
        return result;
    }

    List<String> getPossibleHandles(String handlebarType, String handlebarMaterial, String handlebarGearshift) {
        List<String> result = new ArrayList<>(3);
        for (String handle : getOptions().get(Part.handleType)) {
            Configuration config = new Configuration();
            config.setHandlebarType(handlebarType);
            config.setHandlebarMaterial(handlebarMaterial);
            config.setHandlebarGearshift(handlebarGearshift);
            config.setHandleType(handle);
            if (validatePartialConfiguration(config)) result.add(handle);
        }
        return result;
    }

    // checks full Configuration
    public boolean validateFullConfiguration(Configuration configuration) {
        if (!getOptions().get(Part.handlebarType).contains(configuration.getHandlebarType())) return false;
        if (!getOptions().get(Part.handlebarMaterial).contains(configuration.getHandlebarMaterial())) return false;
        if (!getOptions().get(Part.handlebarGearshift).contains(configuration.getHandlebarGearshift())) return false;
        if (!getOptions().get(Part.handleType).contains(configuration.getHandleType())) return false;
        return validatePartialConfiguration(configuration);
    }

    // checks partial configuration against all constraints in the DB
    public boolean validatePartialConfiguration(Configuration configuration) {
        for (Restriction restriction : restrictionRepository.findAll())
            if (!restriction.validate(configuration)) {
                return false;
            }
        return true;
    }
}
