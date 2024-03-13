import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tablet-user-image.reducer';

export const TabletUserImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tabletUserImageEntity = useAppSelector(state => state.tabletUserImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tabletUserImageDetailsHeading">
          <Translate contentKey="danakApp.tabletUserImage.detail.title">TabletUserImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tabletUserImageEntity.id}</dd>
          <dt>
            <span id="tabletUserImageType">
              <Translate contentKey="danakApp.tabletUserImage.tabletUserImageType">Tablet User Image Type</Translate>
            </span>
          </dt>
          <dd>{tabletUserImageEntity.tabletUserImageType}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletUserImage.file">File</Translate>
          </dt>
          <dd>{tabletUserImageEntity.file ? tabletUserImageEntity.file.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.tabletUserImage.tabletUser">Tablet User</Translate>
          </dt>
          <dd>{tabletUserImageEntity.tabletUser ? tabletUserImageEntity.tabletUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tablet-user-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tablet-user-image/${tabletUserImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TabletUserImageDetail;
