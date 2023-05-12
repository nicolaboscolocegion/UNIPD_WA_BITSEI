import React, {useEffect, useState} from "react";
import {useSelector, connect} from "react-redux";
import { getLists } from "../../../Store/companies/listsThunk";
import Item from "../List/Item/Item";
import {Link, useParams} from "react-router-dom";
import Header from "../../../Components/Header/Header";

function Item_L() {
    const user = useSelector((state) => state.auth.user);
    const { company_id } = useParams();
    const companies = useSelector((state) => state.companies);
    console.log(company_id)
    // const company = companies.items.find((company) => company.id === parseInt(company_id));
    const list = [
        {
            "name": "sdfsd",
            "value": "Company 1",
        },
        {
            "name": "sdfsdf",
            "value": "Company 1",
        }
    ]
    // useEffect(() => {
    //     getLists();
    // },  [getLists]);

    return companies.pending ? ("Loading") : (
        <>
        <Header/>
        <section style={{backgroundColor: "#eee", marginTop: "58px"}}>
            {/*
                TODO: add header to the page to show the user's name and avatar
            */}
            <div className="container py-5">
                {/*
                    Todo: Replace this with the company data
                */}
                <Item id={1} img={"https://mdbcdn.b-cdn.net/img/Photos/Horizontal/E-commerce/Products/img%20(4).webp"} business_name={"business name"} title={"company 1"} details={list}/>
            </div>
        </section>
        </>
    )
}

export default connect(null, {})(Item_L);