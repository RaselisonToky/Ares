import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
${IMPORTS}

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
${ROUTES}
                </Routes>
            </div>
        </Router>
    );
}

export default App;
