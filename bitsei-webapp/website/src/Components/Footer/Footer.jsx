import React from "react";
import logo from "./Logo_Università_Padova.png";

function Footer() {
    return (
        <footer className="py-4 bg-light mt-auto sticky-bottom">
            <div className="container-fluid px-4">
                <div className="d-flex align-items-center justify-content-between small">
                    <div className="text-muted">Copyright © BITSEI 2023</div>
                    <div>
                        <img src={logo} alt="Logo" className="logo-icon" style={{ height: "40px" }} />
                    </div>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
