import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import gate from "../../gate";
import {useParams} from "react-router-dom";
import Form from "../../Components/Form/Form";
import Customer from "../../Components/SidebarFilters/Filters/Customer"
import {toast} from "react-toastify";
import {components} from "react-select";
import {default as ReactSelect} from "react-select";
function AddInvoice() {

    const {register, handleSubmit, formState: { errors }} = useForm();
    const [pending, setPending] = useState(false);
    const {company_id} = useParams();

    const [customerList, setCustomerList] = useState([]);
    const [customerOptionSelected, setCustomerOptionSelected] = useState(null);

    useEffect(() => {
        console.log("listing customers onLoad TO REMOVE");

        // Call listCustomers
        gate.listCustomers(company_id)
            .then((customerResponse) => {
                const customers = customerResponse.data['resource-list'].map((item) => item.customer);
                setCustomerList(customers);
            })
            .catch((error) => {
                toast.error("Something went wrong in listCustomers. " + error);
            });

    }, []);

    const customerOptions = customerList.map((customer) => ({
        value: customer.customerID,
        label: customer.businessName,
    }));

    const handleCustomerChange = (selected) => {
        setCustomerOptionSelected(selected);
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

    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);
        console.log(data)

        gate
            .addInvoice({customer: {companyID: company_id, ...data}}, company_id)
            .then((response) => {
                console.log(response.data)
                setPending(false)
            })
            .catch((error) => {
                console.log(error)
            })


    };


    const fields = [
        [{name: "Product TODO", type: "string"}, {name: "Quantity", type: "int"}],
        [{name: "Total", type: "double"}, {name: "Discount", type: "double"}],
        [{name: "Pension fund refund", type: "double"}, {name: "Has stamp", type: "boolean"}],
    ]

    return (
        <>
            <Form title={"Invoice"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register} errors={errors}/>
            <ReactSelect
                placeholder='Select Customer(s)'
                options={customerOptions}
                closeMenuOnSelect={true}
                hideSelectedOptions={false}
                components={{
                    Option
                }}
                onChange={handleCustomerChange}
                allowSelectAll={true}
                value={customerOptionSelected}
            />
        </>

    )
}

export default AddInvoice;