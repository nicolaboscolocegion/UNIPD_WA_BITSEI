import types from "./authActionTypes";

const INITIAL_STATE = {
  user: null,
  pending: false,
  accessToken: null,
  isLoggedIn: false,
  errors: [
    {
      email: null,
      password: null,
    },
  ],
};

const userReducer = (currentState = INITIAL_STATE, { type, payload }) => {
  switch (type) {
    case types.LOGIN_START:
      return {
        ...currentState,
        pending: true,
      };
    case types.LOGIN_SUCCESS:
      return {
        ...currentState,
        pending: false,
        isLoggedIn: true,
        user: payload.user,
        accessToken: payload.token,
        errors: [
          {
            email: null,
            password: null,
          },
        ],
      };
    case types.LOGIN_FAIL:
      return {
        ...currentState,
        pending: false,
        errors: payload,
      };
    case types.UPDATE_USER:
      return {
        ...currentState,
        user: payload,
      };
    case types.LOGOUT:
      return {
        user: null,
        pending: false,
        accessToken: null,
        isLoggedIn: false,
        errors: [
          {
            email: null,
            password: null,
          },
        ],
      };
    default:
      return currentState;
  }
};

export default userReducer;
