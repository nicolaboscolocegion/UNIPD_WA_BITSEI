import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {toast} from "react-toastify";
import gate from "../../../gate";
import Form  from "../../../Components/Form/Form";
import {useParams} from "react-router-dom";


function EditBankAccount() {
    const [pending, setPending] = useState(false);
    const {company_id, bankaccount_id} = useParams();
    const { register, handleSubmit, reset, formState:{errors}} = useForm();


    useEffect(() => {
        setPending(true);
        gate
            .getBankAccount(bankaccount_id, company_id)
            .then(response => {
                console.log(response);
                const bankAccount = response.data;
                reset({
                    IBAN: bankAccount.IBAN,
                    bank_name: bankAccount.bank_name,
                    bankaccount_friendly_name: bankAccount.bankaccount_friendly_name,
                    company_id: bankAccount.company_id,
                })
                setPending(false);
            }).catch( () => {
                toast.error("Something went wrong.");}
            );
    }, []);


    
    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);

        console.log(data);

        gate
            .editBankAccount({bankAccount: {...data, bankaccount_id: parseInt(bankaccount_id)}}, company_id, bankaccount_id)
            .then((response) => {
                console.log(response.data)
                setPending(false)
            })
            .catch((error) => {
                console.log(error)
            })

        };

        const fields = [
            [{name: "IBAN", type: "string", options: {
                required: "Required",
                minlength: 27,
                message: "Please enter a IBAN",
            }}],
    
            [{name: "bank_name", type: "string", options: {
                minlength: 3,
                message: "Please enter a bank name",
            }}],
    
            [{name: "bankaccount_friendly_name", type: "string",options: {
                required: "Required",
                minlength: 3,
                message: "Please enter a frendly name",
            }}],
            
        ]
    


    return (
        
        <Form title={"BankAccount"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register} errors={errors}/>
        
        
    )
}

export default EditBankAccount;