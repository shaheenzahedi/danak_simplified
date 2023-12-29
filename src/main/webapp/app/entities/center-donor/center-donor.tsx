import React, { useState, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICenterDonor } from 'app/shared/model/center-donor.model';
import { getEntities } from './center-donor.reducer';

export const CenterDonor = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();

  const centerDonorList = useAppSelector(state => state.centerDonor.entities);
  const loading = useAppSelector(state => state.centerDonor.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="center-donor-heading" data-cy="CenterDonorHeading">
        <Translate contentKey="danakApp.centerDonor.home.title">Center Donors</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="danakApp.centerDonor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/center-donor/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="danakApp.centerDonor.home.createLabel">Create new Center Donor</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {centerDonorList && centerDonorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="danakApp.centerDonor.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.centerDonor.center">Center</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.centerDonor.donor">Donor</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {centerDonorList.map((centerDonor, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/center-donor/${centerDonor.id}`} color="link" size="sm">
                      {centerDonor.id}
                    </Button>
                  </td>
                  <td>{centerDonor.center ? <Link to={`/center/${centerDonor.center.id}`}>{centerDonor.center.id}</Link> : ''}</td>
                  <td>{centerDonor.donor ? <Link to={`/donor/${centerDonor.donor.id}`}>{centerDonor.donor.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/center-donor/${centerDonor.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/center-donor/${centerDonor.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/center-donor/${centerDonor.id}/delete`}
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
              <Translate contentKey="danakApp.centerDonor.home.notFound">No Center Donors found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CenterDonor;
