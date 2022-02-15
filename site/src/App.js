import logo from './logo.svg';
import './App.css';
import React from "react";
import TopNavBar from "./components/TopNavBar/TopNavBar";
import {searchArticlesApiMethod} from "./lib/api/articles";
import ArticleList from "./components/ArticleList/ArticleList";


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            articles: undefined
        };
    }

    componentDidMount() {
        // this.performSearch("");
    }

    performSearch(searchString) {
        searchArticlesApiMethod(searchString)
            .then(res => this.setState({articles: res}))
            .catch(err => console.error(err));

    }

    handleSearch = (searchString) => {
        this.performSearch(searchString);
    }

    render() {
        const articles = this.state.articles;

        const resultView = (articles === undefined)
            ? this.beforeSearchArticlesLlist()
            : <ArticleList articles={articles}/>;

        return (
            <div className="App">
                <TopNavBar onSearch={this.handleSearch}/>
                {resultView}
            </div>
        );
    }

    beforeSearchArticlesLlist() {
        return (
            <div className="BeforeSearch">Have a good copy-paste!</div>
        );
    }
}

export default App;
