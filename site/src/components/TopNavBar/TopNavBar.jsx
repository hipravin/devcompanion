import React from 'react';
import PropTypes from 'prop-types';
import './TopNavBar.css';
import {Button, TextField} from "@material-ui/core";


class TopNavBar extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
    }

    render() {
        return (
            <div className="TopNavBar">
                LOGO
                <TextField id="topNavSearchInput" label="Outlined" variant="outlined" />
                <Button variant="outlined">Find</Button>

            </div>
        );
    }
}

export default TopNavBar;
