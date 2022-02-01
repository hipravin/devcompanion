import logo from './logo.svg';
import './App.css';
import React from "react";
import TopNavBar from "./components/TopNavBar/TopNavBar";


class App extends React.Component{
  constructor(props) {
    super(props);
    this.state = {

    };
  }

  componentDidMount() {
  }

  render() {
    return (
        <div className="App">
            <TopNavBar/>

          <div className="maindiv">

          </div>
        </div>
    );
  }

}

export default App;
