import React, {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
import {useParams} from "react-router-dom";
import {toast} from "react-toastify";
import {Table} from "react-bootstrap";

import Form from "./Form";
import gate from "../../../gate";


// TODO: Refactor sending the request section
// TODO: Error Handling section -> duplicated entry or things like that
// TODO: Add a button to go back to the invoices Page
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
                        console.log(item)
                        return {
                            ...item.invoiceproduct,
                            product_name: products.filter(product => product.product_id === item.invoiceproduct.product_id)[0].title
                        }
                    })
                );
            }).catch((error) => {
            toast.error("Something went wrong");
        });
    }, [products])


    const editHandler = (id) => {
        const invoiceProduct = invoiceProducts.find((ip) => ip.invoice_id === parseInt(invoice_id) && ip.product_id === id);
        console.log(invoiceProduct)
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
        console.log( data);
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
                data.product_id
            )
            .then((response => {
                setInvoiceProducts(invoiceProducts.map((ip) => {
                    if (ip.product_id === data.product_id) {
                        return {
                            ...ip,
                            ...data,
                            product_name: products.filter(product => product.product_id === data.product_id)[0].title,
                            related_price: data.related_price ? parseFloat(data.related_price) : 0,
                            product_id: parseInt(data.product_id),
                            quantity: parseInt(data.quantity),
                            unit_price: parseFloat(data.unit_price),
                        }
                    }
                    return ip;
                }))
                console.log(response)
            }))
            .catch(error => {
                console.log(error)
            })
        reset();
        setActionHandler(0);
    };

    const onAddItem = (data) => {
        console.log(data);
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
                console.log(data.product_id)
                setInvoiceProducts([...invoiceProducts, {
                    ...data,
                    product_name: products.filter(product => product.product_id === data.product_id)[0].title,
                    related_price: data.related_price ? parseFloat(data.related_price) : 0,
                    product_id: parseInt(data.product_id),
                    quantity: parseInt(data.quantity),
                    unit_price: parseFloat(data.unit_price),
                }])
            }))
            .catch(error => {

            })
        reset();
        setActionHandler(0);
    };


    const itemDeleteHandler = (invoice_item, product_id) => {
        gate
            .deleteInvoiceItem(company_id, invoice_id, product_id)
            .then((response => {
                setInvoiceProducts(invoiceProducts.filter((ip) => ip.product_id !== product_id))
            }))
            .catch(error => {
                console.log(error)
            })
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
                    Invoice Items
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
                                            products={products}
                                            setValue={setValue}
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
                                        products={products}
                                        setValue={setValue}
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