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

const customers = {
    getCustomer: (customer_id, company_id) => api.get(`rest/customer/${customer_id}/company/${company_id}`),
    getCustomers: (company_id) => api.get(`rest/list-customer/company/${company_id}`)
}

const gate = {
    ...me,
    ...user,
    ...auth,
    ...companies,
    ...customers
};

export default gate;
