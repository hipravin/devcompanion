function getRootUrl() {
  const dev = process.env.NODE_ENV !== 'production';

  const DEV_ROOT_URL = "http://localhost:8000";
  const DOCKER_URL = "";//relative path from same context, because nginx handles reverse proxy
  const ROOT_URL = dev ?  DEV_ROOT_URL : DOCKER_URL;
  return ROOT_URL;
}

module.exports = getRootUrl;
