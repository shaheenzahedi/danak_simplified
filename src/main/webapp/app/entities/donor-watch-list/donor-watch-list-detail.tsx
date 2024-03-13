import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './donor-watch-list.reducer';

export const DonorWatchListDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const donorWatchListEntity = useAppSelector(state => state.donorWatchList.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="donorWatchListDetailsHeading">
          <Translate contentKey="danakApp.donorWatchList.detail.title">DonorWatchList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{donorWatchListEntity.id}</dd>
          <dt>
            <Translate contentKey="danakApp.donorWatchList.donor">Donor</Translate>
          </dt>
          <dd>{donorWatchListEntity.donor ? donorWatchListEntity.donor.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.donorWatchList.user">User</Translate>
          </dt>
          <dd>{donorWatchListEntity.user ? donorWatchListEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/donor-watch-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/donor-watch-list/${donorWatchListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DonorWatchListDetail;
