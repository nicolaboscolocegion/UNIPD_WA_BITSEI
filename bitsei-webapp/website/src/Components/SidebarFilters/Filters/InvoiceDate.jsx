import React, {useEffect, useState} from "react";
import logo from "../../CompanyItem/bitseiLogo";
import { Form } from 'react-bootstrap';

function InvoiceDate({id, name, details, filter}) {

    const [checkboxChecked, setCheckboxChecked] = useState(filter.isEnabled);
    const [rangeValue1, setRangeValue1] = useState(filter.fromValue ? filter.fromValue : "dd-mm-yyyy");
    const [rangeValue2, setRangeValue2] = useState(filter.toValue? filter.toValue : "dd-mm-yyyy");

    const handleCheckboxChange = (event) => {
        setCheckboxChecked(event.target.checked);
    };
    
    const handleRangeClick = () => {
        if (!checkboxChecked) {
          setCheckboxChecked(true);
        }
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
            <h4 class="d-flex justify-content-start ms-2 mt-2">By Inovice Date</h4>
            <div class="container mt-3">
                <div class="row align-items-center">
                    <div class="col-auto">
                        <Form.Switch id="flexSwitchCheckDefault" checked={checkboxChecked} onChange={handleCheckboxChange}/>
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
                            <div class="container px-4 mb-3" onClick={handleRangeClick}>
                                <Form.Control size="sm" style={{marginBottom: 0.5+'rem'}} type="date" value={rangeValue1} disabled={!checkboxChecked} onChange={handleRangeChange1}/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="container px-4" onClick={handleRangeClick}>
                                <Form.Control size="sm" style={{marginBottom: 0.5+'rem'}} type="date" value={rangeValue2} disabled={!checkboxChecked} onChange={handleRangeChange2}/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default InvoiceDate;