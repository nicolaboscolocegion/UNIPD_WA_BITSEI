import { lazy } from "react";

const Login = lazy(() => import("../Pages/Login/Login"));
const ResetPassword = lazy(() => import("../Pages/Login/ResetPassword"));
const Companies = lazy(() => import("../Pages/Company/List/List"));
const Company = lazy(() => import("../Pages/Company/Item/Item"));
const EditCompany = lazy(() => import("../Pages/Company/EditCompany/Edit"));
const AddCompany = lazy(() => import("../Pages/Company/AddNewCompany/Add"));
const Customer = lazy(() => import("../Pages/Customers/GetCustomers/Item"));
const Customers = lazy(() => import("../Pages/Customers/GetCustomers/List"));

export const routes = [
    { exact: true, path: "/login", component: Login },
    { exact: true, path: "/reset-password/:token", component: ResetPassword },
];

export const privateRoutes = [
    { exact: true, path: "/companies", component: Companies },
    { exact: true, path: "/companies/add", component: AddCompany },
    { exact: true, path: "/companies/:company_id", component: Company },
    { exact: true, path: "/companies/edit/:company_id", component: EditCompany },
    { exact: true, path: "/customers/:customer_id/company/:company_id", component: Customer },
    { exact: true, path: "/list-customer/company/:company_id", component: Customers },];
