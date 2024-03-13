import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './uploaded-file.reducer';

export const UploadedFileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const uploadedFileEntity = useAppSelector(state => state.uploadedFile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="uploadedFileDetailsHeading">
          <Translate contentKey="danakApp.uploadedFile.detail.title">UploadedFile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{uploadedFileEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">
              <Translate contentKey="danakApp.uploadedFile.createTimeStamp">Create Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {uploadedFileEntity.createTimeStamp ? (
              <TextFormat value={uploadedFileEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="deleteTimeStamp">
              <Translate contentKey="danakApp.uploadedFile.deleteTimeStamp">Delete Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {uploadedFileEntity.deleteTimeStamp ? (
              <TextFormat value={uploadedFileEntity.deleteTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isPublic">
              <Translate contentKey="danakApp.uploadedFile.isPublic">Is Public</Translate>
            </span>
          </dt>
          <dd>{uploadedFileEntity.isPublic ? 'true' : 'false'}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="danakApp.uploadedFile.name">Name</Translate>
            </span>
          </dt>
          <dd>{uploadedFileEntity.name}</dd>
          <dt>
            <span id="path">
              <Translate contentKey="danakApp.uploadedFile.path">Path</Translate>
            </span>
          </dt>
          <dd>{uploadedFileEntity.path}</dd>
          <dt>
            <span id="updateTimeStamp">
              <Translate contentKey="danakApp.uploadedFile.updateTimeStamp">Update Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {uploadedFileEntity.updateTimeStamp ? (
              <TextFormat value={uploadedFileEntity.updateTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/uploaded-file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/uploaded-file/${uploadedFileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UploadedFileDetail;
