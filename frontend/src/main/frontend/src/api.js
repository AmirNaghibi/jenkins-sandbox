import axios from 'axios';


const apiUrl = window.API_URL;

const adapter = axios.create({
    baseURL: `${apiUrl}/mcr1`,
    headers: {
        Accept: 'application/json'
    }
});


export default {
    mcr1: {
        fetchUsers: (page = 0, size = 10) => adapter.get(`/users/search?page=${page}&size=${size}`)
    }
};