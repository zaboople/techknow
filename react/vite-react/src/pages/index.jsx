import React from "react";
import reactLogo from '../assets/react.svg'
import viteLogo from '/vite.svg'

import "./styles.css";


export default function Home(){
    return <>
        <div className="subbody">
            <div>
                <img className="VerticalCenter" src="/adhesive-strip.svg" height="50px"/>
                <span className="VerticalCenter"><b>Made with Vite + React</b></span>
                <img className="VerticalCenter" src="/adhesive-strip.svg" height="50px"/>
            </div>
            <p>
                <a href="https://vitejs.dev" target="_blank">
                    <img src={viteLogo} className="logo" alt="Vite logo" />
                </a>
                <a href="https://react.dev/learn" target="_blank">
                    <img src={reactLogo} className="logo react" alt="React logo" />
                </a>
            </p>
            <p>
                Click on the Vite and React logos to learn more
            </p>
            <p>
                Also visit <a target="_blank" href="https://github.com/zaboople">my github site</a>
            </p>
        </div>
    </>;
};

