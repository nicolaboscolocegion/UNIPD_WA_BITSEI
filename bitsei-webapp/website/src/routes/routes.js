import { lazy } from "react";

const Login = lazy(() => import("../Pages/Login/Login"));
const ResetPassword = lazy(() => import("../Pages/Login/ResetPassword"));
const Companies = lazy(() => import("../Pages/Company/List/List"));
const Company = lazy(() => import("../Pages/Company/Item/Item"));
const EditCompany = lazy(() => import("../Pages/Company/EditCompany/Edit"));
const AddCompany = lazy(() => import("../Pages/Company/AddNewCompany/Add"));
const ShowChart = lazy(() => import("../Pages/Insights/Show"));
const Customer = lazy(() => import("../Pages/Customers/GetCustomers/Item"));
const Customers = lazy(() => import("../Pages/Customers/GetCustomers/List"));
const GetInvoicesByFilters = lazy(() => import("../Pages/Insights/GetInvoicesByFilters"));
const ListCustomers = lazy(() => import("../Pages/Insights/GetInvoicesByFilters"));
const ListProducts = lazy(() => import("../Pages/Insights/GetInvoicesByFilters"));
const AddCustomer = lazy(() => import("../Pages/Customers/AddCustomer/Add"));

export const routes = [
    { exact: true, path: "/login", component: Login },
    { exact: true, path: "/reset-password/:token", component: ResetPassword },
];

export const privateRoutes = [
    { exact: true, path: "/companies", component: Companies },
    { exact: true, path: "/companies/add", component: AddCompany },
    { exact: true, path: "/companies/:company_id", component: Company },
    { exact: true, path: "/companies/:company_id/edit", component: EditCompany },
    { exact: true, path: "/companies/:company_id/customers/:customer_id", component: Customer },
    { exact: true, path: "/companies/:company_id/list-customer", component: Customers },
    { exact: true, path: "/companies/:company_id/insights", component: ShowChart },
    { exact: true, path: "/companies/edit/:company_id", component: EditCompany },
    { exact: true, path: "/insights", component: ShowChart },
    { exact: true, path: "/companies/edit/:company_id", component: EditCompany },
    { exact: true, path: "/get-invoices", component: GetInvoicesByFilters },
    { exact: true, path: "/list-customer", component: ListCustomers },
    { exact: true, path: "/companies/:company_id/customer/add", component: AddCustomer },
    { exact: true, path: "/list-product", component: ListProducts },];
