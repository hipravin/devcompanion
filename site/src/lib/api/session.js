import sendRequest from './sendRequest';

const BASE_PATH = '/api/v1';

export const sessionInfoApiMethod = () =>
    sendRequest(`${BASE_PATH}/session/current`, {
        method: 'GET',
    });
