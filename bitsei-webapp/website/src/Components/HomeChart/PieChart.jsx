// src/components/PieChart.js
import React from "react";
import { Pie } from "react-chartjs-2";

function PieChart({ chartData, textData }) {
    return (
        <div className="chart-container mt-1">
            <h3 style={{ textAlign: "center" }}>{textData}</h3>
            <Pie data={chartData} />
        </div>
    );
}
export default PieChart;