import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './file.reducer';

export const FileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fileEntity = useAppSelector(state => state.file.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fileDetailsHeading">
          <Translate contentKey="danakApp.file.detail.title">File</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{fileEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="danakApp.file.name">Name</Translate>
            </span>
          </dt>
          <dd>{fileEntity.name}</dd>
          <dt>
            <span id="checksum">
              <Translate contentKey="danakApp.file.checksum">Checksum</Translate>
            </span>
          </dt>
          <dd>{fileEntity.checksum}</dd>
          <dt>
            <span id="path">
              <Translate contentKey="danakApp.file.path">Path</Translate>
            </span>
          </dt>
          <dd>{fileEntity.path}</dd>
          <dt>
            <span id="size">
              <Translate contentKey="danakApp.file.size">Size</Translate>
            </span>
          </dt>
          <dd>{fileEntity.size}</dd>
          <dt>
            <Translate contentKey="danakApp.file.placement">Placement</Translate>
          </dt>
          <dd>{fileEntity.placement ? fileEntity.placement.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/file/${fileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FileDetail;
