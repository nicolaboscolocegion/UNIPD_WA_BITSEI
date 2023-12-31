import React, {useEffect, useState} from "react";
import DeleteConfirm from "../../../Components/DeleteConfirm/DeleteConfirm";
import {useParams} from "react-router-dom";
import gate from "../../../gate";
import {toast} from "react-toastify";
import {Table} from "react-bootstrap";
import {setActiveCompanyId} from "../../../Store/companies/listsThunk";
import {Link} from "react-router-dom";
import {FaPencilAlt, FaTrash} from "react-icons/fa";

function ListBankAccounts() {
    const {company_id} = useParams();

    const [show, setShow] = useState(false);
    const [pending, setPending] = useState(false);
    const [bankAccounts, setBankAccount] = useState([]);
    const [bankAccountToDelete, setBankAccountToDelete] = useState(-1)

    //pop up for deliting consent 
    const handleDeleteModal = (bankAccount_id) => {
        setBankAccountToDelete(bankAccount_id)
        setShow(true)
    }

    const handleClose = () => setShow(false);

    //delete bank account 
    const handleDelete = (bankAccountId) => {
        gate
            .deleteBankAccount(company_id, bankAccountId)
            .then(response => {
                toast.success("Bank account deleted successfully.");
                //hides the delited bank account
                setBankAccount([...bankAccounts].filter(item => item.bankaccount_id !== bankAccountId))
                setShow(false)
            })
            .catch(() => {
                toast.error("Something went wrong.");
                setShow(false)
            })
        ;
    }

    useEffect(() => {
        //retirvies all bank account of the company
        setPending(true);
        gate
            .listBankAccount(company_id)
            .then(response => {
                console.log(response);
                console.log(response.data['resource-list'])
                setBankAccount(response.data['resource-list'])

                setPending(false);
            }).catch(() => {
                toast.error("Something went wrong.");
            }
        );
    }, [company_id]);


    const handleCompanySubmit = (company_id) => {
        setActiveCompanyId(company_id)
    }

    //returns the table with all the bankaccounts
    return pending ? ("Loading") : (
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
                            <th>BANK NAME</th>
                            <th>FRIENDLY NAME</th>
                            <th className="text-center">ACTIONS</th>
                        </tr>
                        </thead>
                        <tbody>
                        {bankAccounts.map((bankAccount) =>
                            <tr>
                                <td>{bankAccount.IBAN} </td>
                                <td>{bankAccount.bank_name} </td>
                                <td>{bankAccount.bankaccount_friendly_name} </td>
                                <td className="text-center">
                                    <Link className="w-full"
                                          to={`/companies/${bankAccount.company_id}/bankAccount/${bankAccount.bankaccount_id}/edit`}
                                          onClick={() => handleCompanySubmit(bankAccount.company_id)}>
                                        <button className="btn btn-primary btn-sm active btn-block mx-2 "
                                                type="button"><FaPencilAlt />
                                        </button>
                                    </Link>

                                    <button
                                        className="btn btn-danger btn-sm active btn-block mx-2"
                                        onClick={() => handleDeleteModal(bankAccount.bankaccount_id)}
                                        type="button"
                                    >
                                        <FaTrash />
                                    </button>

                                    <DeleteConfirm
                                        show={show}
                                        handleClose={handleClose}
                                        handleSubmit={handleDelete}
                                        heading="ATTENTION"
                                        body="Are you sure to delete this bank account?"
                                        item_id={bankAccountToDelete}
                                    />
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </Table>
                </div>
            </div>
        </div>

    )
}

export default ListBankAccounts;