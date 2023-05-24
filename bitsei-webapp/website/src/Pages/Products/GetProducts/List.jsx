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
    const [products, setProducts] = useState([]);
    const [pending, setPending] = useState(false);
    useEffect(() => {
        setPending(true);
        gate
            .getProducts(company_id)
            .then(response => {
                console.log(response.data['resource-list']);
                setProducts(response.data['resource-list']);
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
                    {products.map((item) => {
                        let product = item.product
                        return (
                            <>
                                <h1>{product.productID} </h1>
                                <h1>{product.title} </h1>
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