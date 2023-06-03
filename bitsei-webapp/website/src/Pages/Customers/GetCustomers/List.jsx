import React, {useEffect, useState} from "react";
import {connect} from "react-redux";
import {Link, useParams} from "react-router-dom";
import gate from "../../../gate"
import {toast} from "react-toastify";
import "./style.css"
import {Table} from "react-bootstrap";
import DeleteConfirm from "../../../Components/DeleteConfirm/DeleteConfirm";
import {setActiveCompanyId} from "../../../Store/companies/listsThunk";


function List() {
    const {company_id} = useParams();
    const [customers, setCustomers] = useState([]);
    const [customerToDelete, setcustomerToDelete] = useState([]);
    const [pending, setPending] = useState(false);
    const [show, setShow] = useState(false);

    const handleCompanySubmit = (company_id) => {
        setActiveCompanyId(company_id)
    }
    const handleClose = () => setShow(false);
    const handleDeleteModal = (customer_id) => {
        setcustomerToDelete(customer_id)
        setShow(true)
    }

    const handleDelete = (customer_id) => {
        gate.deleteCustomer(customer_id, company_id);
        setCustomers([...customers].filter(item => item.customer.customerID !== customer_id))
        setShow(false)
    }


    useEffect(() => {
        setPending(true);
        gate
            .getCustomers(company_id)
            .then(response => {
                setCustomers(response.data['resource-list']);
                setPending(false);
            }).catch(() => {
                toast.error("Something went wrong.");
            }
        );
    }, [company_id]);

    return pending ? ("Loading") : (
        <div className="container-fluid px-4">
            <h1 className="mt-4">Customers Dashboard</h1>


            <div className="card mb-4">
                <div className="card-header">
                    <i className="fas fa-table me-1"/>
                    Customers
                </div>
                <div className="card-body">
                    <Table id="datatablesSimple">
                        <thead>
                        <tr>
                            <th>BUSINESS NAME</th>
                            <th>VAT NUMBER</th>
                            <th>FULL ADDRESS</th>
                            <th className="text-center">EMAIL</th>
                            <th className="text-center">UNIQUE CODE</th>
                            <th className="text-center">ACTIONS</th>
                        </tr>
                        </thead>
                        <tbody>
                        {customers.map((item) => {
                                let customer = item

                                return (
                                    <tr key={customer.customer.businessName}>
                                        <td>{customer.customer.businessName} </td>
                                        <td>{customer.customer.vatNumber} </td>
                                        <td>{customer.customer.address} - {customer.customer.postalCode} {customer.customer.city} ({customer.customer.province})</td>
                                        <td className="text-center">
                                            {customer.customer.emailAddress}
                                        </td>
                                        <td className="text-center">
                                            {customer.customer.uniqueCode}
                                        </td>
                                        <td className="text-center">
                                            <Link className="w-full"
                                                  to={`/companies/${company_id}/customer/edit/${customer.customer.customerID}`}
                                                  onClick={() => handleCompanySubmit(company_id)}>
                                                <button className="btn btn-primary btn-sm active btn-block mx-2 "
                                                        type="button">Edit
                                                </button>
                                            </Link>

                                            <button
                                                className="btn btn-danger btn-sm active btn-block mx-2"
                                                onClick={() => handleDeleteModal(customer.customer.customerID)}
                                                type="button"
                                            >
                                                Delete
                                            </button>

                                            <DeleteConfirm
                                                show={show}
                                                handleClose={handleClose}
                                                handleSumbit={handleDelete}
                                                heading="ATTENTION"
                                                body="Are you sure to delete this customer?"
                                                item_id={customerToDelete}
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