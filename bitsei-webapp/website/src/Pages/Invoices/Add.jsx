import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import gate from "../../gate";
import {useParams} from "react-router-dom";
import Form from "../../Components/Form/Form";
import {toast} from "react-toastify";
import {history} from "../../index";

function AddInvoice() {

    const {register, handleSubmit, formState: {errors}} = useForm();
    const [pending, setPending] = useState(false);
    const {company_id} = useParams();

    const [hasStamp, setHasStamp] = useState(false);
    const [customerList, setCustomerList] = useState([]);
    const [customerOptionSelected, setCustomerOptionSelected] = useState(null);

    useEffect(() => {
        gate.listCustomers(company_id)
            .then((customerResponse) => {
                const customers = customerResponse.data['resource-list'].map((item) => item.customer);
                setCustomerList(customers);
            })
            .catch((error) => {
                toast.error("Something went wrong in listing customers. " + error);
            });

    }, []);


    const submitHandler = (data, e) => {
        e.preventDefault();
        setPending(true);

        gate
            .addInvoice(
                {
                    invoice: {
                        customer_id: customerOptionSelected,
                        pension_fund_refund: parseFloat(data["Pension fund refund"]),
                        has_stamp: hasStamp
                    }
                }, company_id)
            .then((response) => {
                toast.success("Invoice added successfully");
                history.push(`/companies/${company_id}/list-invoices`);
                setPending(false)
            })
            .catch((error) => {
                toast.error("Something went wrong in adding the invoice. ")
                setPending(false)
            })
    };

    //customer
    const fields = [
        [{value: "Pension fund refund", name: "Pension fund refund", type: "double", options:{required: true}}],
    ]

    return (
        <>
            <Form
                title={"Add Invoice"}
                onSubmit={handleSubmit(submitHandler)}
                fields={fields}
                register={register}
                errors={errors}
                pending={pending}
            >
                <div className="row justify-content-between text-left">
                    <div className="form-group col-sm-6 flex-column d-flex">
                        <label
                            className="form-control-label px-3"
                            htmlFor="has_stamp">
                            Has Stamp
                        </label>
                        <input
                            className="cinput"
                            type="checkbox"
                            value=""
                            id="has_stamp"
                            checked={hasStamp}
                            onClick={() => {
                                setHasStamp(!hasStamp)
                            }}
                        />
                    </div>
                    <div className="form-group col-sm-6 flex-column d-flex">
                        <label
                            className="form-control-label px-3"
                            htmlFor="has_mail_notifications">
                            Customer:
                        </label>
                        <select {...register("customerID", {required: true})} onChange={(e) => {
                            setCustomerOptionSelected(parseInt(e.target.value))
                        }} className="form-select">
                            <option value="default" selected disabled hidden>Select a customer</option>
                            {customerList.map(customer => {
                                    return (
                                        <option
                                            // selected={customer.customer_id === product_id}
                                            value={customer.customerID}

                                        >
                                            {customer.businessName}
                                        </option>
                                    )
                                }
                            )}
                        </select>
                    </div>
                </div>
            </Form>
        </>

    )
}

export default AddInvoice;