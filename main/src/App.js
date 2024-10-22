import React from 'react';
import Header from './components/Header';
import Window from './components/Window';
import './App.css';

function App() {
  return (
      <div className="App">
        <Header />
        <div className="main-container">
          <Window title="Window 1" />
          <Window title="Window 2" />
        </div>
      </div>
  );
}

export default App;
