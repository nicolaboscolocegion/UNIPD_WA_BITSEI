import React, {useEffect, useState} from "react";
import {useSelector, connect} from "react-redux";
import {Link, useParams} from "react-router-dom";
import gate from "../../../gate"
import {toast} from "react-toastify";
import "./style.css"
import Header from "../../../Components/Header/Header";
import Item from "../../../Components/CompanyItem/Item";

function List() {
    const {company_id} = useParams();
    const [customers, setCustomers] = useState([]);
    const [pending, setPending] = useState(false);
    useEffect(() => {
        setPending(true);
        gate
            .getCustomers(company_id)
            .then(response => {
                console.log(response.data['resource-list']);
                setCustomers(response.data['resource-list']);
                setPending(false);
            }).catch(() => {
                toast.error("Something went wrong.");
            }
        );
    }, [company_id]);

    return pending ? ("Loading") : (
        <>
            <Header>
                <Link className="nav-link" to={"/companies/add"} style={{color: "green"}}>
                    + Add Company
                </Link>
            </Header>
            <section className="py-5" style={{backgroundColor: "#eee"}}>
                <div className="container py-5">
                    {customers.map((item) => {
                        let customer = item.customer
                        return (
                            <>
                                <h1>{customer.customerID} </h1>
                                <h1>{customer.businessName} </h1>
                            </>
                            )
                        }
                    )}
                </div>
            </section>
        </>
    )

}

export default connect(null, {})(List);