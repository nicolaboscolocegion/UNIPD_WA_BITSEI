import types from "./authActionTypes";
import axios from "axios";
import gate from "../../gate";
import { history } from "../../index";
import { toast } from "react-toastify"

export const userLogin = (data) => {
    return async (dispatch) => {
        dispatch({ type: types.LOGIN_START });
        gate
            .login(data)
            .then((response) => {
                console.log(response);
                window.localStorage.setItem("Authorization", response.data.token);
                axios.defaults.headers.common["Authorization"] = response.data.token;

                gate
                    .userInfo()
                    .then((res) => {
                        dispatch({
                            type: types.LOGIN_SUCCESS,
                            payload: { user: res.data.user, token: response.data.token },
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
                        payload: "Internet connection error",
                    });
                    return;
                }

                dispatch({
                    type: types.LOGIN_FAIL,
                    payload: error.response.data.messages,
                });

                if (error.response.data.message.message){
                    toast.error(error.response.data.message.message)
                } else {
                    toast.error("Something went wrong. Please try again.")
                }
            });
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
        localStorage.removeItem("Authorization");
        dispatch({ type: types.LOGOUT });
    };
};
