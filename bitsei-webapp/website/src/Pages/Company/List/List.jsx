import React, {useEffect} from "react";
import {Table} from "react-bootstrap";
import {useSelector, connect} from "react-redux";
import {getLists} from "../../../Store/companies/listsThunk";
import Navbar from "../../../Components/Navbar/Navbar";
import Sidebar from "../../../Components/SidebarFilters/SidebarFilter";
import Image from "../../../Components/Image/Image";
import Item from "../../../Components/CompanyItem/Item";
import {Link} from "react-router-dom";
import Header from "../../../Components/Header/Header";
import getCompanyDetails from "../../../helpers/getCompanyDetails";

function List({getLists}) {
    const companies = useSelector((state) => state.companies);

    useEffect(() => {
        getLists();

    }, [getLists]);

    /*return companies.pending ? ("Loading") : (
        <>
            <Header>
                <Link className="nav-link" to={"/companies/add"} style={{color: "green"}}>
                    + Add Company
                </Link>
            </Header>
            <section className="py-5" style={{backgroundColor: "#eee"}}>
                <div className="container py-5">
                    {companies.items.map((company) => (
                        <Item
                            key={company.company_id}
                            id={company.company_id}
                            name={`${company.business_name} | ${company.province}(${company.city})`}
                            details={getCompanyDetails(company)}
                        />
                    ))}
                </div>
            </section>
        </>
    )*/


    /*
    *  <link
                href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css"
                rel="stylesheet"
            />
    * */

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
                                <td>{company.business_name}</td>
                                <td>{`${company.address} ${company.postal_code} ${company.city} ${company.province}`}</td>
                                <td>{company.vat_number}</td>
                                <td>
                                    <Link className="w-full" to={`/companies/${company.company_id}`}>
                                        <button className="btn btn-primary btn-sm active btn-block mx-auto"
                                                type="button">Go
                                        </button>
                                    </Link>
                                    <Link className="w-full" to={`/companies/edit/${company.company_id}`}>
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
    getLists
})(List);