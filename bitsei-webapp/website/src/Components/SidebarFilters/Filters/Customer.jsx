import React, {useEffect, useState} from "react";
import logo from "../../CompanyItem/bitseiLogo";

function Customer({id, name, details}) {

    const [checkboxChecked, setCheckboxChecked] = useState(false);

    const handleCheckboxChange = (event) => {
        setCheckboxChecked(event.target.checked);
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
                        <div class="container ps-1 pe-3">
                            <select class="form-select" aria-label="Customer select">
                                <option selected>Select customer</option>
                                <option value="1">One</option>
                                <option value="2">Two</option>
                                <option value="3">Three</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default Customer;