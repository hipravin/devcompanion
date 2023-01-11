import React from 'react';
import PropTypes from 'prop-types';
import './CodeBlock.css';
import Highlighter from "react-highlight-words";

class CodeBlock extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
    }

    render() {

        const codeBlock = this.props.codeBlock;
        const terms = this.props.terms;

        return (
            <div className="CodeBlock">
                <div className="CodeBlockTitle">
                    <Highlighter
                        highlightClassName="TermHighlighted"
                        searchWords={terms}
                        autoEscape={true}
                        textToHighlight={codeBlock.title}
                    />
                </div>
                <div className="CodeBlockCode">
                    <Highlighter
                        highlightClassName="TermHighlighted"
                        searchWords={terms}
                        autoEscape={true}
                        textToHighlight={codeBlock.code}
                    />
                </div>
            </div>
        );
    }
}

CodeBlock.propTypes = {};

CodeBlock.defaultProps = {};

export default CodeBlock;
