import React, {Suspense, useEffect} from 'react';
import {useSelector, connect} from "react-redux";
import {Switch, Route, Redirect} from "react-router-dom";
import {ToastContainer} from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import 'bootstrap/dist/css/bootstrap.min.css';
import './styles.css';
import axios from "axios";
import {privateRoutes, routes} from "./routes/routes";
import Navbar from "./Components/Navbar/Navbar";
import SideBar from "./Components/SideBar/SideBar";
import Footer from "./Components/Footer/Footer";

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
        <div className="bg-primary">
            <Suspense fallback={"...loading"}>
                {auth.isLoggedIn ? (
                    <>
                        <Navbar/>
                        <div id="layoutSidenav">
                            <SideBar/>
                            <div id="layoutSidenav_content">
                                <main>
                                    <Switch>
                                        {privateRoutes.map((route) => (
                                            <Route key={route.path} {...route}/>
                                        ))}
                                        <Redirect to={"/companies"}/>
                                    </Switch>
                                </main>
                                <Footer/>
                            </div>
                        </div>
                    </>
                ) : (
                    <Switch>
                        {routes.map((route) => (
                            <Route key={route.path} {...route}/>
                        ))}
                        <Redirect to={"/login"}/>
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
