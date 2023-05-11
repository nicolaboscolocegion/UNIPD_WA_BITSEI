import React from "react";

function Input({ name, register, type, error }) {
  return (
      <div className="form-outline mb-4">
          <input
              className="form-control form-control-lg"
              type={type}
              name={name}
              {...register}
          />
          <label className="form-label">{name}</label>
      </div>
  );
}

export default Input;
