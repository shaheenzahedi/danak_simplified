import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './panel-log.reducer';

export const PanelLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const panelLogEntity = useAppSelector(state => state.panelLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="panelLogDetailsHeading">
          <Translate contentKey="danakApp.panelLog.detail.title">PanelLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{panelLogEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">
              <Translate contentKey="danakApp.panelLog.createTimeStamp">Create Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {panelLogEntity.createTimeStamp ? (
              <TextFormat value={panelLogEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="panelLogType">
              <Translate contentKey="danakApp.panelLog.panelLogType">Panel Log Type</Translate>
            </span>
          </dt>
          <dd>{panelLogEntity.panelLogType}</dd>
          <dt>
            <Translate contentKey="danakApp.panelLog.user">User</Translate>
          </dt>
          <dd>{panelLogEntity.user ? panelLogEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/panel-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/panel-log/${panelLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PanelLogDetail;
