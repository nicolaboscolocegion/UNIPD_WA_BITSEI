import React from "react";
import {useSelector} from "react-redux";
import {Link,} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faBuilding, faAngleDown, faFileInvoice, faFolder, faHippo, faBank, faLineChart} from "@fortawesome/free-solid-svg-icons";

import types from "./sideBarItemsType";

function Sidebar() {
    const user = useSelector((state) => state.auth.user) || {firstname: "Bitsei", lastname: "User"};
    const company_id = useSelector((state) => state.companies.activeCompany)
        || window.location.pathname.split("/")[2];

    const [isExpanded, setIsExpanded] = React.useState([
        {name: types.PRODUCTS, isExpanded: true},
        {name: types.INVOICES, isExpanded: true},
        {name: types.CUSTOMERS, isExpanded: true},
    ]);


    let sideBarItems = {
        [types.CORE]: [
            {path: "/companies", name: "Dashboard", icon: faHippo}
        ],
    }

    if (company_id) {
        sideBarItems[types.COMPANIES] = [
            {
                path: `#`, name: types.CUSTOMERS, icon: faBuilding, subItems: [
                    {path: `/companies/${company_id}/list-customer`, name: "List Customers"},
                    {path: `/companies/${company_id}/customer/add`, name: "Add Customer"},
                ]
            },
            {
                path: `#`, name: types.PRODUCTS, icon: faFolder, subItems: [
                    {path: `/companies/${company_id}/list-products/`, name: "List Products"},
                    {path: `/companies/${company_id}/product/add`, name: "Add Product"},
                ]
            },
            {
                path: `#`, name: types.INVOICES, icon: faFileInvoice, subItems: [
                    {path: `/companies/${company_id}/list-invoices`, name: "List Invoices"},
                    {path: `/companies/${company_id}/invoice/add`, name: "Add Invoice"},
                ]
            },
            {
                path: `#`, name: types.BANKACCOUNT, icon: faBank, subItems: [
                    {path: `/companies/${company_id}/bankAccount/`, name: "List Bank Accounts"},
                    {path: `/companies/${company_id}/bankAccount/add`, name: "Add Bank Account"},
                ]
            },

            {
                path: `/companies/${company_id}/insights`, name: types.INSIGHTS, icon: faLineChart
            },

        ]
    }

    const collapseMenu = (item_name) => {
        setIsExpanded({...isExpanded, [item_name]: !isExpanded[item_name]})
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
                            <div key={key}>
                                <div className="sb-sidenav-menu-heading">{key}</div>
                                {sideBarItems[key].map((item) => (
                                    <div key={item.name}>
                                        <div className="nav-link">
                                            <Link className="nav-link" to={item.path}>
                                                <div className="sb-nav-link-icon">
                                                    <FontAwesomeIcon icon={item.icon} color="white"/>
                                                </div>
                                                {item.name}
                                            </Link>
                                            {item.subItems && (
                                                <div className="sb-sidenav-collapse-arrow"
                                                     onClick={() => collapseMenu(item.name)}>
                                                    <FontAwesomeIcon icon={faAngleDown} color="white"/>
                                                </div>
                                            )}
                                        </div>
                                        {item.subItems && (
                                            <div
                                                className={`collapse ${isExpanded[item.name] ? "show" : ""}`}
                                                id="collapseLayouts"
                                                aria-labelledby="headingOne"
                                                data-bs-parent="#sidenavAccordion"
                                            >
                                                <nav className="sb-sidenav-menu-nested nav">
                                                    {item.subItems.map((subItem) => (
                                                        <Link key={subItem.name} className="nav-link" to={subItem.path}>
                                                            {subItem.name}
                                                        </Link>
                                                    ))}
                                                </nav>
                                            </div>
                                        )}
                                    </div>
                                ))}
                            </div>
                        ))}
                    </div>
                </div>
                <div className="sb-sidenav-footer sticky-bottom">
                    <div className="small">Logged in as:</div>
                    {`${user.firstname} ${user.lastname}`}
                </div>
            </nav>
        </div>

    );
}

export default Sidebar;
