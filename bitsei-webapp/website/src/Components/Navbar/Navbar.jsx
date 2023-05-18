import React, {useState} from "react";
import {useSelector, connect} from "react-redux";

function Navbar() {

    return (
        <nav className="sb-topnav navbar navbar-expand navbar-dark bg-dark">
            {/* Navbar Brand*/}
            <a className="navbar-brand ps-3" href="index.html">
                Start Bootstrap
            </a>
            {/* Sidebar Toggle*/}
            <button
                className="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0"
                id="sidebarToggle"
                href="#!"
            >
                <i className="fas fa-bars"/>
            </button>
            {/* Navbar Search*/}

            {/* Navbar*/}
            <ul className="navbar-nav ms-auto ms-md-0 me-3 me-lg-4">
                <li className="nav-item dropdown">
                    <a
                        className="nav-link dropdown-toggle"
                        id="navbarDropdown"
                        href="#"
                        role="button"
                        data-bs-toggle="dropdown"
                        aria-expanded="false"
                    >
                        <i className="fas fa-user fa-fw"/>
                    </a>
                    <ul
                        className="dropdown-menu dropdown-menu-end"
                        aria-labelledby="navbarDropdown"
                    >
                        <li>
                            <a className="dropdown-item" href="#!">
                                Settings
                            </a>
                        </li>
                        <li>
                            <a className="dropdown-item" href="#!">
                                Activity Log
                            </a>
                        </li>
                        <li>
                            <hr className="dropdown-divider"/>
                        </li>
                        <li>
                            <a className="dropdown-item" href="#!">
                                Logout
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </nav>


    );
}

export default connect(null, {})(Navbar);
