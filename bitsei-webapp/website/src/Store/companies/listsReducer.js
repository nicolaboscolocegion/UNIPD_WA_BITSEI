import types from "./listsActionTypes";

const INITIAL_STATE = {
  items: [],
  pending: true,
  errors: [],
};

const companiesReducer = (currentState = INITIAL_STATE, { type, payload }) => {
  switch (type) {
    case types.GET_LISTS_START:
      return {
        ...currentState,
        pending: true,
      };
    case types.GET_LISTS_SUCCESS:
      return {
        ...currentState,
        pending: false,
        companies: payload,
        isLoggedIn: true,
      };
    case types.GET_LISTS_FAIL:
      return {
        ...currentState,
        pending: false,
        errors: payload,
      };
    default:
      return currentState;
  }
};

export default companiesReducer;
