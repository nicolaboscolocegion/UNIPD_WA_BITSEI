import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import gate from "../../../gate";
import {useParams} from "react-router-dom";
import Form from "../../../Components/Form/Form";
import {toast} from "react-toastify";

function EditCustomer() {
    const [pending, setPending] = useState(false);
    const {company_id, customer_id} = useParams();
    const { register, handleSubmit, reset } = useForm();

    useEffect(() => {
        setPending(true);
        gate
            .getCustomer(customer_id,company_id)
            .then(response => {
                console.log(response.data.customer);
                const customer = response.data.customer;
                console.log(customer)
                reset({
                    businessName: customer.businessName,
                        vatNumber: customer.vatNumber,
                        taxCode: customer.taxCode,
                        address: customer.address,
                        city: customer.city,
                        province: customer.province,
                        postalCode: customer.postalCode,
                        emailAddress: customer.emailAddress,
                        pec: customer.pec,
                        uniqueCode: customer.uniqueCode,
                })
                setPending(false);
            }).catch( () => {
                toast.error("Something went wrong.");
            }

        );
    }, [customer_id, company_id]);



    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);

        const formData = new FormData();
        formData.append("businessName", data.businessName);
        formData.append("vatNumber", data.vatNumber);
        formData.append("taxCode", data.taxCode);
        formData.append("address", data.address);
        formData.append("city", data.city);
        formData.append("province", data.province);
        formData.append("postalCode", data.postalCode);
        formData.append("emailAddress", data.emailAddress);
        formData.append("pec", data.pec);
        formData.append("uniqueCode", data.uniqueCode);

        console.log(data)

        gate
            .editCustomer({customer: {companyID: company_id, ...data}}, company_id, customer_id)
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

export default EditCustomer;