import "./App.css";
import ChampionPage from "./Champion/ChampionPage";
import ErrorPage from "./Error/ErrorPage";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
function App() {
  return (
    <Router>
      <Routes>
        <Route exact path="/champion/payment/:id" element={<ChampionPage />} />
        <Route path="/*" element={<ErrorPage />} />
      </Routes>
    </Router>
  );
}
export default App;
