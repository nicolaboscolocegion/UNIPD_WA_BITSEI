import React, {useState} from "react";
import {useSelector, connect} from "react-redux";
import logo from "../../assets/bitseiLogo";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPersonFalling} from "@fortawesome/free-solid-svg-icons";
import {Button} from "react-bootstrap";
import {logout} from "../../Store/auth/authThunk";

function Navbar({logout}) {

    return (
        <nav className="sb-topnav navbar navbar-expand navbar-dark bg-dark sticky-top">
            {/* Navbar Brand*/}
            <a className="navbar-brand ps-3" href="index.html">
                <img
                    width="100"
                    className="d-inline-block align-text-top"
                    src={`data:image/png;base64, ${logo()}`}
                    alt={"company_logo"}
                />
            </a>


            <Button
                type="button"
                onClick={() => logout()}
            >
                <FontAwesomeIcon icon={faPersonFalling}/>
            </Button>

        </nav>


    );
}

export default connect(null, {logout})(Navbar);
