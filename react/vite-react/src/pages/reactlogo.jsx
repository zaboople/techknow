import { useState } from 'react'
import reactLogo from '../assets/react.svg'
import viteLogo from '/vite.svg'
import Navbar from "../components/Navbar";
import "./styles.css";

function Buttoon() {
    const [count, setCount] = useState(0);
    return <button onClick={() => setCount((count) => count + 1)}>
        count is {count}
    </button>;
}
export default function LogoTest() {

  return (
        <>
        <Navbar/>
        <div className="subbody">
            <div>
                <img className="VerticalCenter" src="/adhesive-strip.svg" height="50px"/>
                <span className="VerticalCenter"><b>Vite + React</b></span>
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
            <div className="card">
                <Buttoon/>
            </div>
        </div>
    </>
  )
}


