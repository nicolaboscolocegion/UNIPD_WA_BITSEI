import React from "react";
import "./style.css";
import {faSpinner} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {errorMessages} from "../../helpers/errorMessages";


function Form({title, onSubmit, fields, register, errors={}, pending=false, children}) {
    console.log(pending)
    return (
        <div className="container-fluid px-1 py-5 mx-auto">
            <div className="row d-flex justify-content-center">
                <div className="col-xl-7 col-lg-8 col-md-9 col-11 text-center">
                    <h3>Add {title}</h3>
                    <div className="card">
                        <h5 className="text-center mb-4"/>
                        <form
                            onSubmit={onSubmit}
                            className="form-card"
                        >
                            {fields.map((field =>
                                    <div className="row justify-content-between text-left">
                                        {field.map((item => {
                                                return(
                                                <div
                                                    className={`form-group flex-column d-flex ${field.length > 1 && "col-sm-6"}`}>
                                                    <label className="form-control-label px-3">
                                                        {item.name}<span className="text-danger"> *</span>
                                                    </label>
                                                    <input
                                                        type={item.type}
                                                        id={item.name}
                                                        name={item.name}
                                                        placeholder={item.placeholder || ""}
                                                        {...register(item.name, item.options || {})}
                                                    />
                                                    {
                                                        errors[item.name]
                                                        // && errors[item.name].type === "required"
                                                        && <span className="error">
                                                                {
                                                                    errors[item.name].message || errorMessages[errors[item.name].type] || "Not valid"
                                                                }
                                                            </span>
                                                    }
                                                </div>
                                        )}
                                        ))}

                                    </div>
                            ))}
                            {children && children}
                            <div className="row justify-content-center">
                                <div className="form-group col-sm-6">
                                    {pending
                                        ? <FontAwesomeIcon icon={faSpinner} color="blue" spin/>
                                        : <input
                                            type="submit"
                                            className={`btn btn-primary ${pending && "disabled"}}`}
                                            value="Submit"
                                        />
                                    }
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Form;