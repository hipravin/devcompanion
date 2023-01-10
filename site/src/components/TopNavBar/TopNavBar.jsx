import React from 'react';
import './TopNavBar.css';
import {Button, IconButton, TextField} from "@material-ui/core";
import SearchIcon from '@mui/icons-material/Search';
import logo from '../../img/icons8-java-black.svg'

class TopNavBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchString: ""
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
        if(e.keyCode === 13){ //enter pressed
            this.handleSearch();
        }
    }

    render() {

        return (
            <header className="TopNavBar">
                <img src={logo} alt="Logo" />
                <div className="SearchTextField">
                    <TextField id="topNavSearchInput"
                               autoFocus
                               onChange={event => this.setSearchString(event.target.value)}
                               onKeyDown={this.keyPress}
                               size="small"
                               fullWidth
                               variant="outlined"/>
                </div>

                <IconButton aria-label="search" size="medium" onClick={this.handleSearch}>
                    <SearchIcon fontSize="inherit" />
                </IconButton>

                {/*<Button variant="outlined" size="large" onClick={this.handleSearch}>Find</Button>*/}
                {/*<span className="ResultCount">Shown: {this.props.resultArticlesCount||0} articles</span>*/}
                <span className="UserInfo">Logged in as {this.props.userInfo.user_name}</span>
            </header>
        );
    }
}

export default TopNavBar;
