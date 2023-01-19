import './App.css';
import packageJson from '../package.json'; //TODO: don't expose whole package.json to users! So far it's up to webpack config.
import React from "react";
import notify from "./lib/notify";
import TopNavBar from "./components/TopNavBar/TopNavBar";
import {searchArticlesApiMethod, searchArticlesPageApiMethod} from "./lib/api/articles";
import {userInfoApiMethod} from "./lib/api/users";
import {sessionInfoApiMethod} from "./lib/api/session";
import Relogin from "./components/Relogin/Relogin";
import Article from "./components/Article/Article";
import {queryToTerms} from "./lib/highlight";
import Notifier from "./components/Notifier";
import {ThreeDots} from "react-loader-spinner";

class App extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            articles: undefined,
            articlesPage: undefined,
            user: undefined,
            showRelogin: undefined,
            searchPage: undefined,
            searchString: undefined,
            showDotsLoader: false
        };
    }

    componentDidMount() {
        if (this.props.queryString) {
            this.performSearch(this.props.queryString, this.props.page);
        }
        this.requestUserInfo();

        this.scheduleUserInfoBounce(60000);
        this.scheduleSessionBounce(20000);
    }

    performSearch(searchString, page) {
        if (searchString.trim() === "") {
            this.setState({searchString: undefined});
            return;
        }
        this.setState({searchString: searchString, searchPage: page, showDotsLoader: true});

        const searchRequest = page ? searchArticlesPageApiMethod(searchString, page) : searchArticlesApiMethod(searchString);

        searchRequest
            .then(res => this.setState({articles: res.content, articlesPage: res}))
            .catch(err => {
                console.error(err);
                notify("Service temporarily unavailable, please refresh a page or try again later.");
            })
            .finally(res => {
                this.setState({showDotsLoader: false});
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
        this.setState({searchPage: undefined});
        window.history.replaceState(null, null, "?q=" + encodeURIComponent(searchString));
        this.performSearch(searchString, undefined);
    }

    render() {
        const articles = this.state.articles;

        const articlesComponent = this.articlesComponent();
        const showPages = this.state.articlesPage && (this.state.articlesPage.totalPages > 1);
        const pagesComponent = showPages ? this.pagesComponent() : undefined;
        const articlesCount = articles ? articles.length : 0;
        const userInfo = this.state.user ? this.state.user : {user_name: ""};
        const showRelogin = this.state.showRelogin === true;
        const appVersion = packageJson.version;

        return (
            <div className="App">
                {showRelogin && <Relogin/>}
                <TopNavBar queryString={this.props.queryString} resultArticlesCount={articlesCount} userInfo={userInfo}
                           onSearch={this.handleSearch}/>
                <main className="MainContent">
                    {articlesComponent}
                    {showPages && pagesComponent}
                    <div className="ThreeDotSpinner">
                        <ThreeDots
                            height="70"
                            width="70"
                            radius="9"
                            color="grey"
                            ariaLabel="three-dots-loading"
                            wrapperStyle={{}}
                            wrapperClassName=""
                            visible={this.state.showDotsLoader}/>
                    </div>
                </main>
                <footer className="Footer">v{appVersion} @Copyleft Alex K. 1890-9990 A.D.</footer>
                {/*<CssBaseline />*/}
                <Notifier/>
            </div>
        );
    }

    pagesComponent() {
        const totalPages = this.state.articlesPage.totalPages;
        const thisPageNumber = this.state.articlesPage.pageNumber;

        const pages = [];

        for(let i = 0; i < totalPages && i < 50; i++) {
            pages.push({page: i, isCurrentPage: i === thisPageNumber});
        }

        const linksItems = pages.map(p => {
            const href = "/?q=" + encodeURIComponent(this.state.searchString) + "&page=" + encodeURIComponent(p.page);
            return <a key={p.page} href={href} className={p.isCurrentPage ? "ThisPage" : "NotThisPage"}>{p.page}</a>
        });

        return (
            <div className="Pages">
                {linksItems}
            </div>
        );
    }

    articlesComponent() {
        if(this.state.showDotsLoader && this.state.articles === undefined) {
            return this.blankIntermediate();
        } else if (this.state.articles === undefined) {
            return this.suggestionsComponent();
        } else if (this.state.articles && this.state.articles.length === 0) {
            return this.emptyResult();
        } else {
            return this.articleNotEmptyList();
        }
    }

    suggestionsComponent() {
        const suggestions = [
            "java factory method pattern",
            "spring load file classpath",
            "jackson javascript date",
            "maven ban duplicated dependencies",
            "maven versions set",
            "postgres sequence",
            "linux find name",
            "apt get search install",
            "docker spring layer",
            "hibernate log show sql",
            "postgre repeatable read"
        ];

        const liItems = suggestions.map(s => {
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

    blankIntermediate() {
        return (
            <div className="EmptyResult"></div>
        );
    }
}

export default App;
