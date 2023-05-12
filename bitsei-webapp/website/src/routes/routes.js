import { lazy } from "react";

const Login = lazy(() => import("../Pages/Login/Login"));
const Companies = lazy(() => import("../Pages/Company/List/List"));
const Company = lazy(() => import("../Pages/Company/Item/Item"));
const EditCompany = lazy(() => import("../Pages/Company/EditCompany/Edit"));
const AddCompany = lazy(() => import("../Pages/Company/AddNewCompany/Add"));

export const routes = [
    { exact: true, path: "/login", component: Login },
    { exact: true, path: "/companies", component: Companies },
    { exact: true, path: "/companies/add", component: AddCompany },
    { exact: true, path: "/companies/:company_id", component: Company },
    { exact: true, path: "/companies/edit/:company_id", component: EditCompany },
];

export const privateRoutes = [
    // { exact: true, path: "/companies", component: Companies },
];
