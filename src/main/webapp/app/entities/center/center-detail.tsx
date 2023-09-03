import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './center.reducer';

export const CenterDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
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
