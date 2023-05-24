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
        //setPending(true);
        gate
            .getProducts(company_id)
            .then((response) => {
                //console.log(response.data['resource-list']);
                setProducts(response.data['resource-list']);
                //setPending(false);
            }).catch((error) => {
                toast.error("Something went wrong.");
            }
        );
    }, [company_id]);


    return (
            <>
            {pending
                ? (<h1>pending...</h1>)
                : (<div className="container py-5">

                      <header className="text-center text-white">
                      <h1 className="display-4">Product list</h1>
                      </header>

                        <div className="row py-5">
                            <div className="col-lg-7 mx-auto">

                                <div className="card shadow mb-4">
                                    <div className="card-body p-5">
                                        <h4 className="mb-4">Products list:</h4>

                                        <ul className="list-bullets">
                                            {products.map((item) => {
                                            let product = item.product
                                                return (
                                                    <li>
                                                        <h5>{product.productID} </h5>
                                                        <h5>{product.title} </h5>
                                                    </li>
                                                )
                                            }
                                            )}
                                        </ul>
                                    </div>
                                </div>



                            </div>
                        </div>
                    </div>

                )
            }
            </>
    )

}

export default connect(null, {})(List);