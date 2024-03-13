import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tablet-log.reducer';

export const TabletLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tabletLogEntity = useAppSelector(state => state.tabletLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tabletLogDetailsHeading">
          <Translate contentKey="danakApp.tabletLog.detail.title">TabletLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tabletLogEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">
              <Translate contentKey="danakApp.tabletLog.createTimeStamp">Create Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {tabletLogEntity.createTimeStamp ? (
              <TextFormat value={tabletLogEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="danakApp.tabletLog.tablet">Tablet</Translate>
          </dt>
          <dd>{tabletLogEntity.tablet ? tabletLogEntity.tablet.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tablet-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tablet-log/${tabletLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TabletLogDetail;
