import { lazy } from "react";

const Login = lazy(() => import("../Pages/Login/Login"));
// const Companies = lazy(() => import("Pages/Compnay/MainPage"));

export const routes = [
    { exact: true, path: "/login", element: <Login/> },
];

export const privateRoutes = [
    // { exact: true, path: "/companies", element: <Companies/> },
];
