import { toast } from "react-toastify";

import types from "./listsActionTypes";
import gate from "../../gate";
// import { errorGen } from "helpers/errorGenerator";

export const getLists = () => {
  return (dispatch) => {
    dispatch({ type: types.GET_LISTS_START });
    gate
      .getCompanies()
      .then((response) => {
        dispatch({ type: types.GET_LISTS_SUCCESS, payload: response.data });
      })
      .catch((error) => {
        // console.log(error.response);
        dispatch({ type: types.GET_LISTS_FAIL, payload: "ERROR" });
      });
  };
};