import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import {toast} from "react-toastify";
import {clearCompanies} from "../../Store/companies/listsThunk";
import gate from "../../gate";
import {history} from "../../index";
import Input from "./Input/Input";
import SidebarFilter from "../../Components/SidebarFilters/SidebarFilter";
import Button from 'react-bootstrap/Button';
import Offcanvas from 'react-bootstrap/Offcanvas';
import Sidebar from "../../Components/SideBar/SideBar";


// TODO: Add validation for all fields
// TODO: Add error handling for all fields
// TODO: Add loading for creating company
// TODO: HTML CSS for this page
function ShowCharts() {
    
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    /*const [maxHeight, setMaxHeight] = useState(0);

    useEffect(() => {
        const handleResize = () => {
            const windowHeight = window.innerHeight;
            setMaxHeight(windowHeight);
        };

        window.addEventListener('resize', handleResize);
        handleResize();

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);*/


    return (
        /*{<section>
            <div class="container-fluid">
                <div class="d-flex flex-row">
                    <div class="main-content">
                        <div className="container-fluid bg-white py-5">
                            <section className="w-100 p-4 text-center pb-4">
                                <h1>Amazing charts</h1>
                            </section>
                        </div>
                    </div>
                    <div class="sidebar flex-shrink-0">
                        <SidebarFilter>Boh</SidebarFilter>
                    </div>
                </div>
            </div>
        </section>}*/
        
        /*{<section>
            <div class="container-fluid m-0 p-0">
                <div class="row m-0">
                    <div class="col">
                        <div class="main-content">
                            <div className="container-fluid bg-white py-5">
                                <section className="w-100 p-4 text-center pb-4">
                                    <h1>Amazing charts</h1>
                                </section>
                            </div>
                        </div>
                    </div>
                    <div class="col-3">
                            <div class="sidebar">
                                <SidebarFilter>Boh</SidebarFilter>
                            </div>
                    </div>
                </div>
            </div>
        </section>}*/

        <section>
                <div className="container-fluid bg-white my-5">
                    <section className="w-100 p-4 text-center pb-4">
                        <h1>Amazing charts</h1>
                        
                        <SidebarFilter handleShow={handleShow} handleClose={handleClose} shows={show}/>
                        
                        <Button variant="outline-primary" onClick={handleShow}>
                            Manage filters
                        </Button>

                    </section>
                </div>
        </section>
    )
}

export default ShowCharts;