import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect, useSelector} from "react-redux";
import {toast} from "react-toastify";
import {clearCompanies} from "../../../Store/companies/listsThunk";
import gate from "../../../gate";
import {history} from "../../../index";
import {useParams} from "react-router-dom";
import Form from "../../../Components/Form/Form";
import Image from "../../../Components/Image/Image";

function EditCompany({clearCompanies}) {
    const companies = useSelector((state) => state.companies);

    const {company_id} = useParams();
    const company = companies.items.filter((company) => company.company_id === parseInt(company_id))[0];

    const {register, handleSubmit, formState: {errors}} = useForm({
        defaultValues: {
            title: company.title,
            business_name: company.business_name,
            vat_number: company.vat_number,
            tax_code: company.tax_code,
            address: company.address,
            province: company.province,
            city: company.city,
            unique_code: company.unique_code,
        },
    });

    const [pending, setPending] = useState(false);
    const [preview, setPreview] = useState(false);
    const [selectedFile, setSelectedFile] = useState();
    const logoRef = useRef();
    const [hasTelegramNotification, setHasTelegramNotification] = useState(company.has_mail_notifications);
    const [hasEmailNotification, setHasEmailNotification] = useState(company.has_telegram_notifications);


    useEffect(() => {
        if (!selectedFile) {
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
        setPreview(true);
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
            .updateCompany(company_id, formData)
            .then((response) => {
                setPending(false);
                toast.success("Company edited successfully !");
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
        [{name: "title", type: "string", options: {required: true, maxLength: 30}}, {
            name: "business_name",
            type: "string",
            options: {required: true, maxLength: 30}
        }],
        [{name: "vat_number", type: "string", options: {required: true, minLength:11, maxLength: 11}}, {
            name: "tax_code",
            type: "string",
            options: {required: true, minLength:16, maxLength: 16}
        }],
        [{name: "unique_code", type: "string", options: {required: true, maxLength: 30}}, {
            name: "city",
            type: "string",
            options: {required: true, maxLength: 30}
        }],
        [{name: "province", type: "string", options: {required: true, minLength: 2, maxLength:2}}, {
            name: "address",
            type: "string",
            options: {required: true, maxLength: 30}
        }],
    ]

    return (
        <Form title={"Edit Company"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register}
              errors={errors} pending={pending}>
            {/** Another fields like image or selects that you can't use the default things that we are written in the fields **/}
            <div className="row justify-content-between text-left">
                {company.logo && !preview && (
                    <>
                        <div className="form-group col-sm-3 flex-column d-flex">
                            <Image id={company.company_id}/>
                        </div>
                        <div className="form-group col-sm-3 flex-column d-flex">
                            <button className={"btn btn-danger"} onClick={onDeleteFile}>Delete</button>
                        </div>
                    </>
                )}
                <div className="form-group col-sm-6 flex-column d-flex ">
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

export default connect(null, {clearCompanies})(EditCompany);