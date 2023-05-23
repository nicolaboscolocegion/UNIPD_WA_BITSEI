import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import gate from "../../../gate";
import {useParams} from "react-router-dom";
import Form from "../../../Components/Form/Form";

function AddCustomer() {
    const {register, handleSubmit} = useForm();
    const [pending, setPending] = useState(false);
    const {company_id} = useParams();

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
        <Form title={"Customer"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register}/>
    )
}

export default AddCustomer;