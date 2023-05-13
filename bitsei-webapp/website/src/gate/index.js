import api from './config';

const auth = {
    login: (data) =>
        api.post('rest/login', data),
    forgetPass: (data) =>
        api.post('/auth/user/fpwmail', data),
    changePassword: (token, data) =>
        api.post(`/auth/user/reset/${token}`, data)
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
    createCompany: (data) => api.post("/api/v1/company", data),
    getCompanies: () => api.get("rest/company"),
    getCompany: (id) => api.get(`/api/v1/company/${id}`),
    updateCompany: (id, data) => api.put(`/api/v1/company/${id}`, data),
    getCompanyImage: (path) => api.get(`rest/company/image/${path}`, {
        responseType: "arraybuffer"
    }),
}

const gate = {
    ...me,
    ...user,
    ...auth,
    ...companies
};

export default gate;
