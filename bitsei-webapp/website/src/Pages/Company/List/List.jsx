import React, {useEffect, useState} from "react";
import {useSelector, connect} from "react-redux";
import { getLists } from "../../../Store/companies/listsThunk";

function List({getLists}) {
    const user = useSelector((state) => state.auth.user);
    const companies = useSelector((state) => state.companies);

    useEffect(() => {
        getLists();
    },  [getLists]);

    return companies.pending ? ("Loading") : (
        <div>
            <h1>Ciao</h1>
        {/*<Header/>*/}
            {companies.items.map((item, index) => (
                <span>{item.name}</span>
            ))}
        </div>
    )
}

export default connect(null, {getLists})(List);