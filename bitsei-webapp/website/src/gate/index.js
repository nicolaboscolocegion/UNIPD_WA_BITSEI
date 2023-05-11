import api from './config';

const auth = {
  login: (data) =>
    api.post('/rest/user/login', data),
  forgetPass: (data)=>
    api.post('/auth/user/fpwmail', data),
  changePassword: (token, data) => 
    api.post(`/auth/user/reset/${token}`, data)
};

const user = {
  updateProfile:(data) => api.post('/api/v1/user/company', data, {
    headers: {
      // prepare for file upload
      'Content-Type': 'multipart/form-data'
    },
  }),
}

const me = {
  userInfo: () => api.get("/api/v1/user/me"),
}


const gate = {
  ...me,
  ...user,
  ...auth,
};

export default gate;
