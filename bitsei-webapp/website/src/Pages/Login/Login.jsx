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

  /*return (
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
  );*/

  return (
      <>
      {isRestPassword ? (
          <>
              <title>Password Reset - SB Admin</title>
              <div id="layoutAuthentication">
                <div id="layoutAuthentication_content">
                  <main>
                    <div className="container">
                      <div className="row justify-content-center">
                        <div className="col-lg-5">
                          <div className="card shadow-lg border-0 rounded-lg mt-5">
                            <div className="card-header">
                              <h3 className="text-center font-weight-light my-4">
                                Password Recovery
                              </h3>
                            </div>
                            <div className="card-body">
                              <div className="small mb-3 text-muted">
                                Enter your email address and we will send you a link to
                                reset your password.
                              </div>
                              <form onSubmit={handleSubmit(restPasswordHandler)}>
                                <div className="form-floating mb-3">
                                    <Input name="Email Address" type="email" register={{...register("email", {
                                            required: "Required",
                                            pattern: {
                                                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                                message: "invalid email address"
                                            }
                                        })}} errors={errors} />
                                  <label htmlFor="inputEmail">Email address</label>
                                </div>
                                <div className="d-flex align-items-center justify-content-between mt-4 mb-0">
                                  <a className="small" href="login.html">
                                    Return to login
                                  </a>
                                  <a className="btn btn-primary" href="login.html">
                                    Reset Password
                                  </a>
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
                  <footer className="py-4 bg-light mt-auto">
                    <div className="container-fluid px-4">
                      <div className="d-flex align-items-center justify-content-between small">
                        <div className="text-muted">Copyright © BITSEI 2023</div>
                        <div>
                          <a href="#">Privacy Policy</a>·
                          <a href="#">Terms &amp; Conditions</a>
                        </div>
                      </div>
                    </div>
                  </footer>
                </div>
              </div>
            </>


        ) : (
      <>
        <title>BITSEI</title>
        <div id="layoutAuthentication">
          <div id="layoutAuthentication_content">
            <main>
              <div className="container">
                <div className="row justify-content-center">
                  <div className="col-lg-5">
                    <div className="card shadow-lg border-0 rounded-lg mt-5">
                      <div className="card-header">
                        <h3 className="text-center font-weight-light my-4">Login</h3>
                      </div>
                      <div className="card-body">
                        <form onSubmit={handleSubmit(submitHandler)}>

                              <Input name="Email Address" type="email" register={{...register("email", {
                                      required: "Required",
                                      pattern: {
                                          value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                                          message: "invalid email address"
                                      }
                                  })}} errors={errors} />

                              <Input name="Password" type="password" register={{...register("password", { required: true })}} errors={errors} />
                          <div className="form-check mb-3">
                            <input
                                className="form-check-input"
                                id="inputRememberPassword"
                                type="checkbox"
                                defaultValue=""
                            />
                            <label
                                className="form-check-label"
                                htmlFor="inputRememberPassword"
                            >
                              Remember Password
                            </label>
                          </div>
                          <div className="d-flex align-items-center justify-content-between mt-4 mb-0">
                            <a className="small btn"  onClick={()=>setIsRestPassword(true)}>
                              Forgot Password?
                            </a>

                            <Button  title={"Login"}  >
                              Login
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
            <footer className="py-4 bg-light mt-auto">
              <div className="container-fluid px-4">
                <div className="d-flex align-items-center justify-content-between small">
                  <div className="text-muted">Copyright © BITSEI 2023</div>
                  <div>
                    <a href="#">Privacy Policy</a>·
                    <a href="#">Terms &amp; Conditions</a>
                  </div>
                </div>
              </div>
            </footer>
          </div>
        </div>
      </>
        )}
      </>

  );
}

export default connect(null, { userLogin, logout })(Login);
