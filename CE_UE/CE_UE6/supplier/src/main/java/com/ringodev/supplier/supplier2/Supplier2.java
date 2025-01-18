package com.ringodev.supplier.supplier2;

import com.ringodev.supplier.supplier1.Supplier1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("supplier2")
public class Supplier2 {

    private final Logger logger = LoggerFactory.getLogger(Supplier2.class);

    @PostMapping("/requestOffer")
    public ResponseEntity<Object> getOffer(@RequestBody Offer offer) {

        logger.info("Supplier2 got a request");

        long random = new Random().nextInt(14) * 24 * 60 * 60 * 1000;

        Date randomDate = new Date(new Date().getTime() + 14 * 24 * 60 * 60 * 1000 + random);
        int randomPrice = 30+new Random().nextInt(40);

        return new ResponseEntity<>(new Response(randomPrice,randomDate),HttpStatus.OK);
    }

    // consists of a price and a delivery date
    static class Response {

        Response() {
        }

        Response(int price, Date date) {
            this.price = price;
            this.date = date.getTime();
        }

        int price;
        long date;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }
    }

    static class Offer {

    }
}
