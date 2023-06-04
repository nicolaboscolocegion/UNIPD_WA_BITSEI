import React, {useEffect, useState} from "react";
import {useSelector, connect} from "react-redux";
import {useParams} from "react-router-dom";
import Chart from "chart.js/auto";
import {CategoryScale} from "chart.js";
import LineChart from "../../../Components/HomeChart/LineChart";
import gate from "../../../gate";
import {toast} from "react-toastify";
import PieChart from "../../../Components/HomeChart/PieChart";
import Image from "../../../Components/Image/Image";
import {
    faEuroSign,
    faClockFour,
    faMoneyBillTrendUp,
    faPeopleGroup
} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

function CompanyDetail() {
    const {company_id} = useParams();
    const companies = useSelector((state) => state.companies);
    const company = companies.items.filter((company) => company.company_id === parseInt(company_id))[0];
    const [chart1, setChart1] = useState([]);
    const [chart2, setChart2] = useState([]);
    const [chart3, setChart3] = useState([]);
    const [homeData, setHomeData] = useState([]);

    useEffect(() => {
        gate
            .getChartInvoiceByFilters(company_id, {"chart_type": 2, "chart_period": 1})
            .then((response) => {
                setChart1(response.data["chart"]);
            })
            .catch((error) => {
                toast.error("Something went wrong in invoices listing");
            });

        gate
            .getChartInvoiceByFilters(company_id, {"chart_type": 4, "chart_period": 1})
            .then((response) => {
                console.log(response.data["chart"]);
                setChart2(response.data["chart"]);
            })
            .catch((error) => {
                toast.error("Something went wrong in invoices listing");
            });

        gate
            .getChartInvoiceByFilters(company_id, {"chart_type": 5, "chart_period": 1})
            .then((response) => {
                console.log(response.data["chart"]);
                setChart3(response.data["chart"]);
            })
            .catch((error) => {
                toast.error("Something went wrong in invoices listing");
            });

        gate
            .getHomeData(company_id)
            .then((response) => {
                setHomeData(response.data["home-data"]);
            })
            .catch((error) => {
                toast.error("Something went wrong in invoices listing");
            });

    }, [company_id]);


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
    }, [chart1]);

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
    }, [chart2]);

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
    }, [chart3]);

    Chart.register(CategoryScale);

    return companies.pending ? ("Loading") : (
        <>
            <div className="container-fluid mx-auto mt-3 d-flex align-items-center justify-content-center">

                <Image id={company.company_id}/>
                <p className="d-inline mx-5 fs-4 fw-bold align-middle">Welcome back, <span
                    className="badge bg-info text-dark">{company.business_name}</span></p>

            </div>
            <div className={"container-fluid mx-auto mt-3 d-flex align-items-center justify-content-center"}>
                <div className={"card homecard mb-1 w-49 "}>
                    <div className={"card-header"}>
                        <i className="fas fa-table me-1"></i>
                        Line Chart
                    </div>
                    <LineChart chartData={chartData1}/>
                </div>
                <div className={"card homecard mb-1 w-24 mx-1 "}>
                    <div className={"card-header"}>
                        <i className="fas fa-table me-1"></i>
                        Pie Chart
                    </div>
                    <PieChart chartData={chartData2} textData="Number of invoices by customer" />
                </div>
                <div className={"card homecard mb-1 w-24 "}>
                    <div className={"card-header"}>
                        <i className="fas fa-table me-1"></i>
                        Pie Chart
                    </div>
                    <PieChart chartData={chartData3} textData="Total profit by customer" />
                </div>
            </div>

            <div className="container-fluid mx-auto mt-3 d-flex align-items-center justify-content-center">
                <div className="bottomcol-md-10 ">
                    <div className="row ">
                        <div className="col-xl-3 col-lg-6">
                            <div className="bottomcard l-bg-blue-dark">
                                <div className="card-statistic-3 p-4">
                                    <div className="card-icon card-icon-large">
                                        <FontAwesomeIcon icon={faEuroSign} className="fs-icon mx-3"/>
                                    </div>
                                    <div className="mb-4">
                                        <h5 className="card-title mb-0">Total revenues</h5>
                                    </div>
                                    <div className="row align-items-center mb-2 d-flex">
                                        <div className="col-8 d-flex">
                                            <h2 className="d-flex align-items-center mb-0 d-inline">{(homeData.total * 1).toFixed(2)}&euro;</h2>
                                            <p className="d-flex align-items-center mb-0 d-inline align-text-bottom">&nbsp;&nbsp;(goal
                                                85.000&euro;)</p>
                                        </div>
                                        <div className="col-4 text-right">
              <span>
                {(homeData.total / 850).toFixed(2)}%<i className="fa fa-arrow-up"/>
              </span>
                                        </div>
                                    </div>
                                    <div className="progress mt-1 " data-height={8} style={{height: 8}}>
                                        <div
                                            className="progress-bar l-bg-cyan"
                                            role="progressbar"
                                            data-width={`${(parseInt(homeData.total) / 850)}%`}
                                            aria-valuenow={homeData.total}
                                            aria-valuemin={0}
                                            aria-valuemax={85000}
                                            style={{width: homeData.total / 850 + "%"}}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-xl-3 col-lg-6">
                            {parseInt(homeData.closed_inv) === 0 ? (
                                <div className="bottomcard l-bg-green">

                                    <div className="card-statistic-3 p-4">
                                        <div className="card-icon card-icon-large">
                                            <FontAwesomeIcon icon={faClockFour} className="fs-icon m-1 mx-2"/>
                                        </div>
                                        <div className="mb-4">
                                            <h5 className="card-title mb-0">Expiring closed invoices</h5>
                                        </div>
                                        <div className="row align-items-center mb-2 d-flex">
                                            <div className="col-8">
                                                <h2 className="d-flex align-items-center mb-0">{homeData.closed_inv}</h2>
                                            </div>
                                            <div className="col-4 text-right">
              <span>
                 <i className="fa fa-arrow-up"/>
              </span>
                                            </div>
                                        </div>
                                        <div className="progress mt-1 invisible" data-height={8} style={{height: 8}}>
                                            <div
                                                className="progress-bar l-bg-green"
                                                role="progressbar"
                                                data-width="25%"
                                                aria-valuenow={25}
                                                aria-valuemin={0}
                                                aria-valuemax={100}
                                                style={{width: "25%"}}
                                            />
                                        </div>
                                    </div>
                                </div>
                            ) : (
                                <div className="bottomcard l-bg-red">

                                    <div className="card-statistic-3 p-4">
                                        <div className="card-icon card-icon-large">
                                            <FontAwesomeIcon icon={faClockFour} className="fs-icon m-1 mx-2"/>
                                        </div>
                                        <div className="mb-4">
                                            <h5 className="card-title mb-0">Expiring closed invoices</h5>
                                        </div>
                                        <div className="row align-items-center mb-2 d-flex">
                                            <div className="col-8">
                                                <h2 className="d-flex align-items-center mb-0">{homeData.closed_inv}</h2>
                                            </div>
                                            <div className="col-4 text-right">
              <span>
                 <i className="fa fa-arrow-up"/>
              </span>
                                            </div>
                                        </div>
                                        <div className="progress mt-1 invisible" data-height={8} style={{height: 8}}>
                                            <div
                                                className="progress-bar l-bg-green"
                                                role="progressbar"
                                                data-width="25%"
                                                aria-valuenow={25}
                                                aria-valuemin={0}
                                                aria-valuemax={100}
                                                style={{width: "25%"}}
                                            />
                                        </div>
                                    </div>
                                </div>
                            )}
                        </div>
                        <div className="col-xl-3 col-lg-6">
                            <div className="bottomcard l-bg-cherry">
                                <div className="card-statistic-3 p-4">
                                    <div className="card-icon card-icon-large">
                                        <FontAwesomeIcon icon={faMoneyBillTrendUp} className="fs-icon m-1 mx-2"/>
                                    </div>
                                    <div className="mb-1">
                                        <h5 className="card-title mb-0">Most profitable customer</h5>
                                    </div>
                                    <div className="row align-items-center mb-1 d-flex">
                                        <div className="col-8">
                                            <p className="d-flex align-items-center mb-0 fw-bold">{homeData.most_money_cust}</p>
                                            <h2 className="d-flex align-items-center mb-0">{(homeData.most_money_cust_val * 1).toFixed(2)} &euro;</h2>
                                        </div>
                                        <div className="col-4 text-right">
              <span>
                 <i className="fa fa-arrow-up"/>
              </span>
                                        </div>
                                    </div>
                                    <div className="progress mt-1 invisible" data-height={8} style={{height: 8}}>
                                        <div
                                            className="progress-bar l-bg-orange"
                                            role="progressbar"
                                            data-width="25%"
                                            aria-valuenow={25}
                                            aria-valuemin={0}
                                            aria-valuemax={100}
                                            style={{width: "25%"}}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-xl-3 col-lg-6">
                            <div className="bottomcard l-bg-orange">
                                <div className="card-statistic-3 p-4">
                                    <div className="card-icon card-icon-large">
                                        <FontAwesomeIcon icon={faPeopleGroup} className="fs-icon m-1 mx-2"/>
                                    </div>
                                    <div className="mb-4">
                                        <h5 className="card-title mb-0">Active customers</h5>
                                    </div>
                                    <div className="row align-items-center mb-2 d-flex">
                                        <div className="col-8">
                                            <div className="col-8 d-flex">
                                                <h2 className="d-flex align-items-center mb-0 d-inline">{homeData.active_cust}</h2>
                                                <p className="d-flex align-items-center mb-0 d-inline align-text-bottom">&nbsp;(goal
                                                    50)</p>
                                            </div>
                                        </div>
                                        <div className="col-4 text-right">
              <span>
                {(homeData.active_cust / 0.5).toFixed(2)}% <i className="fa fa-arrow-up"/>
              </span>
                                        </div>
                                    </div>
                                    <div className="progress mt-1 " data-height={8} style={{height: 8}}>
                                        <div
                                            className="progress-bar l-bg-cyan"
                                            role="progressbar"
                                            data-width="25%"
                                            aria-valuenow={25}
                                            aria-valuemin={0}
                                            aria-valuemax={100}
                                            style={{width: "25%"}}
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