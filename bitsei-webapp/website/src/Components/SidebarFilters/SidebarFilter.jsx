import React, {useEffect, useState} from "react";
import logo from "../CompanyItem/bitseiLogo";
import Total from ".//Filters/Total";
import Discount from ".//Filters/Discount";
import InvoiceDate from ".//Filters/InvoiceDate";
import WarningDate from ".//Filters/WarningDate";
import Pfr from ".//Filters/Pfr";
import Customer from ".//Filters/Customer";
import Product from ".//Filters/Product";

function SidebarFilter({id, name, details}) {

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
        <div class="d-flex flex-column p-3 text-white bg-black position-fixed end-0 overflow-auto" style={{ top: 56+'px', bottom: 69+'px'}}>
            <h5 class="text-center">FILTERS</h5>
            <Total></Total>
            <Discount></Discount>
            <InvoiceDate></InvoiceDate>
            <WarningDate></WarningDate>
            <Pfr></Pfr>
            <Customer></Customer>
            <Product></Product>
        </div>

        /*{<div class="offcanvas offcanvas-end show" tabindex="-1" id="offcanvas" aria-labelledby="offcanvasLabel" style={{ top: 56+'px', bottom: 69+'px'}}>
            <div class="offcanvas-header">
                <h5 class="offcanvas-title" id="offcanvasLabel">FILTERS</h5>
                <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
            </div>
            <div class="offcanvas-body">
                <Total></Total>
                <Discount></Discount>
                <InvoiceDate></InvoiceDate>
                <WarningDate></WarningDate>
                <Pfr></Pfr>
                <Customer></Customer>
                <Product></Product>
            </div>
        </div>}*/
    );
}

export default SidebarFilter