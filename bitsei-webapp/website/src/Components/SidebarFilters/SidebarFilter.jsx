import React, {useEffect, useState} from "react";
import logo from "../CompanyItem/bitseiLogo";
import Total from ".//Filters/Total";
import Discount from ".//Filters/Discount";
import InvoiceDate from ".//Filters/InvoiceDate";
import WarningDate from ".//Filters/WarningDate";
import Pfr from ".//Filters/Pfr";
import Customer from ".//Filters/Customer";
import Product from ".//Filters/Product";
import Offcanvas from 'react-bootstrap/Offcanvas';
import Button from 'react-bootstrap/Button';

function SidebarFilter({handleShow, handleClose, shows, filterByTotal, filterByDiscount, filterByInvoiceDate, filterByWarningDate, filterByPfr, dataToSend}) {
    const handleSubmit = () => {
        /*
        console.log("filterByTotal: " + filterByTotal.isEnabled + ", " + filterByTotal.fromValue + ", " + filterByTotal.toValue);
        console.log("filterByDiscount: " + filterByDiscount.isEnabled + ", " + filterByDiscount.fromValue + ", " + filterByDiscount.toValue);
        console.log("filterByInvoiceDate: " + filterByInvoiceDate.isEnabled + ", " + filterByInvoiceDate.fromValue + ", " + filterByInvoiceDate.toValue);
        console.log("filterByWarningDate: " + filterByWarningDate.isEnabled + ", " + filterByWarningDate.fromValue + ", " + filterByWarningDate.toValue);
        console.log("filterByDiscount: " + filterByPfr.isEnabled + ", " + filterByPfr.fromValue + ", " + filterByPfr.toValue);
        */
        if(filterByTotal.isEnabled === true) {
            dataToSend["fromTotal"] = filterByTotal.fromValue;
            dataToSend["toTotal"] = filterByTotal.toValue;
        }
    }

    return (
        /*{<div class="Container">
            <h5 class="text-center">FILTERS</h5>
            <Total></Total>
            <Discount></Discount>
            <InvoiceDate></InvoiceDate>
            <WarningDate></WarningDate>
            <Pfr></Pfr>
            <Customer></Customer>
            <Product></Product>
        </div>}*/
        /*{<div class="container position-fixed end-0 top-0" style={{width: 400+'px'}}>
            <h5 class="text-center">FILTERS</h5>
            <Total></Total>
            <Discount></Discount>
            <InvoiceDate></InvoiceDate>
            <WarningDate></WarningDate>
            <Pfr></Pfr>
            <Customer></Customer>
            <Product></Product>
        </div>}*/
        /*{<div class="d-flex flex-column p-3 text-white bg-black position-fixed end-0 overflow-auto" style={{ top: 56+'px', bottom: 69+'px'}}>
            <h5 class="text-center">FILTERS</h5>
            <Total></Total>
            <Discount></Discount>
            <InvoiceDate></InvoiceDate>
            <WarningDate></WarningDate>
            <Pfr></Pfr>
            <Customer></Customer>
            <Product></Product>
        </div>}*/

        <Offcanvas show={shows} onHide={handleClose} placement="end" scroll="true" style={{ top: 56+'px', bottom: 69+'px', width: 500+'px'}}>
            <Offcanvas.Header closeButton>
                <Offcanvas.Title>FILTERS</Offcanvas.Title>
                <Button variant="outline-success" onClick={handleSubmit}>Submit filters</Button>
            </Offcanvas.Header>
            <Offcanvas.Body>
                <Total filter={filterByTotal}></Total>
                <Discount filter={filterByDiscount}></Discount>
                <InvoiceDate filter={filterByInvoiceDate}></InvoiceDate>
                <WarningDate filter={filterByWarningDate}></WarningDate>
                <Pfr filter={filterByPfr}></Pfr>
                <Customer></Customer>
            </Offcanvas.Body>
        </Offcanvas>
    );
}

export default SidebarFilter