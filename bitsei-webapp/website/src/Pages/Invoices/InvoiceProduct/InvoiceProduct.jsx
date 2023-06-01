import React, {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import {useParams} from "react-router-dom";

import gate from "../../../gate";

import {toast} from "react-toastify";
import {Table} from "react-bootstrap";

// TODO: Handle Edit
// TODO: Handle Add new Item
// TODO: Link it to previous page
// TODO: Handle it on the navBar

function InvoiceProduct() {

    const {register, handleSubmit, formState: {errors}} = useForm();
    const [pending, setPending] = useState(false);
    const [actionHandler, setActionHandler] = useState(0)
    const [products, setProducts] = useState([])
    const [customers, setCustomers] = useState([])
    const [invoiceProducts, setInvoiceProducts] = useState([])


    const {company_id, invoice_id} = useParams();

    useEffect(() => {
        gate
            .getInvoiceProducts(company_id, invoice_id)
            .then((response) => {
                gate
                    .getProducts(company_id)
                    .then((res) => {
                        setProducts(res.data['resource-list'].map(item => item.product))
                    })
                    .catch(() => {
                        toast.error("Something went wrong")
                    });
                // TODO: remove flatMap & change it to map
                // TODO: map the names of the products inside here
                setInvoiceProducts(
                    response.data['resource-list'].flatMap(item => [
                        {...item.invoiceproduct, product_name: "fds"},
                        {...item.invoiceproduct, product_name: "fds", invoice_id: 2},
                        {...item.invoiceproduct, product_name: "fds", invoice_id: 3}
                    ]))
            })
            .catch(() => {
                toast.error("Something went wrong")
            });

    }, []);


    useEffect(() => {
        gate
            .getCustomers(company_id)
            .then((res) => {
                setCustomers(res.data['resource-list'].map(item => item.customer))
            })
            .catch(() => {
                toast.error("Something went wrong")
            });
    }, []);

    const editHandler = (id) => {
        setActionHandler(id)
    }

    return (
        <div className="container-fluid px-4">
            <h1 className="mt-4">Invoice items</h1>
            <ol className="breadcrumb mb-4">
                <li className="breadcrumb-item active">Items</li>
            </ol>

            <div className="card mb-4">
                <div className="card-header">
                    <i className="fas fa-table me-1"/>
                    Select company
                    <button className="btn btn-primary btn-sm active btn-block float-end"
                            type="button" onClick={() => editHandler(-1)}>Add Row
                    </button>
                </div>

                <div className="card-body">
                    <Table id="datatablesSimple">
                        <thead>
                        <tr>
                            {/*<th>Invoice Id</th>*/}
                            <th>Product</th>
                            <th>Quantity</th>
                            <th>Unit Price</th>
                            <th>Related Price</th>
                            <th>RP description</th>
                            <th>Date</th>
                            <th>Actions</th>
                        </tr>
                        </thead>

                        <tbody>
                        {invoiceProducts.map((ip) => {
                            return ip.invoice_id === actionHandler ?
                                <h1>STH</h1>
                                :
                                <tr>
                                    {/*<td>{ip.invoice_id}</td>*/}
                                    <td>{ip.product_name}</td>
                                    <td>{ip.quantity}</td>
                                    <td>{ip.unit_price}</td>
                                    <td>{ip.related_price}</td>
                                    <td>{ip.related_price_description}</td>
                                    <td>{ip.purchase_date}</td>
                                    <td>
                                        <button className="btn btn-primary btn-sm active btn-block mx-auto"
                                                type="button" onClick={() => editHandler(ip.invoice_id)}>Edit
                                        </button>
                                        <button className="btn btn-danger btn-sm active btn-block mx-auto"
                                                type="button">Delete
                                        </button>
                                    </td>
                                </tr>
                        })}

                        {actionHandler === -1 && (
                            <h1>STH</h1>
                        )}
                        </tbody>
                    </Table>
                </div>
            </div>
        </div>

    )
}

export default InvoiceProduct;