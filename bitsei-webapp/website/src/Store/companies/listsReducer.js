import types from "./listsActionTypes";

const INITIAL_STATE = {
  items: [],
  activeCompany: null,
  pending: true,
  errors: [],
};

const companiesReducer = (currentState = INITIAL_STATE, { type, payload }) => {
  switch (type) {
    case types.GET_LISTS_START:
      return {
        ...currentState,
        activeCompany: 0,
        pending: true,
      };
    case types.GET_LISTS_SUCCESS:
      return {
        ...currentState,
        pending: false,
        items: payload,
        activeCompany: payload.length > 0 ? payload[0]["company_id"] : 0,
        isLoggedIn: true,
      };
    case types.GET_LISTS_FAIL:
      return {
        ...currentState,
        pending: false,
        errors: payload,
        activeCompany: 0
      };
    default:
      return currentState;
  }
};

export default companiesReducer;
