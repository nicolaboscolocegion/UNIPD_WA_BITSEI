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
            <div className="container-fluid mx-auto mt-3">
                <a className="navbar-brand" href="#">
                    <Image id={company.company_id}/>
                    <p className="d-inline mx-5 fs-4 fw-bold">Welcome back, <span className="badge bg-info text-dark">{company.business_name}</span></p>
                </a>
            </div>
            <div className={"container-fluid mx-auto mt-3 "}>
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
        </>
    )
}

export default connect(null, {})(CompanyDetail);