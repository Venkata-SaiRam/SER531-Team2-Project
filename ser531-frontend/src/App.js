import React, { Component } from "react";
import States from "./states.js";
import Axios from "axios";
import FadeIn from "react-fade-in";
import Lottie from "react-lottie";
import * as legoData from "./legoloading.json";

const defaultOptions = {
  loop: true,
  autoplay: true,
  animationData: legoData.default,
  rendererSettings: {
    preserveAspectRatio: "xMidYMid slice"
  }
};

class App extends Component {
  state = {
    states: [],
    gotResults: false
  };

  componentDidMount() {
    Axios.get("http://localhost:8080/getStates?mock=true")
      .then(data => {
        this.setState({ states: data.data.states, gotResults: true });
      })
      .catch(console.log);
  }

  render() {
    return (
      <div style={{ color: "white" }}>
        {console.log(`Results : ${this.state.gotResults}`)}
        {this.state.gotResults ? (
          <States states={this.state.states} />
        ) : (
          <FadeIn>
            <div>
              <h1>Fetching States. Please wait...</h1>
              <Lottie options={defaultOptions} height={120} width={120} />
            </div>
          </FadeIn>
        )}
      </div>
    );
  }
}
export default App;
