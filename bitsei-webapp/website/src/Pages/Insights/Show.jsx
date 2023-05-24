import React, {useEffect, useRef, useState} from "react";
import Select from 'react-select';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../Invoices/App.css';
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import {toast} from "react-toastify";
import {clearCompanies} from "../../Store/companies/listsThunk";
import gate from "../../gate";
import {history} from "../../index";
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
function ShowChart() {
    const [invoices, setInvoices] = useState([]);
    const [dataToSend, setDataToSend] = useState({});

    useEffect(() => {
        console.log("listing invoices onLoad TO REMOVE");

        gate
            .getInvoicesByFilters(dataToSend)
            .then((response) => {
                setInvoices(response.data["resource-list"]);
            })
            .catch((error) => {
               toast.error("Something went wrong in invoices listing");
            });

    }, [dataToSend]);

    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleSubmit = () => {
        setShow(false);
        console.log("## hSubmit called ##");
    }

    /*useEffect(() => {
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
    };*/

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

    return (
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
                                <h5 className="card-header elegant-color-dark white-text text-center">Insights</h5>
                                        <section className="text-center">
                                            <SidebarFilter handleShow={handleShow} handleClose={handleClose} shows={show} filterByTotal={filterByTotal} filterByDiscount={filterByDiscount} filterByPfr={filterByPfr} filterByInvoiceDate={filterByInvoiceDate} filterByWarningDate={filterByWarningDate} dataToSend={dataToSend}/>

                                            <Button variant="outline-primary" onClick={handleShow}>
                                                Manage filters
                                            </Button>
                                        </section>
                                <div className="card-body">
                                    <div className="dropdown float-left">
                                        
                                        <Button
                                            onClick={() => {
                                                console.log("filterByTotal: " + filterByTotal.isEnabled + ", " + filterByTotal.fromValue + ", " + filterByTotal.toValue +
                                                "\nfilterByDiscount: " + filterByDiscount.isEnabled + ", " + filterByDiscount.fromValue + ", " + filterByDiscount.toValue +
                                                "\nfilterByInvoiceDate: " + filterByInvoiceDate.isEnabled + ", " + filterByInvoiceDate.fromValue + ", " + filterByInvoiceDate.toValue +
                                                "\nfilterByWarningDate: " + filterByWarningDate.isEnabled + ", " + filterByWarningDate.fromValue + ", " + filterByWarningDate.toValue +
                                                "\nfilterByDiscount: " + filterByPfr.isEnabled + ", " + filterByPfr.fromValue + ", " + filterByPfr.toValue);
                                            }}
                                        >
                                            Console Log filters
                                        </Button>

                                    </div>
                                    <div className="container-fluid">
                                        
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

export default ShowChart;