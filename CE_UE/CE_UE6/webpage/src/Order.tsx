import React from "react";
import {Order} from "./App";


interface OrderComponentProps {
    order: Order
}

const OrderComponent: React.FC<OrderComponentProps> = (props) => {
    const date = new Date(props.order.deliveryDate);
    return (
        <>
            <div id={"container"}>
                <div className={"card grey darken-1"} id={"card"}>
                    <div style={{textAlign: "center"}} id={"content"} className={"card-content white-text"}>
                        <div className={"card-title"}>Thanks for your order!</div>
                        <div>We received your order with the ordernumber {props.order.orderId}.</div>
                        <div>It will be delivered by {date.toDateString()}. </div>

                        <div style={{marginTop:"1em"}}>
                            <div>Your chosen handlebar:</div>
                            <div>{props.order.handlebarType}</div>
                            <div>{props.order.handlebarMaterial}</div>
                            <div>{props.order.handlebarGearshift}</div>
                            <div>{props.order.handleMaterial}</div>
                        </div>


                    </div>
                    <div style={{textAlign: "right"}} className="card-action">
                        <a style={{color: "#ffb300"}} href="/">Order another one</a>
                    </div>
                </div>
            </div>

        </>
    )
}

export default OrderComponent
