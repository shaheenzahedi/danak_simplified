import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './center.reducer';

export const CenterDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const centerEntity = useAppSelector(state => state.center.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="centerDetailsHeading">
          <Translate contentKey="danakApp.center.detail.title">Center</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{centerEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">
              <Translate contentKey="danakApp.center.createTimeStamp">Create Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {centerEntity.createTimeStamp ? <TextFormat value={centerEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updateTimeStamp">
              <Translate contentKey="danakApp.center.updateTimeStamp">Update Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {centerEntity.updateTimeStamp ? <TextFormat value={centerEntity.updateTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="name">
              <Translate contentKey="danakApp.center.name">Name</Translate>
            </span>
          </dt>
          <dd>{centerEntity.name}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="danakApp.center.city">City</Translate>
            </span>
          </dt>
          <dd>{centerEntity.city}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="danakApp.center.country">Country</Translate>
            </span>
          </dt>
          <dd>{centerEntity.country}</dd>
          <dt>
            <span id="archived">
              <Translate contentKey="danakApp.center.archived">Archived</Translate>
            </span>
          </dt>
          <dd>{centerEntity.archived ? 'true' : 'false'}</dd>
          <dt>
            <span id="centerType">
              <Translate contentKey="danakApp.center.centerType">Center Type</Translate>
            </span>
          </dt>
          <dd>{centerEntity.centerType}</dd>
          <dt>
            <span id="latitude">
              <Translate contentKey="danakApp.center.latitude">Latitude</Translate>
            </span>
          </dt>
          <dd>{centerEntity.latitude}</dd>
          <dt>
            <span id="longitude">
              <Translate contentKey="danakApp.center.longitude">Longitude</Translate>
            </span>
          </dt>
          <dd>{centerEntity.longitude}</dd>
          <dt>
            <Translate contentKey="danakApp.center.archivedBy">Archived By</Translate>
          </dt>
          <dd>{centerEntity.archivedBy ? centerEntity.archivedBy.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.center.createdBy">Created By</Translate>
          </dt>
          <dd>{centerEntity.createdBy ? centerEntity.createdBy.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.center.modifiedBy">Modified By</Translate>
          </dt>
          <dd>{centerEntity.modifiedBy ? centerEntity.modifiedBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/center" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/center/${centerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CenterDetail;
