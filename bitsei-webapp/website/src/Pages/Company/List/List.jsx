import React, {useEffect} from "react";
import {Table} from "react-bootstrap";
import {useSelector, connect} from "react-redux";
import {getLists, setActiveCompanyId} from "../../../Store/companies/listsThunk";
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

            <div className="card mb-4">
                <div className="card-header">
                    <i className="fas fa-table me-1"/>
                    Select company
                    <Link to={`/companies/add`}>
                        <button
                            className="btn btn-success btn-sm active btn-block float-end"
                            type="button"
                        >
                            Add company
                        </button>
                    </Link>
                </div>
                <div className="card-body">
                    <Table id="datatablesSimple">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Address</th>
                            <th>VAT Number</th>
                            <th>Actions</th>
                        </tr>
                        </thead>

                        <tbody>
                        {companies.items.map((company) => (
                            <tr key={company.business_name}>
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
})(List);