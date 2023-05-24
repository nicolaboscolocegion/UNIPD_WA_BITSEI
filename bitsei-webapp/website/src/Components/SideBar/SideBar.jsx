import React from "react";
import {useSelector} from "react-redux";
import {Link,} from "react-router-dom";

function Sidebar() {
    const company_id = useSelector((state) => state.companies.activeCompany)
        || window.location.pathname.split("/")[2];

    let sideBarItems = {
        "Core": [
            {path: "/companies", name: "Dashboard"}
        ],
    }

    // check the company id is integer or not
    // if (company_id && isNaN(parseInt(company_id))) {
    //     console.log("company id is not integer");
    //     // history.push("/companies");
    // }

    if (company_id) {
        sideBarItems["Companies"] = [
            {path: `/companies/${company_id}/customers`, name: "Customers"},
            {path: `/companies/${company_id}/products`, name: "Products"},
            {path: `/companies/${company_id}/invoices`, name: "Invoices"},
        ]
    }

    return (
        <div id="layoutSidenav_nav">
            <nav
                className="sb-sidenav accordion sb-sidenav-dark"
                id="sidenavAccordion"
            >
                <div className="sb-sidenav-menu">
                    <div className="nav">
                        {Object.keys(sideBarItems).map((key) => (
                            <>
                                <div className="sb-sidenav-menu-heading">{key}</div>
                                {sideBarItems[key].map((item) => (
                                    <Link className="nav-link" to={item.path}>
                                        <div className="sb-nav-link-icon">
                                            <i className="fas fa-tachometer-alt"/>
                                        </div>
                                        {item.name}
                                    </Link>
                                ))}
                            </>
                        ))}
                    </div>
                </div>
                <div className="sb-sidenav-footer sticky-bottom">
                    <div className="small">Logged in as:</div>
                    BITSEI USER
                </div>
            </nav>
        </div>

    );
}

export default Sidebar;
