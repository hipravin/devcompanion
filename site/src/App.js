import './App.css';
import React from "react";
import notify from "./lib/notify";
import TopNavBar from "./components/TopNavBar/TopNavBar";
import {searchArticlesApiMethod} from "./lib/api/articles";
import {userInfoApiMethod} from "./lib/api/users";
import {sessionInfoApiMethod} from "./lib/api/session";
import Relogin from "./components/Relogin/Relogin";
import Article from "./components/Article/Article";
import {queryToTerms} from "./lib/highlight";
import Notifier from "./components/Notifier";


class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            articles: undefined,
            user: undefined,
            showRelogin: undefined,
            searchString: undefined
        };
    }

    componentDidMount() {
        if(this.props.queryString) {
            this.performSearch(this.props.queryString);
        }
        this.requestUserInfo();

        this.scheduleUserInfoBounce(60000);
        this.scheduleSessionBounce(20000);
    }

    performSearch(searchString) {
        window.history.replaceState(null, null, "?q=" + searchString)
        if(searchString.trim() === "") {
            this.setState({searchString: undefined});
            return;
        }
        this.setState({searchString: searchString});

        searchArticlesApiMethod(searchString)
            .then(res => this.setState({articles: res}))
            .catch(err => {
                console.error(err);
                notify("Service temporarily unavailable, please refresh a page or try again later.");
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
                <TopNavBar queryString={this.props.queryString} resultArticlesCount={articlesCount} userInfo={userInfo} onSearch={this.handleSearch}/>
                <main className="MainContent">
                    {articlesComponent}
                </main>
                <footer className="Footer">@Copyleft Alex K. 1890-9990 A.D.</footer>
                {/*<CssBaseline />*/}
                <Notifier/>
            </div>
        );
    }

    articlesComponent() {
        if (this.state.searchString === undefined || this.state.articles === undefined) {
            return this.suggestionsComponent();
        } else if (this.state.articles.length === 0) {
            return this.emptyResult();
        } else {
            return this.articleNotEmptyList();
        }
    }

    suggestionsComponent() {
        const sugesstions = [
            "postgres sequence",
            "linux find name"
        ];

        const liItems = sugesstions.map( s => {
            return <li key={s}><a href={"/?q=" + s}>{s}</a></li>
        });

        return (
            <div className="Suggestions">
                <div className="SuggestionsWelcome">Are you looking for...</div>
                {liItems}
            </div>
        );
    }

    articleNotEmptyList() {
        const terms = queryToTerms(this.state.searchString);

        const articles = this.state.articles.map(article => {
                return <Article key={article.id} terms={terms} article={article}/>;
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
