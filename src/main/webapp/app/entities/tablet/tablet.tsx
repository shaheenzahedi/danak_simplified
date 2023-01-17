import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITablet } from 'app/shared/model/tablet.model';
import { getEntities } from './tablet.reducer';

export const Tablet = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const tabletList = useAppSelector(state => state.tablet.entities);
  const loading = useAppSelector(state => state.tablet.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="tablet-heading" data-cy="TabletHeading">
        <Translate contentKey="danakApp.tablet.home.title">Tablets</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="danakApp.tablet.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/tablet/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="danakApp.tablet.home.createLabel">Create new Tablet</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {tabletList && tabletList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="danakApp.tablet.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.tablet.name">Name</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {tabletList.map((tablet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/tablet/${tablet.id}`} color="link" size="sm">
                      {tablet.id}
                    </Button>
                  </td>
                  <td>{tablet.name}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/tablet/${tablet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/tablet/${tablet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/tablet/${tablet.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="danakApp.tablet.home.notFound">No Tablets found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Tablet;
