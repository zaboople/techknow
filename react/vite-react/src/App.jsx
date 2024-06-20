import {
    createHashRouter,
    createRoutesFromElements,
    RouterProvider,
    Route,
} from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages";
import SignUp from "./pages/signup";
import Contact from "./pages/contact";
import Chat from "./pages/chat";
import Wait from "./pages/wait";
import Wait2 from "./pages/wait2";
import VideoPlayer from "./pages/video";
import Cats from "./pages/cats";
import RerenderTest from "./pages/rerender";


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

    // Using a hash router because we don't have SSR. That
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
            {MyRoute("/chat" , <Chat />)}
            {MyRoute("/await"   , <Wait />)}
            {MyRoute("/await2"   , <Wait2 />)}
            {MyRoute("/videos"   , <VideoPlayer />)}
            {MyRoute("/cats"   , <Cats />)}
            {MyRoute("/rerender"   , <RerenderTest />)}
        </>)
    );

    // For this to work correctly, it has to be all by itself.
    // Otherwise, it will *seem* to work, but it will reload
    // the application.
    return (<>
        <div className="atTop">
            <RouterProvider router={router} />
        </div>
        <div className="atBottom">
            &copy; Mine <MyYear/>
        </div>
    </>);
}

export default App;
