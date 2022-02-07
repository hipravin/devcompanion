import React from 'react';
import './Article.css';
import CodeBlock from "../CodeBlock/CodeBlock";

class Article extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
    }

    render() {
        const article = this.props.article;
        const codeBlockComponents = article.codeBlocks.map(cb => {
                return <CodeBlock key={cb.title} codeBlock={cb}/>;
            }
        );

        return (
            <div className="Article">
                <div className="ArticleTitle">{article.title}</div>
                <div className="ArticleDescription">{article.description}</div>
                <div className="ArticleCodeBlocks">
                    {codeBlockComponents}
                </div>
            </div>
        );
    }
}

Article.propTypes = {};

Article.defaultProps = {};

export default Article;
