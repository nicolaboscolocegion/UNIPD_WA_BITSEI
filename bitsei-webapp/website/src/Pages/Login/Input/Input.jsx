import React from "react";
import "../../../styles.css";
import {errorMessages} from "../../../helpers/errorMessages";

function Input({name, register, type, errors}) {
    console.log(type)
    return (
        <div className="form-floating mb-3">
            <input
                className="form-control"
                type={type}
                name={name}
                {...register}
            />
            <label>{name}</label>
            {
                errors[type]
                &&
                <span style={{
                    display: 'inline-block',
                    padding: "5px 10px",
                    backgroundColor: "#ffcccc",
                    color: "#ff0000",
                    borderRadius: "3px"
                }}>
                    {errors[type].message || errorMessages[errors[type].type] || "Not valid"}
                </span>
            }
        </div>
    );
}

export default Input;
