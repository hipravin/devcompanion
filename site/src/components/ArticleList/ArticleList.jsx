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

        return (
            <div className="ArticleList">
                {articles}
            </div>
        );
    }
}

ArticleList.propTypes = {};

ArticleList.defaultProps = {};

export default ArticleList;
