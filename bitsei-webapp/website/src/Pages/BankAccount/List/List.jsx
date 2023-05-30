import React, {useEffect, useRef, useState} from "react";
import {useParams} from "react-router-dom";
import gate from "../../../gate";
import {toast} from "react-toastify";
import {Table} from "react-bootstrap";
import {useSelector, connect} from "react-redux";
import {getLists, setActiveCompanyId} from "../../../Store/companies/listsThunk";
import Image from "../../../Components/Image/Image";
import {Link} from "react-router-dom";

function ListBankAccounts() {
    const [pending, setPending] = useState(false);
    const [bankAccounts, setBankAccount] = useState([]);
    const {company_id} = useParams();

    
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
                                    <Link className="w-full" >
                                        <button className="btn btn-secondary btn-sm active btn-block mx-auto"
                                                type="button">Delete
                                        </button>
                                    </Link>

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