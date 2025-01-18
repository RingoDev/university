package application;

import Items.*;

public class App {

    public static void main(String[] args) {
        Item chives = new Herb("Schnittlauch", 10, 5);
        Item sauerkraut = new Herb("Sauerkraut", 15, 10);
        Item spiderLeg = new AnimalProduct("Spinnenbein");
        Item chickenbreast = new AnimalProduct("Huehnerbrust");
        Item darkpotion = new Potion("Dunkeltrank", chives, spiderLeg);
        Item nightpotion = new Potion("Nachttrank", darkpotion, spiderLeg);
        Item superpotion = new Potion("Supertrank", nightpotion, chickenbreast, sauerkraut);


        print(chives);

        print(sauerkraut);

        print(spiderLeg);

        print(chickenbreast);

        print(darkpotion);

        print(nightpotion);

        print(superpotion);

    }

    public static void print(Item item) {
        System.out.println(item);
        System.out.println(item.getPrice() + " Geld");
        System.out.println(item.getPower());
        System.out.println(item.getCooldown());
        System.out.println(item.getReducedPrice(DiscountRate.HIGH) + " Geld");
        System.out.println(item.getReducedPrice() + " Geld");
        System.out.println();
    }
}
