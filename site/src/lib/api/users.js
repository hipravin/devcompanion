import sendRequest from './sendRequest';

const BASE_PATH = '/api/v1/users';

export const userInfoApiMethod = () =>
    sendRequest(`${BASE_PATH}/me/info`, {
        method: 'GET',
    });