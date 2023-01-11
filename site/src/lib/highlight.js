function queryToTerms(queryString) {
    return !queryString
        ? []
        : queryString.trim().replace(/\s+/g, ' ').split(" ");
}

module.exports = {queryToTerms};
