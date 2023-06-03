import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import gate from "../../gate";
import {useParams} from "react-router-dom";
import Form from "../../Components/Form/Form";
import {toast} from "react-toastify";
import {history} from "../../index";

function EditInvoice(props) {
    const {register, handleSubmit, formState: {errors}} = useForm(
        {
            defaultValues: {
                "Pension fund refund": props.location.state.invoice.pension_fund_refund || 0,
                "Discount": props.location.state.invoice.discount || 0
            }
        }
    );

    const [pending, setPending] = useState(false);
    const {company_id, invoice_id} = useParams();

    const [hasStamp, setHasStamp] = useState(props.location.state.invoice.has_stamp || false);
    const [customerList, setCustomerList] = useState([]);
    const [customerOptionSelected, setCustomerOptionSelected] = useState(props.location.state.invoice.customer_id || null);

    useEffect(() => {
        gate.listCustomers(company_id)
            .then((customerResponse) => {
                const customers = customerResponse.data['resource-list'].map((item) => item.customer);
                setCustomerList(customers);
            })
            .catch((error) => {
                toast.error("Something went wrong in listCustomers. " + error);
            });
    }, []);


    const submitHandler = (data, e) => {
        console.log({
            invoice: {
                invoice_id: parseInt(invoice_id),
                customer_id: customerOptionSelected,
                pension_fund_refund: parseFloat(data["Pension fund refund"]),
                has_stamp: hasStamp,
                discount: parseFloat(data["Discount"]) || 0
            }
        })
        e.preventDefault();
        setPending(true);
        gate
            .editInvoice(
                {
                    invoice: {
                        invoice_id: parseInt(invoice_id),
                        customer_id: customerOptionSelected,
                        pension_fund_refund: parseFloat(data["Pension fund refund"]),
                        has_stamp: hasStamp,
                        discount: parseFloat(data["Discount"]) || 0
                    }
                }, company_id, invoice_id)
            .then((response) => {
                toast.success("Invoice edited successfully");
                history.push(`/companies/${company_id}/list-invoices`);
                setPending(false)
            })
            .catch((error) => {
                toast.error("Something went wrong in edit Invoice. ")
                setPending(false)
            })
    };

    const fields = [
        [
            {name: "Pension fund refund", type: "double", options:{required: true}},
            {name: "Discount", type: "double"}
        ],
    ]

    return (
        <>
            <Form
                title={"Invoice"}
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
                        <select {...register("customerID", {required: true})} className="form-select">
                            <option value="default" disabled selected>Select a customer</option>
                            {customerList.map(customer => {
                                    return (
                                        <option
                                            selected={parseInt(customer.customerID) === customerOptionSelected}
                                            value={customer.customerID}
                                            onClick={(e) => {
                                                setCustomerOptionSelected(parseInt(e.target.value))
                                            }}
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

export default EditInvoice;