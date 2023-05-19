import React from "react";
import {toast} from "react-toastify";
import {useForm} from "react-hook-form";
import {connect, useSelector} from "react-redux";
import Input from "./Input/Input";
import Button from "./Button/Button";

import gate from "../../gate";
import {history} from "../../index";
import {useParams} from "react-router-dom";
import Footer from "../../Components/Footer/Footer";

function ChangePassword() {
    const {register, handleSubmit} = useForm();
    const {token} = useParams()
    const errors = useSelector((state) => state.auth.errors);

    const changePasswordHandler = (data, e) => {
        e.preventDefault();
        data.reset_token = token;
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
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div className="container">
                        <div className="row justify-content-center">
                            <div className="col-lg-5">
                                <div className="card shadow-lg border-0 rounded-lg mt-5">
                                    <div className="card-header">
                                        <h3 className="text-center font-weight-light my-4">
                                            Change Password
                                        </h3>
                                    </div>
                                    <div className="card-body">
                                        <div className="small mb-3 text-muted">
                                            Enter new password, you can access to your account with this password.
                                        </div>
                                        <form
                                            onSubmit={handleSubmit(changePasswordHandler)}>
                                            <div className="form-floating mb-3">
                                                <Input name="New Password" type="password" register={{
                                                    ...register("password", {
                                                        required: "Required"
                                                    })
                                                }} errors={errors}/>
                                            </div>
                                            <div>
                                                <Button title={"Rest Password"}>
                                                    Rest Password
                                                </Button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
            <div id="layoutAuthentication_footer">
                <Footer/>
            </div>

        </div>
    )

}

export default connect(null, {})(ChangePassword);
