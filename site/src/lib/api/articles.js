import sendRequest from './sendRequest';

const BASE_PATH = '/api/v1/articles';

export const searchArticlesApiMethod = (searchString) =>
    sendRequest(`${BASE_PATH}/search?q=${searchString}`, {
        method: 'GET',
    });