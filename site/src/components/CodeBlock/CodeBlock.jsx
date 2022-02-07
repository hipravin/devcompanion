import React from 'react';
import PropTypes from 'prop-types';
import './CodeBlock.css';


class CodeBlock extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
    }

    render() {

        const codeBlock = this.props.codeBlock;

        return (
            <div className="CodeBlock">
                <div className="CodeBlockTitle">
                    {codeBlock.title}
                </div>
                <div className="CodeBlockCode">
                    {codeBlock.code}
                </div>
            </div>
        );
    }
}

CodeBlock.propTypes = {};

CodeBlock.defaultProps = {};

export default CodeBlock;
