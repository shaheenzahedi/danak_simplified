import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './center-donor.reducer';

export const CenterDonorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const centerDonorEntity = useAppSelector(state => state.centerDonor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="centerDonorDetailsHeading">
          <Translate contentKey="danakApp.centerDonor.detail.title">CenterDonor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{centerDonorEntity.id}</dd>
          <dt>
            <span id="joinedTimeStamp">
              <Translate contentKey="danakApp.centerDonor.joinedTimeStamp">Joined Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {centerDonorEntity.joinedTimeStamp ? (
              <TextFormat value={centerDonorEntity.joinedTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="donorType">
              <Translate contentKey="danakApp.centerDonor.donorType">Donor Type</Translate>
            </span>
          </dt>
          <dd>{centerDonorEntity.donorType}</dd>
          <dt>
            <Translate contentKey="danakApp.centerDonor.center">Center</Translate>
          </dt>
          <dd>{centerDonorEntity.center ? centerDonorEntity.center.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.centerDonor.donor">Donor</Translate>
          </dt>
          <dd>{centerDonorEntity.donor ? centerDonorEntity.donor.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/center-donor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/center-donor/${centerDonorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CenterDonorDetail;
