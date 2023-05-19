import React, {useEffect, useState} from "react";
import logo from "../CompanyItem/bitseiLogo";
import Total from ".//Filters/Total";

function SidebarFilter({id, name, details}) {

    return (
        <div class="offcanvas offcanvas-end show" tabindex="-1" id="offcanvas" aria-labelledby="offcanvasLabel">
            <div class="offcanvas-header">
                <h5 class="offcanvas-title" id="offcanvasLabel">FILTRI</h5>
                <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
            </div>
            <div class="offcanvas-body">
                <Total></Total>
            </div>
        </div>
    );
}

export default SidebarFilter