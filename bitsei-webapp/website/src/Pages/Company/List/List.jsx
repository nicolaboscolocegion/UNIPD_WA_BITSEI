import React, {useEffect} from "react";
import {useSelector, connect} from "react-redux";
import {getLists} from "../../../Store/companies/listsThunk";
import Item from "../../../Components/CompanyItem/Item";
import {Link} from "react-router-dom";
import Header from "../../../Components/Header/Header";
import getCompanyDetails from "../../../helpers/getCompanyDetails";

function List({getLists}) {
    const companies = useSelector((state) => state.companies);

    useEffect(() => {
        getLists();
    }, [getLists]);

    return companies.pending ? ("Loading") : (
        <>
            <Header/>
            <section style={{backgroundColor: "#eee"}}>
                <Link className="w-full" to="/new-company">
                    <button className="btn btn-outline-primary btn-sm mt-2" type="button">
                        Add Company
                    </button>
                </Link>
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
    )
}

export default connect(null, {getLists})(List);