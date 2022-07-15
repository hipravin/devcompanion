import logo from './logo.svg';
import './App.css';
import React from "react";
import TopNavBar from "./components/TopNavBar/TopNavBar";
import {searchArticlesApiMethod} from "./lib/api/articles";
import {userInfoApiMethod} from "./lib/api/users";
import ArticleList from "./components/ArticleList/ArticleList";


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            articles: undefined,
            user: undefined
        };
    }

    componentDidMount() {
        // this.performSearch("");
        this.requestUserInfo();
    }

    performSearch(searchString) {
        const user = this.state.user;

        searchArticlesApiMethod(searchString)
            .then(res => this.setState({articles: res, user: user}))
            .catch(err => console.error(err));

    }

    requestUserInfo() {
        const articles = this.state.articles;

        userInfoApiMethod()
            .then(res => this.setState({articles: articles, user: res}))
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

        const articlesCount = articles ? articles.length : 0;

        const userInfo = this.state.user ? this.state.user : {user_name: ""};

        return (
            <div className="App">
                <TopNavBar resultArticlesCount={articlesCount} userInfo={userInfo} onSearch={this.handleSearch}/>
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
