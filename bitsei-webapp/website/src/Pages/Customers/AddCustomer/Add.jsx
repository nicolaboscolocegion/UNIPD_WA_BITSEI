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
        [{ name: "businessName", value: "Business Name", type: "string" }, { name: "vatNumber", value: "Vat Number", type: "string" },],
        [{ name: "taxCode", value: "Tax Code", type: "string" }, { name: "address", value: "Address", type: "string" },],
        [{ name: "city", value: "City", type: "string" }, { name: "province", value: "Province", type: "string" },],
        [{ name: "postalCode", value: "Postal Code", type: "string" }, { name: "emailAddress", value: "Email Address", type: "email" },],
        [{ name: "pec", value: "PEC", type: "email" }, { name: "uniqueCode", value: "Unique Code", type: "string" },]
    ]

    return (
        <Form title={"Customer"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register} errors={errors}/>
    )
}

export default AddCustomer;