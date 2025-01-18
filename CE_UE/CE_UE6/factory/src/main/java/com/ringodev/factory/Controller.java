package com.ringodev.factory;

import com.ringodev.factory.data.Configuration;
import client.RunClient;
import com.ringodev.factory.data.OrderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api")
public class Controller {

    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    OrderRepository orderRepository;
    ValidatorService validator;
    SupplierService supplierService;
    Environment env;


    @Autowired
    Controller(Environment env,ValidatorService validator, OrderRepository orderRepository, SupplierService supplierService) {
        this.validator = validator;
        this.orderRepository = orderRepository;
        this.supplierService = supplierService;
        this.env = env;
    }

    @GetMapping("/handlebarType")
    public ResponseEntity<List<String>> getHandlebarType() {
        return new ResponseEntity<>(validator.getPossibleHandlebarTypes(), HttpStatus.OK);
    }

    @GetMapping("/handlebarMaterial")
    public ResponseEntity<List<String>> getHandlebarMaterial(@RequestParam String handlebarType) {
        if (handlebarType == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(validator.getPossibleMaterials(handlebarType), HttpStatus.OK);
    }

    @GetMapping("/handlebarGearshift")
    public ResponseEntity<List<String>> getHandlebarGearshift(@RequestParam String handlebarType, @RequestParam String handlebarMaterial) {
        if (handlebarType == null || handlebarMaterial == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(validator.getPossibleGearshifts(handlebarType, handlebarMaterial), HttpStatus.OK);
    }

    @GetMapping("/handleType")
    public ResponseEntity<List<String>> getHandleType(@RequestParam String handlebarType, @RequestParam String handlebarMaterial, @RequestParam String handlebarGearshift) {
        if (handlebarType == null || handlebarMaterial == null || handlebarGearshift == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(validator.getPossibleHandles(handlebarType, handlebarMaterial, handlebarGearshift), HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<Object> postConfiguration(@RequestBody Configuration config) throws RemoteException {

        logger.info("Got a Configuration");
        if (validator.validateFullConfiguration(config)) {
            OrderImpl myOrder = new OrderImpl(config.getHandlebarType(), config.getHandlebarMaterial(), config.getHandlebarGearshift(), config.getHandleType());
            // to set id automatically
            orderRepository.save(myOrder);

            try {
                RunClient.sendOrderToFibu(myOrder.toOrder(), Arrays.asList(env.getActiveProfiles()).contains("production") ? "accounting":"localhost");
            } catch (RemoteException e) {
                logger.warn("Couldn't submit Order to FIBU");
                logger.warn(e.getMessage());
            }

            SupplierService.DiscreteOffer chosenOffer;

            try {
                chosenOffer = supplierService.sendRequests(myOrder);
                myOrder.setPrice(chosenOffer.price);
                myOrder.setDeliveryDate(chosenOffer.date);
                orderRepository.save(myOrder);
                return new ResponseEntity<>(myOrder, HttpStatus.OK);
            } catch (Exception e) {
                logger.warn("Couldn't get Offer from Suppliers");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }


        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
