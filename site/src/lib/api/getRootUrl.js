function getRootUrl() {
  // const dev = process.env.NODE_ENV !== 'production';
  // const ROOT_URL = dev ? process.env.URL_APP : process.env.PRODUCTION_URL_APP;

  const ROOT_URL = "http://localhost:8080";
  return ROOT_URL;
}

module.exports = getRootUrl;
