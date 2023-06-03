import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import gate from "../../../gate";
import {useParams} from "react-router-dom";
import Form from "../../../Components/Form/Form";
import {history} from "../../../index";
import {toast} from "react-toastify";

function AddCustomer() {
    const {register, handleSubmit, formState: { errors }} = useForm();
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
                history.push(`/companies/${company_id}/list-customer`);
                setPending(false)
            })
            .catch((error) => {
                console.log(error)
                toast.error("Something went wrong in customer creation.")
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
        <Form title={"Customer"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register} errors={errors}/>
    )
}

export default AddCustomer;