import React, {useState} from "react";
import {useSelector, connect} from "react-redux";
import logo from "../../assets/bitseiLogo";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPersonFalling} from "@fortawesome/free-solid-svg-icons";
import {Button} from "react-bootstrap";
import {logout} from "../../Store/auth/authThunk";
import {Link} from "react-router-dom";

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
            <div className="d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0">
                <Button
                    type="button"
                    onClick={() => logout()}
                >
                    <FontAwesomeIcon icon={faPersonFalling}/>
                </Button>
            </div>
        </nav>
    );
}

export default connect(null, {logout})(Navbar);
