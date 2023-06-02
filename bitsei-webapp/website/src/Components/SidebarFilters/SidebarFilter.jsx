import React, {useEffect, useState} from "react";
import logo from "../CompanyItem/bitseiLogo";
import Total from ".//Filters/Total";
import Discount from ".//Filters/Discount";
import InvoiceDate from ".//Filters/InvoiceDate";
import WarningDate from ".//Filters/WarningDate";
import Pfr from ".//Filters/Pfr";
import Customer from ".//Filters/Customer";
import Status from ".//Filters/Status";
import Product from ".//Filters/Product";
import Offcanvas from 'react-bootstrap/Offcanvas';
import Button from 'react-bootstrap/Button';

function SidebarFilter({handleShow, handleClose, shows, filterByTotal, filterByDiscount, filterByInvoiceDate, filterByWarningDate, filterByPfr, filterByCustomerId, filterByStatus, setFilters, listInvoice}) {
    const handleSubmit = () => {
        /*
        console.log("filterByTotal: " + filterByTotal.isEnabled + ", " + filterByTotal.fromValue + ", " + filterByTotal.toValue);
        console.log("filterByDiscount: " + filterByDiscount.isEnabled + ", " + filterByDiscount.fromValue + ", " + filterByDiscount.toValue);
        console.log("filterByInvoiceDate: " + filterByInvoiceDate.isEnabled + ", " + filterByInvoiceDate.fromValue + ", " + filterByInvoiceDate.toValue);
        console.log("filterByWarningDate: " + filterByWarningDate.isEnabled + ", " + filterByWarningDate.fromValue + ", " + filterByWarningDate.toValue);
        console.log("filterByDiscount: " + filterByPfr.isEnabled + ", " + filterByPfr.fromValue + ", " + filterByPfr.toValue);
        */
        setFilters();
        handleClose();
    }
    
    const handleReset = () => {
        filterByTotal.isEnabled = false;
        filterByTotal.fromValue = filterByTotal.toValue = null;

        filterByDiscount.isEnabled = false;
        filterByDiscount.fromValue = filterByDiscount.toValue = null;

        filterByPfr.isEnabled = false;
        filterByPfr.fromValue = filterByPfr.toValue = null;

        filterByInvoiceDate.isEnabled = false;
        filterByInvoiceDate.fromValue = filterByInvoiceDate.toValue = null;

        filterByWarningDate.isEnabled = false;
        filterByWarningDate.fromValue = filterByWarningDate.toValue = null;

        filterByCustomerId.isEnabled = false;
        filterByCustomerId.fromCustomerId = null;

        handleSubmit();
    }

    return (
        <Offcanvas show={shows} onHide={handleClose} placement="end" scroll="true" style={{ top: 56+'px', bottom: 69+'px', width: 500+'px'}}>
            <Offcanvas.Header closeButton>
                <Offcanvas.Title>FILTERS</Offcanvas.Title>
                <Button variant="outline-success" onClick={handleSubmit}>Submit filters</Button>
                <Button variant="outline-success" onClick={handleReset}>Reset filters</Button>
            </Offcanvas.Header>
            <Offcanvas.Body>
                <Status filter={filterByStatus}></Status>
                <Total filter={filterByTotal}></Total>
                <Discount filter={filterByDiscount}></Discount>
                <InvoiceDate filter={filterByInvoiceDate}></InvoiceDate>
                <WarningDate filter={filterByWarningDate}></WarningDate>
                <Pfr filter={filterByPfr}></Pfr>
                <Customer filter={filterByCustomerId}></Customer>
            </Offcanvas.Body>
        </Offcanvas>
    );
}

export default SidebarFilter