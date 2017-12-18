import {combineReducers} from 'redux';
import userReducer from './user_reducer';
import keycloakReducer from './keycloak_reducer';

const rootReducer = combineReducers({
    users: userReducer,
    keycloak: keycloakReducer
});

export default rootReducer;