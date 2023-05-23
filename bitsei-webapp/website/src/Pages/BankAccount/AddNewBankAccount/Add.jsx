import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {toast} from "react-toastify";
import gate from "../../../gate";
import Form from "../../../Components/Form/Form";
import {useParams} from "react-router-dom";

function AddBankAccount() {//TODO clear companies?
    const {register, handleSubmit, formState: {errors}} = useForm();
    const [pending, setPending] = useState(false);
    const {company_id} = useParams();

    useEffect(() => {

    }, []);


    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);

        gate
            .createBankAccount({company_id: parseInt(company_id), ...data}, company_id)
            .then((response) => {
                setPending(false);
                toast.success("New Bank Account added successfully !");
                //history.push("/companies");
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

    const fields = [
        [{
            name: "IBAN", type: "string", options: {
                required: "Required",
                minlength: 27,
                message: "Please enter a IBAN",
            }
        }],

        [{
            name: "bank_name", type: "string", options: {
                minlength: 3,
                message: "Please enter a bank name",
            }
        }],

        [{
            name: "bankaccount_friendly_name", type: "string", options: {
                required: "Required",
                minlength: 3,
                message: "Please enter a frendly name",
            }
        }],

    ]

    return (
        <Form
            title={"BankAccount"}
            onSubmit={handleSubmit(submitHandler)}
            fields={fields}
            register={register}
            errors={errors}
        />
    )
}

export default AddBankAccount;