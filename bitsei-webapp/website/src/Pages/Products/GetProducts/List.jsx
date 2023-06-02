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
        setProducts([...products].filter(item => item.product.product_id !== product_id))
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
                            <th>TITLE</th>
                            <th className="text-center">DEFAULT PRICE</th>
                            <th className="text-center">MEASUREMENT UNIT</th>
                            <th className="text-center">DESCRIPTION</th>
                            <th className="text-center">ACTIONS</th>
                        </tr>
                        </thead>
                        <tbody>
                        {products.map((item) => {
                                console.log(item.product.title)
                                let product = item

                                return (

                                    <tr>

                                            <td>{product.product.title} </td>
                                            <td className="text-center">{product.product.default_price} </td>
                                            <td className="text-center">{product.product.measurement_unit}</td>
                                            <td className="text-center">
                                                {product.product.description}
                                            </td>
                                            <td className="text-center">
                                                <Link className="w-full" to={`/companies/${company_id}/product/edit/${product.product.product_id}`} onClick={() => handleCompanySubmit(company_id)}>
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



/*import React, {useEffect} from "react";
  import {Table} from "react-bootstrap";
  import {useSelector, connect} from "react-redux";
  import {getLists, setActiveCompanyId} from "../../../Store/companies/listsThunk";
  import Image from "../../../Components/Image/Image";
  import {Link} from "react-router-dom";

  function List({getLists, setActiveCompanyId}) {
      const companies = useSelector((state) => state.companies);

      useEffect(() => {
          getLists();
          setActiveCompanyId(null);
      }, [getLists, setActiveCompanyId]);

      const handleCompanySubmit = (company_id) => {
          setActiveCompanyId(company_id)
      }

      return companies.pending ? ("Loading") : (
          <div className="container-fluid px-4">
              <h1 className="mt-4">Dashboard</h1>
              <ol className="breadcrumb mb-4">
                  <li className="breadcrumb-item active">Dashboard</li>
              </ol>

              <div className="card mb-4">
                  <div className="card-header">
                      <i className="fas fa-table me-1"/>
                      Select company
                  </div>
                  <div className="card-body">
                      <Table id="datatablesSimple">
                          <thead>
                          <tr>
                              <th>Logo</th>
                              <th>Name</th>
                              <th>Address</th>
                              <th>VAT Number</th>
                              <th>Actions</th>
                          </tr>
                          </thead>

                          <tbody>
                          {companies.items.map((company) => (
                              <tr>
                                  <td>
                                      <Image id={company.company_id}/>
                                  </td>
                                  <td>{company.business_name ? company.business_name : ''}</td>
                                  <td>{`${company.address ? company.address : ''} ${company.postal_code ? company.postal_code : '' } ${company.city ? company.city : ''} ${company.province ? company.province : '' }`}</td>
                                  <td>{company.vat_number ? company.vat_number : ''}</td>
                                  <td>
                                      <Link className="w-full" to={`/companies/${company.company_id}`} onClick={() => handleCompanySubmit(company.company_id)}>
                                          <button className="btn btn-primary btn-sm active btn-block mx-auto"
                                                  type="button">Go
                                          </button>
                                      </Link>
                                      <Link className="w-full" to={`/companies/${company.company_id}/edit`} onClick={() => handleCompanySubmit(company.company_id)}>
                                          <button className="btn btn-secondary btn-sm active btn-block mx-auto"
                                                  type="button">Edit Company
                                          </button>
                                      </Link>
                                  </td>
                              </tr>
                          ))}

                          </tbody>
                      </Table>
                  </div>
              </div>
          </div>
      )
  }

  export default connect(null, {
      getLists, setActiveCompanyId
  })(List);*/