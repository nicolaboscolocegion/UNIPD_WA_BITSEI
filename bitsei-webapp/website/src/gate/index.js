import api from './config';

const auth = {
    login: (data) =>
        api.post('rest/login', data),
    forgetPass: (data) =>
        api.post('rest/user/reset-password', data),
    changePassword: (data) =>
        api.post(`rest/user/change-password`, data)
};

const user = {
    updateProfile: (data) => api.post('/api/v1/user/company', data, {
        headers: {
            // prepare for file upload
            'Content-Type': 'multipart/form-data'
        },
    }),
}

const me = {
    userInfo: () => api.get("/api/v1/user/me"),
}

const companies = {
    createCompany: (data) => api.post("rest/company", data, {
        headers: {
            "Content-Type": "multipart/form-data",
        }
    }),
    getCompanies: () => api.get("rest/company"),
    getCompany: (id) => api.get(`/api/v1/company/${id}`),
    updateCompany: (id, data) => api.put(`rest/company/${id}`, data, {
        headers: {
            "Content-Type": "multipart/form-data",
        }
    }),
    getCompanyImage: (path) => api.get(`rest/company/image/${path}`, {
        responseType: "arraybuffer"
    }),
}

const invoices = {
    getInvoicesByFilters: (id, data) => api.post("rest/filter-invoices/company/" + id, data),
    listCustomers: (id) => api.get("rest/list-customer/company/" + id),
    listProducts: (id) => api.get("rest/list-product/company/" + id),
    closeInvoice: (company_id, invoice_id) => api.put("rest/closeinvoice/" + invoice_id + "/company/" + company_id, null, {
        headers: { 'Content-Type': 'application/json' },
    }),
    generateInvoice: (company_id, invoice_id) => api.put("rest/generateinvoice/" + invoice_id + "/company/" + company_id, null, {
        headers: { 'Content-Type': 'application/json' },
    }),
    getInvoiceDocument: (company_id, invoice_id, document_type) => api.get("rest/getdocument/" + document_type + "/invoice/" + invoice_id  + "/company/" + company_id),
    getInvoiceProducts: (company_id, invoice_id) => api.get(`rest/invoice/${company_id}/${invoice_id}`),
}

const insights = {
    getChartInvoiceByFilters: (data) => api.post("rest/charts/company/1", data)
}

const bankAccount ={
    createBankAccount: (data, company_id) => api.post("rest/bankaccount/company/" + company_id, data ),
    getBankAccount: (bankAccount_id, company_id) => api.get("rest/bankaccount/" + bankAccount_id + "/company/" + company_id),
    editBankAccount: (data, bankAccount_id, company_id) => api.put("rest/bankaccount/"+bankAccount_id+"/company/" + company_id, data),
    listBankAccount: (company_id) => api.get("rest/bankaccount/company/" + company_id),
    deleteBankAccount: (company_id, bankAccount_id) => api.delete("/rest/bankaccount/"+ bankAccount_id +"/company/"+ company_id),
}

const customers = {
    getCustomer: (customer_id, company_id) => api.get("rest/customer/" + customer_id + "/company/" + company_id),
    getCustomers: (company_id) => api.get("rest/list-customer/company/" + company_id),
    addCustomer: (data, company_id) => api.post("rest/customer/company/" + company_id, data),
    editCustomer: (data, customer_id, company_id) => api.put("rest/customer/" + customer_id + "/company/" + company_id, data),
    deleteCustomer: (customer_id, company_id) => api.delete("rest/customer/" + customer_id + "/company/" + company_id),
}

const products = {
    getProduct: (product_id, company_id) => api.get("rest/product/" + product_id + "/company/" + company_id),
    getProducts: (company_id) => api.get("rest/list-product/company/" + company_id),
    addProduct: (data, company_id) => api.post("rest/product/company/" + company_id, data),
    editProduct: (data, product_id, company_id) => api.put("rest/product/" + product_id + "/company/" + company_id, data),
    deleteProduct: (product_id, company_id) => api.delete("rest/product/" + product_id + "/company/" + company_id),
}

const gate = {
    ...me,
    ...user,
    ...auth,
    ...companies,
    ...invoices,
    ...insights,
    ...customers,
    ...products,
    ...bankAccount
};

export default gate;
