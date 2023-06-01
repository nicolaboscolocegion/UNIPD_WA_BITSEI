import React, {useEffect, useState} from "react";
import logo from "../../CompanyItem/bitseiLogo";
import gate from "../../../gate";
import {toast} from "react-toastify";
import {default as ReactSelect } from "react-select";
import {components} from "react-select";
import {useParams} from "react-router-dom";

function Status({filter}) {
    const [checkboxChecked, setCheckboxChecked] = useState(false);
    const [statusOption, setStatusOption] = useState([
        { value: 0, label: "Open" },
        { value: 1, label: "Pending" },
        { value: 2, label: "Closed" },
    ]);
    const [statusOptionSelected, setStatusOptionSelected] = useState(filter.fromStatus ? filter.fromStatus : null);

    useEffect(() => {
        if(statusOptionSelected !== null && statusOptionSelected.length > 0) {
            filter.isEnabled = true;
        }
        else {
            filter.isEnabled = false;
        }
        filter.fromStatus = statusOptionSelected;
    },[statusOptionSelected]);

    const handleCheckboxChange = (event) => {
        setCheckboxChecked(event.target.checked);
    };

    const handleStatusChange = (selected) => {
        setStatusOptionSelected(selected);
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
            <h4 class="d-flex justify-content-start ms-2 mt-2">By Status</h4>
            <div class="container mt-3 mb-2">
                <div class="row align">
                    <div class="col-4">
                        <div class="container text-end p-0">
                            <label for="customRange1" class="form-label">Status:</label>
                        </div>
                    </div>
                    <div class="col">
                        <div className="container ps-1 pe-3">
                            <ReactSelect
                                placeholder='Select Status(es)'
                                options={statusOption}
                                isMulti
                                closeMenuOnSelect={false}
                                hideSelectedOptions={false}
                                components={{
                                    Option
                                }}
                                onChange={handleStatusChange}
                                allowSelectAll={true}
                                value={statusOptionSelected}
                            />
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default Status;