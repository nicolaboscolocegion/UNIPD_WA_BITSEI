import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import gate from "../../../gate";
import {useParams} from "react-router-dom";
import Form from "../../../Components/Form/Form";

function AddProduct() {
    const {register, handleSubmit, formState: { errors }} = useForm();
    const [pending, setPending] = useState(false);
    const {company_id} = useParams();

    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);
        console.log(data)

        gate
            .addProduct({product: {companyID: company_id, ...data}}, company_id)
            .then((response) => {
                console.log(response.data)
                setPending(false)
            })
            .catch((error) => {
                console.log(error)
            })


    };

    const fields = [
        [{name: "title", type: "string"}, {name: "default_price", type: "int"}],
        [{name: "measurement_unit", type: "string"}, {name: "description", type: "string"} ],
        [{name: "logo", type: "image"}],
    ]

    return (
        <Form title={"Product"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register} errors={errors}/>
    )
}

export default AddProduct;