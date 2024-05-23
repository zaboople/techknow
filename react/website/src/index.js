import React, { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App";

// React renders twice when in Strict mode in development
// to verify no side effects...?
createRoot(document.getElementById("root")).render(
    <StrictMode>
        <App />
    </StrictMode>
);

/*
createRoot(document.getElementById("root")).render(
    <App />
);
*/
