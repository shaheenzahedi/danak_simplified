import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getSortState, JhiItemCount, JhiPagination, TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './user-activity.reducer';

export const UserActivity = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const userActivityList = useAppSelector(state => state.userActivity.entities);
  const loading = useAppSelector(state => state.userActivity.loading);
  const totalItems = useAppSelector(state => state.userActivity.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="user-activity-heading" data-cy="UserActivityHeading">
        <Translate contentKey="danakApp.userActivity.home.title">User Activities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="danakApp.userActivity.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-activity/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="danakApp.userActivity.home.createLabel">Create new User Activity</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userActivityList && userActivityList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="danakApp.userActivity.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createTimeStamp')}>
                  <Translate contentKey="danakApp.userActivity.createTimeStamp">Create Time Stamp</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updateTimeStamp')}>
                  <Translate contentKey="danakApp.userActivity.updateTimeStamp">Update Time Stamp</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('deviceTimeStamp')}>
                  <Translate contentKey="danakApp.userActivity.deviceTimeStamp">Device Time Stamp</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('listName')}>
                  <Translate contentKey="danakApp.userActivity.listName">List Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('total')}>
                  <Translate contentKey="danakApp.userActivity.total">Total</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('completed')}>
                  <Translate contentKey="danakApp.userActivity.completed">Completed</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('uniqueName')}>
                  <Translate contentKey="danakApp.userActivity.uniqueName">Unique Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('version')}>
                  <Translate contentKey="danakApp.userActivity.version">Version</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="danakApp.userActivity.description">Description</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="danakApp.userActivity.activity">Activity</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userActivityList.map((userActivity, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-activity/${userActivity.id}`} color="link" size="sm">
                      {userActivity.id}
                    </Button>
                  </td>
                  <td>
                    {userActivity.createTimeStamp ? (
                      <TextFormat type="date" value={userActivity.createTimeStamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {userActivity.updateTimeStamp ? (
                      <TextFormat type="date" value={userActivity.updateTimeStamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {userActivity.deviceTimeStamp ? (
                      <TextFormat type="date" value={userActivity.deviceTimeStamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userActivity.listName}</td>
                  <td>{userActivity.total}</td>
                  <td>{userActivity.completed}</td>
                  <td>{userActivity.uniqueName}</td>
                  <td>{userActivity.version}</td>
                  <td>{userActivity.description}</td>
                  <td>
                    {userActivity.activity ? <Link to={`/tablet-user/${userActivity.activity.id}`}>{userActivity.activity.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-activity/${userActivity.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-activity/${userActivity.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-activity/${userActivity.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="danakApp.userActivity.home.notFound">No User Activities found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userActivityList && userActivityList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default UserActivity;
