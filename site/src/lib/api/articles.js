import sendRequest from './sendRequest';

const BASE_PATH = '/api/v1/articles';

export const searchArticlesApiMethod = (searchString) =>
    sendRequest(`${BASE_PATH}/search?q=${encodeURIComponent(searchString)}`, {
        method: 'GET',
    });

export const searchArticlesPageApiMethod = (searchString, page) =>
    sendRequest(`${BASE_PATH}/search?q=${encodeURIComponent(searchString)}&page=${encodeURIComponent(page)}`, {
        method: 'GET',
    });