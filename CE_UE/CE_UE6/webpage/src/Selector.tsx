import React, {useEffect, useState} from "react";

interface SelectorProps {
    list: string[]
    submit: (option: string) => void
    last?: boolean
    type: string
}

const Selector: React.FC<SelectorProps> = (props) => {

    const [selected, setSelected] = useState<string>('')

    const checkAndSubmit = (option: string) => {
        if (option !== '') props.submit(option)
        else window.alert("Please select an option");
    }

    useEffect(() => {
        setSelected(props.list[0])
    }, []) //eslint-disable-line

    return (
        <>
            <div id={"container"}>
                <div className={"card grey darken-1"} id={"card"}>
                    <div id={"content"} className={"card-content white-text"}>
                        <div style={{textAlign: "center",fontSize:"1.5em"}} className={"card-title"}>CHOOSE YOUR {props.type}</div>

                    </div>
                    <div className={"card-action"} id={"action"}>
                        <select className={"browser-default"}
                                onChange={(event) => setSelected(event.target.value)}>
                            {props.list.map((type, idx) => {
                                    return (
                                        <option key={idx} value={type}>{type}
                                        </option>
                                    )
                                }
                            )}
                        </select>
                        <button className={"waves-effect waves-light  amber darken-1 btn"}
                                onClick={() => checkAndSubmit(selected)}>{!props.last ? "Submit" : "Submit and Order"}</button>
                    </div>
                </div>
            </div>
        </>
    )
}


export default Selector
