import React, {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import {Link, useParams} from "react-router-dom";
import {toast} from "react-toastify";
import {Table} from "react-bootstrap";

import Form from "./Form";
import gate from "../../../gate";

const getProductName = (products, product_id) => products.filter(product => product.product_id === product_id)[0]?.title

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
    const [invoiceProducts, setInvoiceProducts] = useState([]);

    const {company_id, invoice_id} = useParams();

    useEffect(() => {
        gate
            .getProducts(company_id)
            .then((response) => {
                setProducts(response.data["resource-list"].map((item) => item.product));
            })
            .catch(() => {
                toast.error("Something went wrong");
            });

    }, []);

    useEffect(() => {
        if (products.length === 0) return;

        gate
            .getInvoiceProducts(company_id, invoice_id)
            .then((response) => {
                setInvoiceProducts(
                    response.data["resource-list"].map((item) => {
                        return {
                            ...item.invoiceproduct,
                            product_name: getProductName(products, item.invoiceproduct.product_id),
                        }
                    })
                );
            })
            .catch((error) => {
                toast.error("Problem fetching invoice products, please try again later");
            });
    }, [products])


    const editHandler = (id) => {
        const invoiceProduct = invoiceProducts.find((ip) => ip.invoice_id === parseInt(invoice_id) && ip.product_id === id);
        if (invoiceProduct) {
            Object.keys(invoiceProduct).forEach((key) => {
                setValue(key, invoiceProduct[key]);
            });
        } else {
            reset();
        }
        setActionHandler(id);
    };

    const onEditItem = (data) => {
        gate
            .editInvoiceItem(
                {
                    invoiceproduct: {
                        ...data,
                        invoice_id: parseInt(invoice_id),
                        related_price: data.related_price ? parseFloat(data.related_price) : 0,
                        product_id: parseInt(data.product_id),
                        quantity: parseInt(data.quantity),
                        unit_price: parseFloat(data.unit_price),
                    }
                },
                company_id,
                invoice_id,
                actionHandler
            )
            .then((response => {
                setInvoiceProducts(invoiceProducts.map((ip) => {
                    if (ip.product_id === data.product_id) {
                        return {
                            ...ip,
                            ...data,
                            product_name: getProductName(products, data.product_id),
                            related_price: data.related_price ? parseFloat(data.related_price) : 0,
                            product_id: parseInt(data.product_id),
                            quantity: parseInt(data.quantity),
                            unit_price: parseFloat(data.unit_price),
                        }
                    }
                    return ip;
                }))
                toast.success("Invoice item edited successfully")
            }))
            .catch(error => {
                toast.error("Can't edit invoice item, please try again later.")
            })
        reset();
        setActionHandler(0);
    };

    const onAddItem = (data) => {
        gate
            .addInvoiceItem({
                    invoiceproduct: {
                        ...data,
                        invoice_id: parseInt(invoice_id),
                        related_price: data.related_price ? parseFloat(data.related_price) : 0,
                        product_id: parseInt(data.product_id),
                        quantity: parseInt(data.quantity),
                        unit_price: parseFloat(data.unit_price),
                    }
                },
                company_id,
                invoice_id,
                parseInt(data.product_id)
            )
            .then((response => {
                setInvoiceProducts([...invoiceProducts, {
                    ...data,
                    product_name: getProductName(products, parseInt(data.product_id)),
                    related_price: data.related_price ? parseFloat(data.related_price) : 0,
                    product_id: parseInt(data.product_id),
                    quantity: parseInt(data.quantity),
                    unit_price: parseFloat(data.unit_price),
                }])
                toast.success("Invoice item added successfully")
            }))
            .catch(error => {
                toast.error("Can't add invoice item, please try again later.")
            })
        reset();
        setActionHandler(0);
    };


    const itemDeleteHandler = (invoice_item, product_id) => {
        gate
            .deleteInvoiceItem(company_id, invoice_id, product_id)
            .then((response => {
                setInvoiceProducts(invoiceProducts.filter((ip) => ip.product_id !== product_id))
                toast.success("Invoice item deleted successfully")
            }))
            .catch(error => {
                toast.error("Can't delete invoice item, please try again later.")
            })
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
                    Invoice Items
                    <Link to={`/companies/${company_id}/list-invoices`}>
                        <button
                            className="btn btn-success btn-sm active btn-block float-end"
                            type="button"
                        >
                            Invoices List
                        </button>
                    </Link>
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
                            return ip.product_id === actionHandler ? (
                                <tr key={ip.invoice_id}>
                                    <td colSpan={7}>
                                        <Form
                                            onSubmit={handleSubmit(onEditItem)}
                                            product_id={ip.product_id}
                                            register={register}
                                            products={products.filter((product) => {
                                                return !invoiceProducts.some((ipp) => ipp.product_id === product.product_id && ip.product_id !== product.product_id)
                                            })}
                                            setValue={setValue}
                                            errors={errors}
                                        />
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
                                            onClick={() => editHandler(ip.product_id)}
                                        >
                                            Edit
                                        </button>
                                        <button
                                            className="btn btn-danger btn-sm active btn-block mx-auto"
                                            type="button"
                                            onClick={() => itemDeleteHandler(ip.invoice_id, ip.product_id)}
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
                                    <Form
                                        onSubmit={handleSubmit(onAddItem)}
                                        register={register}
                                        products={products.filter((product) => {
                                            return !invoiceProducts.some((ip) => ip.product_id === product.product_id)
                                        })}
                                        setValue={setValue}
                                        errors={errors}
                                    />
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