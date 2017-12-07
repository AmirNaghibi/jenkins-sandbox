import React, {Component} from 'react';

import './App.css';
import ReactTable from 'react-table';
import {Col, Grid, Row} from 'react-bootstrap';
import {Multiselect} from 'react-widgets';
import {connect} from 'react-redux';
import {fetchUsers, setUsersFilter} from './actions';
import _ from 'lodash';

class App extends Component {

    constructor() {
        super();
        this.fetchData = this.fetchData.bind(this);
        this.setFilter = this.setFilter.bind(this);

        this.state = {open: false};
    }

    setFilter(filter = []) {
        this.props.setUsersFilter(filter);
        this.props.fetchUsers(0, this.props.pageSize, this.props.filter);
    }

    fetchData(state, instance) {
        this.props.fetchUsers(state.page, state.pageSize, this.props.filter);
    }


    render() {
        const {page, loading, defaultPageSize, filter} = this.props;

        const columns = [{
            Header: 'Name',
            accessor: 'name' // String-based value accessors!
        }, {
            Header: 'Dept',
            accessor: 'department',
        }];

        const data = page.content;

        let ListItem = ({item}) => (
            <span>
                Search&nbsp;<strong>{item.field}:</strong>{item.term}
              </span>
        );
        let TagItem = ({item}) => (
            <span>
                <strong>{item.field}:</strong>{item.term}
              </span>
        );
        const filterData = [
            {"field": "name", "term": ""},
            {"field": "department", "term": ""}
        ];
        //const values = [];
        return (
            <Grid>
                <Row>
                    <Col lg={4}>
                        <h1>Users</h1>
                    </Col>
                </Row>
                <Row>
                    <Col lg={4}>
                        <Multiselect data={filterData}
                                     value={filter}

                                     itemComponent={ListItem}
                                     tagComponent={TagItem}
                                     filter={() => true}
                                     onSearch={(searchTerm) => {
                                         _.forEach(filterData, (it) => {
                                             it.term = searchTerm;
                                             it.id = `${it.field}${searchTerm}`;
                                         });
                                         //this.setState({open: true});
                                     }}
                                     onChange={(items, metadata) => {
                                         if (metadata.action === 'insert') {

                                             filter.push({
                                                 "field": metadata.dataItem.field,
                                                 "term": metadata.searchTerm
                                             })
                                         }
                                         if (metadata.action === 'remove') {
                                             _.remove(filter, (it) =>
                                                 it.field === metadata.dataItem.field && it.term === metadata.dataItem.term
                                             );
                                         }
                                         setTimeout(() => {
                                             this.setFilter(filter);
                                         }, 50);
                                         //this.setState({open: false});
                                     }}

                        />
                    </Col>
                    <Col lg={8}>
                        <br/>
                    </Col>
                </Row>
                <Row>
                    <ReactTable
                        manual
                        data={data}
                        pages={page.totalPages}
                        columns={columns}
                        loading={loading}
                        defaultPageSize={defaultPageSize}
                        onFetchData={this.fetchData}
                    />
                </Row>
            </Grid>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        loading: state.users.loading,
        page: state.users.page,
        defaultPageSize: state.users.defaultPageSize,
        filter: state.users.filter,
    };
};

export default connect(mapStateToProps, {fetchUsers, setUsersFilter})(App);
