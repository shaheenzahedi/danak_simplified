import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserActivity } from 'app/shared/model/user-activity.model';
import { getEntities } from './user-activity.reducer';

export const UserActivity = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const userActivityList = useAppSelector(state => state.userActivity.entities);
  const loading = useAppSelector(state => state.userActivity.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

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
                <th>
                  <Translate contentKey="danakApp.userActivity.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.userActivity.createTimeStamp">Create Time Stamp</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.userActivity.updateTimeStamp">Update Time Stamp</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.userActivity.deviceTimeStamp">Device Time Stamp</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.userActivity.listName">List Name</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.userActivity.total">Total</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.userActivity.completed">Completed</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.userActivity.uniqueName">Unique Name</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.userActivity.activity">Activity</Translate>
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
                      <Button tag={Link} to={`/user-activity/${userActivity.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-activity/${userActivity.id}/delete`}
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
    </div>
  );
};

export default UserActivity;
