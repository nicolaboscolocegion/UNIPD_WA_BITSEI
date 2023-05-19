import React, {useEffect} from "react";
import {toast} from "react-toastify";
import {useForm} from "react-hook-form";
import {connect, useSelector} from "react-redux";
import Input from "./Input/Input";
import Button from "./Button/Button";
import {userLogin, logout} from "../../Store/auth/authThunk";
import axios from "axios";
import gate from "../../gate";
import types from "../../Store/auth/authActionTypes";
import Footer from "../../Components/Footer/Footer";

function Login({userLogin, logout}) {
    const [isRestPassword, setIsRestPassword] = React.useState(false);
    const {register, handleSubmit} = useForm();
    const errors = useSelector((state) => state.auth.errors);

    useEffect(() => {
        logout();
    }, [logout]);

    const submitHandler = (data, e) => {
        e.preventDefault();
        userLogin(data);
    };

    const restPasswordHandler = (data, e) => {
        e.preventDefault();
        gate
            .forgetPass(data)
            .then((response) => {
                console.log(response);
                toast.success("Email sent successfully.");
                setIsRestPassword(false)
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
                                            {isRestPassword ? ("Password Recovery") : ("Login")}
                                        </h3>
                                    </div>
                                    <div className="card-body">
                                        {isRestPassword && <div className="small mb-3 text-muted">
                                            Enter your email address and we will send you a link to
                                            reset your password.
                                        </div>}

                                        <form
                                            onSubmit={handleSubmit(isRestPassword ? restPasswordHandler : submitHandler)}>
                                            <div className="form-floating mb-3">
                                                <Input name="Email Address" type="email" register={{
                                                    ...register("email", {
                                                        required: "Required",
                                                        pattern: {
                                                            value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                                            message: "invalid email address"
                                                        }
                                                    })
                                                }} errors={errors}/>

                                                {!isRestPassword &&
                                                    <Input name="Password"
                                                           type="password"
                                                           register={{...register("password", {required: true})}}
                                                           errors={errors}/>
                                                }
                                            </div>
                                            <div
                                                className="d-flex align-items-center justify-content-between mt-4 mb-0">
                                                <a className="small btn"
                                                   onClick={() => setIsRestPassword(!isRestPassword)}>
                                                    {isRestPassword ? ("Login Page") : ("Forgot Password?")}
                                                </a>
                                                {isRestPassword ? (
                                                    <Button title={"Rest Password"}>
                                                        Rest Password
                                                    </Button>
                                                ) : (
                                                    <Button title={"Login"}>
                                                        Login
                                                    </Button>
                                                )}
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
    );
}

export default connect(null, {userLogin, logout})(Login);
