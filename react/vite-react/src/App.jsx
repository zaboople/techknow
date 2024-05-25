import React from "react";
import {
    createBrowserRouter,
    createHashRouter,
    createRoutesFromElements,
    RouterProvider,
    Route,
} from "react-router-dom";
import Home from "./pages";
import SignUp from "./pages/signup";
import Contact from "./pages/contact";
import Wait from "./pages/wait";
import Navbar from "./components/Navbar";


function MyYear() {
    return new Date(Date.now()).getFullYear();
}
function MyRoute(path, elem) {
    const myel = <><Navbar/>{elem}</>
    return path=="/"
        ?<Route exact path={path}  element={myel} />
        :<Route path={path}  element={myel} />;
}
function App() {
    console.log("App()...")

    // Using a hash router because we don't have next.js. That
    // seems to mean that everything starts with "/#/" but it's
    // hidden from the programming logic, so we write stuff as if
    // it begins with "/".
    // I can't use MyRoute as a component because stupid thing gets
    // mad if it's not an actual Route instance.
    const router = createHashRouter(
        createRoutesFromElements(<>
            {MyRoute("/"  , <Home />)}
            {MyRoute("/contact" , <Contact />)}
            {MyRoute("/sign-up" , <SignUp />)}
            {MyRoute("/await"   , <Wait />)}
        </>)
    );

    // For this to work correctly, it has to be all by itself.
    // Otherwise, it will *seem* to work, but it will reload
    // the application.
    return (<>
        <RouterProvider router={router} />
        <div className="atBottom">
            &copy; Mine <MyYear/>
        </div>
    </>);
}

export default App;
