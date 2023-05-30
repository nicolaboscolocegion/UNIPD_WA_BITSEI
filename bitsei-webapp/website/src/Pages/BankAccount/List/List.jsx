import React, {useEffect, useRef, useState} from "react";
import DeleteConfirm from "../../../Components/DeleteConfirm/DeleteConfirm";
import {useParams} from "react-router-dom";
import gate from "../../../gate";
import {toast} from "react-toastify";
import {Table, Modal, Button } from "react-bootstrap";
import {useSelector, connect} from "react-redux";
import {getLists, setActiveCompanyId} from "../../../Store/companies/listsThunk";
import Image from "../../../Components/Image/Image";
import {Link} from "react-router-dom";

function ListBankAccounts() {
    const [pending, setPending] = useState(false);
    const [bankAccounts, setBankAccount] = useState([]);
    const [bankAccountToDelete, setBankAccountToDelete ] = useState()

    const {company_id} = useParams();
    const [show, setShow] = useState(false);

    const handleDeleteModal = (bankAccount_id) => {
        setBankAccountToDelete(bankAccount_id)
        setShow(true)
    }

    const handleClose = () => setShow(false);

    const handleDelete = (bankaccount_id) => {
        console.log(bankaccount_id);

        gate.deleteBankAccount(company_id, bankaccount_id);
        console.log([...bankAccounts], [...bankAccounts].filter(item => item.bankaccount_id !== bankaccount_id));
        setBankAccount([...bankAccounts].filter(item => item.bankaccount_id !== bankaccount_id))
        setShow(false)
    }

    useEffect(() => {
        setPending(true);
        gate
            .listBankAccount(company_id)
            .then(response => {
                console.log(response);
                console.log(response.data['resource-list'])
                setBankAccount(response.data['resource-list'])
                
                setPending(false);
            }).catch( () => {
                toast.error("Something went wrong.");}
            );
    }, [company_id]);

    
    

    const handleCompanySubmit = (company_id) => {
        setActiveCompanyId(company_id)
    }

    return pending ? ("Loading") :(
        
            
            <div className="container-fluid px-4">
            <h1 className="mt-4">Bank Account Dashboard</h1>
            

            <div className="card mb-4">
                <div className="card-header">
                    <i className="fas fa-table me-1"/>
                    Bank Accounts 
                </div>
                <div className="card-body">
                    <Table id="datatablesSimple">
                    <thead>
                        <tr>
                            <th>IBAN</th>
                            <th>Bank name</th>
                            <th>Friendly name</th>
                            <th>Edit options</th>
                        </tr>
                    </thead>
                    <tbody>
                    {bankAccounts.map((item) => {
                        
                        let bankAccount = item
                        return (
                            
                            <tr>
                                <td>{bankAccount.IBAN} </td>
                                <td>{bankAccount.bank_name} </td>
                                <td>{bankAccount.bankaccount_friendly_name} </td>
                                <td>
                                    <Link className="w-full" to={`/companies/edit/${bankAccount.company_id}/bankAccount/${bankAccount.bankaccount_id}`} onClick={() => handleCompanySubmit(bankAccount.company_id)}>
                                        <button className="btn btn-secondary btn-sm active btn-block mx-auto"
                                                type="button">Edit
                                        </button>
                                    </Link>
                                        <button 
                                            className="btn btn-danger btn-sm active btn-block mx-auto"
                                            onClick={() => handleDeleteModal(bankAccount.bankaccount_id)}  
                                            type="button"
                                        >
                                            Delete
                                        </button>
                                       
                                    <DeleteConfirm 
                                       show={show}
                                       handleClose={handleClose}
                                       handleSumbit={handleDelete}
                                       heading="ATTENTION" 
                                       body="Are you sure to delete this bank account?" 
                                       item_id={bankAccountToDelete}
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

export default ListBankAccounts;