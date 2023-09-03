import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './donor.reducer';

export const DonorDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const donorEntity = useAppSelector(state => state.donor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="donorDetailsHeading">
          <Translate contentKey="danakApp.donor.detail.title">Donor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{donorEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">
              <Translate contentKey="danakApp.donor.createTimeStamp">Create Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {donorEntity.createTimeStamp ? <TextFormat value={donorEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updateTimeStamp">
              <Translate contentKey="danakApp.donor.updateTimeStamp">Update Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {donorEntity.updateTimeStamp ? <TextFormat value={donorEntity.updateTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="name">
              <Translate contentKey="danakApp.donor.name">Name</Translate>
            </span>
          </dt>
          <dd>{donorEntity.name}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="danakApp.donor.city">City</Translate>
            </span>
          </dt>
          <dd>{donorEntity.city}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="danakApp.donor.country">Country</Translate>
            </span>
          </dt>
          <dd>{donorEntity.country}</dd>
          <dt>
            <Translate contentKey="danakApp.donor.user">User</Translate>
          </dt>
          <dd>{donorEntity.user ? donorEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/donor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/donor/${donorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DonorDetail;
