import React from 'react';
import PropTypes from 'prop-types';
import './ArticleList.css';
import Article from "../Article/Article";

class ArticleList extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
    }

    render() {
        const articles = this.props.articles.map(article => {
                return <Article key={article.id} article={article}/>;
            }
        );

        if(articles.length > 0) {
            return (
                <div className="ArticleList">
                    {articles}
                </div>
            );
        } else {
            return this.emptyResult();
        }
    }

    emptyResult() {
        return (
            <div className="EmptyResult">No results</div>
        );
    }
}

ArticleList.propTypes = {};

ArticleList.defaultProps = {};

export default ArticleList;
