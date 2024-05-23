'use client'
import React from "react";
import { Link, useNavigate, useSearchParams } from 'react-router-dom';

const Navbar = () => {
    //console.log("Rendering menu... "+window.location.href);
    const [currMenu, setCurrMenu] = React.useState("Home");
    const [searchParams, setSearchParams] = useSearchParams();
    const pathSplit = window.location.href.split("#");
    const pathToMatch = pathSplit.length==2 ?pathSplit[1] :"/";

    function MyAnchor({id, href, text, last}) {
        const isWindow = href == pathToMatch;
        var cclass = isWindow ?"NavFocus" :"NavUnfocus";
        if (isWindow)
            document.title="My "+text;
        var afterPart = (<>
            {last ?<></> :<span className="MyNavBullet">&nbsp;&bull;&nbsp;&nbsp;</span>}
        </>);
        return (<>
            <Link className={cclass} to={href}>{text}</Link> {afterPart}
        </>);
    }

    return (<>
        <div className="MyNavBar">
            <MyAnchor id="NavHome" href="/" text="Home"/>
            <MyAnchor id="NavAbout" href="/about" text="About"/>
            <MyAnchor id="NavContact" href="/contact" text="Contact"/>
            <MyAnchor id="NavBlogs" href="/blogs" text="Blogs"/>
            <MyAnchor id="NavSignup" href="/sign-up" text="Sign Up"/>
            <MyAnchor id="NavWait" href="/await" text="Await" last={true}/>
        </div>
    </>);
};

export default Navbar;