import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUploadedFiles } from 'app/entities/uploaded-file/uploaded-file.reducer';
import { getEntities as getCenters } from 'app/entities/center/center.reducer';
import { CenterImageType } from 'app/shared/model/enumerations/center-image-type.model';
import { createEntity, getEntity, reset, updateEntity } from './center-image.reducer';

export const CenterImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const uploadedFiles = useAppSelector(state => state.uploadedFile.entities);
  const centers = useAppSelector(state => state.center.entities);
  const centerImageEntity = useAppSelector(state => state.centerImage.entity);
  const loading = useAppSelector(state => state.centerImage.loading);
  const updating = useAppSelector(state => state.centerImage.updating);
  const updateSuccess = useAppSelector(state => state.centerImage.updateSuccess);
  const centerImageTypeValues = Object.keys(CenterImageType);

  const handleClose = () => {
    navigate('/center-image' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUploadedFiles({}));
    dispatch(getCenters({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...centerImageEntity,
      ...values,
      file: uploadedFiles.find(it => it.id.toString() === values.file.toString()),
      center: centers.find(it => it.id.toString() === values.center.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          centerImageType: 'GALLERY',
          ...centerImageEntity,
          file: centerImageEntity?.file?.id,
          center: centerImageEntity?.center?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.centerImage.home.createOrEditLabel" data-cy="CenterImageCreateUpdateHeading">
            <Translate contentKey="danakApp.centerImage.home.createOrEditLabel">Create or edit a CenterImage</Translate>
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
                  id="center-image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.centerImage.centerImageType')}
                id="center-image-centerImageType"
                name="centerImageType"
                data-cy="centerImageType"
                type="select"
              >
                {centerImageTypeValues.map(centerImageType => (
                  <option value={centerImageType} key={centerImageType}>
                    {translate('danakApp.CenterImageType.' + centerImageType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="center-image-file"
                name="file"
                data-cy="file"
                label={translate('danakApp.centerImage.file')}
                type="select"
              >
                <option value="" key="0" />
                {uploadedFiles
                  ? uploadedFiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="center-image-center"
                name="center"
                data-cy="center"
                label={translate('danakApp.centerImage.center')}
                type="select"
              >
                <option value="" key="0" />
                {centers
                  ? centers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/center-image" replace color="info">
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

export default CenterImageUpdate;
