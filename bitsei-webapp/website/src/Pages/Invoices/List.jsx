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
import {FaCheck, FaDollarSign, FaEye, FaFilePdf, FaLock, FaLockOpen, FaPencilAlt, FaTrash} from "react-icons/fa";
import {AiFillLock} from "react-icons/ai";
import {useParams} from "react-router-dom";
import {BsFiletypeXml} from "react-icons/bs";


// TODO: Add validation for all fields
// TODO: Add error handling for all fields
// TODO: Add loading for creating company
// TODO: HTML CSS for this page
function ListInvoices() {
    const {company_id} = useParams();
    const [invoices, setInvoices] = useState([]);
    const [dataToSend, setDataToSend] = useState({});
    const [refresh, setRefresh] = useState(false);

    useEffect(() => {
        console.log("listing invoices onLoad TO REMOVE");

        gate
            .getInvoicesByFilters(company_id, dataToSend)
            .then((response) => {
                setInvoices(response.data["resource-list"]);
                setRefresh(!refresh);
            })
            .catch((error) => {
               toast.error("Something went wrong in invoices listing");
            });


    }, [dataToSend, refresh]);

    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleSubmit = () => {
        setShow(false);
        console.log("## hSubmit called ##");
    }

    const [orderByOption, setOrderByOption] = useState([
        {value: 0, label: "Status"},
        {value: 1, label: "Invoice ID"},
        {value: 2, label: "Customer Name"},
        {value: 3, label: "Invoice Date"},
        {value: 4, label: "Total"}]);
    const [sortedOption, setSortedOption] = useState([
        {value: 1, label: "Ascending"},
        {value: 2, label: "Descending"}]);

    const [orderByOptionSelected, setOrderByOptionSelected] = useState(orderByOption[1]);
    const [sortedOptionSelected, setSortedOptionSelected] = useState(sortedOption[0]);

    const handleOrderByOptionChange = (selected) => {
        // if the sorted set is supported
        console.log("sortedOptionSelected: " + sortedOptionSelected.value + " - " + sortedOptionSelected.label);
        if(orderByOption.indexOf(selected) > -1) {
            setOrderByOptionSelected(selected);
            if(selected.value === 0) {
                console.log("Order by Status found - sortedOptionSelected: " + sortedOptionSelected.value + ", " + sortedOptionSelected.label);
                if(parseInt(sortedOptionSelected.value) === 2) {
                    const sortedInvoices = [...invoices].sort((a, b) => b.invoice.status - a.invoice.status);
                    setInvoices(sortedInvoices);
                }
                else {
                    const sortedInvoices = [...invoices].sort((a, b) => a.invoice.status - b.invoice.status);
                    setInvoices(sortedInvoices);
                }
            }

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
    },[sortedOptionSelected, invoices, refresh]);

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

    const [filterByCustomerId, setFilterByCustomerId] = useState({
        isEnabled: false,
        fromCustomerId: null
    })

    const [filterByStatus, setFilterByStatus] = useState({
        isEnabled: false,
        fromStatus: null
    })

    const setFilters = () => {
        const tmpDataToSend = {};
        if(filterByTotal.isEnabled === true) {
            tmpDataToSend["fromTotal"] = filterByTotal.fromValue;
            tmpDataToSend["toTotal"] = filterByTotal.toValue;
        }

        if(filterByDiscount.isEnabled === true) {
            tmpDataToSend["fromDiscount"] = filterByDiscount.fromValue;
            tmpDataToSend["toDiscount"] = filterByDiscount.toValue;
        }

        if(filterByPfr.isEnabled === true) {
            tmpDataToSend["fromPfr"] = filterByPfr.fromValue;
            tmpDataToSend["toPfr"] = filterByPfr.toValue;
        }

        if(filterByInvoiceDate.isEnabled === true) {
            tmpDataToSend["fromInvoiceDate"] = filterByInvoiceDate.fromValue;
            tmpDataToSend["toInvoiceDate"] = filterByInvoiceDate.toValue;
        }

        if(filterByWarningDate.isEnabled === true) {
            tmpDataToSend["fromWarningDate"] = filterByWarningDate.fromValue;
            tmpDataToSend["toWarningDate"] = filterByWarningDate.toValue;
        }

        if(filterByCustomerId.isEnabled) {
            let customerId = ""
            let countCustomerId = 0;
            for(let option in filterByCustomerId.fromCustomerId){
                console.log(filterByCustomerId.fromCustomerId[option], filterByCustomerId.fromCustomerId[option].label)
                if(countCustomerId > 0)
                    customerId += "-";
                customerId += filterByCustomerId.fromCustomerId[option].value.toString();
                countCustomerId++;
            }
            if(countCustomerId > 0)
                tmpDataToSend["fromCustomerId"] = customerId;
        }

        if(filterByStatus.isEnabled) {
            let status = ""
            let countStatus = 0;
            for(let option in filterByStatus.fromStatus){
                console.log(filterByStatus.fromStatus[option], filterByStatus.fromStatus[option].label)
                if(countStatus > 0)
                    status += "-";
                status += filterByStatus.fromStatus[option].value.toString();
                countStatus++;
            }
            if(countStatus > 0)
                tmpDataToSend["fromStatus"] = status;
        }

        setDataToSend(tmpDataToSend);
    }

    const handleCloseInvoice = (invoice_id) => {
        gate
            .closeInvoice(company_id, invoice_id)
            .then((response) => {
                toast.success("Invoice closed correctly!");
                setRefresh(!refresh);
            })
            .catch((error) => {
                toast.error("Something went wrong in closing invoice");
                setRefresh(!refresh);
            });
    }

    const handleGenerateInvoice = (invoice_id) => {
        gate
            .generateInvoice(company_id, invoice_id)
            .then((response) => {
                toast.success("Invoice generated correctly!");
                setRefresh(!refresh);
            })
            .catch((error) => {
                toast.error("Something went wrong in generating invoice");
                setRefresh(!refresh);
            });
    }

    const handleGetInvoiceDocument = (invoice_id, document_type) => {
        gate
            .getInvoiceDocument(company_id, invoice_id, document_type)
            .then((response) => {
                toast.success("Invoice documentation fetched correctly!");
                setRefresh(!refresh);
            })
            .catch((error) => {
                toast.error("Something went wrong in fetching invoice documentation");
                setRefresh(!refresh);
            });
    }

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
                                <h5 className="card-header elegant-color-dark white-text text-center">Invoices</h5>
                                        <section className="text-center">
                                            <SidebarFilter handleShow={handleShow} handleClose={handleClose} shows={show} filterByTotal={filterByTotal} filterByDiscount={filterByDiscount} filterByPfr={filterByPfr} filterByInvoiceDate={filterByInvoiceDate} filterByWarningDate={filterByWarningDate} filterByCustomerId={filterByCustomerId} filterByStatus={filterByStatus} setFilters={setFilters} listInvoice={ListInvoices}/>

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
                                        <Button
                                            onClick={() => {
                                                console.log("filterByTotal: " + filterByTotal.isEnabled + ", " + filterByTotal.fromValue + ", " + filterByTotal.toValue +
                                                "\nfilterByDiscount: " + filterByDiscount.isEnabled + ", " + filterByDiscount.fromValue + ", " + filterByDiscount.toValue +
                                                "\nfilterByInvoiceDate: " + filterByInvoiceDate.isEnabled + ", " + filterByInvoiceDate.fromValue + ", " + filterByInvoiceDate.toValue +
                                                "\nfilterByWarningDate: " + filterByWarningDate.isEnabled + ", " + filterByWarningDate.fromValue + ", " + filterByWarningDate.toValue +
                                                "\nfilterByDiscount: " + filterByPfr.isEnabled + ", " + filterByPfr.fromValue + ", " + filterByPfr.toValue +
                                                "\nfilterByCustomerId: " + filterByCustomerId.isEnabled + ", " + filterByCustomerId.fromCustomerId);
                                            }}
                                        >
                                            Console Log filters
                                        </Button>

                                    </div>
                                    <div className="table-responsive">
                                        {invoices.length === 0 ? (
                                            <div className="container">
                                                <table className="table table-hover table-bordered">
                                                    <thead className="indigo lighten-5">
                                                    <tr>
                                                        <th className="text-center">Invoice ID</th>
                                                        <th className="text-center">Customer Name</th>
                                                        <th className="text-center">Status</th>
                                                        <th className="text-center">Invoice Date</th>
                                                        <th className="text-center">Warning Date</th>
                                                        <th className="text-center">Discount</th>
                                                        <th className="text-center">Total</th>
                                                        <th className="text-center">Actions</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody></tbody>
                                                </table>
                                                <h4 style={{ flex: 1, justifyContent: 'center', textAlign: "center", alignItems:"center", lineHeight:"100px"}}>No invoice retrieved</h4>
                                            </div>
                                        ) : (
                                            <table className="table table-hover table-bordered">
                                                <thead className="indigo lighten-5">
                                                <tr>
                                                    <th className="text-center">Invoice ID</th>
                                                    <th className="text-center">Customer Name</th>
                                                    <th className="text-center">Status</th>
                                                    <th className="text-center">Invoice Date</th>
                                                    <th className="text-center">Warning Date</th>
                                                    <th className="text-center">Discount</th>
                                                    <th className="text-center">Total</th>
                                                    <th className="text-center">Actions</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                {invoices.map((item) => {
                                                    const invoice = item.invoice;
                                                    let statusInvoice;
                                                    let statusIcon;
                                                    let warningPdfIcon;
                                                    let invoicePdfIcon;
                                                    let invoiceXmlIcon;
                                                    if (invoice.status === 0) {
                                                        statusInvoice = "Open";
                                                        statusIcon =
                                                                <button
                                                                    onClick={() => handleCloseInvoice(invoice.invoice_id)}
                                                                    title = "Click to change the status to 'Pending'"
                                                                >
                                                                    <AiFillLock />
                                                                </button>;
                                                    } else if (invoice.status === 1) {
                                                        statusInvoice = "Pending";
                                                        statusIcon =
                                                            <button
                                                                onClick={() => handleGenerateInvoice(invoice.invoice_id)}
                                                                title = "Click to change the status to 'Closed'"
                                                            >
                                                                <FaDollarSign />
                                                            </button>;
                                                        warningPdfIcon =
                                                            <button
                                                                onClick={() => toast.success("handleGetWarningPdfFile(invoice.invoice_id)")}
                                                                title = "Click to open the Warning File PDF"
                                                            >
                                                                <FaFilePdf />
                                                            </button>;
                                                    } else {
                                                        statusInvoice = "Closed";
                                                        statusIcon = <FaCheck />;
                                                        warningPdfIcon =
                                                            <button
                                                                onClick={() => handleGetInvoiceDocument(invoice.invoice_id, 0)}
                                                                title = "Click to open the Warning File PDF"
                                                            >
                                                                <FaFilePdf />
                                                            </button>;
                                                        invoicePdfIcon =
                                                            <button
                                                                onClick={() => handleGetInvoiceDocument(invoice.invoice_id, 1)}
                                                                title = "Click to open the Invoice File PDF"
                                                            >
                                                                <FaFilePdf />
                                                            </button>;
                                                        invoiceXmlIcon =
                                                            <button
                                                                onClick={() => handleGetInvoiceDocument(invoice.invoice_id, 2)}
                                                                title = "Click to open the Invoice File XML"
                                                            >
                                                                <BsFiletypeXml />
                                                            </button>;
                                                    }

                                                    const isEditable = (invoice.status === 0);

                                                    return (
                                                        <tr key={invoice.invoice_id}>
                                                            <td className="text-center">{invoice.invoice_id}</td>
                                                            <td className="text-center">{invoice.business_name}</td>
                                                            <td className="text-center">{statusInvoice} {statusIcon}</td>
                                                            <td className="text-center">{invoice.invoice_date} {invoicePdfIcon} {invoiceXmlIcon}</td>
                                                            <td className="text-center">{invoice.warning_date} {warningPdfIcon}</td>
                                                            <td className="text-center">{invoice.discount}</td>
                                                            <td className="text-center">{invoice.total}</td>
                                                            <td className="text-center" style={{ verticalAlign: 'top' }}>
                                                                    <button
                                                                        onClick={() => toast.success("handleShowInvoiceDetails(invoice.invoice_id)")}
                                                                        title = "Click to see the invoice's details"
                                                                    >
                                                                        <FaEye />
                                                                    </button>
                                                                    <button
                                                                        onClick={() => toast.success("handleEditInvoice(invoice.invoice_id)")}
                                                                        title = "Click to edit the invoice"
                                                                        disabled={!isEditable}
                                                                    >
                                                                        <FaPencilAlt />
                                                                    </button>
                                                                    <button
                                                                        onClick={() => toast.success("handleDeleteInvoice(invoice.invoice_id)")}
                                                                        title = "Click to delete the invoice"
                                                                        disabled={!isEditable}
                                                                    >
                                                                        <FaTrash />
                                                                    </button>
                                                            </td>
                                                        </tr>
                                                    );
                                                })}
                                                </tbody>
                                            </table>
                                        )}
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

export default ListInvoices;
