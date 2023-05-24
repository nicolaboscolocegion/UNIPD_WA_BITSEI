import React from "react";

function Input({name, register, type, defaultValue }) {
  return (
      <div className="form-group mb-4">
          <label className="form-label text-start">{name}:</label>
          <input
              placeholder={`Enter ${name}`}
              className="form-control form-control-lg"
              type={type}
              name={name}
              defaultValue={defaultValue}
              {...register}
          />
      </div>
  );
}

export default Input;
