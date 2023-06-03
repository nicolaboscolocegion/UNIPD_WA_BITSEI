import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import {toast} from "react-toastify";
import {clearCompanies} from "../../../Store/companies/listsThunk";
import gate from "../../../gate";
import {history} from "../../../index";
import Form from "../../../Components/Form/Form";

function AddCompany({clearCompanies}) {
    const {register, handleSubmit, formState: {errors}} = useForm();
    const [pending, setPending] = useState(false);
    const [selectedFile, setSelectedFile] = useState();
    const logoRef = useRef();
    const [hasTelegramNotification, setHasTelegramNotification] = useState(false);
    const [hasEmailNotification, setHasEmailNotification] = useState(false);


    useEffect(() => {
        if (!selectedFile) {
            return;
        }

        const objectUrl = URL.createObjectURL(selectedFile);

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

    const fields = [
        [
            {name: "title", type: "string", options: {required: true, maxLength: 30}},
            {name: "business_name", type: "string", options: {required: true, maxLength: 30}}
        ],
        [
            {name: "vat_number", type: "string", options: {required: true, minLength:11, maxLength: 11}},
            {name: "tax_code", type: "string", options: {required: true, minLength:16, maxLength: 16}}
        ],
        [
            {name: "unique_code", type: "string", options: {required: true, maxLength: 30}},
            {name: "city", type: "string", options: {required: true, maxLength: 30}}
        ],
        [
            {name: "province", type: "string", options: {required: true, minLength:2, maxLength: 2}},
            {name: "address", type: "string", options: {required: true, maxLength: 30}}
        ],
    ]
    return (
        <Form title={"Company"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register}
              errors={errors} pending={pending}>
            {/** Another fields like image or selects that you can't use the default things that we are written in the fields **/}
            <div className="row justify-content-between text-left">
                <div className="form-group flex-column d-flex">
                    <label className="form-control-label px-3" htmlFor="logo">Logo</label>
                    <input
                        id="logo"
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
            </div>
            <div className="row justify-content-between text-left">

                <div className="form-group col-sm-6 flex-column d-flex">
                    <label
                        className="form-control-label px-3"
                        htmlFor="has_mail_notifications">
                        Mail Notification
                    </label>
                    <input
                        type="checkbox"
                        value=""
                        id="has_mail_notifications"
                        checked={hasEmailNotification}
                        onClick={() => {
                            setHasEmailNotification(!hasEmailNotification)
                        }}
                    />
                </div>
                <div className="form-group col-sm-6 flex-column d-flex">
                    <label
                        className="form-control-label px-3"
                        htmlFor="has_telegram_notifications">
                        Telegram Notification
                    </label>
                    <input
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
        </Form>
    )
}

export default connect(null, {clearCompanies})(AddCompany);