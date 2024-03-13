import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './center-image.reducer';

export const CenterImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const centerImageEntity = useAppSelector(state => state.centerImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="centerImageDetailsHeading">
          <Translate contentKey="danakApp.centerImage.detail.title">CenterImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{centerImageEntity.id}</dd>
          <dt>
            <span id="centerImageType">
              <Translate contentKey="danakApp.centerImage.centerImageType">Center Image Type</Translate>
            </span>
          </dt>
          <dd>{centerImageEntity.centerImageType}</dd>
          <dt>
            <Translate contentKey="danakApp.centerImage.file">File</Translate>
          </dt>
          <dd>{centerImageEntity.file ? centerImageEntity.file.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.centerImage.center">Center</Translate>
          </dt>
          <dd>{centerImageEntity.center ? centerImageEntity.center.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/center-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/center-image/${centerImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CenterImageDetail;
