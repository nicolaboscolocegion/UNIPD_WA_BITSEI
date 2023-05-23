import React, {useEffect, useState} from "react";
import logo from "../../CompanyItem/bitseiLogo";
import { Form } from 'react-bootstrap';
import { InputGroup } from 'react-bootstrap';

function Pfr({id, name, details, filter}) {

    const [rangeValue1, setRangeValue1] = useState(0.0);
    const [rangeValue2, setRangeValue2] = useState(0.0);
    const [checkboxChecked, setCheckboxChecked] = useState(false);

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
            <h4 class="d-flex justify-content-start ms-2 mt-2">By Pfr</h4>
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
                            <div class="container px-4 mb-3">
                                <InputGroup size="sm" onClick={handleRangeClick}>
                                    <Form.Control style={{marginBottom: 0.5+'rem'}} placeholder="From Pfr" aria-label="From Pfr" aria-describedby="basic-addon1" value={rangeValue1} disabled={!checkboxChecked} onChange={handleRangeChange1}/>
                                    <InputGroup.Text style={{marginBottom: 0.5+'rem'}} id="basic-addon1">%</InputGroup.Text>
                                </InputGroup>
                            </div>
                        </div>
                        <div class="row">
                            <div class="container px-4">
                                <InputGroup size="sm" onClick={handleRangeClick}>
                                    <Form.Control style={{marginBottom: 0.5+'rem'}} placeholder="To Pfr" aria-label="To Pfr" aria-describedby="basic-addon2" value={rangeValue2} disabled={!checkboxChecked} onChange={handleRangeChange2}/>
                                    <InputGroup.Text style={{marginBottom: 0.5+'rem'}} id="basic-addon2">%</InputGroup.Text>
                                </InputGroup>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default Pfr;