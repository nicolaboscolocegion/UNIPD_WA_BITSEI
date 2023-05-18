import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import {toast} from "react-toastify";
import {clearCompanies} from "../../Store/companies/listsThunk";
import gate from "../../gate";
import {history} from "../../index";
import Input from "./Input/Input";
import SideBar from "../../Components/SideBar/SideBar";

// TODO: Add validation for all fields
// TODO: Add error handling for all fields
// TODO: Add loading for creating company
// TODO: HTML CSS for this page
function AddCompany({clearCompanies}) {
    const {register, handleSubmit} = useForm();
    const [pending, setPending] = useState(false);
    const [preview, setPreview] = useState();
    const [selectedFile, setSelectedFile] = useState();
    const logoRef = useRef();
    const [hasTelegramNotification, setHasTelegramNotification] = useState(false);
    const [hasEmailNotification, setHasEmailNotification] = useState(false);


    useEffect(() => {
        if (!selectedFile) {
            setPreview(undefined);
            return;
        }

        const objectUrl = URL.createObjectURL(selectedFile);
        setPreview(objectUrl);

        // free memory when ever this component is unmounted
        return () => URL.revokeObjectURL(objectUrl);
    }, [selectedFile]);

    const onSelectFile = (e) => {
        if (!e.target.files || e.target.files.length === 0) {
            setSelectedFile(undefined);
            return;
        }

        // I've kept simple by using the first image instead of multiple
        setSelectedFile(e.target.files[0]);
    };

    const onDeleteFile = (e) => {
        e.preventDefault();
        logoRef.current.value = "";
        logoRef.current.type = "file";
        setSelectedFile(undefined);
    };

    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);

        const formData = new FormData();
        formData.append("title", data.title);
        formData.append("business_name", data.business_name);
        formData.append("vat_number", data.vat_number);
        formData.append("tax_code", data.tax_code);
        formData.append("address", data.address);
        formData.append("province", data.province);
        formData.append("city", data.city);
        formData.append("unique_code", data.unique_code);
        formData.append("has_mail_notifications", hasEmailNotification);
        formData.append("has_telegram_notifications", hasTelegramNotification);

        logoRef.current.files[0] && formData.append("logo", logoRef.current.files[0]);

        gate
            .createCompany(formData)
            .then((response) => {
                setPending(false);
                toast.success("New Company added successfully !");
                clearCompanies();
                history.push("/companies");
            })
            .catch((error) => {
                setPending(false);
                if (error.response.data) {
                    toast.error(error.response.data.message.message);
                } else {
                    toast.error("Something went wrong");
                }
            });
    };


    return (
        <section className="py-5"> 
            <div className="container bg-white">
                <section className="w-100 p-4 text-center pb-4">
                    <SideBar>Boh</SideBar>
                    <h1>Amazing charts</h1>
                </section>
            </div>
        </section>

    )
}

export default connect(null, {clearCompanies})(AddCompany);