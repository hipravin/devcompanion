import React from 'react';
import './TopNavBar.css';
import {Button, TextField} from "@material-ui/core";


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
                LOGO
                <TextField id="topNavSearchInput"
                           onChange={event => this.setSearchString(event.target.value)}
                           onKeyDown={this.keyPress}
                           label="Outlined" variant="outlined"/>
                <Button variant="outlined" onClick={this.handleSearch}>Find</Button>

            </div>
        );
    }
}

export default TopNavBar;
