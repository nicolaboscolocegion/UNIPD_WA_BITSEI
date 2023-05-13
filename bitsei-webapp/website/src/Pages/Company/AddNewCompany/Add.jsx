import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import {toast} from "react-toastify";
import {clearCompanies} from "../../../Store/companies/listsThunk";
import gate from "../../../gate";
import {history} from "../../../index";
import Input from "./Input/Input";

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
                    <form onSubmit={handleSubmit(submitHandler)}>
                        <div className="row mb-4">
                            <div className="col">
                                <Input type="text" name="Title" register={{
                                    ...register("title", {
                                        required: "Required",
                                        minlength: 3,
                                        message: "Please enter a title",
                                    })
                                }}/>
                            </div>
                            <div className="col">
                                <Input type="text" name="Business Name" register={{
                                    ...register("business_name", {
                                        required: "Required",
                                        minlength: 3,
                                        message: "Please enter a business name",
                                    })
                                }}/>
                            </div>
                        </div>

                        <div className="form-outline mb-4">
                            <label className="form-label" htmlFor="logo">Logo</label>
                            <input
                                id="logo"
                                className="form-control"
                                onChange={onSelectFile}
                                onClick={(e) => {
                                    e.target.value = null;
                                }}
                                ref={logoRef}
                                name="logo"
                                type="file"
                                accept="image/*"
                            />
                        </div>
                        <div className="row mb-4">
                            <div className="col">
                                <Input type="text" name="VAT Number" register={{
                                    ...register("vat_number", {
                                        required: "Required",
                                        minlength: 3,
                                        message: "Please enter a business name",
                                    })
                                }}/>
                            </div>
                            <div className="col">
                                <Input type="text" name="TAX Code" register={{
                                    ...register("tax_code", {
                                        required: "Required",
                                        minlength: 3,
                                        message: "Please enter a business name",
                                    })
                                }}/>
                            </div>
                            <div className="col">
                                <Input type="text" name="Unique Code" register={{
                                    ...register("unique_code", {
                                        required: "Required",
                                        minlength: 3,
                                        message: "Please enter a business name",
                                    })
                                }}/>
                            </div>
                        </div>

                        <div className="row mb-4">
                            <div className="col">
                                <Input type="text" name="City" register={{
                                    ...register("city", {
                                        required: "Required",
                                        minlength: 3,
                                        message: "Please enter a business name",
                                    })
                                }}/>

                            </div>
                            <div className="col">
                                <Input type="text" name="Province" register={{
                                    ...register("province", {
                                        required: "Required",
                                        minlength: 3,
                                        message: "Please enter a business name",
                                    })
                                }}/>
                            </div>
                        </div>
                        <Input type="text" name="Address" register={{
                            ...register("address", {
                                required: "Required",
                                minlength: 3,
                                message: "Please enter a business name",
                            })
                        }}/>
                        <div className="row mb-4">
                            <div className="col">
                                <label
                                    className="form-check-label"
                                    htmlFor="has_mail_notifications">
                                    Mail Notification
                                </label>
                                <input
                                    className="form-check-input me-2"
                                    type="checkbox"
                                    value=""
                                    id="has_mail_notifications"
                                    checked={hasEmailNotification}
                                    onClick={() => {
                                        setHasEmailNotification(!hasEmailNotification)
                                    }}
                                />
                            </div>
                            <div className="col">
                                <label
                                    className="form-check-label"
                                    htmlFor="has_telegram_notifications">
                                    Telegram Notification
                                </label>
                                <input
                                    className="form-check-input me-2"
                                    type="checkbox"
                                    value=""
                                    id="has_telegram_notifications"
                                    checked={hasTelegramNotification}
                                    onClick={() => {
                                        setHasTelegramNotification(!hasTelegramNotification)
                                    }}
                                />
                            </div>
                        </div>
                        <button type="submit" className="btn btn-primary btn-block mb-4">Add Company</button>
                    </form>
                </section>
            </div>
        </section>

    )
}

export default connect(null, {clearCompanies})(AddCompany);