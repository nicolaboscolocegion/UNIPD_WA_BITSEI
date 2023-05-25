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
import Chart from 'chart.js/auto'
import Nav from 'react-bootstrap/Nav';


function ShowChart() {
    const [chart, setChart] = useState([]);             //Data for drawing the chart (obtained with the response)
    const [dataToSend, setDataToSend] = useState({});   //Data to send in the request
    //const chartRef = useRef(null);

    useEffect(() => {
        //Process request (the payload is dataToSend) to get data for drawing chart
        gate
            .getChartInvoiceByFilters(dataToSend)
            .then((response) => {
                setChart(response.data["chart"]);
            })
            .catch((error) => {
               toast.error("Something went wrong in invoices listing");
            });
        
        //Contruct the chart (based on chartType)
        const ctx = document.getElementById('myChart');
    
        if(chart.type == 1){
            console.log("TYPE 1");
            const myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                labels: chart.labels,
                datasets: [{
                    label: '# of Invoices',
                    data: chart.data,
                    borderWidth: 1
                }]
                },
                options: {
                scales: {
                    y: {
                    beginAtZero: true
                    }
                }
                }
            });

            return () => {
                myChart.destroy();
            };
        }
        if(chart.type == 2){
            console.log("TYPE 2");
            const myChart = new Chart(ctx, {
                type: 'line',
                data: {
                labels: chart.labels,
                datasets: [{
                    label: 'Total profit',
                    data: chart.data,
                    borderWidth: 1,
                    borderColor: 'rgb(75, 192, 192)'
                }]
                },
                options: {
                scales: {
                    y: {
                    beginAtZero: true
                    }
                }
                }
            });

            return () => {
                myChart.destroy();
            };
        }
    }, [dataToSend]);


    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleSubmit = () => {
        setShow(false);
        console.log("## Submit called ##");
    }
    
    const handleTabSelect = (selectedTab) => {
        setChartType(parseInt(selectedTab));
        setFilters();
    }

    //Request data (stored in dataToSend)
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
    const [chartType, setChartType] = useState(1)
    const [chartPeriod, setChartPeriod] = useState(1)

    //Function that fills dataToSend
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
        tmpDataToSend["chart_type"] = chartType;
        tmpDataToSend["chart_period"] = chartPeriod;
        setDataToSend(tmpDataToSend);
    }

    return(
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
                                            <SidebarFilter handleShow={handleShow} handleClose={handleClose} shows={show} filterByTotal={filterByTotal} filterByDiscount={filterByDiscount} filterByPfr={filterByPfr} filterByInvoiceDate={filterByInvoiceDate} filterByWarningDate={filterByWarningDate} setFilters={setFilters}/>

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
                                                "\nfilterByDiscount: " + filterByPfr.isEnabled + ", " + filterByPfr.fromValue + ", " + filterByPfr.toValue +
                                                "\nType: " + chartType + " Period: " + chartPeriod);
                                            }}
                                        >
                                            Console Log filters
                                        </Button>

                                    </div>
                                    <div className="container-fluid">
                                        
                                        <Nav fill variant="tabs" activeKey={chartType} defaultActiveKey="1" onSelect={handleTabSelect}>
                                            <Nav.Item>
                                                <Nav.Link eventKey="1">Invoices by Period</Nav.Link>
                                            </Nav.Item>
                                            <Nav.Item>
                                                <Nav.Link eventKey="2">Total by Period</Nav.Link>
                                            </Nav.Item>
                                        </Nav>

                                        <div className="chart-container">
                                            <canvas id="myChart" />
                                        </div>

                                        <div className="container"> 
                                            <span className="text-center">{chart.labels}    </span>
                                            <span className="text-center">{chart.data}   </span>
                                            <span className="text-center">{chart.type}   </span>
                                            <span className="text-center">{chart.period}   </span>
                                        </div>

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