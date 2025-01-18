package Items;

public interface Priced {

    float getPrice();

    float getDiscountPercent(DiscountRate rate);

    float getDiscountPercent();

    float getDiscount(DiscountRate rate);

    float getDiscount();

    float getReducedPrice(DiscountRate rate);

    float getReducedPrice();

}
