import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import gate from "../../../gate";
import {useParams} from "react-router-dom";
import Form from "../../../Components/Form/Form";
import {toast} from "react-toastify";

function EditProduct() {
    const [pending, setPending] = useState(false);
    const {company_id, product_id} = useParams();
    const { register, handleSubmit, reset } = useForm();

    useEffect(() => {
        setPending(true);
        gate
            .getProduct(product_id,company_id)
            .then(response => {
                console.log(response.data.product);
                const product = response.data.product;
                console.log(product)
                reset({
                    title: product.title,
                        defaultPrice: product.defaultPrice,
                        logo: product.logo,
                        measurementUnit: product.measurementUnit,
                        description: product.description,
                })
                setPending(false);
            }).catch( () => {
                toast.error("Something went wrong.");
            }

        );
    }, [product_id, company_id]);



    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);

        const formData = new FormData();
        formData.append("title", data.title);
        formData.append("defaultPrice", data.defaultPrice);
        formData.append("logo", data.logo);
        formData.append("measurementUnit", data.measurementUnit);
        formData.append("description", data.description);

        console.log(data)

        gate
            .editProduct({product: {companyID: company_id, ...data}}, company_id, product_id)
            .then((response) => {
                console.log(response.data)
                setPending(false)
            })
            .catch((error) => {
                console.log(error)
            })
    };


    const fields = [
        [{name: "title", type: "string"}, {name: "defaultPrice", type: "int"}],
        [{name: "logo", type: "string"}, {name: "measurementUnit", type: "string"}],
        [{name: "description", type: "string"}],
    ]

    return (
        <Form title={"Product"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register}/>
    )
}

export default EditProduct;