import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tablet-user.reducer';

export const TabletUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
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
            <span id="description">
              <Translate contentKey="danakApp.tabletUser.description">Description</Translate>
            </span>
          </dt>
          <dd>{tabletUserEntity.description}</dd>
          <dt>
            <span id="recoveryPhrase">
              <Translate contentKey="danakApp.tabletUser.recoveryPhrase">Recovery Phrase</Translate>
            </span>
          </dt>
          <dd>{tabletUserEntity.recoveryPhrase}</dd>
          <dt>
            <span id="archived">
              <Translate contentKey="danakApp.tabletUser.archived">Archived</Translate>
            </span>
          </dt>
          <dd>{tabletUserEntity.archived ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletUser.tablet">Tablet</Translate>
          </dt>
          <dd>{tabletUserEntity.tablet ? tabletUserEntity.tablet.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletUser.archivedBy">Archived By</Translate>
          </dt>
          <dd>{tabletUserEntity.archivedBy ? tabletUserEntity.archivedBy.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletUser.modifiedBy">Modified By</Translate>
          </dt>
          <dd>{tabletUserEntity.modifiedBy ? tabletUserEntity.modifiedBy.id : ''}</dd>
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
