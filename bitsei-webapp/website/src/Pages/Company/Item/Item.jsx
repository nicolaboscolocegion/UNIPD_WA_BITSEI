import React from "react";
import {useSelector, connect} from "react-redux";
import Item from "../../../Components/CompanyItem/Item";
import { useParams} from "react-router-dom";
import Header from "../../../Components/Header/Header";
import getCompanyDetails from "../../../helpers/getCompanyDetails";

function CompanyDetail() {
    const { company_id } = useParams();
    const companies = useSelector((state) => state.companies);
    const company = companies.items.filter((company) => company.company_id === parseInt(company_id))[0];

    return companies.pending ? ("Loading") : (
        <>
        <Header activeCompany={company_id}/>
        <section style={{backgroundColor: "#eee", marginTop: "58px"}}>
            <div className="container py-5">
                <Item
                    id={company.company_id}
                    name={`${company.business_name} | ${company.province}(${company.city})`}
                    details={getCompanyDetails(company)}
                />
            </div>
        </section>
        </>
    )
}

export default connect(null, {})(CompanyDetail);