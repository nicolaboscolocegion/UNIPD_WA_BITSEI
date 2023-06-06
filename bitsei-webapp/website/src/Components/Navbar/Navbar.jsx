import React, {useState} from "react";
import {useSelector, connect} from "react-redux";
import logo from "../../assets/bitseiLogo";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faBars,
    faBank,
    faBuilding,
    faFileInvoice,
    faFolder,
    faPersonFalling,
    faHippo, faLineChart
} from "@fortawesome/free-solid-svg-icons";
import {Button} from "react-bootstrap";
import {logout} from "../../Store/auth/authThunk";
import {Link} from "react-router-dom";
import Dropdown from "react-bootstrap/Dropdown";


function Navbar({logout}) {
    const company_id = useSelector((state) => state.companies.activeCompany)
        || window.location.pathname.split("/")[2];

    return (
        <nav className="sb-topnav navbar navbar-expand navbar-dark bg-dark sticky-top">
            <Link className="navbar-brand ps-3" to={'/companies'}>
                <img
                    width="100"
                    className="d-inline-block align-text-top"
                    src={`data:image/png;base64, ${logo()}`}
                    alt={"company_logo"}
                />
            </Link>


            <Dropdown
                className="m-auto me-4 menu-button-resize"
            >
                <Dropdown.Toggle id="dropdown-button-dark-example1" variant="secondary">
                    <FontAwesomeIcon icon={faBars}/>
                </Dropdown.Toggle>

                <Dropdown.Menu variant="dark">
                    <Dropdown.Item href="/companies">
                            <FontAwesomeIcon icon={faHippo} color="white" className="me-2"/>
                        Company
                        </Dropdown.Item>
                    {company_id && (
                        <>
                            <Dropdown.Divider/>
                            <Dropdown.Item href={`/companies/${company_id}/list-customer`}>
                                <FontAwesomeIcon icon={faBuilding} color="white" className="me-2"/>
                                Customers</Dropdown.Item>
                            <Dropdown.Item href={`/companies/${company_id}/list-products`}>
                                <FontAwesomeIcon icon={faFolder} color="white" className="me-2"/>
                                Products</Dropdown.Item>
                            <Dropdown.Item href={`/companies/${company_id}/list-invoices`}>
                                <FontAwesomeIcon icon={faFileInvoice} color="white" className="me-2"/>
                                Invoices</Dropdown.Item>
                            <Dropdown.Divider/>
                            <Dropdown.Item href={`/companies/${company_id}/bankAccount`}>
                                <FontAwesomeIcon icon={faBank} color="white" className="me-2"/>
                                Bank Account</Dropdown.Item>
                            <Dropdown.Item href={`/companies/${company_id}/insights`}>
                                <FontAwesomeIcon icon={faLineChart} color="white" className="me-2"/>
                                Insights
                            </Dropdown.Item>
                        </>
                    )}
                </Dropdown.Menu>
            </Dropdown>


            <Button
                type="button"
                className="me-4"
                onClick={() => logout()}
            >
                <FontAwesomeIcon icon={faPersonFalling}/>
            </Button>

        </nav>


    );
}

export default connect(null, {logout})(Navbar);
