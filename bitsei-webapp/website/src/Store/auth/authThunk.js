import types from "./authActionTypes";
import axios from "axios";
import gate from "../../gate";
import { history } from "../../index";
import { toast } from "react-toastify";

axios.defaults.baseURL = "http://localhost:8080/bitsei-v1/api";

export const userLogin = (data) => {
    return async (dispatch) => {
        dispatch({ type: types.LOGIN_START });
        setTimeout(function () {

        gate
            .login(data)
            .then((response) => {
                console.log(response);
                window.localStorage.setItem("accessToken", response.data.accessToken);
                axios.defaults.headers.common["accessToken"] = response.data.accessToken;
                gate
                    .userInfo()
                    .then((res) => {
                        console.log(res);
                        dispatch({
                            type: types.LOGIN_SUCCESS,
                            payload: { user: res.data, tokens: response.data },
                        });
                        history.push("/companies");
                    })
                    .catch((error) => {
                        console.log(error.response);
                    });
            })
            .catch((error) => {
                // check if error.response is not null -> maybe the internet is not working
                if (error.response === undefined) {
                    toast.error("Please check your internet connection.");
                    dispatch({
                        type: types.LOGIN_FAIL,
                        payload: "internet connection error",
                    });
                    return;
                }

                dispatch({
                    type: types.LOGIN_FAIL,
                    payload: error.response.data.messages,
                });

                if (typeof error.response.data.messages[0] === "string") {
                    error.response.data.messages.map((item) => toast.error(item));
                }
            });
        }, 5000);
    };
};

// export const updateUser = () => {
//     return (dispatch) => {
//         gate
//             .userInfo()
//             .then((response) => {
//                 // console.log(response);
//                 dispatch({ type: types.UPDATE_USER, payload: response.data });
//             })
//             .catch((error) => {
//                 toast.error(errorGen(errorGen));
//             });
//     };
// };
//
export const logout = () => {
    return (dispatch) => {
        localStorage.removeItem("accessToken");

        dispatch({ type: types.LOGOUT });
    };
};
