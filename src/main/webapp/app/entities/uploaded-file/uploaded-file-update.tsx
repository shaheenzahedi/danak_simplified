import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity, getEntity, reset, updateEntity } from './uploaded-file.reducer';

export const UploadedFileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const uploadedFileEntity = useAppSelector(state => state.uploadedFile.entity);
  const loading = useAppSelector(state => state.uploadedFile.loading);
  const updating = useAppSelector(state => state.uploadedFile.updating);
  const updateSuccess = useAppSelector(state => state.uploadedFile.updateSuccess);

  const handleClose = () => {
    navigate('/uploaded-file' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);
    values.deleteTimeStamp = convertDateTimeToServer(values.deleteTimeStamp);
    values.updateTimeStamp = convertDateTimeToServer(values.updateTimeStamp);

    const entity = {
      ...uploadedFileEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createTimeStamp: displayDefaultDateTime(),
          deleteTimeStamp: displayDefaultDateTime(),
          updateTimeStamp: displayDefaultDateTime(),
        }
      : {
          ...uploadedFileEntity,
          createTimeStamp: convertDateTimeFromServer(uploadedFileEntity.createTimeStamp),
          deleteTimeStamp: convertDateTimeFromServer(uploadedFileEntity.deleteTimeStamp),
          updateTimeStamp: convertDateTimeFromServer(uploadedFileEntity.updateTimeStamp),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.uploadedFile.home.createOrEditLabel" data-cy="UploadedFileCreateUpdateHeading">
            <Translate contentKey="danakApp.uploadedFile.home.createOrEditLabel">Create or edit a UploadedFile</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="uploaded-file-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.uploadedFile.createTimeStamp')}
                id="uploaded-file-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.uploadedFile.deleteTimeStamp')}
                id="uploaded-file-deleteTimeStamp"
                name="deleteTimeStamp"
                data-cy="deleteTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.uploadedFile.isPublic')}
                id="uploaded-file-isPublic"
                name="isPublic"
                data-cy="isPublic"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('danakApp.uploadedFile.name')}
                id="uploaded-file-name"
                name="name"
                data-cy="name"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.uploadedFile.path')}
                id="uploaded-file-path"
                name="path"
                data-cy="path"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.uploadedFile.updateTimeStamp')}
                id="uploaded-file-updateTimeStamp"
                name="updateTimeStamp"
                data-cy="updateTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/uploaded-file" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UploadedFileUpdate;
