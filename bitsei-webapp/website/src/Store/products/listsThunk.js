import types from "./listsActionTypes";
import gate from "../../gate";

export const getLists = () => {
  return (dispatch) => {
    dispatch({ type: types.GET_LISTS_START });
    gate
      .getProducts()
      .then((response) => {
        dispatch({ type: types.GET_LISTS_SUCCESS, payload: response.data["resource-list"] });
      })
      .catch((error) => {
        // console.log(error.response);
        dispatch({ type: types.GET_LISTS_FAIL, payload: "ERROR" });
      });
  };
};

export const clearProducts = () => ({ type: types.CLEAR_LISTS_STORE });

//export const setActiveProductId = (company_id) => ({ type: types.CHANGE_ACTIVE_COMPANY, payload: product_id })