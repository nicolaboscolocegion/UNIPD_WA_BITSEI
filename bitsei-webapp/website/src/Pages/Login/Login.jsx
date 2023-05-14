import React, { useEffect } from "react";
import {toast} from "react-toastify";
import { useForm } from "react-hook-form";
import { connect, useSelector } from "react-redux";
import Input from "./Input/Input";
import Button from "./Button/Button";

import { userLogin, logout } from "../../Store/auth/authThunk";
import axios from "axios";
import gate from "../../gate";
import types from "../../Store/auth/authActionTypes";

function Login({ userLogin, logout }) {
  const [isRestPassword, setIsRestPassword] = React.useState(false);
  const { register, handleSubmit } = useForm();
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
    <div>
      <main>
        <section className="vh-100">
          <div className="container-fluid">
            <div className="row">
              <div className="col-sm-6 text-black">
                <div className="px-5 ms-xl-4">
                  <span className="h1 fw-bold mb-0">BITSEI</span>
                </div>
                <div className="d-flex align-items-center h-custom-2 px-5 ms-xl-4 mt-5 pt-5 pt-xl-0 mt-xl-n5">
                  {isRestPassword ? (
                      <form style={{ width: '23rem' }} onSubmit={handleSubmit(restPasswordHandler)}>

                        <h3 className="fw-normal mb-3 pb-3" style={{ letterSpacing: '1px' }}>Reset Password</h3>

                        <Input name="Email Address" type="email" register={{...register("email", {
                            required: "Required",
                            pattern: {
                              value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                              message: "invalid email address"
                            }
                          })}} errors={errors} />

                        <Button title={"Send Rest Password"}/>
                        <h1 className={"btn"} onClick={()=>setIsRestPassword(false)}>Login</h1>
                      </form>
                  ) : (
                  <form style={{ width: '23rem' }} onSubmit={handleSubmit(submitHandler)}>

                    <h3 className="fw-normal mb-3 pb-3" style={{ letterSpacing: '1px' }}>Log in</h3>

                    <Input name="Email Address" type="email" register={{...register("email", {
                        required: "Required",
                        pattern: {
                          value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                          message: "invalid email address"
                        }
                    })}} errors={errors} />

                    <Input name="Password" type="password" register={{...register("password", { required: true })}} errors={errors} />

                    <Button title={"Login"}/>
                    <h1 className={"btn"} onClick={()=>setIsRestPassword(true)}>Forget Password ? Reset ...</h1>
                    </form>
                  )}
                </div>
              </div>
              <div className="col-sm-6 px-0 d-none d-sm-block">
                <img
                    src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/img3.webp"
                    alt="Login"
                    className="w-100 vh-100"
                    style={{ objectFit: 'cover', objectPosition: 'left' }}
                />
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>
  );
}

export default connect(null, { userLogin, logout })(Login);
