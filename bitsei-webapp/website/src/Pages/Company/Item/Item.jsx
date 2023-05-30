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


        </>
    )
}

export default connect(null, {})(CompanyDetail);