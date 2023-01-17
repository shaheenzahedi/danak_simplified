import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-activity.reducer';

export const UserActivityDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const userActivityEntity = useAppSelector(state => state.userActivity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userActivityDetailsHeading">
          <Translate contentKey="danakApp.userActivity.detail.title">UserActivity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userActivityEntity.id}</dd>
          <dt>
            <span id="listName">
              <Translate contentKey="danakApp.userActivity.listName">List Name</Translate>
            </span>
          </dt>
          <dd>{userActivityEntity.listName}</dd>
          <dt>
            <span id="total">
              <Translate contentKey="danakApp.userActivity.total">Total</Translate>
            </span>
          </dt>
          <dd>{userActivityEntity.total}</dd>
          <dt>
            <span id="completed">
              <Translate contentKey="danakApp.userActivity.completed">Completed</Translate>
            </span>
          </dt>
          <dd>{userActivityEntity.completed}</dd>
          <dt>
            <Translate contentKey="danakApp.userActivity.activity">Activity</Translate>
          </dt>
          <dd>{userActivityEntity.activity ? userActivityEntity.activity.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-activity" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-activity/${userActivityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserActivityDetail;
