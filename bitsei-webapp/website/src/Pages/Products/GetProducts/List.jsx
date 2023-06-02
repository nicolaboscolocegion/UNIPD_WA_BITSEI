import React, {useEffect, useState} from "react";
import {useSelector, connect} from "react-redux";
import {Link, useParams} from "react-router-dom";
import gate from "../../../gate"
import {toast} from "react-toastify";
import "./style.css"
import Header from "../../../Components/Header/Header";
import Item from "../../../Components/CompanyItem/Item";
import {Table} from "react-bootstrap";
import DeleteConfirm from "../../../Components/DeleteConfirm/DeleteConfirm";
import {setActiveCompanyId} from "../../../Store/companies/listsThunk";





function List() {
    const {company_id} = useParams();
    const [products, setProducts] = useState([]);
    const [productToDelete, setproductToDelete] = useState([]);
    const [pending, setPending] = useState(false);
    const [show, setShow] = useState(false);

    const handleCompanySubmit = (company_id) => {
        setActiveCompanyId(company_id)
    }
    const handleClose = () => setShow(false);
    const handleDeleteModal = (product_id) => {
        setproductToDelete(product_id)
        setShow(true)
    }

    const handleDelete = (product_id) => {


        gate.deleteProduct(product_id, company_id);
        setProducts([...products].filter(item => item.product.productID !== product_id))
        setShow(false)
    }


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
        <div className="container-fluid px-4">
            <h1 className="mt-4">Products Dashboard</h1>


            <div className="card mb-4">
                <div className="card-header">
                    <i className="fas fa-table me-1"/>
                    Products
                </div>
                <div className="card-body">
                    <Table id="datatablesSimple">
                        <thead>
                        <tr>
                            <th>LOGO</th>
                            <th>TITLE</th>
                            <th>DEFAULT PRICE</th>
                            <th>MEASUREMENT UNIT</th>
                            <th>DESCRIPTION</th>
                            <th>ACTIONS</th>
                        </tr>
                        </thead>
                        <tbody>
                        {products.map((item) => {
                                console.log(item.product.title)
                                let product = item

                                return (

                                    <tr>
                                            <td>{product.product.logo}</td>
                                            <td>{product.product.title} </td>
                                            <td>{product.product.defaultPrice} </td>
                                            <td>
                                                {product.product.measurementUnit}
                                            </td>
                                            <td>
                                                {product.product.description}
                                            </td>
                                            <td>
                                                <Link className="w-full" to={`/companies/${company_id}/product/edit/${product.product.productID}`} onClick={() => handleCompanySubmit(company_id)}>
                                                    <button className="btn btn-primary btn-sm active btn-block mx-2 "
                                                            type="button">Edit
                                                    </button>
                                                </Link>

                                                <button
                                                    className="btn btn-danger btn-sm active btn-block mx-2"
                                                    onClick={() => handleDeleteModal(product.product.productID)}
                                                    type="button"
                                                >
                                                    Delete
                                                </button>

                                                <DeleteConfirm
                                                    show={show}
                                                    handleClose={handleClose}
                                                    handleSumbit={handleDelete}
                                                    heading="ATTENTION"
                                                    body="Are you sure to delete this product?"
                                                    item_id={productToDelete}
                                                />
                                            </td>
                                    </tr>

                                )
                            }
                        )}
                        </tbody>
                    </Table>
                </div>
            </div>
        </div>
    )

}

export default connect(null, {})(List);