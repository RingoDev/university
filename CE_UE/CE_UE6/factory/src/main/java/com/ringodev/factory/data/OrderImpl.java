package com.ringodev.factory.data;

import shared.Order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.text.MessageFormat;


@Entity
public class OrderImpl implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;
    private String handlebarType;
    private String handlebarMaterial;
    private String handlebarGearshift;
    private String handleMaterial;
    private int price;
    private long deliveryDate;

    public OrderImpl(final String handlebarType,
                     final String handlebarMaterial, final String handlebarGearshift,
                     final String handleMaterial) {
        this.handlebarType = handlebarType;
        this.handlebarMaterial = handlebarMaterial;
        this.handlebarGearshift = handlebarGearshift;
        this.handleMaterial = handleMaterial;
    }

    public OrderImpl(Order order){
        this.handlebarType = order.getHandlebarType();
        this.handlebarMaterial = order.getHandlebarMaterial();
        this.handlebarGearshift = order.getHandlebarGearshift();
        this.handleMaterial = order.getHandleMaterial();
    }

    public OrderImpl() {

    }

    public Order toOrder(){
        Order order =  new Order(this.handlebarType,this.handlebarMaterial,this.handlebarGearshift,this.handlebarType);
        order.setOrderId(this.orderId);
        return order;
    }

    public long getOrderId(){
        return this.orderId;
    }

    public String getHandlebarType(){
        return this.handlebarType;
    }

    public String getHandlebarMaterial(){
        return this.handlebarMaterial;
    }

    public String getHandlebarGearshift(){
        return this.handlebarGearshift;
    }

    public String getHandleMaterial(){
        return this.handleMaterial;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(long deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override public String toString() {
        return MessageFormat.format(
                "HandlebarConfig'{'orderId=''{0}'', handlebarType=''{1}'', handlebarMaterial=''{2}'', handlebarGearshift=''{3}'', handleMaterial=''{4}'''}'",
                orderId, handlebarType, handlebarMaterial, handlebarGearshift, handleMaterial);
    }
}
