import React, {useEffect, useRef, useState, Component} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import gate from "../../gate";
import {toast} from "react-toastify";
import Button from 'react-bootstrap/Button';
import { default as ReactSelect } from "react-select";
import { components } from "react-select";
function GetInvoicesByFilters() {
    const {register, handleSubmit} = useForm();
    const [pending, setPending] = useState(false);

    const [customerList, setCustomerList] = useState([]);
    const [optionSelected, setOptionSelected] = useState(null);

    useEffect(() => {
        console.log("listing customers list onLoad TO REMOVE");
        gate
            .listCustomers()
            .then((response) => {
            const customers = response.data['resource-list'].map((item) => item.customer);
            setCustomerList(customers);
        })
        .catch((error) => {
            toast.error("Something went wrong UseEffect." + error);
        })
    }, []);

    const customerOptions = customerList.map((customer) => ({
        value: customer.customerID,
        label: customer.businessName,
    }));

    const handleChange = (selected) => {
        setOptionSelected(selected);
    };

    const Option = (props) => {
        return (
            <div>
                <components.Option {...props}>
                    <input
                        type="checkbox"
                        checked={props.isSelected}
                        onChange={() => null}
                    />{" "}
                    <label>{props.label}</label>
                </components.Option>
            </div>
        );
    };

    const [filterbyTotal, setFilterByTotal] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [filterByDiscount, setFilterByDiscount] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [filterByPfr, setFilterByPfr] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [filterByInvoiceDate, setFilterByInvoiceDate] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [filterByWarningDate, setFilterByWarningDate] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [dataToSend, setDataToSend] = useState({
        isEmpty: true,
        data: null
    })
    const [invoices, setInvoices] = useState([]);
    const submitHandler = (data, e) => {
        e.preventDefault();
        console.log(data);
        setPending(true);
        const data_to_send = {};

        for (let key in data) {
            if (data[key]) {
                data_to_send[key] = data[key];
            }
        }

        let customerId = ""
        let countCustomerId = 0;
        for(let option in optionSelected){
            console.log(optionSelected[option], optionSelected[option].label)
            if(countCustomerId > 0)
                customerId += "-";
            customerId += optionSelected[option].value.toString();
            countCustomerId++;
        }
        if(countCustomerId > 0)
            data_to_send["fromCustomerId"] = customerId;
        gate
            .getInvoicesByFilters(data_to_send)
            .then((response) => {
                setInvoices(response.data["resource-list"]);
            })
            .catch((error) => {
                toast.error("Something went wrong.");
            });
    };

    return (
        <>
            {invoices.length > 0 ? (
            <>
                <title>Invoices Retrieved By Filters</title>
                <h1>Invoices Retrieved By Filters</h1>
                <table>
                    <thead>
                        <tr>
                            <td>Invoice id</td>
                            <td>Customer</td>
                            <td>Product</td>
                            <td>Status</td>
                            <td>Invoice number</td>
                            <td>Invoice date</td>
                            <td>Warning date</td>
                            <td>Total</td>
                            <td>Discount</td>
                            <td>Pension fund refund</td>
                        </tr>
                    </thead>
                    <tbody>
                        {invoices.map((item) => {
                            const invoice = item.invoice
                            return (
                                <tr>
                                <td>{invoice.invoice_id}</td>
                                <td>{invoice.business_name}</td>
                                <td>{invoice.product_title}</td>
                                <td>{invoice.status}</td>
                                <td>{invoice.invoice_number}</td>
                                <td>{invoice.invoice_date}</td>
                                <td>{invoice.warning_date}</td>
                                <td>{invoice.total}</td>
                                <td>{invoice.discount}</td>
                                <td>{invoice.pension_fund_refund}</td>
                            </tr>
                            )
                        })
                        }
                    </tbody>
                </table>
            </>
            ) :
            (
                <>
                    <title>Get Invoices By Filters</title>
                    <h1>Get Invoices By Filters</h1>
                    <form onSubmit={handleSubmit(submitHandler)}>
                        <hr/>
                        <label htmlFor="fromTotalID">from Total:</label>
                        <input id="fromTotalID" name="fromTotal" type="number" {
                            ...register("fromTotal", {valueAsNumber: true})
                        }/>
                        <br/>
                        <br/>
                        <label htmlFor="toTotalID">to Total:</label>
                        <input id="toTotalID" name="toTotal" type="number" {
                            ...register("toTotal", {valueAsNumber: true})
                        }/><br/>
                        <br/>
                        <hr/>
                        <label htmlFor="fromDiscountID">from Discount:</label>
                        <input id="fromDiscountID" name="fromDiscount" type="number" {
                            ...register("fromDiscount", {valueAsNumber: true})
                        }/>
                        <br/>
                        <br/>
                        <label htmlFor="toDiscountID">to Discount:</label>
                        <input id="toDiscountID" name="toDiscount" type="number" {
                            ...register("toDiscount", {valueAsNumber: true})
                        }/>
                        <br/>
                        <br/>
                        <hr/>
                        <label htmlFor="fromPfrID">from Pension Fund Refund:</label>
                        <input id="fromPfrID" name="fromPfr" type="number" {
                            ...register("fromPfr", {valueAsNumber: true})
                        }/>
                        <br/>
                        <br/>
                        <label htmlFor="toPfrID">to Pension Fund Refund:</label>
                        <input id="toPfrID" name="toPfr" type="number" {
                            ...register("toPfr", {valueAsNumber: true})
                        }/>
                        <br/>
                        <br/>
                        <hr/>

                        <label htmlFor="fromInvoiceDateID">from Invoice Date:</label>
                        <input id="fromInvoiceDateID" name="fromInvoiceDate" type="date" {
                            ...register("fromInvoiceDate", {valueAsDate: true})
                        }/>
                        <br/>
                        <br/>
                        <label htmlFor="toInvoiceDateID">to Invoice Date:</label>
                        <input id="toInvoiceDateID" name="toInvoiceDate" type="date" {
                            ...register("toInvoiceDate", {valueAsDate: true})
                        }/>
                        <br/>
                        <br/>
                        <hr/>
                        <label htmlFor="fromWarningDateID">from Warning Date:</label>
                        <input id="fromWarningDateID" name="fromWarningDate" type="date" {
                            ...register("fromWarningDate", {valueAsDate: true})
                        }/>
                        <br/>
                        <br/>
                        <label htmlFor="toWarningDateID">to Warning Date:</label>
                        <input id="toWarningDateID" name="toWarningDate" type="date" {
                            ...register("toWarningDate", {valueAsDate: true})
                        }/>
                        <br/>
                        <br/>
                        <hr/>
                        <ReactSelect
                            placeholder='Select Customer(s)'
                            options={customerOptions}
                            isMulti
                            closeMenuOnSelect={false}
                            hideSelectedOptions={false}
                            components={{
                                Option
                            }}
                            onChange={handleChange}
                            allowSelectAll={true}
                            value={optionSelected}
                        />
                        <br/>
                        <br/>
                        <hr/>
                        <button type="submit">Submit</button>
                        <br/>
                        <button type="reset">Reset the form</button>
                    </form>
                </>
            )}
        </>
    )

}

export default connect(null, {})(GetInvoicesByFilters);