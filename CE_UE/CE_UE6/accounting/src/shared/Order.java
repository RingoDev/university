package shared;

import java.io.Serializable;
import java.text.MessageFormat;

public class Order implements Serializable {


    private long orderId;
    private final String handlebarType;
    private final String handlebarMaterial;
    private final String handlebarGearshift;
    private final String handleMaterial;

    public Order(final String handlebarType,
                 final String handlebarMaterial, final String handlebarGearshift,
                 final String handleMaterial) {
        this.handlebarType = handlebarType;
        this.handlebarMaterial = handlebarMaterial;
        this.handlebarGearshift = handlebarGearshift;
        this.handleMaterial = handleMaterial;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return this.orderId;
    }

    public String getHandlebarType() {
        return this.handlebarType;
    }

    public String getHandlebarMaterial() {
        return this.handlebarMaterial;
    }

    public String getHandlebarGearshift() {
        return this.handlebarGearshift;
    }

    public String getHandleMaterial() {
        return this.handleMaterial;
    }

    @Override
    public String toString() {
        return MessageFormat.format(
                "HandlebarConfig'{'orderId=''{0}'', handlebarType=''{1}'', handlebarMaterial=''{2}'', handlebarGearshift=''{3}'', handleMaterial=''{4}'''}'",
                orderId, handlebarType, handlebarMaterial, handlebarGearshift, handleMaterial);
    }

}
