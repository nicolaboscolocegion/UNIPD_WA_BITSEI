import types from "./listsActionTypes";

const INITIAL_STATE = {
    items: [],
    activeCompany: null,
    pending: true,
    errors: [],
};

const companiesReducer = (currentState = INITIAL_STATE, {type, payload}) => {
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
                isLoggedIn: true,
            };
        case types.GET_LISTS_FAIL:
            return {
                ...currentState,
                pending: false,
                errors: payload,
                activeCompany: 0
            };
        case types.CHANGE_ACTIVE_COMPANY:
            return {
                ...currentState,
                activeCompany: payload,
            }
        default:
            return currentState;
    }
};

export default companiesReducer;
