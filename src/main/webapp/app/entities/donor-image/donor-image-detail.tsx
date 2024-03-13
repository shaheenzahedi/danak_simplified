import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './donor-image.reducer';

export const DonorImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const donorImageEntity = useAppSelector(state => state.donorImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="donorImageDetailsHeading">
          <Translate contentKey="danakApp.donorImage.detail.title">DonorImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{donorImageEntity.id}</dd>
          <dt>
            <span id="donorImageType">
              <Translate contentKey="danakApp.donorImage.donorImageType">Donor Image Type</Translate>
            </span>
          </dt>
          <dd>{donorImageEntity.donorImageType}</dd>
          <dt>
            <Translate contentKey="danakApp.donorImage.file">File</Translate>
          </dt>
          <dd>{donorImageEntity.file ? donorImageEntity.file.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.donorImage.donor">Donor</Translate>
          </dt>
          <dd>{donorImageEntity.donor ? donorImageEntity.donor.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/donor-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/donor-image/${donorImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DonorImageDetail;
