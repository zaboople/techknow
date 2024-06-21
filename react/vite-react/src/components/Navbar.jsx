'use client'
import {Link} from 'react-router-dom';
import PropTypes from 'prop-types';

const MENU_ITEMS = Object.freeze([
    {href: "/" , text: "Home"},
    {href: "/contact" , text: "Contact"},
    {href: "/chat" , text: "Hot Chat"},
    {href: "/videos" , text: "Videos"},
    {href: "/cats" , text: "Cats"},
    {href: "/sign-up" , text: "Sign Up"},
    {href: "/await" , text: "Promises"},
    {href: "/await2" , text: "Promises II"},
    {href: "/rerender" , text: "Dumb Re-rendering"},
]);

function OneBullet() {
    return <span
        className="MyNavBullet">&nbsp;&nbsp;&bull;&nbsp;&nbsp;</span>;
}

OneAnchor.propTypes = {
    // Or else linter screams:
    item: PropTypes.object,
    isCurrent: PropTypes.bool.isRequired
};

function OneAnchor({item, isCurrent}){
    const cclass = isCurrent ?"NavFocus" :"NavUnfocus";
    if (isCurrent)
        document.title="My "+item.text;
    return <Link className={cclass} key={item.href}
        to={item.href}>{item.text}</Link>;
}

export default function Navbar() {
    console.log("Navbar(): Rendering menu... "+window.location.href);
    const pathSplit = window.location.href.split("#");
    const pathToMatch = pathSplit.length==2 ?pathSplit[1] :"/";

    function Anchors() {
        const justShort = (2 * MENU_ITEMS.length)-1;
        return MENU_ITEMS.map(a => [
                <OneAnchor key={a.href} item={a} isCurrent={a.href==pathToMatch}/>,
                <OneBullet key={a.href + "*b"} />
            ]).flat().slice(0, justShort);
    }
    return <div className="MyNavBar"><Anchors/></div>;
}
