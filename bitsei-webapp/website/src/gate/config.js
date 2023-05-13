import axios from "axios";
import store from "../Store/store";

const env = process.env;
const baseUrl = env.REACT_APP_API_BASE_URL;
const siteUrl = env.REACT_APP_BASE_URL;
/*************************************
 *------* Setup Axios Configs *------*
 *************************************/
const client = axios;
client.defaults.timeout = 5000;
client.defaults.baseURL = baseUrl;

/*************************************
 *------* Request Interceptor *------*
 *************************************/
axios.defaults.headers.common["Content-Type"] = "application/json";
axios.defaults.headers.common["Accept"] = "application/json";


/**************************************
 *------* Response Interceptor *------*
 **************************************/
client.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      const { pathname } = window.location;
      if (pathname !== "/login") {
        store.dispatch({ type: "RESET_ACTION" });
        window.location.replace(`${siteUrl}/login`);
      }
    }
    return Promise.reject(error);
  }
);

export default client;
