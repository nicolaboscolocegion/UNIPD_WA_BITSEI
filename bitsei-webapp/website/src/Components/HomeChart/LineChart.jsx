// components/LineChart.js
import React from "react";
import { Line } from "react-chartjs-2";

function LineChart({ chartData }) {
    return (
        <div className="chart-container">
            <h2 style={{ textAlign: "center" }}>Total profit by month</h2>
            <Line
                data={chartData}
                options={{
                    plugins: {
                        legend: {
                            display: false
                        }
                    }
                }}
            />
        </div>
    );
}
export default LineChart;