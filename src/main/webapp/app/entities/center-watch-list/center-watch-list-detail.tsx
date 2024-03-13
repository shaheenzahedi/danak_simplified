import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './center-watch-list.reducer';

export const CenterWatchListDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const centerWatchListEntity = useAppSelector(state => state.centerWatchList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="centerWatchListDetailsHeading">
          <Translate contentKey="danakApp.centerWatchList.detail.title">CenterWatchList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{centerWatchListEntity.id}</dd>
          <dt>
            <Translate contentKey="danakApp.centerWatchList.center">Center</Translate>
          </dt>
          <dd>{centerWatchListEntity.center ? centerWatchListEntity.center.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.centerWatchList.user">User</Translate>
          </dt>
          <dd>{centerWatchListEntity.user ? centerWatchListEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/center-watch-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/center-watch-list/${centerWatchListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CenterWatchListDetail;
