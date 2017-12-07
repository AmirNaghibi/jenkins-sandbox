import api from '../api';

export function fetchUsers(page=0, size=10, filter= []) {

    return dispatch => api.mcr1.fetchUsers(page, size, filter).then((resp) => {
        dispatch({
            type: 'users/page',
            payload: {
                loading: false,
                page: resp.data
            }
        });
    });

}

export function setUsersFilter(filter= []) {
    return {
        type: 'users/filter',
        payload: filter
    };
}