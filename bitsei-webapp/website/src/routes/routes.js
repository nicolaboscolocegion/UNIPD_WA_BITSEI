import { lazy } from "react";

const Login = lazy(() => import("../Pages/Login/Login"));
const Companies = lazy(() => import("../Pages/Company/List/List"));

export const routes = [
    { exact: true, path: "/login", component: Login },
];

export const privateRoutes = [
    { exact: true, path: "/companies", component: Companies },
];
