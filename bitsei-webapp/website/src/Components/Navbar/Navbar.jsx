import React, {useState} from "react";
import {useSelector, connect} from "react-redux";
import logo from "../../assets/bitseiLogo";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPersonFalling} from "@fortawesome/free-solid-svg-icons";
import {faBars} from "@fortawesome/free-solid-svg-icons";
import {Button} from "react-bootstrap";
import {logout} from "../../Store/auth/authThunk";
import {Link} from "react-router-dom";
import Dropdown from "react-bootstrap/Dropdown";


function Navbar({logout}) {
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
                className = "m-auto me-4 menu-button-resize"
            >
                <Dropdown.Toggle id="dropdown-button-dark-example1" variant="secondary">
                    <FontAwesomeIcon icon={faBars} />
                </Dropdown.Toggle>

                <Dropdown.Menu variant="dark">
                    <Dropdown.Item href="#/action-1">Company</Dropdown.Item>
                    <Dropdown.Divider />
                    <Dropdown.Item href="#/action-1">Customers</Dropdown.Item>
                    <Dropdown.Item href="#/action-2">Products</Dropdown.Item>
                    <Dropdown.Item href="#/action-3">Invoices</Dropdown.Item>
                    <Dropdown.Divider />
                    <Dropdown.Item href="#/action-4">Bank Account</Dropdown.Item>
                    <Dropdown.Item href="#/action-5">Insights</Dropdown.Item>
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
