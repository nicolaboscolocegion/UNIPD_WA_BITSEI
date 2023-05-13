import React, { useState } from "react";
import {Link, useParams} from "react-router-dom";
import {Button} from "react-bootstrap";

function DropDown({companies=[], activeCompany= 0}) {

    const [isExpanded, setIsExpanded] = useState(false);
    const company = companies.filter((company) => company.company_id === parseInt(activeCompany));
    const paramsLength = Object.keys(useParams()).length;
    const handleToggle = () => {
        setIsExpanded(!isExpanded);
    };

    return (
        <div className="dropdown">
            <Button
                className={`btn btn-primary dropdown-toggle ${isExpanded ? "show" : ""}`}
                type="button"
                id="dropdownMenuLink"
                data-mdb-toggle="dropdown"
                aria-expanded={isExpanded}
                onClick={handleToggle}
            >
                {paramsLength > 0
                    ? company.length > 0 ? company[0].business_name : "Select Company"
                    : "Select Company"
                }
            </Button>

            <ul className={`dropdown-menu ${isExpanded ? "show": ""}`} aria-labelledby="dropdownMenuLink">
                {companies.map((company) => (
                    <li><Link className="dropdown-item" to={`/companies/${company.company_id}`}>{company.business_name}</Link></li>
                ))}
            </ul>
        </div>
    );
}

export default DropDown;
