import React from "react";
import {
    createBrowserRouter,
    createHashRouter,
    createRoutesFromElements,
    RouterProvider,
    Route,
} from "react-router-dom";
import Home from "./pages";
import About from "./pages/about";
import Blogs from "./pages/blogs";
import SignUp from "./pages/signup";
import Contact from "./pages/contact";

function App() {
    console.log("App()...")

    // Using a hash router because we don't have next.js. That
    // seems to mean that everything starts with "/#/" but it's
    // hidden from the programming logic, so we write stuff as if
    // it begins with "/".
    const router = createHashRouter(
        createRoutesFromElements(<>
            <Route exact path="/"  element={<Home />} />
            <Route path="/about"   element={<About/>} />
            <Route path="/contact" element={<Contact />}/>
            <Route path="/blogs"   element={<Blogs />} />
            <Route path="/sign-up" element={<SignUp />}/>
        </>)
    );

    // For this to work correctly, it has to be all by itself.
    // Otherwise, it will *seem* to work, but it will reload
    // the application.
    return (<>
        <RouterProvider router={router} />
        <p>
        &copy; Mine
        </p>
    </>);
}

export default App;