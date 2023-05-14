import React from "react";
import {toast} from "react-toastify";
import {useForm} from "react-hook-form";
import {connect, useSelector} from "react-redux";
import Input from "./Input/Input";
import Button from "./Button/Button";

import gate from "../../gate";
import {history} from "../../index";
import {useParams} from "react-router-dom";

function ChangePassword() {
    const {register, handleSubmit} = useForm();
    const {token} = useParams()
    const errors = useSelector((state) => state.auth.errors);

    const changePasswordHandler = (data, e) => {
        e.preventDefault();
        data.reset_token = token;
        console.log(data);
        gate
            .changePassword({data})
            .then((response) => {
                console.log(response);
                toast.success("Password changed successfully.");
                history.push("/login")
            })
            .catch((error) => {
                toast.error("Something went wrong.");
            });
    }

    return (
        <div>
            <main>
                <section className="vh-100">
                    <div className="container-fluid">
                        <div className="row">
                            <div className="col-sm-6 text-black">
                                <div className="px-5 ms-xl-4">
                                    <span className="h1 fw-bold mb-0">BITSEI</span>
                                </div>
                                <div
                                    className="d-flex align-items-center h-custom-2 px-5 ms-xl-4 mt-5 pt-5 pt-xl-0 mt-xl-n5">
                                    <form style={{width: '23rem'}} onSubmit={handleSubmit(changePasswordHandler)}>

                                        <h3 className="fw-normal mb-3 pb-3" style={{letterSpacing: '1px'}}>Reset
                                            Password</h3>

                                        <Input name="New Password" type="password" register={{
                                            ...register("password", {
                                                required: "Required"
                                            })
                                        }} errors={errors}/>

                                        <Button title={"Change Password"}/>
                                    </form>

                                </div>
                            </div>
                            <div className="col-sm-6 px-0 d-none d-sm-block">
                                <img
                                    src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/img3.webp"
                                    alt="Login"
                                    className="w-100 vh-100"
                                    style={{objectFit: 'cover', objectPosition: 'left'}}
                                />
                            </div>
                        </div>
                    </div>
                </section>
            </main>
        </div>
    );
}

export default connect(null, {})(ChangePassword);
