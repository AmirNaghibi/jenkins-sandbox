const initialState = {
    loading: false,
    page: {
        content: [],
        size: 10,
        first: true,
        last: true
    },
    filter: []
};

export default function (state = initialState, action) {
    switch (action.type) {
        case 'users/page':
            return {
                ...state,
                loading: false,
                page: action.payload.page
            };
        case 'users/filter':
            return {
                ...state,
                filter: action.payload || []
            };
    }
    return state;
};