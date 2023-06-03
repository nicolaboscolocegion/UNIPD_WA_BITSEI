import React, {useEffect, useState} from "react";


// TODO: Handle errors
function Form({onSubmit, product_id = -1, register, products, setValue}) {
    const [firstLoad, setFirstLoad] = useState(true);
    const [selectedProduct, setSelectedProduct] = useState(product_id);

    useEffect(() => {
        if (firstLoad) { setFirstLoad(false); return; }
        const defaultProduct = products.find(product => product.product_id === selectedProduct)?.default_price;
        setValue("unit_price", defaultProduct);
    }, [selectedProduct]);


    return (
        <form onSubmit={onSubmit} className="row g-3">
            <input type="hidden" {...register("invoice_id")} />
            <div className="col-md-2">
                <label className="form-label">Product:</label>
                <select {...register("product_id")} className="form-select">
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
                <input type="number" {...register("quantity")}
                       className="form-control"/>
            </div>
            <div className="col-md-1">
                <label className="form-label">Unit Price:</label>
                <input type="number" step="any" {...register("unit_price")}
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
                <input
                    type="date"
                    {...register("purchase_date")}
                    className="form-control"
                    max={new Date().toISOString().split('T')[0]}
                />
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