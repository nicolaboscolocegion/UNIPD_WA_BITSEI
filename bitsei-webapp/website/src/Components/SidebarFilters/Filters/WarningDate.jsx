import React, {useEffect, useState} from "react";
import logo from "../../CompanyItem/bitseiLogo";

function WarningDate({id, name, details, filter}) {

    const [rangeValue1, setRangeValue1] = useState("dd-mm-yyyy");
    const [rangeValue2, setRangeValue2] = useState("dd-mm-yyyy");
    const [checkboxChecked, setCheckboxChecked] = useState(false);

    const handleCheckboxChange = (event) => {
        setCheckboxChecked(event.target.checked);
    };

    const handleRangeChange1 = (event) => {
        setRangeValue1(event.target.value);
    };
    const handleRangeChange2 = (event) => {
        setRangeValue2(event.target.value);
    };

    useEffect(() => {
        filter.isEnabled = checkboxChecked;
        filter.fromValue = rangeValue1;
        filter.toValue = rangeValue2;
    },[checkboxChecked, rangeValue1, rangeValue2]);

    return (
        <form class="border border-2 rounded-4 mb-2">
            <h4 class="d-flex justify-content-start ms-2 mt-2">By Warning Date</h4>
            <div class="container mt-3">
                <div class="row align-items-center">
                    <div class="col-auto">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckDefault" checked={checkboxChecked} onChange={handleCheckboxChange}></input>
                        </div>
                    </div>
                    <div class="col-auto">
                        <div class="row">
                            <div class="container p-0 mb-3 text-end">
                                <label for="customRange1" class="form-label">From:</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="container p-0 text-end">
                                <label for="customRange1" class="form-label">To:</label>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="row">
                            <div class="container px-4 mb-3">
                                <div class="input-group input-group-sm">
                                    <input style={{marginBottom: 0.5+'rem'}} type="date" class="form-control" value={rangeValue1} disabled={!checkboxChecked} onChange={handleRangeChange1}></input>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="container px-4">
                                <div class="input-group input-group-sm">
                                    <input style={{marginBottom: 0.5+'rem'}} type="date" class="form-control" value={rangeValue2} disabled={!checkboxChecked} onChange={handleRangeChange2}></input>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default WarningDate;