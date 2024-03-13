import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './file-share.reducer';

export const FileShareDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fileShareEntity = useAppSelector(state => state.fileShare.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fileShareDetailsHeading">
          <Translate contentKey="danakApp.fileShare.detail.title">FileShare</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{fileShareEntity.id}</dd>
          <dt>
            <Translate contentKey="danakApp.fileShare.file">File</Translate>
          </dt>
          <dd>{fileShareEntity.file ? fileShareEntity.file.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.fileShare.user">User</Translate>
          </dt>
          <dd>{fileShareEntity.user ? fileShareEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/file-share" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/file-share/${fileShareEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FileShareDetail;
