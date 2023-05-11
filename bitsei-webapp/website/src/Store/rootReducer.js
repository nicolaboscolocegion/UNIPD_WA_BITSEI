import { combineReducers } from "redux";
import authReducer from "./auth/authReducer";
import companiesReducer from "./companies/listsReducer";

const appReducer = combineReducers({
    auth: authReducer,
    companies: companiesReducer,
});

const rootReducer = (state, action) => {
    if (action.type === "RESET_ACTION") {
        localStorage.removeItem("persist:bitsei");
        window.localStorage.removeItem("accessToken");

        const { routing } = state;
        state = { routing };
    }

    return appReducer(state, action);
};

export default rootReducer;
