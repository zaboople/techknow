'use client'
import React from "react";
import { Link, useNavigate, useSearchParams } from 'react-router-dom';

const MENU_ITEMS = Object.freeze([
    {href: "/" , text: "Home"},
    {href: "/contact" , text: "Contact"},
    {href: "/sign-up" , text: "Sign Up"},
    {href: "/await" , text: "Await"},
]);

function OneBullet() {
    return <span
        className="MyNavBullet">&nbsp;&nbsp;&bull;&nbsp;&nbsp;</span>;
}

export default function Navbar() {
    console.log("Navbar(): Rendering menu... "+window.location.href);
    const pathSplit = window.location.href.split("#");
    const pathToMatch = pathSplit.length==2 ?pathSplit[1] :"/";

    function OneAnchor({item}){
        const isWindow = item.href == pathToMatch;
        const cclass = isWindow ?"NavFocus" :"NavUnfocus";
        if (isWindow)
            document.title="My "+item.text;
        return <Link className={cclass} key={item.href}
            to={item.href}>{item.text}</Link>;
    }
    function Anchors() {
        const justShort = (2 * MENU_ITEMS.length)-1;
        return MENU_ITEMS.map(a => [
                <OneAnchor key={a.href} item={a}/>,
                <OneBullet key={a.href + "*b"} />
            ]).flat().slice(0, justShort);
    }
    return <div className="MyNavBar"><Anchors/></div>;
};
