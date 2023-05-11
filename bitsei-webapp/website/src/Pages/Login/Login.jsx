import React, { useEffect } from "react";
import { useForm } from "react-hook-form";
import { connect, useSelector } from "react-redux";
import Input from "./Input/Input";
import Button from "./Button/Button";

import { userLogin, logout } from "../../Store/auth/authThunk";

function Login({ userLogin, logout }) {
  const { register, handleSubmit } = useForm();
  const errors = useSelector((state) => state.auth.errors);

  useEffect(() => {
    logout();
  }, [logout]);

  const submitHandler = (data, e) => {
    e.preventDefault();
    console.log(data)
    userLogin(data);
  };

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

                    <Button/>

                    </form>
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
