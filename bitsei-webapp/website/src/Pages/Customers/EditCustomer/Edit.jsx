import React, {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import gate from "../../../gate";
import {useParams} from "react-router-dom";
import Form from "../../../Components/Form/Form";
import {toast} from "react-toastify";

function EditCustomer() {
    const [pending, setPending] = useState(false);
    const {company_id, customer_id} = useParams();
    const {register, handleSubmit, reset} = useForm();

    useEffect(() => {
        setPending(true);
        gate
            .getCustomer(customer_id, company_id)
            .then(response => {
                const customer = response.data.customer;
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
            }).catch(() => {
                toast.error("Something went wrong.");
            }
        );
    }, [customer_id, company_id, reset]);


    const submitHandler = (data, e) => {
        e.preventDefault();
        setPending(true);
        gate
            .editCustomer({customer: {companyID: parseInt(company_id), ...data}}, company_id, customer_id)
            .then((response) => {
                setPending(false)
            })
            .catch((error) => {
                toast.error("Something went wrong.");
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
        <Form title={"Customer"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register}
              pending={pending}/>
    )
}

export default EditCustomer;