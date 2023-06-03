import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import gate from "../../../gate";
import {useParams} from "react-router-dom";
import Form from "../../../Components/Form/Form";

function AddProduct() {
    const {register, handleSubmit, formState: { errors }} = useForm();
    const [pending, setPending] = useState(false);
    const {company_id} = useParams();

    const submitHandler = (data, e) => {
        e.preventDefault();

        setPending(true);
        console.log(data)
        data.default_price = parseFloat(data.default_price);
        gate
            .addProduct({product: {company_id: parseInt(company_id), ...data}}, parseInt(company_id))
            .then((response) => {
                console.log(response.data)
                setPending(false)
            })
            .catch((error) => {
                console.log(error)
            })


    };

    const fields = [
        [{value: "Title", name: "title", type: "string"}, {value: "Default Price", name: "default_price", type: "double"}],
        [{value: "Measurement Unit", name: "measurement_unit", type: "string"}, {value: "Description", name: "description", type: "string"} ],
    ]

    return (
        <Form title={"Add Product"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register} errors={errors}/>
    )
}

export default AddProduct;

/*import React, {useEffect, useRef, useState} from "react";
import {useForm} from "react-hook-form";
import {connect} from "react-redux";
import {toast} from "react-toastify";
//import {clearProducts} from "../../../Store/products/listsThunk";
import gate from "../../../gate";
import {history} from "../../../index";
import Form from "../../../Components/Form/Form";
import {useParams} from "react-router-dom";

function AddProduct({clearProducts}) {
    const {register, handleSubmit, formState: { errors }} = useForm();
    const [pending, setPending] = useState(false);
    const [preview, setPreview] = useState();
    const [selectedFile, setSelectedFile] = useState();
    const logoRef = useRef();
    const {company_id} = useParams();


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
        formData.append("default_price", data.default_price);
        formData.append("measurement_unit", data.measurement_unit);
        formData.append("description", data.description);

        logoRef.current.files[0] && formData.append("logo", logoRef.current.files[0]);

        gate
            .createProduct(company_id, formData)
            .then((response) => {
                setPending(false);
                toast.success("New product added successfully !");
                //clearProducts();
                history.push("/products");
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
        [{name: "title", type: "string", options: { required: true, maxLength: 30 }}, {name: "default_price", type: "int"}],
        [{name: "measurement_unit", type: "string"}, {name: "description", type: "string"}],
    ]
    return (
        <Form title={"Product"} onSubmit={handleSubmit(submitHandler)} fields={fields} register={register} errors={errors} pending={pending}>
            {}
            <div className="row justify-content-between text-left">
                <div className="form-group flex-column d-flex">
                    <label className="form-control-label px-3" htmlFor="logo">logo</label>
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
        </Form>
    )
}

//export default connect(null, {clearProducts})(AddProduct);
export default connect(null)(AddProduct);*/