import api from '../api';

export function fetchUsers(page, size) {

    return dispatch => api.mcr1.fetchUsers(page, size).then((resp) => {
        dispatch({
            type: 'users/page',
            payload: {
                loading: false,
                page: resp.data
            }
        });
    });

}
