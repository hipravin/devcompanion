import logo from './logo.svg';
import './App.css';
import React from "react";
import notify from "./lib/notify";
import Notifier from "./components/Notifier";
import TopNavBar from "./components/TopNavBar/TopNavBar";
import {searchArticlesApiMethod} from "./lib/api/articles";
import {userInfoApiMethod} from "./lib/api/users";
import {sesionInfoApiMethod, sessionInfoApiMethod} from "./lib/api/session";
import ArticleList from "./components/ArticleList/ArticleList";
import Relogin from "./components/Relogin/Relogin";


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            articles: undefined,
            user: undefined,
            reloginVisible: undefined
        };
    }

    componentDidMount() {
        // this.performSearch("");
        this.requestUserInfo();

        this.scheduleUserInfoBounce(60000);
        this.scheduleSessionBounce(20000);
    }

    performSearch(searchString) {
        const user = this.state.user;

        searchArticlesApiMethod(searchString)
            .then(res => this.setState({articles: res, user: user}))
            .catch(err => {
                console.error(err);
                notify("Service temporarily unavailable, please try refreshing a page.");
            });
    }

    requestUserInfo() {
        const articles = this.state.articles;

        userInfoApiMethod()
            .then(res => this.setState({articles: articles, user: res}))
            .catch(err => console.error(err));

    }

    logSessionInfo() {
        sessionInfoApiMethod()
            .then(res => console.log('session info: ' + res))
            .catch(err => {
                console.error('failed session info: ' + err);
                this.setState({
                    articles: undefined,
                    user: undefined,
                    reloginVisible: true
                });
            });
    }

    logUserInfo() {
        userInfoApiMethod()
            .then(res => console.log('user info: ' + res))
            .catch(err => console.error('failed user info: ' + err));
    }

    scheduleSessionBounce(delayMillis) {
        setInterval(() => {
            this.logSessionInfo();
        }, delayMillis);
    }

    scheduleUserInfoBounce(delayMillis) {
        setInterval(() => {
            this.logUserInfo();
        }, delayMillis);
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

        const showRelogin = this.state.reloginVisible === true;

        return (

            <div className="App">
                <Relogin visible={showRelogin}/>
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
