import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import "./style.css";
import gate from "../../../gate";
import {useParams} from "react-router-dom";

// TODO: Add validation for all fields
// TODO: Add error handling for all fields
// TODO: Add loading for creating company
// TODO: HTML CSS for this page
function AddCustomer() {
    const {register, handleSubmit} = useForm();
    const [pending, setPending] = useState(false);
    const {company_id} = useParams();

    useEffect(() => {

    }, []);

    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);
        console.log(data)

        gate
            .addCustomer({customer: {companyID: company_id, ...data}}, company_id)
            .then((response) => {
                console.log(response.data)
                setPending(false)
            })
            .catch((error) => {
                console.log(error)
            })


    };

    const fields = [
        [{name: "businessName", type: "string"}, {name: "vatNumber", type: "string"}],
        [{name: "taxCode", type: "string"}, {name: "address", type: "string"}],
        [{name: "city", type: "string"}, {name: "province", type: "string"}],
        [{name: "postalCode", type: "string"}, {name: "emailAddress", type: "email"}],
        [{name: "pec", type: "email"}, {name: "uniqueCode", type: "string"}],
    ]

    return (
        <main>
            <div className="container-fluid px-1 py-5 mx-auto">
                <div className="row d-flex justify-content-center">
                    <div className="col-xl-7 col-lg-8 col-md-9 col-11 text-center">
                        <h3>Add Customer</h3>
                        <div className="card">
                            <h5 className="text-center mb-4"/>
                            <form
                                onSubmit={handleSubmit(submitHandler)}
                                className="form-card"
                            >
                                {fields.map((field =>
                                        <div className="row justify-content-between text-left">
                                            {field.map((item =>
                                                    <div className="form-group col-sm-6 flex-column d-flex">
                                                        {" "}
                                                        <label className="form-control-label px-3">
                                                            {item.name}<span className="text-danger"> *</span>
                                                        </label>{" "}
                                                        <input
                                                            type={item.type}
                                                            id={item.name}
                                                            name={item.name}
                                                            placeholder=" "
                                                            {...register(item.name)}
                                                        />{" "}
                                                    </div>
                                            ))}
                                        </div>
                                ))}

                                <div className="row justify-content-center">
                                    <div className="form-group col-sm-6">
                                        {" "}
                                        <input
                                            type="submit"
                                            className="btn btn-primary"
                                            defaultValue="Inserisci"
                                        />{" "}
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>


    )
}

export default connect(null, {})(AddCustomer);