import React from 'react';
import './Article.css';
import CodeBlock from "../CodeBlock/CodeBlock";
import Highlighter from "react-highlight-words";

class Article extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
    }

    render() {
        const article = this.props.article;
        const terms = this.props.terms;
        const codeBlockComponents = article.codeBlocks.map(cb => {
                return <CodeBlock key={cb.title} terms={terms} codeBlock={cb}/>;
            }
        );

        const links = article.links.map(l => {
                return <a key={l.url} href={l.url}>{l.title}</a>;
            }
        );

        return (
            <div className="Article">
                <div className="ArticleTitle">
                    <Highlighter
                        highlightClassName="TermHighlighted"
                        searchWords={terms}
                        autoEscape={true}
                        textToHighlight={article.title}
                    />
                </div>
                <div className="ArticleLinks">{links}</div>
                <div className="ArticleDescription">
                    <Highlighter
                        highlightClassName="TermHighlighted"
                        searchWords={terms}
                        autoEscape={true}
                        textToHighlight={article.description}
                    />
                </div>

                <div className="ArticleCodeBlocks">
                    {codeBlockComponents}
                </div>
                <hr/>
            </div>
        );
    }
}
//TODO: research this maybe
Article.propTypes = {};

Article.defaultProps = {};

export default Article;
