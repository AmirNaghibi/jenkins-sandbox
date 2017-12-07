import axios from 'axios';
import _ from 'lodash';


const apiUrl = window.API_URL;

const adapter = axios.create({
    baseURL: `${apiUrl}/mcr1`,
    headers: {
        Accept: 'application/json'
    }
});


export default {
    mcr1: {
        fetchUsers: (page = 0, size = 10, filter= []) => {
            const filterStr = _.map(filter, (it)=>`${it.field}=${it.term}`);
            return adapter.get(`/users/search?page=${page}&size=${size}&${_.join(filterStr, '&')}`);
        }
    }
};