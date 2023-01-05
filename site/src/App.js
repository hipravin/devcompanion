import logo from './logo.svg';
import './App.css';
import React from "react";
import notify from "./lib/notify";
import Notifier from "./components/Notifier";
import TopNavBar from "./components/TopNavBar/TopNavBar";
import {searchArticlesApiMethod} from "./lib/api/articles";
import {userInfoApiMethod} from "./lib/api/users";
import {sessionInfoApiMethod} from "./lib/api/session";
import Relogin from "./components/Relogin/Relogin";
import Article from "./components/Article/Article";


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            articles: undefined,
            user: undefined,
            showRelogin: undefined
        };
    }

    componentDidMount() {
        // this.performSearch("");
        this.requestUserInfo();

        this.scheduleUserInfoBounce(60000);
        this.scheduleSessionBounce(20000);
    }

    performSearch(searchString) {
        searchArticlesApiMethod(searchString)
            .then(res => this.setState({articles: res}))
            .catch(err => {
                console.error(err);
                notify("Service temporarily unavailable, please try refreshing a page.");
            });
    }

    requestUserInfo() {
        userInfoApiMethod()
            .then(res => this.setState({user: res}))
            .catch(err => console.error(err));
    }

    logSessionInfo() {
        sessionInfoApiMethod()
            .then(res => console.log('session info: ' + res))
            .catch(err => {
                console.error('failed session info: ' + err);
                this.setState({//old state is merged with new one, need to explicitely clear additional keys if intended
                    // articles: undefined,
                    // user: undefined,
                    showRelogin: true
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

        const articlesComponent = this.articlesComponent();

        const articlesCount = articles ? articles.length : 0;

        const userInfo = this.state.user ? this.state.user : {user_name: ""};

        const showRelogin = this.state.showRelogin === true;

        return (
            <div className="App">
                {showRelogin && <Relogin/>}
                <TopNavBar resultArticlesCount={articlesCount} userInfo={userInfo} onSearch={this.handleSearch}/>
                {articlesComponent}
            </div>
        );
    }

    articlesComponent() {
        if (this.state.articles === undefined) {
            return this.beforeSearchArticlesList();
        } else if (this.state.articles.length === 0) {
            return this.emptyResult();
        } else {
            return this.articleNotEmptyList();
        }
    }

    beforeSearchArticlesList() {
        return (
            <div className="BeforeSearch">E.g. "kubectl get pod"</div>
        );
    }

    articleNotEmptyList() {
        const articles = this.state.articles.map(article => {
                return <Article key={article.id} article={article}/>;
            }
        );

        return (
            <div className="ArticleList">{articles}</div>
        );
    }

    emptyResult() {
        return (
            <div className="EmptyResult">No results</div>
        );
    }
}

export default App;
