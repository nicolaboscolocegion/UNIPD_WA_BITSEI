import React, {useEffect, useState} from "react";
import {errorMessages} from "../../../helpers/errorMessages";

const ErrorComponent = ({errors, name}) => {
    return (
        <>
            {
                errors[name] && errors[name].type === "required"
                &&
                <span style={{
                    display: 'inline-block',
                    padding: "5px 10px",
                    backgroundColor: "#ffcccc",
                    color: "#ff0000",
                    borderRadius: "3px"
                }}>
                    {errors[name].message || errorMessages[errors[name].type] || "Not valid"}
                </span>
            }
        </>
    )
}

function Form({onSubmit, product_id = -1, register, products, setValue, errors}) {
    const [firstLoad, setFirstLoad] = useState(true);
    const [selectedProduct, setSelectedProduct] = useState(product_id);

    useEffect(() => {
        if (firstLoad) {
            setFirstLoad(false);
            return;
        }
        const defaultProduct = products.find(product => product.product_id === selectedProduct)?.default_price;
        setValue("unit_price", defaultProduct);
    }, [selectedProduct]);


    return (
        <form onSubmit={onSubmit} className="row g-3">
            <input type="hidden" {...register("invoice_id")} />
            <div className="col-md-2">
                <label className="form-label">Product:</label>
                <select {...register("product_id", {required: true})} className="form-select">
                    <option value="default" disabled selected>Select a product</option>
                    {products.map(product =>
                        <option
                            selected={product.product_id === product_id}
                            value={product.product_id}
                            onClick={(e) => {
                                setSelectedProduct(parseInt(e.target.value))
                            }}
                        >
                            {product.title}
                        </option>
                    )}
                </select>
            </div>
            <div className="col-md-1">
                <label className="form-label">Quantity:</label>
                <input type="number" {...register("quantity", {required: true})}
                       className="form-control"/>
                <ErrorComponent errors={errors} name="quantity"/>
            </div>
            <div className="col-md-1">
                <label className="form-label">Unit Price:</label>
                <input type="number" step="any" {...register("unit_price", {required: true})}
                       className="form-control"/>
                <ErrorComponent errors={errors} name="unit_price"/>
            </div>
            <div className="col-md-1">
                <label className="form-label">RP:</label>
                <input {...register("related_price", {required: true})} className="form-control"/>
                <ErrorComponent errors={errors} name="related_price"/>
            </div>
            <div className="col-md-2">
                <label className="form-label">RP description:</label>
                <input {...register("related_price_description")}
                       className="form-control"/>
                <ErrorComponent errors={errors} name="related_price_description"/>
            </div>
            <div className="col-md-2">
                <label className="form-label">Date:</label>
                <input
                    type="date"
                    {...register("purchase_date", {required: true})}
                    className="form-control"
                    max={new Date().toISOString().split('T')[0]}
                />
                <ErrorComponent errors={errors} name="purchase_date"/>
            </div>
            <div className="col-md-2">
                <label className="form-label">Action:</label>
                <button type="submit" className="btn btn-primary form-control">
                    Save
                </button>
            </div>
        </form>
    );
}

export default Form;