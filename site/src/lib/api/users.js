import sendRequest from './sendRequest';
import Cookies from 'js-cookie';

const BASE_PATH = '/api/v1/users';
const CSRF_COOKE_NAME = 'XSRF-TOKEN';

export const userInfoApiMethod = () => {

    const csrfTokenHeaderValue = Cookies.get(CSRF_COOKE_NAME) || '';
    return sendRequest(`${BASE_PATH}/me/info`, {
        method: 'POST',
        headers: {
            'X-XSRF-TOKEN': csrfTokenHeaderValue
        }
    });
}