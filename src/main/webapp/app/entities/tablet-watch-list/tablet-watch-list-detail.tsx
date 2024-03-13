import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tablet-watch-list.reducer';

export const TabletWatchListDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tabletWatchListEntity = useAppSelector(state => state.tabletWatchList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tabletWatchListDetailsHeading">
          <Translate contentKey="danakApp.tabletWatchList.detail.title">TabletWatchList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tabletWatchListEntity.id}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletWatchList.tablet">Tablet</Translate>
          </dt>
          <dd>{tabletWatchListEntity.tablet ? tabletWatchListEntity.tablet.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletWatchList.user">User</Translate>
          </dt>
          <dd>{tabletWatchListEntity.user ? tabletWatchListEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tablet-watch-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tablet-watch-list/${tabletWatchListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TabletWatchListDetail;
