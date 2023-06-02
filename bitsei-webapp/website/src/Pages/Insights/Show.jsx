import React, {useEffect, useState} from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import '../Invoices/App.css';
import {toast} from "react-toastify";
import gate from "../../gate";
import SidebarFilter from "../../Components/SidebarFilters/SidebarFilter";
import Button from 'react-bootstrap/Button';
import Chart from 'chart.js/auto'
import Nav from 'react-bootstrap/Nav';
import Dropdown from 'react-bootstrap/Dropdown';
import {useParams} from "react-router-dom";

// TODO: Refactor the logic section of code
// TODO: Use functions to generate the chart based on that
// TODO: Find a way to remove the link and script tags from the component, it's not a good practice
// TODO: Suggestions: 1. Use complex objects to store the setting for the chart
// TODO:              2. Think about another way to implement setFilters() function
function ShowChart() {
    const [chart, setChart] = useState([]);             //Data for drawing the chart (obtained with the response)
    const [dataToSend, setDataToSend] = useState({"chart_type": 1, "chart_period": 1});   //Data to send in the request
    const [chartType, setChartType] = useState(1)
    const [chartPeriod, setChartPeriod] = useState(1)
    const [show, setShow] = useState(false);
    const [showTable, setShowTable] = useState(false);
    const {company_id} = useParams();
    var count = -1;
    const mapPeriods = {
        1: "Months",
        2: "Years",
        3: "Days",
    };

    useEffect(() => {
        console.log("Company ID: " + company_id);
        //Process request (the payload is dataToSend) to get data for drawing chart
        gate
            .getChartInvoiceByFilters(company_id, dataToSend)
            .then((response) => {
                console.log(response.data["chart"]);
                setChart(response.data["chart"]);
            })
            .catch((error) => {
                toast.error("Something went wrong in invoices listing");
            });
    }, [dataToSend]);

    const plugin = {
        id: 'customCanvasBackgroundColor',
        beforeDraw: (chart, args, options) => {
          const {ctx} = chart;
          ctx.save();
          ctx.globalCompositeOperation = 'destination-over';
          ctx.fillStyle = options.color || '#99ffff';
          ctx.fillRect(0, 0, chart.width, chart.height);
          ctx.restore();
        }
    };

    useEffect(() => {
        const ctx = document.getElementById('myChart');
        if (chart.type === 1) {
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
                    },
                    plugins: {
                        customCanvasBackgroundColor: {
                          color: 'white',
                        }
                    }
                },
                plugins: [plugin]
            });
            return () => {
                myChart.destroy();
            };
        } else if (chart.type === 2) {
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
        } else if (chart.type === 3) {
            const myChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: chart.labels,
                    datasets: [{
                        label: 'Average discount',
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
        } else if (chart.type === 4) {
            console.log("Type: "+chart.type+" | Labels: "+chart.labels+" | Data: "+chart.data);
            const myChart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: chart.labels,
                    datasets: [{
                        label: '# of Invoices',
                        data: chart.data,
                        borderWidth: 2,
                        hoverOffset: 4
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
        } else if (chart.type === 5) {
            console.log("Type: "+chart.type+" | Labels: "+chart.labels+" | Data: "+chart.data);
            const myChart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: chart.labels,
                    datasets: [{
                        label: 'Total profit',
                        data: chart.data,
                        borderWidth: 2,
                        hoverOffset: 4
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
    }, [chart]);

    useEffect(() => {
        count = -1;
        setShowTable(true);
    },[chart]);

    useEffect(() => {
        setFilters();
    }, [chartType, chartPeriod]);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleSubmit = () => {
        setShow(false);
        console.log("## Submit called ##");
    }

    const handleTabSelect = (selectedTab) => {
        setChartType(parseInt(selectedTab));
    }
    
    const handlePeriodSelect = (selectedItem) => {
        setChartPeriod(parseInt(selectedItem));
        console.log(parseInt(selectedItem))
    }

    function buttonTitle(){
        console.log(mapPeriods[chart.period]);
        return mapPeriods.hasOwnProperty(chart.period) ? (mapPeriods[chart.period]) : (console.log("Error"));
    }

    const handleClick = () => {
        var canvas = document.getElementById("myChart");
        const dataURL = canvas.toDataURL("image/png");
        
        const link = document.createElement("a");
        link.href = dataURL;//canvas.toDataURL("image/png").replace("image/png", "image/octet-stream");;
        link.download = "chart.png";
        link.click();
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
    const [filterByCustomerId, setFilterByCustomerId] = useState({
        isEnabled: false,
        fromCustomerId: null
    })
    const [filterByStatus, setFilterByStatus] = useState({
        isEnabled: false,
        fromStatus: null
    })


    //Function that fills dataToSend
    const setFilters = () => {
        const tmpDataToSend = {};
        if (filterByTotal.isEnabled === true) {
            tmpDataToSend["fromTotal"] = filterByTotal.fromValue;
            tmpDataToSend["toTotal"] = filterByTotal.toValue;
        }

        if (filterByDiscount.isEnabled === true) {
            tmpDataToSend["fromDiscount"] = filterByDiscount.fromValue;
            tmpDataToSend["toDiscount"] = filterByDiscount.toValue;
        }

        if (filterByPfr.isEnabled === true) {
            tmpDataToSend["fromPfr"] = filterByPfr.fromValue;
            tmpDataToSend["toPfr"] = filterByPfr.toValue;
        }

        if (filterByInvoiceDate.isEnabled === true) {
            tmpDataToSend["fromInvoiceDate"] = filterByInvoiceDate.fromValue;
            tmpDataToSend["toInvoiceDate"] = filterByInvoiceDate.toValue;
        }

        if (filterByWarningDate.isEnabled === true) {
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
        tmpDataToSend["chart_type"] = chartType;
        tmpDataToSend["chart_period"] = chartPeriod;
        setDataToSend(tmpDataToSend);
    }

    return (
        <>
            <head>
                <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.2/css/mdb.min.css"
                      rel="stylesheet"/>
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
                                    <SidebarFilter handleShow={handleShow} handleClose={handleClose} shows={show}
                                                   filterByTotal={filterByTotal} filterByDiscount={filterByDiscount}
                                                   filterByPfr={filterByPfr} filterByInvoiceDate={filterByInvoiceDate}
                                                   filterByWarningDate={filterByWarningDate} filterByCustomerId={filterByCustomerId}
                                                   filterByStatus={filterByStatus} setFilters={setFilters}/>
                                    <div className="d-flex justify-content-between mt-3 mx-5">
                                        <Button variant="outline-primary" id="downButton" onClick={handleClick}>
                                            Download
                                        </Button>
                                        <Button variant="outline-primary">
                                            Export
                                        </Button>
                                        <Button variant="outline-primary" onClick={handleShow}>
                                            Manage filters
                                        </Button>
                                    </div>
                                </section>
                                <div className="card-body">
                                    <div className="container-fluid">
                                        <Nav fill variant="tabs" activeKey={chartType} defaultActiveKey="1"
                                             onSelect={handleTabSelect}>
                                            <Nav.Item>
                                                <Nav.Link eventKey="1">Invoices by Period</Nav.Link>
                                            </Nav.Item>
                                            <Nav.Item>
                                                <Nav.Link eventKey="2">Total by Period</Nav.Link>
                                            </Nav.Item>
                                            <Nav.Item>
                                                <Nav.Link eventKey="3">Discount by Period</Nav.Link>
                                            </Nav.Item>
                                            <Nav.Item>
                                                <Nav.Link eventKey="4">Invoices by Customer</Nav.Link>
                                            </Nav.Item>
                                            <Nav.Item>
                                                <Nav.Link eventKey="5">Total by Customer</Nav.Link>
                                            </Nav.Item>
                                        </Nav>

                                        <div className="chart-container w-75 h-75 mx-auto">
                                            <canvas id="myChart" />
                                        </div>

                                        <div className="dropdown mb-3">
                                            <div className="row d-flex justify-content-end">
                                                <div className="col col-auto d-flex align-items-center">
                                                    Group by: 
                                                </div>
                                                <div className="col col-auto">
                                                    <Dropdown onSelect={handlePeriodSelect}>
                                                        {mapPeriods.hasOwnProperty(chart.period) ? (
                                                            <Dropdown.Toggle variant="outline-primary" id="dropdown-basic">
                                                                {mapPeriods[chart.period]}
                                                            </Dropdown.Toggle>
                                                        ) : (
                                                            <Dropdown.Toggle variant="outline-primary" id="dropdown-basic">
                                                                Error
                                                            </Dropdown.Toggle>
                                                        )}
                                                        <Dropdown.Menu>
                                                            <Dropdown.Item eventKey="1">Months</Dropdown.Item>
                                                            <Dropdown.Item eventKey="2">Years</Dropdown.Item>
                                                            <Dropdown.Item eventKey="3">Days</Dropdown.Item>
                                                        </Dropdown.Menu>
                                                    </Dropdown>
                                                </div>
                                            </div>
                                        </div>

                                        {/*<div className="container">
                                            <span className="text-center">Type: {chart.type}     </span>
                                            <span className="text-center">Period: {chart.period}     </span>
                                            <span className="text-center">Labels: {chart.labels}      </span>
                                            <span className="text-center">Data: {chart.data}     </span>
                                        </div>*/}
                                        <h3 className="text-center mb-3">Table view</h3>
                                        {showTable && (
                                        <div className="table-responsive d-flex justify-content-center">
                                            <table className="table table-hover table-bordered table-sm w-50">
                                                <thead className="indigo lighten-5">
                                                <tr>
                                                    <th className="text-center">Labels</th>
                                                    <th className="text-center">Data</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                {
                                                    
                                                    (chart.labels)?.map((item) => {
                                                        count++;
                                                        return (
                                                            <tr>
                                                                <td className="text-center">{item}</td>
                                                                <td className="text-center">{chart.data[count]}</td>
                                                            </tr>
                                                        )
                                                    })
                                                }
                                                </tbody>
                                            </table>
                                        </div>
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

export default ShowChart;