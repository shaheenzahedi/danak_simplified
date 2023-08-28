import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITabletUser } from 'app/shared/model/tablet-user.model';
import { getEntities } from './tablet-user.reducer';

export const TabletUser = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const tabletUserList = useAppSelector(state => state.tabletUser.entities);
  const loading = useAppSelector(state => state.tabletUser.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="tablet-user-heading" data-cy="TabletUserHeading">
        <Translate contentKey="danakApp.tabletUser.home.title">Tablet Users</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="danakApp.tabletUser.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/tablet-user/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="danakApp.tabletUser.home.createLabel">Create new Tablet User</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {tabletUserList && tabletUserList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="danakApp.tabletUser.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.tabletUser.createTimeStamp">Create Time Stamp</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.tabletUser.updateTimeStamp">Update Time Stamp</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.tabletUser.firstName">First Name</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.tabletUser.lastName">Last Name</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.tabletUser.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.tabletUser.tablet">Tablet</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {tabletUserList.map((tabletUser, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/tablet-user/${tabletUser.id}`} color="link" size="sm">
                      {tabletUser.id}
                    </Button>
                  </td>
                  <td>
                    {tabletUser.createTimeStamp ? (
                      <TextFormat type="date" value={tabletUser.createTimeStamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {tabletUser.updateTimeStamp ? (
                      <TextFormat type="date" value={tabletUser.updateTimeStamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{tabletUser.firstName}</td>
                  <td>{tabletUser.lastName}</td>
                  <td>{tabletUser.email}</td>
                  <td>{tabletUser.tablet ? <Link to={`/tablet/${tabletUser.tablet.id}`}>{tabletUser.tablet.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/tablet-user/${tabletUser.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/tablet-user/${tabletUser.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/tablet-user/${tabletUser.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="danakApp.tabletUser.home.notFound">No Tablet Users found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default TabletUser;
