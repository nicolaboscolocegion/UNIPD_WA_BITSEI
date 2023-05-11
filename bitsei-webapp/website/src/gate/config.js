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
client.interceptors.request.use(
  async (config) => {
    // *------* Set Headers *------*
    config.headers["Accept"] = "application/json";
    config.headers["Content-Type"] = "application/json";

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

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
      if (pathname !== "/login" && pathname !== "/register") {
        store.dispatch({ type: "RESET_ACTION" });

        const url = env.NODE_ENV === "production" ? siteUrl : ""; // redirect to localhost in development mode
        window.location.replace(`${url}/login`);
      }
    }
    return Promise.reject(error);
  }
);

export default client;
