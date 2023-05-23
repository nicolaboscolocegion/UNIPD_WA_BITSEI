import React, {useEffect, useState} from "react";
import logo from "../../CompanyItem/bitseiLogo";

function Discount({id, name, details, filter}) {

    const [rangeValue1, setRangeValue1] = useState(100);
    const [rangeValue2, setRangeValue2] = useState(100);
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
            <h4 class="d-flex justify-content-start ms-2 mt-2">By Discount</h4>
            <div class="container mt-4">
                <div class="row align-items-center">
                    <div class="col-auto">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckDefault" checked={checkboxChecked} onChange={handleCheckboxChange}></input>
                        </div>
                    </div>
                    <div class="col-auto">
                        <div class="row">
                            <div class="container p-0 mb-3 text-emd">
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
                            <div class="container position-relative mb-3">
                                <input style={{marginBottom: 0.5+'rem'}} type="range" class="form-range" min="100" max="3000" step="50" id="customRange1" value={rangeValue1} onChange={handleRangeChange1} disabled={!checkboxChecked}></input>
                                <output className="form-range-value position-absolute" style={{top: '-25px', left: `${((rangeValue1 / 3000) * 100)*0.83}%`}}>{rangeValue1}€</output>
                            </div>
                        </div>
                        <div class="row">
                            <div class="container position-relative">
                                <input style={{marginBottom: 0.5+'rem'}} type="range" class="form-range" min="100" max="3000" step="50" id="customRange2" value={rangeValue2} onChange={handleRangeChange2} disabled={!checkboxChecked}></input>
                                <output className="form-range-value position-absolute" style={{top: '-25px', left: `${((rangeValue2 / 3000) * 100)*0.83}%`}}>{rangeValue2}€</output>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default Discount;