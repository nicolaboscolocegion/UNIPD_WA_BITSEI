import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import gate from "../../../gate";
import {useParams} from "react-router-dom";
import Form from "../../../Components/Form/Form";
import {history} from "../../../index";
import {toast} from "react-toastify";

function AddProduct() {
    const {register, handleSubmit, formState: { errors }} = useForm();
    const [pending, setPending] = useState(false);
    const {company_id} = useParams();

    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);
        console.log(data)
        data.default_price = parseFloat(data.default_price);
        gate
            .addProduct({product: {company_id: parseInt(company_id), ...data}}, parseInt(company_id))
            .then((response) => {
                console.log(response.data);
                setPending(false);
                toast.success("Product added successfully !");
                history.push("/companies/"+company_id+"/list-products/");
            })
            .catch((error) => {
                console.log(error)
            })


    };

    const fields = [
        [{value: "Title", name: "title", type: "string"}, {value: "Default Price", name: "default_price", type: "double"}],
        [{value: "Measurement Unit", name: "measurement_unit", type: "string"}, {value: "Description", name: "description", type: "string"} ],
    ]

    return (
        <Form title={"Add Product"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register} errors={errors}/>
    )
}

export default AddProduct;