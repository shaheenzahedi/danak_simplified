import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tablet-user-watch-list.reducer';

export const TabletUserWatchListDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tabletUserWatchListEntity = useAppSelector(state => state.tabletUserWatchList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tabletUserWatchListDetailsHeading">
          <Translate contentKey="danakApp.tabletUserWatchList.detail.title">TabletUserWatchList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tabletUserWatchListEntity.id}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletUserWatchList.tabletUser">Tablet User</Translate>
          </dt>
          <dd>{tabletUserWatchListEntity.tabletUser ? tabletUserWatchListEntity.tabletUser.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletUserWatchList.user">User</Translate>
          </dt>
          <dd>{tabletUserWatchListEntity.user ? tabletUserWatchListEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tablet-user-watch-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tablet-user-watch-list/${tabletUserWatchListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TabletUserWatchListDetail;
