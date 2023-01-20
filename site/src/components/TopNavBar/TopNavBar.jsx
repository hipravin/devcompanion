import React from 'react';
import './TopNavBar.css';
import SearchIcon from '@mui/icons-material/Search';
import logo from '../../img/icons8-java-black.svg'
import {IconButton, TextField} from "@mui/material";
import PersonRounded from '@mui/icons-material/PersonRounded';

class TopNavBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchString: this.props.queryString
        }
    }

    componentDidMount() {
    }

    setSearchString = (ss) => {
        this.setState({searchString: ss})
    }

    handleSearch = () => {
        this.props.onSearch(this.state.searchString);
    }

    keyPress = (e) => {
        if (e.keyCode === 13) { //enter pressed
            this.handleSearch();
        }
    }

    render() {
        return (
            <header className="TopNavBar">
                <a href="/">
                    <img src={logo} alt="Logo"/>
                </a>
                <div className="SearchTextField">
                    <TextField id="topNavSearchInput"
                               defaultValue={this.state.searchString}
                               autoFocus
                               onChange={event => this.setSearchString(event.target.value)}
                               onKeyDown={this.keyPress}
                               size="small"
                               fullWidth
                               variant="outlined"/>
                </div>

                <IconButton aria-label="search" size="medium" onClick={this.handleSearch}>
                    <SearchIcon fontSize="inherit"/>
                </IconButton>

                <span className="UserInfo">
                    <PersonRounded fontSize="large"/>
                    <span className="UserName">{this.props.userInfo.user_name}</span>
                </span>
            </header>
        );
    }
}

export default TopNavBar;
