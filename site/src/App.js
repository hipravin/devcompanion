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
            articles: []
        };
    }

    componentDidMount() {
        this.performSearch("");
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

        return (
            <div className="App">
                <TopNavBar onSearch={this.handleSearch}/>
                <ArticleList articles={articles}/>
            </div>
        );
    }
}

export default App;
