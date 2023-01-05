import React from 'react';
import './Relogin.css';

class Relogin extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
    }

    render() {
        return (
            <div className="ontop-relogin">
                <div className="ontop-holder">
                    <div className="ontop-content">
                        <p>
                            <h3>
                                <span>Your session is expired, please reload page</span>
                            </h3>
                            <h4>
                                <a href="/">Reload</a>
                            </h4>
                        </p>
                    </div>
                </div>
            </div>
        );
    }

}

Relogin.propTypes = {};

Relogin.defaultProps = {};

export default Relogin;
