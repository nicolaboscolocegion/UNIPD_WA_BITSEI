import axios from "axios";
import store from "../Store/store";

const env = process.env;
const baseUrl = env.BITBUCKET_PIPELINE ? "http://bitsei.it/rest/" : env.REACT_APP_API_BASE_URL || "http://localhost:8080/bitsei-1.0/";
const siteUrl = env.BITBUCKET_PIPELINE ? "http://bitsei.it/login" : env.REACT_APP_BASE_URL || "http://localhost:3000";
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
