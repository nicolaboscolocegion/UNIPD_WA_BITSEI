import React, {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import {useParams} from "react-router-dom";

import gate from "../../../gate";

import {toast} from "react-toastify";
import {Table} from "react-bootstrap";

function InvoiceProduct() {
    const {
        register,
        handleSubmit,
        formState: {errors},
        setValue,
        reset,
    } = useForm();
    const [pending, setPending] = useState(false);
    const [actionHandler, setActionHandler] = useState(0);
    const [products, setProducts] = useState([]);
    const [customers, setCustomers] = useState([]);
    const [invoiceProducts, setInvoiceProducts] = useState([]);

    const {company_id, invoice_id} = useParams();

    useEffect(() => {
        gate.getInvoiceProducts(company_id, invoice_id).then((response) => {
            gate.getProducts(company_id).then((res) => {
                setProducts(res.data["resource-list"].flatMap((item) => [item.product, {
                    ...item.product,
                    product_id: 2,
                    title: "test_data"
                }]));
            }).catch(() => {
                toast.error("Something went wrong");
            });
            setInvoiceProducts(
                response.data["resource-list"].flatMap((item) => [
                    {
                        ...item.invoiceproduct,
                        product_name: products.filter(product => product.product_id === 1)[0].title
                    },
                    {...item.invoiceproduct, product_name: "fds", invoice_id: 2},
                    {...item.invoiceproduct, product_name: "fget", invoice_id: 3},
                ])
            );
        }).catch(() => {
            toast.error("Something went wrong");
        });
    }, []);

    useEffect(() => {
        gate.getCustomers(company_id).then((res) => {
            setCustomers(res.data["resource-list"].map((item) => item.customer));
        }).catch(() => {
            toast.error("Something went wrong");
        });
    }, []);

    const editHandler = (id) => {
        const invoiceProduct = invoiceProducts.find((ip) => ip.invoice_id === id);
        if (invoiceProduct) {
            Object.keys(invoiceProduct).forEach((key) => {
                setValue(key, invoiceProduct[key]);
            });
        }
        setActionHandler(id);
    };

    const onSubmit = (data) => {
        console.log("Form Data:", data);
        // TODO: Parse the product_id as integer
        // Call the API to update the invoice product with the form data
        // Reset the form data after update
        reset();
        setActionHandler(0);
    };

    const itemDeleteHandler = (invoice_item) => {
        console.log(invoice_item)
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
                    <button
                        className="btn btn-primary btn-sm active btn-block float-end"
                        type="button"
                        onClick={() => editHandler(-1)}
                    >
                        Add Row
                    </button>
                </div>

                <div className="card-body">
                    <Table id="datatablesSimple">
                        <thead>
                        <tr>
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
                            return ip.invoice_id === actionHandler ? (
                                <tr key={ip.invoice_id}>
                                    <td colSpan={7}>
                                        <form onSubmit={handleSubmit(onSubmit)} className="row g-3">
                                            <input type="hidden" {...register("invoice_id")} />
                                            <div className="col-md-2">
                                                <label className="form-label">Product:</label>
                                                <select {...register("product_id")} className="form-select">
                                                    {products.map(product =>
                                                        <option
                                                            selected={product.product_id === ip.product_id}
                                                            value={product.product_id}
                                                        >
                                                            {product.title}
                                                        </option>
                                                    )}
                                                </select>
                                            </div>
                                            <div className="col-md-1">
                                                <label className="form-label">Quantity:</label>
                                                <input type="number" {...register("quantity")}
                                                       className="form-control"/>
                                            </div>
                                            <div className="col-md-1">
                                                <label className="form-label">Unit Price:</label>
                                                <input type="number" {...register("unit_price")}
                                                       className="form-control"/>
                                            </div>
                                            <div className="col-md-1">
                                                <label className="form-label">RP:</label>
                                                <input {...register("related_price")} className="form-control"/>
                                            </div>
                                            <div className="col-md-2">
                                                <label className="form-label">RP description:</label>
                                                <input {...register("related_price_description")}
                                                       className="form-control"/>
                                            </div>
                                            <div className="col-md-2">
                                                <label className="form-label">Date:</label>
                                                <input type="date" {...register("purchase_date")}
                                                       className="form-control"/>
                                            </div>
                                            <div className="col-md-2">
                                                <label className="form-label">Action:</label>
                                                <button type="submit" className="btn btn-primary form-control">Save
                                                </button>
                                            </div>
                                        </form>
                                    </td>
                                </tr>
                            ) : (
                                <tr key={ip.invoice_id}>
                                    <td>{ip.product_name}</td>
                                    <td>{ip.quantity}</td>
                                    <td>{ip.unit_price}</td>
                                    <td>{ip.related_price}</td>
                                    <td>{ip.related_price_description}</td>
                                    <td>{ip.purchase_date}</td>
                                    <td>
                                        <button
                                            className="btn btn-primary btn-sm active btn-block mx-auto"
                                            type="button"
                                            onClick={() => editHandler(ip.invoice_id)}
                                        >
                                            Edit
                                        </button>
                                        <button
                                            className="btn btn-danger btn-sm active btn-block mx-auto"
                                            type="button"
                                            onClick={() => itemDeleteHandler(ip.invoice_id)}
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            );
                        })}

                        {actionHandler === -1 && (
                            <tr>
                                <td colSpan={7}>
                                    <form onSubmit={handleSubmit(onSubmit)}>
                                        <input
                                            type="hidden"
                                            {...register("invoice_id")}
                                        />
                                        <label>
                                            Product:
                                            <input {...register("product_name")} />
                                        </label>
                                        <label>
                                            Quantity:
                                            <input {...register("quantity")} />
                                        </label>
                                        <label>
                                            Unit Price:
                                            <input {...register("unit_price")} />
                                        </label>
                                        <label>
                                            Related Price:
                                            <input {...register("related_price")} />
                                        </label>
                                        <label>
                                            RP description:
                                            <input
                                                {...register("related_price_description")}
                                            />
                                        </label>
                                        <label>
                                            Date:
                                            <input {...register("purchase_date")} />
                                        </label>
                                        <button type="submit">Add</button>
                                    </form>
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </Table>
                </div>
            </div>
        </div>
    );
}

export default InvoiceProduct;