import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import gate from "../../gate";
import { Buffer } from 'buffer';
import logo from "./bitseiLogo";
function Item({id, name, details}) {
    const [image, setImage] = useState("");

    useEffect(() => {
        gate
            .getCompanyImage(id)
            .then((response) => {
                // check bytes length to see if the image is empty
                if (response.data.byteLength < 500) {
                    setImage(logo());
                    return;
                }
                setImage(Buffer.from(response.data).toString('base64'));
            })
            .catch(() => {
                setImage(logo());
            });
    }, [id]);

    return (
        <div className="row justify-content-center mb-3">
            <div className="col-md-12 col-xl-10">
                <div className="card shadow-0 border rounded-3">
                    <div className="card-body">
                        <div className="row">
                            <div className="col-md-12 col-lg-3 col-xl-3 mb-4 mb-lg-0">
                                <div className="bg-image hover-zoom ripple rounded ripple-surface">
                                    <img
                                        src={`data:image/png;base64, ${image}`}
                                        className="w-100"
                                        alt={"company_logo"}
                                    />
                                    <a href="#!">
                                        <div className="hover-overlay">
                                            <div className="mask"
                                                 style={{backgroundColor: "rgba(253, 253, 253, 0.15)"}}></div>
                                        </div>
                                    </a>
                                </div>
                            </div>
                            <div className="col-md-6 col-lg-6 col-xl-6">
                                <h5>{name}</h5>
                                <div className="mb-2 text-muted small">
                                    {details.map((detail) => (
                                        <>
                                            <span className="text-primary"> • </span>
                                            <span>{detail.name}: {detail.value}<br/></span>
                                        </>
                                    ))}
                                </div>
                                {/*<p className="text-truncate mb-4 mb-md-0">*/}
                                {/*    There are many variations of passages of Lorem Ipsum available, but the*/}
                                {/*    majority have suffered alteration in some form, by injected humour, or*/}
                                {/*    randomised words which don't look even slightly believable.*/}
                                {/*</p>*/}
                            </div>
                            <div className="col-md-6 col-lg-3 col-xl-3 border-sm-start-none border-start">
                                <div className="d-flex flex-column mt-4">
                                    <Link className="w-full" to={`/companies/${id}`}>
                                        <button className="btn btn-primary btn-sm" type="button">Go</button>
                                    </Link>
                                    <Link className="w-full" to={`/companies/edit/${id}`}>
                                        <button className="btn btn-outline-primary btn-sm mt-2" type="button">Edit Company</button>
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Item;
