import React, {useEffect, useState} from "react";
import {useSelector, connect} from "react-redux";
import {Link, useParams} from "react-router-dom";
import gate from "../../../gate"
import {toast} from "react-toastify";
import "./style.css"

function Item() {
    const {product_id, company_id} = useParams();
    const [product, setProduct]  = useState({});
    const [pending, setPending] = useState(false);
    useEffect(() => {
        setPending(true);
        gate
            .getProduct(product_id,company_id)
            .then(response => {
                console.log(response.data.product);
                setProduct(response.data.product);

                setPending(false);
            }).catch( () => {
                toast.error("Something went wrong.");
            }

            );
    }, [product_id, company_id]);

    return (
        <>
        {pending
            ? (<h1>pending...</h1>)
            : (<div className="container py-5">

                    <header className="text-center text-white">
                        <h1 className="display-4">Bootstrap list</h1>
                        <p className="lead mb-0 font-italic">A collection of list variants using Bootstrap 4.</p>
                        <p className="font-italic">Snippet By
                            <a href="https://bootstrapious.com" className="text-white">
                                <u>Bootstrapious</u>
                            </a>
                        </p>
                    </header>

                    <div className="row py-5">
                        <div className="col-lg-7 mx-auto">

                            <div className="card shadow mb-4">
                                <div className="card-body p-5">
                                    <h4 className="mb-4">List with bullets</h4>

                                    <ul className="list-bullets">
                                        <li className="mb-2">Lorem ipsum dolor sit amet.</li>
                                        {Object.entries(product).map((e) => <li className="mb-2">{`${e[0]}: ${e[1]}`}</li> )  }
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

export default connect(null, {

})(Item);