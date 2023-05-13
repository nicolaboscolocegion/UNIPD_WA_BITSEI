import React, { useState } from "react";
import { useSelector, connect } from "react-redux";
import {Link} from "react-router-dom";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import { faBars } from '@fortawesome/free-solid-svg-icons'
import {Button} from "react-bootstrap";
import DropDown from "./DropDown/DropDown";
function Header({activeCompany = -1}) {
  const companies = useSelector((state) => state.companies);

  const activeCompanyStore = useSelector((state) => state.companies.activeCompany);
  if (activeCompany === -1) {
      activeCompany = activeCompanyStore;
  }

  const [isExpanded, setIsExpanded] = useState(false);
  const handleToggle = () => {
      setIsExpanded(!isExpanded);
  };

  const navItems = [
      { name: "Home", link: "/" },
      { name: "Invoice", link: "/about" },
      { name: "Products", link: "/contact" },
      { name: "Customers", link: "/login" },
      { name: "Insights", link: "/register" },
      { name: "Settings", link: `/companies/edit/${activeCompany.id}`}
  ];

  // const companyNames = companies.items.map((company) => company.business_name);

  return (
      <header>
          <nav className="navbar navbar-expand-lg navbar-light bg-white fixed-top">
              <div className="container-fluid">
                  <Button className={`navbar-toggler ${isExpanded ? "" : "collapsed"}`} type="button" data-mdb-toggle="collapse" data-mdb-target="#navbar" aria-controls="navbarExample01" aria-expanded={isExpanded} aria-label="Toggle navigation" onClick={handleToggle}>
                      <FontAwesomeIcon icon={faBars} />
                  </Button>
                  <div className={`collapse navbar-collapse ${isExpanded ? "show": ""}`} id="navbar">
                      <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                            {navItems.map((item) => (
                                <li className="nav-item active">
                                    <Link className="nav-link" to={item.link}>{item.name}</Link>
                                </li>
                            ))}
                      </ul>
                      <ul className="navbar-nav mb-2 mb-lg-0 ml-0">
                          <li className="nav-item active">
                              <DropDown companies={companies.items} activeCompany={activeCompany}/>
                          </li>
                      </ul>
                  </div>
              </div>
          </nav>
      </header>
  );
}

export default connect(null, {  })(Header);
