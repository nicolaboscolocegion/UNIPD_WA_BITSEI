import React, {useEffect, useState} from "react";
import logo from "../../CompanyItem/bitseiLogo";
import { Form } from 'react-bootstrap';
import { InputGroup } from 'react-bootstrap';

function InvoiceDate({id, name, details}) {

    const [checkboxChecked, setCheckboxChecked] = useState(false);

    const handleCheckboxChange = (event) => {
        setCheckboxChecked(event.target.checked);
    };
    
    const handleRangeClick = () => {
        if (!checkboxChecked) {
          setCheckboxChecked(true);
        }
    };

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
                                <Form.Control size="sm" style={{marginBottom: 0.5+'rem'}} type="date" disabled={!checkboxChecked}/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="container px-4" onClick={handleRangeClick}>
                                <Form.Control size="sm" style={{marginBottom: 0.5+'rem'}} type="date" disabled={!checkboxChecked}/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default InvoiceDate;