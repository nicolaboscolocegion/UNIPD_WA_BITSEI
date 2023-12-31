import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {toast} from "react-toastify";
import gate from "../../../gate";
import Form from "../../../Components/Form/Form";
import {useParams} from "react-router-dom";
import {history} from "../../../index";

function AddBankAccount() {
    const {register, handleSubmit, formState: {errors}} = useForm();
    const [pending, setPending] = useState(false);
    const {company_id} = useParams();

    useEffect(() => {

    }, []);


    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);
        //creates the bank account
        gate
            .createBankAccount({company_id: parseInt(company_id), ...data}, company_id)
            .then((response) => {
                setPending(false);
                toast.success("New Bank Account added successfully !");
                history.push(`/companies/${company_id}/bankAccount`);
            })
            .catch((error) => {
                setPending(false);
                console.log(error);
                if (error.response) {
                    toast.error(error.response);
                } else {
                    toast.error("Something went wrong");
                }
            });
    };

    //form inputs
    const fields = [
        [{
            value: "IBAN", name: "IBAN", type: "string", options: {
                required: "Required",
                minlength: 3,
                message: "Please enter a IBAN",
            }
        }],

        [{
            value: "Bank Name", name: "bank_name", type: "string", options: {
                minlength: 3,
                message: "Please enter a bank name",
            }
        }],

        [{
            value: "Bank Account Friendly Name", name: "bankaccount_friendly_name", type: "string", options: {
                required: "Required",
                minlength: 3,
                message: "Please enter a frendly name",
            }
        }],

    ]

    //return forms
    return (
        <Form
            title={"Add Bank Account"}
            onSubmit={handleSubmit(submitHandler)}
            fields={fields}
            register={register}
            errors={errors}
            pending={pending}
        />
    )
}

export default AddBankAccount;