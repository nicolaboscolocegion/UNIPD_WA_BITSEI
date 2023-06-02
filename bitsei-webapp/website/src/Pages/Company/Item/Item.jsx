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
    //const [dataToSend, setDataToSend] = useState({"chart_type": 1, "chart_period": 1});

    //const [chartData, setChartData] = useState({});



    const [chartData, setChartData] = useState({
        labels: Data.map((data) => data.year),
        datasets: [
            {
                label: "Users Gained ",
                data: Data.map((data) => data.userGain),
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

    /*useEffect(() => {
        gate.getChartInvoiceByFilters(dataToSend)
            .then((response) => {
                console.log(response.data["chart"]);
                setChartData(response.data["chart"]);
            })
            .catch((error) => {
                toast.error("Something went wrong in invoices listing");
            });

    }, [dataToSend]);*/


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
                    <LineChart chartData={chartData} />
                </div>
                <div className={"card homecard mb-1 w-24 mx-1 "}>
                    <div className={"card-header"}>
                        <i className="fas fa-table me-1"></i>
                        Pie Chart
                    </div>
                    <PieChart chartData={chartData}/>
                </div>
                <div className={"card homecard mb-1 w-24 "}>
                    <div className={"card-header"}>
                        <i className="fas fa-table me-1"></i>
                        Pie Chart
                    </div>
                    <PieChart chartData={chartData}/>
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