import React from "react";
import Axios from "axios";
import { makeStyles } from "@material-ui/core/styles";
import InputLabel from "@material-ui/core/InputLabel";
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import Button from "@material-ui/core/Button";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
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

const useStyles = makeStyles(theme => ({
  button: {
    display: "block",
    marginTop: theme.spacing(2)
  },
  formControl: {
    margin: theme.spacing(1),
    minWidth: 120
  },
  root: {
    "& > *": {
      margin: theme.spacing(1)
    }
  },
  paper: {
    width: "100%",
    overflowX: "auto"
  },
  table: {
    minWidth: 650
  }
}));
var cities = [];
const States = ({ states }) => {
  const classes = useStyles();
  const [selectedState, setSelectedState] = React.useState("");
  const [selectedCity, setSelectedCity] = React.useState("");
  const [content, setContent] = React.useState([]);
  const [open, setOpen] = React.useState(false);
  const [cityOpen, setCityOpen] = React.useState(false);
  const [displayOn, setDisplayOn] = React.useState(false);
  const [buttonEnable, enableButton] = React.useState(false);
  const [initial, setInitial] = React.useState(true);
  const [waiting, setWaiting] = React.useState(true);
  const [gettingCities, setGettingCities] = React.useState(true);

  const handleChange = event => {
    let selection = event.target.value;
    setGettingCities(true);
    setSelectedState(selection);
    setDisplayOn(false);
    setWaiting(true);
    setInitial(false);
    Axios.get(
      `http://localhost:8080/getCities?state=${selection}&mock=true`
    ).then(data => {
      cities = data.data.cities;
      setGettingCities(false);
    });
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const handleCityChange = event => {
    let selection = event.target.value;
    setDisplayOn(false);
    setSelectedCity(selection);
    enableButton(true);
    console.log(selectedState);
    console.log(selection);
  };

  const handleCityClose = () => {
    setCityOpen(false);
  };

  const handleCityOpen = () => {
    setCityOpen(true);
  };

  const handleOnSubmit = e => {
    e.preventDefault();
    setDisplayOn(true);
    setWaiting(true);
    Axios.get(
      `http://localhost:8080/getCrimeRates?state=${selectedState}&city=${selectedCity}&mock=true`
    ).then(data => {
      setContent(data.data);
      setWaiting(false);
    });
  };

  return (
    <div style={{ color: "white" }}>
      <div>
        <h1 align="center">SER 531 - Crime Rate Analysis</h1>
        <h4 align="right">Team - 2(Fall 2019)</h4>
      </div>
      <div>
        <Button
          className={classes.button}
          onClick={handleOpen}
          style={{ color: "white" }}
        >
          Open to select State
        </Button>
        <FormControl className={classes.formControl}>
          <InputLabel
            id="demo-controlled-open-select-label"
            style={{ color: "white" }}
          >
            State
          </InputLabel>
          <Select
            style={{ color: "white" }}
            labelId="demo-controlled-open-select-label"
            id="demo-controlled-open-select"
            open={open}
            onClose={handleClose}
            onOpen={handleOpen}
            value={selectedState}
            onChange={handleChange}
          >
            <MenuItem key={123} value="">
              <em>None</em>
            </MenuItem>
            {states.map((st, i) => (
              <MenuItem key={i} value={st}>
                {st}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </div>
      {initial ? (
        <h2>Select State to continue.</h2>
      ) : gettingCities ? (
        <h3>Please wait. Fetching Cities...</h3>
      ) : (
        <div>
          <Button
            className={classes.button}
            onClick={handleCityOpen}
            style={{ color: "white" }}
          >
            Open to select City
          </Button>
          <FormControl className={classes.formControl}>
            <InputLabel
              id="demo-controlled-open-select-city-label"
              style={{ color: "white" }}
            >
              City
            </InputLabel>
            <Select
              labelId="demo-controlled-open-select-city-label"
              id="demo-controlled-open-select-city"
              open={cityOpen}
              onClose={handleCityClose}
              onOpen={handleCityOpen}
              value={selectedCity}
              onChange={handleCityChange}
              style={{ color: "white" }}
            >
              <MenuItem key={456} value="">
                <em>None</em>
              </MenuItem>
              {cities.map((city, j) => (
                <MenuItem key={j} value={city}>
                  {city}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </div>
      )}
      <div className={classes.root}>
        {buttonEnable ? (
          <Button
            variant="contained"
            color="primary"
            onClick={handleOnSubmit}
            style={{ color: "white" }}
          >
            Submit
          </Button>
        ) : (
          <Button variant="contained" disabled style={{ color: "white" }}>
            Submit
          </Button>
        )}
      </div>
      {displayOn ? (
        waiting ? (
          <FadeIn>
            <div class="d-flex justify-content-center align-items-center">
              <h1>
                Fetching CrimeRates. Usually takes up to 6 mins, Please wait...
              </h1>
              <Lottie options={defaultOptions} height={120} width={120} />
            </div>
          </FadeIn>
        ) : (
          <div>
            <div>
              <h2 align="center">
                State : {content.state}, City : {content.city}, Year : 2016,
                Population : {content.population}
              </h2>
              <h4 align="center">Property Crime Details</h4>
              <Paper className={classes.paper}>
                <Table className={classes.table} aria-label="simple table">
                  <TableHead>
                    <TableRow>
                      <TableCell align="center" style={{ fontWeight: "bold" }}>
                        Burglary Crimes
                      </TableCell>
                      <TableCell align="center" style={{ fontWeight: "bold" }}>
                        Motor Vehicle Crimes
                      </TableCell>
                      <TableCell align="center" style={{ fontWeight: "bold" }}>
                        Arson Crimes
                      </TableCell>
                      <TableCell align="center" style={{ fontWeight: "bold" }}>
                        Larceny Crimes
                      </TableCell>
                      <TableCell align="center" style={{ fontWeight: "bold" }}>
                        Total Property Crimes
                      </TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    <TableRow key={101}>
                      <TableCell align="center">
                        {content.propertyCrimeRates.burglaryCrimeValue}
                      </TableCell>
                      <TableCell align="center">
                        {content.propertyCrimeRates.motorVehicleCrimeValue}
                      </TableCell>
                      <TableCell align="center">
                        {content.propertyCrimeRates.arsonCrimeValue}
                      </TableCell>
                      <TableCell align="center">
                        {content.propertyCrimeRates.larcenyCrimeValue}
                      </TableCell>
                      <TableCell align="center">
                        {content.propertyCrimeRates.propertyCrimeValue}
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </Paper>
            </div>
            <div>
              <h4 align="center">Violent Crime Details</h4>
              <Paper className={classes.paper}>
                <Table className={classes.table} aria-label="simple table">
                  <TableHead>
                    <TableRow>
                      <TableCell style={{ fontWeight: "bold" }} align="center">
                        Aggravated Assault Crimes
                      </TableCell>
                      <TableCell style={{ fontWeight: "bold" }} align="center">
                        Murder Crimes
                      </TableCell>
                      <TableCell style={{ fontWeight: "bold" }} align="center">
                        Rape 1 Crimes
                      </TableCell>
                      <TableCell style={{ fontWeight: "bold" }} align="center">
                        Rape 2 Crimes
                      </TableCell>
                      <TableCell style={{ fontWeight: "bold" }} align="center">
                        Robbery Crimes
                      </TableCell>
                      <TableCell style={{ fontWeight: "bold" }} align="center">
                        Total Violent Crimes
                      </TableCell>
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    <TableRow key={102}>
                      <TableCell align="center">
                        {content.violentCrimeRates.aggravatedAssaultCrimeValue}
                      </TableCell>
                      <TableCell align="center">
                        {content.violentCrimeRates.murderCrimeValue}
                      </TableCell>
                      <TableCell align="center">
                        {content.violentCrimeRates.rape1CrimeValue}
                      </TableCell>
                      <TableCell align="center">
                        {content.violentCrimeRates.rape2CrimeValue}
                      </TableCell>
                      <TableCell align="center">
                        {content.violentCrimeRates.robberyCrimeValue}
                      </TableCell>
                      <TableCell align="center">
                        {content.violentCrimeRates.violentCrimeValue}
                      </TableCell>
                    </TableRow>
                  </TableBody>
                </Table>
              </Paper>
            </div>
          </div>
        )
      ) : (
        <div>
          <h3>Select State and City to enable Submit.</h3>
        </div>
      )}
    </div>
  );
};
export default States;
