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
import {parse} from "@fortawesome/fontawesome-svg-core";


// TODO: Add validation for all fields
// TODO: Add error handling for all fields
// TODO: Add loading for creating company
// TODO: HTML CSS for this page
function ShowCharts() {
    const [invoices, setInvoices] = useState([]);
    useEffect(() => {
        console.log("listing invoices onLoad TO REMOVE");

        const data_to_send = {};
        data_to_send["fromTotal"] = '0';
        gate
            .getInvoicesByFilters(data_to_send)
            .then((response) => {
                setInvoices(response.data["resource-list"]);
            })
            .catch((error) => {
               toast.error("Something went wrong in invoices listing");
            });

    }, []);


    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleSubmit = () => {
        setShow(false);
    }

    const [orderByOption, setOrderByOption] = useState([
        {value: 1, label: "Invoice ID"},
        {value: 2, label: "Customer Name"},
        {value: 3, label: "Invoice Date"},
        {value: 4, label: "Total"}]);
    const [sortedOption, setSortedOption] = useState([
        {value: 1, label: "Ascending"},
        {value: 2, label: "Descending"}]);
    const [orderByOptionSelected, setOrderByOptionSelected] = useState(orderByOption[0]);
    const [sortedOptionSelected, setSortedOptionSelected] = useState(sortedOption[0]);
    const handleOrderByOptionChange = (selected) => {
        // if the sorted set is supported
        console.log("sortedOptionSelected: " + sortedOptionSelected.value + " - " + sortedOptionSelected.label);
        if(orderByOption.indexOf(selected) > -1) {
            setOrderByOptionSelected(selected);
            if(selected.value === 1) {
                console.log("Order by Invoice ID found - sortedOptionSelected: " + sortedOptionSelected.value + ", " + sortedOptionSelected.label);
                if(parseInt(sortedOptionSelected.value) === 2) {
                    const sortedInvoices = [...invoices].sort((a, b) => b.invoice.invoice_id - a.invoice.invoice_id);
                    setInvoices(sortedInvoices);
                }
                else {
                    const sortedInvoices = [...invoices].sort((a, b) => a.invoice.invoice_id - b.invoice.invoice_id);
                    setInvoices(sortedInvoices);
                }
            }
            if(selected.value === 2) {
                console.log("Order by Customer Name found - sortedOptionSelected: " + sortedOptionSelected.value + ", " + sortedOptionSelected.label);
                if(parseInt(sortedOptionSelected.value) === 2) {
                    const sortedInvoices = [...invoices].sort((a, b) => b.invoice.business_name > a.invoice.business_name ? 1 : -1);
                    setInvoices(sortedInvoices);
                }
                else {
                    const sortedInvoices = [...invoices].sort((a, b) => a.invoice.business_name > b.invoice.business_name ? 1 : -1);
                    setInvoices(sortedInvoices);
                }
            }
            if(selected.value === 3) {
                console.log("Order by Invoice Date found - sortedOptionSelected: " + sortedOptionSelected.value + ", " + sortedOptionSelected.label);
                if(parseInt(sortedOptionSelected.value) === 2) {
                    const sortedInvoices = [...invoices].sort((a, b) => b.invoice.invoice_date > a.invoice.invoice_date ? 1 : -1);
                    setInvoices(sortedInvoices);
                }
                else {
                    const sortedInvoices = [...invoices].sort((a, b) => a.invoice.invoice_date > b.invoice.invoice_date ? 1 : -1);
                    setInvoices(sortedInvoices);
                }
            }
        }
    }
    const handleSortedOptionChange = async(sortSelected) => {
        if(sortedOption.indexOf(sortSelected) > -1) {
            setSortedOptionSelected(sortSelected);
            console.log("sortSelected: " + sortSelected.value + " - " + sortSelected.label);
        }
    }

    useEffect(() => {
        handleOrderByOptionChange(orderByOptionSelected);
    },[sortedOptionSelected]);

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

    const [filterByTotal, setFilterByTotal] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [filterByDiscount, setFilterByDiscount] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [filterByPfr, setFilterByPfr] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [filterByInvoiceDate, setFilterByInvoiceDate] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [filterByWarningDate, setFilterByWarningDate] = useState({
        isEnabled: false,
        fromValue: null,
        toValue: null
    })
    const [dataToSend, setDataToSend] = useState({
        isEmpty: true,
        data: null
    })


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
                <br/>
                <div className="container">
                    <div className="row">
                        <div className="col">
                            <div className="card">
                                <h5 className="card-header elegant-color-dark white-text text-center">Invoices</h5>
                                        <section className="text-center">
                                            <SidebarFilter handleShow={handleShow} handleClose={handleClose} handleSubmit={handleSubmit} shows={show} filterByTotal={filterByTotal} filterByDiscount={filterByDiscount} filterByPfr={filterByPfr} filterByInvoiceDate={filterByInvoiceDate} filterByWarningDate={filterByWarningDate}/>

                                            <Button variant="outline-primary" onClick={handleShow}>
                                                Manage filters
                                            </Button>
                                        </section>
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
                                        <Select
                                            className="react-select-container"
                                            classNamePrefix="react-select"
                                            isSearchable={false}
                                            options={sortedOption}
                                            onChange={handleSortedOptionChange}
                                            value={sortedOptionSelected}
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
                                            {invoices.map((item) => {
                                                const invoice = item.invoice
                                                return (
                                                    <tr>
                                                        <td className="text-center">{invoice.invoice_id}</td>
                                                        <td className="text-center">{invoice.customer_id}</td>
                                                        <td className="text-center">{invoice.business_name}</td>
                                                        <td className="text-center">{invoice.status}</td>
                                                        <td className="text-center">{invoice.invoice_date}</td>
                                                        <td className="text-center">{invoice.total}</td>
                                                        <td className="text-center">{invoice.discount}</td>
                                                    </tr>
                                                )
                                            })
                                            }
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