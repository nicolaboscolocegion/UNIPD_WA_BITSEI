import React, {useEffect, useState} from "react";
import logo from "../../CompanyItem/bitseiLogo";
import gate from "../../../gate";
import {toast} from "react-toastify";
import {default as ReactSelect } from "react-select";
import {components} from "react-select";

function Customer({id, name, details}) {

    const [checkboxChecked, setCheckboxChecked] = useState(false);
    const [customerList, setCustomerList] = useState([]);
    const [customerOptionSelected, setCustomerOptionSelected] = useState(null);


    useEffect(() => {
        console.log("listing customers onLoad TO REMOVE");

        // Call listCustomers
        gate.listCustomers()
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
    const handleCheckboxChange = (event) => {
        setCheckboxChecked(event.target.checked);
    };

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

    return (
        <form class="border border-2 rounded-4 mb-2">
            <h4 class="d-flex justify-content-start ms-2 mt-2">By Customer</h4>
            <div class="container mt-3 mb-2">
                <div class="row align">
                    <div class="col-4">
                        <div class="container text-end p-0">
                            <label for="customRange1" class="form-label">Customer:</label>
                        </div>
                    </div>
                    <div class="col">
                        <div className="container ps-1 pe-3">
                            <ReactSelect
                                placeholder='Select Customer(s)'
                                options={customerOptions}
                                isMulti
                                closeMenuOnSelect={false}
                                hideSelectedOptions={false}
                                components={{
                                    Option
                                }}
                                onChange={handleCustomerChange}
                                allowSelectAll={true}
                                value={customerOptionSelected}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default Customer;