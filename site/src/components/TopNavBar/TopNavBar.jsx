import React from 'react';
import './TopNavBar.css';
import {Button, TextField} from "@material-ui/core";
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
            <div className="TopNavBar">
                <img src={logo} alt="Logo" />
                <TextField id="topNavSearchInput"
                           autoFocus
                           onChange={event => this.setSearchString(event.target.value)}
                           onKeyDown={this.keyPress}
                           size="small"
                           variant="outlined"/>

                <Button variant="outlined" size="large" onClick={this.handleSearch}>Find</Button>
                <span className="ResultCount">Shown: {this.props.resultArticlesCount||0} articles</span>
            </div>
        );
    }
}

export default TopNavBar;
