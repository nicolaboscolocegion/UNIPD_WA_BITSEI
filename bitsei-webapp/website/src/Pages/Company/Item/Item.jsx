import React, {useEffect, useState} from "react";
import {useSelector, connect} from "react-redux";
import { useParams} from "react-router-dom";
import Chart from "chart.js/auto";
import { CategoryScale } from "chart.js";
import LineChart from "../../../Components/HomeChart/LineChart";
import gate from "../../../gate";
import {toast} from "react-toastify";
import { Data } from "./Data";
import PieChart from "../../../Components/HomeChart/PieChart";
import Image from "../../../Components/Image/Image";
import {faShoppingCart, faUsers, faTicketAlt, faDollarSign} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

function CompanyDetail() {
    const { company_id } = useParams();
    const companies = useSelector((state) => state.companies);
    const company = companies.items.filter((company) => company.company_id === parseInt(company_id))[0];
    const [dataToSend1, setDataToSend1] = useState({"chart_type": 2, "chart_period": 1});
    const [dataToSend2, setDataToSend2] = useState({"chart_type": 4, "chart_period": 1});
    const [dataToSend3, setDataToSend3] = useState({"chart_type": 5, "chart_period": 1});
    const [chart1, setChart1] = useState([]);
    const [chart2, setChart2] = useState([]);
    const [chart3, setChart3] = useState([]);

    useEffect(() => {
        console.log("Company ID: " + company_id);
        //Process request (the payload is dataToSend) to get data for drawing chart
        gate
            .getChartInvoiceByFilters(company_id, dataToSend1)
            .then((response) => {
                console.log(response.data["chart"]);
                setChart1(response.data["chart"]);
            })
            .catch((error) => {
                toast.error("Something went wrong in invoices listing");
            });

        gate
            .getChartInvoiceByFilters(company_id, dataToSend2)
            .then((response) => {
                console.log(response.data["chart"]);
                setChart2(response.data["chart"]);
            })
            .catch((error) => {
                toast.error("Something went wrong in invoices listing");
            });

        gate
            .getChartInvoiceByFilters(company_id, dataToSend3)
            .then((response) => {
                console.log(response.data["chart"]);
                setChart3(response.data["chart"]);
            })
            .catch((error) => {
                toast.error("Something went wrong in invoices listing");
            });
    }, []);

    const [chartData1, setChartData1] = useState({
        labels: chart1.labels,
        datasets: [
            {
                label: "Total profit",
                data: chart1.data,
                backgroundColor: [
                    "rgba(75,192,192,1)",
                    "&quot;#ecf0f1",
                    "#50AF95",
                    "#f3ba2f",
                    "#2a71d0"
                ],
                borderColor: "black",
                borderWidth: 2
            }
        ]
    });

    const [chartData2, setChartData2] = useState({
        labels: chart2.labels,
        datasets: [
            {
                label: "Total profit",
                data: chart2.data,
                backgroundColor: [
                    "rgba(75,192,192,1)",
                    "&quot;#ecf0f1",
                    "#50AF95",
                    "#f3ba2f",
                    "#2a71d0"
                ],
                borderColor: "black",
                borderWidth: 2
            }
        ]
    });

    const [chartData3, setChartData3] = useState({
        labels: chart3.labels,
        datasets: [
            {
                label: "Total profit",
                data: chart3.data,
                backgroundColor: [
                    "rgba(75,192,192,1)",
                    "&quot;#ecf0f1",
                    "#50AF95",
                    "#f3ba2f",
                    "#2a71d0"
                ],
                borderColor: "black",
                borderWidth: 2
            }
        ]
    });

    useEffect(() => {
        setChartData1({
            labels: chart1.labels,
            datasets: [
                {
                    label: "Total profit",
                    data: chart1.data,
                    backgroundColor: [
                        "rgba(75,192,192,1)",
                        "&quot;#ecf0f1",
                        "#50AF95",
                        "#f3ba2f",
                        "#2a71d0"
                    ],
                    borderColor: "black",
                    borderWidth: 2
                }
            ]
        });  
    },[chart1]);

    useEffect(() => {
        setChartData2({
            labels: chart2.labels,
            datasets: [
                {
                    label: "# of Invoices",
                    data: chart2.data,
                    backgroundColor: [
                        "rgba(75,192,192,1)",
                        "&quot;#ecf0f1",
                        "#50AF95",
                        "#f3ba2f",
                        "#2a71d0"
                    ],
                    borderColor: "black",
                    borderWidth: 2,
                    hoverOffset: 9
                }
            ]
        });  
    },[chart2]);

    useEffect(() => {
        setChartData3({
            labels: chart3.labels,
            datasets: [
                {
                    label: "Total profit",
                    data: chart3.data,
                    backgroundColor: [
                        "rgba(75,192,192,1)",
                        "&quot;#ecf0f1",
                        "#50AF95",
                        "#f3ba2f",
                        "#2a71d0"
                    ],
                    borderColor: "black",
                    borderWidth: 2,
                    hoverOffset: 9
                }
            ]
        });  
    },[chart3]);

    Chart.register(CategoryScale);

    return companies.pending ? ("Loading") : (
        <>
            <div className="container-fluid mx-auto mt-3 d-flex align-items-center justify-content-center">

                    <Image id={company.company_id}/>
                    <p className="d-inline mx-5 fs-4 fw-bold align-middle">Welcome back, <span className="badge bg-info text-dark">{company.business_name}</span></p>

            </div>
            <div className={"container-fluid mx-auto mt-3 d-flex align-items-center justify-content-center"}>
                <div className={"card homecard mb-1 w-49 "}>
                    <div className={"card-header"}>
                        <i class="fas fa-table me-1"></i>
                        Line Chart
                    </div>
                    <LineChart chartData={chartData1} />
                </div>
                <div className={"card homecard mb-1 w-24 mx-1 "}>
                    <div className={"card-header"}>
                        <i className="fas fa-table me-1"></i>
                        Pie Chart
                    </div>
                    <PieChart chartData={chartData2}/>
                </div>
                <div className={"card homecard mb-1 w-24 "}>
                    <div className={"card-header"}>
                        <i className="fas fa-table me-1"></i>
                        Pie Chart
                    </div>
                    <PieChart chartData={chartData3}/>
                </div>
            </div>

            <div className="container-fluid mx-auto mt-3 d-flex align-items-center justify-content-center">
            <div className="bottomcol-md-10 ">
                <div className="row ">
                    <div className="col-xl-3 col-lg-6">
                        <div className="bottomcard l-bg-cherry">
                            <div className="card-statistic-3 p-4">
                                <div className="card-icon card-icon-large">
                                    <FontAwesomeIcon icon={faShoppingCart} className="fs-icon m-1"/>
                                </div>
                                <div className="mb-4">
                                    <h5 className="card-title mb-0">New Orders</h5>
                                </div>
                                <div className="row align-items-center mb-2 d-flex">
                                    <div className="col-8">
                                        <h2 className="d-flex align-items-center mb-0">3,243</h2>
                                    </div>
                                    <div className="col-4 text-right">
              <span>
                12.5% <i className="fa fa-arrow-up" />
              </span>
                                    </div>
                                </div>
                                <div className="progress mt-1 " data-height={8} style={{ height: 8 }}>
                                    <div
                                        className="progress-bar l-bg-cyan"
                                        role="progressbar"
                                        data-width="25%"
                                        aria-valuenow={25}
                                        aria-valuemin={0}
                                        aria-valuemax={100}
                                        style={{ width: "25%" }}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-xl-3 col-lg-6">
                        <div className="bottomcard l-bg-blue-dark">
                            <div className="card-statistic-3 p-4">
                                <div className="card-icon card-icon-large">
                                    <FontAwesomeIcon icon={faUsers} className="fs-icon m-1"/>
                                </div>
                                <div className="mb-4">
                                    <h5 className="card-title mb-0">Customers</h5>
                                </div>
                                <div className="row align-items-center mb-2 d-flex">
                                    <div className="col-8">
                                        <h2 className="d-flex align-items-center mb-0">15.07k</h2>
                                    </div>
                                    <div className="col-4 text-right">
              <span>
                9.23% <i className="fa fa-arrow-up" />
              </span>
                                    </div>
                                </div>
                                <div className="progress mt-1 " data-height={8} style={{ height: 8 }}>
                                    <div
                                        className="progress-bar l-bg-green"
                                        role="progressbar"
                                        data-width="25%"
                                        aria-valuenow={25}
                                        aria-valuemin={0}
                                        aria-valuemax={100}
                                        style={{ width: "25%" }}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-xl-3 col-lg-6">
                        <div className="bottomcard l-bg-green-dark">
                            <div className="card-statistic-3 p-4">
                                <div className="card-icon card-icon-large">
                                    <FontAwesomeIcon icon={faTicketAlt} className="fs-icon m-1"/>
                                </div>
                                <div className="mb-4">
                                    <h5 className="card-title mb-0">Ticket Resolved</h5>
                                </div>
                                <div className="row align-items-center mb-2 d-flex">
                                    <div className="col-8">
                                        <h2 className="d-flex align-items-center mb-0">578</h2>
                                    </div>
                                    <div className="col-4 text-right">
              <span>
                10% <i className="fa fa-arrow-up" />
              </span>
                                    </div>
                                </div>
                                <div className="progress mt-1 " data-height={8} style={{ height: 8 }}>
                                    <div
                                        className="progress-bar l-bg-orange"
                                        role="progressbar"
                                        data-width="25%"
                                        aria-valuenow={25}
                                        aria-valuemin={0}
                                        aria-valuemax={100}
                                        style={{ width: "25%" }}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="col-xl-3 col-lg-6">
                        <div className="bottomcard l-bg-orange-dark">
                            <div className="card-statistic-3 p-4">
                                <div className="card-icon card-icon-large">
                                    <FontAwesomeIcon icon={faDollarSign} className="fs-icon m-1"/>
                                </div>
                                <div className="mb-4">
                                    <h5 className="card-title mb-0">Revenue Today</h5>
                                </div>
                                <div className="row align-items-center mb-2 d-flex">
                                    <div className="col-8">
                                        <h2 className="d-flex align-items-center mb-0">$11.61k</h2>
                                    </div>
                                    <div className="col-4 text-right">
              <span>
                2.5% <i className="fa fa-arrow-up" />
              </span>
                                    </div>
                                </div>
                                <div className="progress mt-1 " data-height={8} style={{ height: 8 }}>
                                    <div
                                        className="progress-bar l-bg-cyan"
                                        role="progressbar"
                                        data-width="25%"
                                        aria-valuenow={25}
                                        aria-valuemin={0}
                                        aria-valuemax={100}
                                        style={{ width: "25%" }}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </div>

        </>
    )
}

export default connect(null, {})(CompanyDetail);