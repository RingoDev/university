package com.ringodev.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringodev.factory.data.OrderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SupplierService {
    Logger logger = LoggerFactory.getLogger(SupplierService.class);
    Environment env;
    Map<String, String> suppliers;

    @Autowired
    SupplierService(Environment env) {
        this.env = env;
        if (Arrays.asList(env.getActiveProfiles()).contains("production"))
            suppliers = Map.of(
                    "supplier1", "http://suppliers:8081/supplier1",
                    "supplier2", "http://suppliers:8081/supplier2");
        else {
            suppliers = Map.of(
                    "supplier1", "http://localhost:8081/supplier1",
                    "supplier2", "http://localhost:8081/supplier2");
        }
    }


    public DiscreteOffer sendRequests(OrderImpl order) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(order);

        Map<String, Offer> offers = new HashMap<>();

        for (String supplier : suppliers.keySet()) {
            String response = null;
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(suppliers.get(supplier) + "/requestOffer"))
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> res = client.send(request,
                        HttpResponse.BodyHandlers.ofString());

                response = res.body();
            } catch (Exception e) {
                logger.warn("Couldn't get offer from supplier: " + suppliers.get(supplier));
            }

            if (response != null)
                try {
                    offers.put(supplier, objectMapper.readValue(response, Offer.class));
                } catch (JsonProcessingException e) {
                    logger.warn("Couldn't parse response from supplier: " + suppliers.get(supplier) + "\n" +
                            "Response was: " + response);
                }
        }

        // select 1 of the Offers
        DiscreteOffer bestOffer = offers.entrySet().stream().map(e -> new DiscreteOffer(e.getKey(), e.getValue())).reduce((e1, e2) -> {
            if (e1.getPrice() < e2.getPrice())
                return e1;
            else return e2;
        }).orElseThrow();

        logger.info("Chosen offer: " + bestOffer.toString());

        return bestOffer;

    }

    static class Offer {
        Offer() {
        }

        Offer(int price, long date) {
            this.price = price;
            this.date = date;
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

    static class DiscreteOffer extends Offer {

        DiscreteOffer(String supplier, Offer offer) {
            super(offer.price, offer.date);
            this.supplier = supplier;
        }

        private String supplier;

        public String getSupplier() {
            return supplier;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }

        @Override
        public String toString() {
            return "DiscreteOffer{" +
                    "price=" + price +
                    ", date=" + new Date(date).toString() +
                    ", supplier='" + supplier + '\'' +
                    '}';
        }
    }
}
