import React, {useEffect, useState} from 'react';
import './App.css';
import axios from 'axios';
import Selector from "./Selector";
import OrderComponent from "./Order";

let baseURL = "http://localhost:8080/api"

const myAxios = axios.create({baseURL})

export interface Order {
    orderId: number
    handlebarType: string
    handlebarMaterial: string
    handlebarGearshift: string
    handleMaterial: string
    deliveryDate: number
}

const App: React.FC = () => {


    const [pending, setPending] = useState<boolean>(false)
    const [handlebarType, setHandlebarType] = useState<string>()
    const [handlebarMaterial, setHandlebarMaterial] = useState<string>()
    const [handlebarGearshift, setHandlebarGearshift] = useState<string>()
    const [handleType, setHandleType] = useState<string>()
    const [order, setOrder] = useState<Order>()
    const [handlebarTypes, setHandlebarTypes] = useState<string[]>()
    const [handlebarMaterials, setHandlebarMaterials] = useState<string[]>()
    const [handlebarGearshifts, setHandlebarGearshifts] = useState<string[]>()
    const [handleTypes, setHandleTypes] = useState<string[]>()


    useEffect(() => {
        myAxios.get('handlebarType')
            .then((res) => {
                setHandlebarTypes(res.data)
                setPending(false)
            })
        setPending(true)
    }, [])

    if (pending) return (<></>)

    if (!handlebarType && handlebarTypes) {
        return (
            <>
                <Selector type={"HANDLEBAR"} list={handlebarTypes} submit={(option: string) => {
                    setHandlebarType(option)
                    myAxios.get('handlebarMaterial?handlebarType=' + option)
                        .then((res) => {
                            setHandlebarMaterials(res.data)
                            setPending(false)
                        })
                    setPending(true)
                }}/>
            </>
        )
    }
    if (!handlebarMaterial && handlebarMaterials) {
        return (
            <>
                <Selector type={"MATERIAL"} list={handlebarMaterials}
                          submit={(option: string) => {
                              setHandlebarMaterial(option)
                              myAxios.get('handlebarGearshift?handlebarType=' + handlebarType + '&handlebarMaterial=' + option)
                                  .then((res) => {
                                      setHandlebarGearshifts(res.data)
                                      setPending(false);
                                  })
                              setPending(true)
                          }}/>
            </>
        )
    }
    if (!handlebarGearshift && handlebarGearshifts) {
        return (
            <>
                <Selector  type={"GEARSHIFT"} list={handlebarGearshifts}
                          submit={(option: string) => {
                              setHandlebarGearshift(option)
                              myAxios.get('handleType?handlebarType=' + handlebarType + '&handlebarMaterial=' + handlebarMaterial + '&handlebarGearshift=' + option)
                                  .then((res) => {
                                          setHandleTypes(res.data)
                                          setPending(false)
                                      }
                                  )
                              setPending(true)
                          }}
                />
            </>
        )
    }

    if (!handleType && handleTypes) {
        return (
            <>
                <Selector type={"HANDLE"} last={true} list={handleTypes} submit={(option: string) => {
                    setHandleType(option)
                    console.log("Verifying")
                    myAxios.post('verify', {
                        handlebarType,
                        handlebarMaterial,
                        handlebarGearshift,
                        handleType: option
                    })
                        .then((res) => {
                            setOrder(res.data)
                            setPending(false)
                        })
                    setPending(true)
                }
                }/>
            </>
        )
    }

    if (order === undefined) return <p>Loading</p>
    else return (
        <>
            <OrderComponent order={order}/>
        </>
    )
}

export default App;
