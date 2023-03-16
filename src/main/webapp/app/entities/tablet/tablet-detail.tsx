import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tablet.reducer';

export const TabletDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tabletEntity = useAppSelector(state => state.tablet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tabletDetailsHeading">
          <Translate contentKey="danakApp.tablet.detail.title">Tablet</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tabletEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="danakApp.tablet.name">Name</Translate>
            </span>
          </dt>
          <dd>{tabletEntity.name}</dd>
          <dt>
            <span id="androidId">
              <Translate contentKey="danakApp.tablet.androidId">Android Id</Translate>
            </span>
          </dt>
          <dd>{tabletEntity.androidId}</dd>
          <dt>
            <span id="macId">
              <Translate contentKey="danakApp.tablet.macId">Mac Id</Translate>
            </span>
          </dt>
          <dd>{tabletEntity.macId}</dd>
          <dt>
            <span id="model">
              <Translate contentKey="danakApp.tablet.model">Model</Translate>
            </span>
          </dt>
          <dd>{tabletEntity.model}</dd>
        </dl>
        <Button tag={Link} to="/tablet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tablet/${tabletEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TabletDetail;
