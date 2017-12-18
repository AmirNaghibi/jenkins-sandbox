import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import {Provider} from 'react-redux';
import {applyMiddleware, createStore} from 'redux';
import reducers from './reducers';
import thunk from 'redux-thunk';
import 'react-table/react-table.css';
import 'react-widgets/dist/css/react-widgets.css';
import Keycloak from "keycloak-js";
import axios from "axios";

const createStoreWithMiddleware = applyMiddleware(thunk)(createStore);
export const store = createStoreWithMiddleware(reducers, window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__());

const app = (
    <Provider store={store}>
        <App/>
    </Provider>
);


const kc = Keycloak('./keycloak.json');
kc.init({onLoad: 'check-sso'}).success(authenticated => {
    if (authenticated) {
        store.getState().keycloak = kc;

        setInterval(() => {
            kc.updateToken(10).error(() => kc.logout());
        }, 10000);
        axios.defaults.headers.common['Authorization'] = 'Bearer ' + kc.token;
        ReactDOM.render(app, document.getElementById("root"));

    } else {
        // show possibly other page here...
        kc.login();
    }
});

/*
axios.interceptors.request.use(config => {
    config.headers = {...config.headers, ...{
            'Content-Type': 'application/json',
            Accept: 'application/json',
            Authorization: 'Bearer ' + kc.token
        }};
    return config;
});

*/

