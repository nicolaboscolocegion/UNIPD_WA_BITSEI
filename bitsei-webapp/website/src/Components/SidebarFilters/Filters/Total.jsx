import React, {useEffect, useState} from "react";
import logo from "../../CompanyItem/bitseiLogo";

function Total({id, name, details}) {


    const [rangeValue1, setRangeValue1] = useState(0);
    const [rangeValue2, setRangeValue2] = useState(0);
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

    return (
        <form>
            <h3>Filtra per totale</h3>
            <div class="container mt-5">
                <div class="row align-items-center">
                    <div class="col-auto">
                        <div class="form-check form-switch">
                            <input class="form-check-input" type="checkbox" role="switch" id="flexSwitchCheckDefault" checked={checkboxChecked} onChange={handleCheckboxChange}></input>
                        </div>
                    </div>
                    <div class="col-2">
                        <div class="row">
                            <div class="container mb-3">
                                <label for="customRange1" class="form-label">From:</label>
                            </div>
                        </div>
                        <div class="row">
                            <div class="container text-end">
                                <label for="customRange1" class="form-label">To:</label>
                            </div>
                        </div>
                    </div>
                    <div class="col">
                        <div class="row">
                            <div class="container mb-3" style={{ position: 'relative' }}>
                                <input style={{marginBottom: 0.5+'rem'}} type="range" class="form-range" min="100" max="3000" step="20" id="customRange1" value={rangeValue1} onChange={handleRangeChange1} disabled={!checkboxChecked}></input>
                                <span style={{ position: 'absolute', top: '-25px', left: `${((rangeValue1 / 3000) * 100)*0.83}%`}}>{rangeValue1}€</span>
                            </div>
                        </div>
                        <div class="row">
                            <div class="container" style={{ position: 'relative' }}>
                                <input style={{marginBottom: 0.5+'rem'}} type="range" class="form-range" min="100" max="3000" step="20" id="customRange2" value={rangeValue2} onChange={handleRangeChange2} disabled={!checkboxChecked}></input>
                                <span style={{ position: 'absolute', top: '-25px', left: `${((rangeValue2 / 3000) * 100)*0.83}%`}}>{rangeValue2}€</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    );
}

export default Total;