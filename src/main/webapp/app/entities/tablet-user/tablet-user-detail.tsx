import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tablet-user.reducer';

export const TabletUserDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tabletUserEntity = useAppSelector(state => state.tabletUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tabletUserDetailsHeading">
          <Translate contentKey="danakApp.tabletUser.detail.title">TabletUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tabletUserEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">
              <Translate contentKey="danakApp.tabletUser.createTimeStamp">Create Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {tabletUserEntity.createTimeStamp ? (
              <TextFormat value={tabletUserEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updateTimeStamp">
              <Translate contentKey="danakApp.tabletUser.updateTimeStamp">Update Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {tabletUserEntity.updateTimeStamp ? (
              <TextFormat value={tabletUserEntity.updateTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="danakApp.tabletUser.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{tabletUserEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="danakApp.tabletUser.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{tabletUserEntity.lastName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="danakApp.tabletUser.email">Email</Translate>
            </span>
          </dt>
          <dd>{tabletUserEntity.email}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletUser.tablet">Tablet</Translate>
          </dt>
          <dd>{tabletUserEntity.tablet ? tabletUserEntity.tablet.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tablet-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tablet-user/${tabletUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TabletUserDetail;
