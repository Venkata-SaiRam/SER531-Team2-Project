import React, { Component } from "react";
import States from "./states.js";
import Axios from "axios";
class App extends Component {
  state = {
    states: []
  };

  componentDidMount() {
    Axios.get("http://localhost:8080/getStates?mock=true")
      .then(data => {
        this.setState({ states: data.data.states });
      })
      .catch(console.log);
  }
  render() {
    return <States states={this.state.states} />;
  }
}
export default App;
