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

function SidebarFilter({handleShow, handleClose, shows}) {

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
        
        <Offcanvas show={shows} onHide={handleClose} placement="end" scroll="true" style={{ top: 56+'px', bottom: 69+'px'}}>
            <Offcanvas.Header closeButton>
                <Offcanvas.Title>FILTERS</Offcanvas.Title>
                <Button variant="outline-success" onClick={handleClose}>Submit filters</Button>
            </Offcanvas.Header>
            <Offcanvas.Body>
                <Total></Total>
                <Discount></Discount>
                <InvoiceDate></InvoiceDate>
                <WarningDate></WarningDate>
                <Pfr></Pfr>
                <Customer></Customer>
                <Product></Product>
            </Offcanvas.Body>
        </Offcanvas>
    );
}

export default SidebarFilter