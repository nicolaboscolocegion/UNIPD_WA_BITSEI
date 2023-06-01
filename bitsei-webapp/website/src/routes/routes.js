import { lazy } from "react";

const Login = lazy(() => import("../Pages/Login/Login"));
const ResetPassword = lazy(() => import("../Pages/Login/ResetPassword"));
const Companies = lazy(() => import("../Pages/Company/List/List"));
const Company = lazy(() => import("../Pages/Company/Item/Item"));
const EditCompany = lazy(() => import("../Pages/Company/EditCompany/Edit"));
const AddCompany = lazy(() => import("../Pages/Company/AddNewCompany/Add"));
const ListInvoices = lazy(() => import("../Pages/Invoices/List"));
const ShowChart = lazy(() => import("../Pages/Insights/Show"));
const Customer = lazy(() => import("../Pages/Customers/GetCustomers/Item"));
const Customers = lazy(() => import("../Pages/Customers/GetCustomers/List"));
const Product = lazy(() => import("../Pages/Products/GetProducts/Item"));
const Products = lazy(() => import("../Pages/Products/GetProducts/List"));
const GetInvoicesByFilters = lazy(() => import("../Pages/Invoices/GetInvoicesByFilters"));
//const ListCustomers = lazy(() => import("../Pages/Invoices/GetInvoicesByFilters"));
const ListProducts = lazy(() => import("../Pages/Invoices/GetInvoicesByFilters"));
const AddCustomer = lazy(() => import("../Pages/Customers/AddCustomer/Add"));
const EditCustomer = lazy(() => import("../Pages/Customers/EditCustomer/Edit"));
const EditProduct = lazy(() => import("../Pages/Products/EditProduct/Edit"));
const AddProduct = lazy(() => import("../Pages/Products/AddProduct/Add"));
const AddBankAccount = lazy(() => import("../Pages/BankAccount/AddNewBankAccount/Add")); 
const EditBankAccount = lazy(() => import("../Pages/BankAccount/EditBankAccount/Edit"));
const ListBankAccount = lazy(() => import("../Pages/BankAccount/List/List"));
const CloseInvoice = lazy(() => import("../Pages/Invoices/List"));
const GenerateInvoice = lazy(() => import("../Pages/Invoices/List"));
const AddInvoice = lazy(() => import("../Pages/Invoices/Add"));
const GetInvoiceDocument = lazy(() => import("../Pages/Invoices/List"));


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
    { exact: true, path: "/companies/:company_id/products/:product_id", component: Product },
    { exact: true, path: "/companies/:company_id/list-customer", component: Customers },
    { exact: true, path: "/companies/:company_id/list-products", component: Products },
    { exact: true, path: "/companies/:company_id/list-invoices", component: ListInvoices },
    { exact: true, path: "/companies/:company_id/insights", component: ShowChart },
    { exact: true, path: "/companies/:company_id/bankAccount/add", component: AddBankAccount },
    { exact: true, path: "/companies/:company_id/bankAccount/:bankaccount_id/edit", component: EditBankAccount },
    { exact: true, path: "/companies/:company_id/bankAccount/", component: ListBankAccount },
    { exact: true, path: "/companies/edit/:company_id", component: EditCompany },
    { exact: true, path: "/insights", component: ShowChart },
    { exact: true, path: "/companies/edit/:company_id", component: EditCompany },
    { exact: true, path: "/companies/:company_id/get-invoices", component: GetInvoicesByFilters },
    { exact: true, path: "/companies/:company_id/customer/add", component: AddCustomer },
    { exact: true, path: "/companies/:company_id/customer/edit/:customer_id", component: EditCustomer },
    { exact: true, path: "/companies/:company_id/product/edit/:product_id", component: EditProduct },
    { exact: true, path: "/list-product", component: ListProducts },
    { exact: true, path: "/companies/:company_id/product/add", component: AddProduct },
    { exact: true, path: "/companies/:company_id/list-product", component: ListProducts },
    { exact: true, path: "/companies/:company_id/invoice/close", component: CloseInvoice },
    { exact: true, path: "/companies/:company_id/invoice/generate", component: GenerateInvoice},
    { exact: true, path: "/companies/:company_id/invoice/add", component: AddInvoice},
    { exact: true, path: "/companies/:company_id/invoice/get-document", component: GetInvoiceDocument},
];
