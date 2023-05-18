import React from "react";
import "../../../styles.css";

function Input({ name, register, type, error }) {
  return (
      <div className="form-floating mb-3">
          <input
              className="form-control"
              type={type}
              name={name}
              {...register}
          />
          <label >{name}</label>
      </div>
  );
}

export default Input;
