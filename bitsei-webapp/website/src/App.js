import React, {Suspense, useEffect} from 'react';
import {useSelector, connect} from "react-redux";
import {Switch, Route, Redirect} from "react-router-dom";
import {ToastContainer} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import {userLogin} from "./Store/auth/authThunk";
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from "axios";
import {privateRoutes, routes} from "./routes/routes";

function App() {
    const auth = useSelector((state) => state.auth);
    console.log(auth)
    useEffect(() => {
        if (auth.isLoggedIn) {
            window.localStorage.setItem("Authorization", auth.accessToken);
            axios.defaults.headers.common["Authorization"] = auth.accessToken;
        }
    }, [
        auth.isLoggedIn,
        auth.accessToken,
    ]);

    return (
        <div className="h-full">
            <Suspense fallback={"...loading"}>
                {auth.isLoggedIn ? (
                    <Switch>
                        {privateRoutes.map((route) => (
                            <Route key={route.path} {...route}/>
                        ))}
                        <Redirect to={"/companies"} />
                    </Switch>
                ) : (
                    <Switch>
                        {routes.map((route) => (
                            <Route key={route.path} {...route}/>
                        ))}
                        <Redirect to={"/login"} />
                    </Switch>
                )}
            </Suspense>
            <ToastContainer
                position="top-right"
                autoClose={3000}
                closeButton={null}
                hideProgressBar={true}
                closeOnClick
                pauseOnFocusLoss
                draggable
            />
        </div>
    );
}

export default connect(null, {})(App);
