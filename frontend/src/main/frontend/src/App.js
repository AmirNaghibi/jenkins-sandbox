import React, {Component} from 'react';

import './App.css';
import ReactTable from 'react-table';
import {connect} from 'react-redux';
import {fetchUsers} from './actions';
import _ from 'lodash';



class App extends Component {

    constructor() {
        super();
        this.fetchData = this.fetchData.bind(this);
    }
    
    fetchData(state, instance) {
       this.props.fetchUsers(state.page, state.pageSize);
    }
    
    render() {
        const {page, loading, defaultPageSize} = this.props;

        const columns = [{
            Header: 'Name',
            accessor: 'name' // String-based value accessors!
        }, {
            Header: 'Dept',
            accessor: 'department',
        }];

        const data = page.content;
        return (
            <div className="App">
                <header className="App-header">

                    <h1 className="App-title">Frontend App</h1>
                </header>
                <ReactTable
                    manual
                    data={data}
                    pages={page.totalPages}
                    columns={columns}
                    loading={loading}
                    defaultPageSize={defaultPageSize}
                    onFetchData={this.fetchData}
                />
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        loading: state.users.loading,
        page: state.users.page,
        defaultPageSize: state.users.defaultPageSize
    };
};

export default connect(mapStateToProps, {fetchUsers})(App);
