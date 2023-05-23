import React, {useEffect, useRef, useState} from "react";
import Select from 'react-select';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import {toast} from "react-toastify";
import {clearCompanies} from "../../Store/companies/listsThunk";
import gate from "../../gate";
import {history} from "../../index";
import Input from "./Input/Input";
import SidebarFilter from "../../Components/SidebarFilters/SidebarFilter";
import Button from 'react-bootstrap/Button';
import Offcanvas from 'react-bootstrap/Offcanvas';
import Sidebar from "../../Components/SideBar/SideBar";
import {components, default as ReactSelect} from "react-select";


// TODO: Add validation for all fields
// TODO: Add error handling for all fields
// TODO: Add loading for creating company
// TODO: HTML CSS for this page
function ShowCharts() {

    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const [orderByOption, setOrderByOption] = useState([
        {value: 1, label: "Invoice ID"},
        {value: 2, label: "Customer Name"},
        {value:3, label: "Invoice Date"},
        {value:4, label: "Total"}]);
    const [sorted, setSorted] = useState(["Ascending", "Descending"]);
    const [orderByOptionSelected, setOrderByOptionSelected] = useState(orderByOption[0]);
    const handleOrderByOptionChange = (selected) => {
        // if the sorted set is supported
        if(orderByOption.indexOf(selected) > -1) {
            setOrderByOptionSelected(selected);
        }
    }
    const Option = (props) => {
        return (
            <div>
                <components.Option {...props}>
                    <input
                        type="checkbox"
                        checked={props.isSelected}
                        onChange={() => null}
                    />{" "}
                    <label>{props.label}</label>
                </components.Option>
            </div>
        );
    };


    /*const [maxHeight, setMaxHeight] = useState(0);

    useEffect(() => {
        const handleResize = () => {
            const windowHeight = window.innerHeight;
            setMaxHeight(windowHeight);
        };

        window.addEventListener('resize', handleResize);
        handleResize();

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);*/


    return (
        /*{<section>
            <div class="container-fluid">
                <div class="d-flex flex-row">
                    <div class="main-content">
                        <div className="container-fluid bg-white py-5">
                            <section className="w-100 p-4 text-center pb-4">
                                <h1>Amazing charts</h1>
                            </section>
                        </div>
                    </div>
                    <div class="sidebar flex-shrink-0">
                        <SidebarFilter>Boh</SidebarFilter>
                    </div>
                </div>
            </div>
        </section>}*/

        /*{<section>
            <div class="container-fluid m-0 p-0">
                <div class="row m-0">
                    <div class="col">
                        <div class="main-content">
                            <div className="container-fluid bg-white py-5">
                                <section className="w-100 p-4 text-center pb-4">
                                    <h1>Amazing charts</h1>
                                </section>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                            <div class="sidebar">
                                <SidebarFilter>Boh</SidebarFilter>
                            </div>
                    </div>
                </div>
            </div>
        </section>}*/
        <>
        <head>
            <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.2/css/mdb.min.css" rel="stylesheet" />
        </head>

        <body>
            <section>
                <div className="container-fluid bg-white my-5">
                    <section className="w-100 p-4 text-center pb-4">
                        <h1>Amazing charts</h1>

                        <SidebarFilter handleShow={handleShow} handleClose={handleClose} shows={show}/>

                        <Button variant="outline-primary" onClick={handleShow}>
                            Manage filters
                        </Button>
                    </section>
                </div>

                <div className="container">
                    <div className="row">
                        <div className="col">
                            <div className="card">
                                <h5 className="card-header elegant-color-dark white-text text-center">Invoices</h5>
                                <div className="card-body">
                                    <div className="dropdown float-left">
                                        <span className="dropdown-label">Order By:</span>
                                        <Select
                                            className="react-select-container"
                                            classNamePrefix="react-select"
                                            isSearchable={false}
                                            options={orderByOption}
                                            onChange={handleOrderByOptionChange}
                                            value={orderByOptionSelected}
                                        />
                                    </div>
                                    <div className="table-responsive">
                                        <table className="table table-hover table-bordered">
                                            <thead className="indigo lighten-5">
                                            <tr>
                                                <th className="text-center">Invoice ID</th>
                                                <th className="text-center">Customer ID</th>
                                                <th className="text-center">Customer Name</th>
                                                <th className="text-center">Status</th>
                                                <th className="text-center">Invoice Date</th>
                                                <th className="text-center">Total</th>
                                                <th className="text-center">Discount</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td className="text-center">INV001</td>
                                                <td className="text-center">CUST001</td>
                                                <td className="text-center">John Doe</td>
                                                <td className="text-center">Paid</td>
                                                <td className="text-center">2023-05-10</td>
                                                <td className="text-center">$100.00</td>
                                                <td className="text-center">$10.00</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


            </section>

            <script src="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.2/js/mdb.min.js"></script>
        </body>
        </>
    )
}

export default ShowCharts;