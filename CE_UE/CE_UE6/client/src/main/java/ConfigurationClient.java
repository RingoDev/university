import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

public class ConfigurationClient {

    enum Part {
        handlebarType, handlebarMaterial, handlebarGearshift, handleType
    }

    // throws Interrupted Exception if another thread interrupts during sleep...but there are no other threads
    public static void main(String[] args) throws InterruptedException {

        // uncomment to run program for the user
        runProgramSafely();
    }

    private static void runProgramSafely() throws InterruptedException {
        try {
            runProgram();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("The server wasn't ready for your request.");
            System.out.println("The Configuration Process will start again.\n");
            Thread.sleep(1000);
            runProgramSafely();
        }
    }

    private static void runProgram() throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("Starting a new Configuration Process\n\n");
            Map<Part, String> chosenOptions = new HashMap<>();
            for (Part part : Part.values()) {
                System.out.println("Input the type of " + part + " that you want");
                String selectedOption = printDetailedOptions(getOptions(part, chosenOptions));
                chosenOptions.put(part, selectedOption);
                System.out.println("You chose " + selectedOption + "\n");
            }
            System.out.println("==============================================");
            System.out.println("Rechecking your configuration on the server");
            System.out.println("==============================================\n");

            try {
                Order order = checkConfiguration(chosenOptions);
                System.out.println("Your Configuration was confirmed: ");
                System.out.println(order.toString());
                System.out.println("\nIf you want to order another Bicycle press 'y' otherwise enter any key to finish:");
                System.out.print("> ");
                if (!in.nextLine().toLowerCase().equals("y")) break;
            } catch (IllegalArgumentException e) {
                System.out.println("There was an Error with your configuration:");
                System.out.println(e.getMessage());
                System.out.println("\nIf you want to start another configuration process press 'y' otherwise enter any key to finish:");
                System.out.print("> ");
                if (!in.nextLine().toLowerCase().equals("y")) break;
            } catch (Exception r) {
                System.out.println("There was an error with the Connection to the Server.");
            }
        }
    }


    private static List<String> getOptions(Part part, Map<Part, String> selectedOptions) throws IOException, InterruptedException {

        StringBuilder sb = new StringBuilder("http://localhost:8080/api/" + part);

        boolean isFirst = true;
        for (Part selectedPart : selectedOptions.keySet()) {
            sb.append(isFirst ? "?" : "&").append(selectedPart).append("=").append(selectedOptions.get(selectedPart));
            isFirst = false;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(sb.toString()))
                .timeout(Duration.ofSeconds(10))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return parseStringToList(response.body());
    }


    private static Order checkConfiguration(Map<Part, String> map) throws IllegalArgumentException, IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writeValueAsString(map);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/verify"))
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) return objectMapper.readValue(response.body(), Order.class);
        else if (response.statusCode() == 400) throw new IllegalArgumentException(response.body());
        else throw new IOException();
    }


    private static String printDetailedOptions(List<String> options) {
        for (String option : options) {
            System.out.println("Enter " + options.indexOf(option) + " for " + option);
        }
        System.out.print("> ");
        String result;
        try {
            result = options.get(Integer.parseInt(new Scanner(System.in).nextLine()));
        } catch (Exception e) {
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            System.out.println("Wrong Input please try again! ");
            System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            return printDetailedOptions(options);
        }
        return result;
    }

    private static List<String> parseStringToList(String str) {
        str = str.substring(1, str.length() - 1);
        String[] array = str.split(",");
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].substring(1, array[i].length() - 1);
        }
        return List.of(array);
    }
}
