import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUploadedFiles } from 'app/entities/uploaded-file/uploaded-file.reducer';
import { getEntities as getTabletUsers } from 'app/entities/tablet-user/tablet-user.reducer';
import { TabletUserImageType } from 'app/shared/model/enumerations/tablet-user-image-type.model';
import { createEntity, getEntity, reset, updateEntity } from './tablet-user-image.reducer';

export const TabletUserImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const uploadedFiles = useAppSelector(state => state.uploadedFile.entities);
  const tabletUsers = useAppSelector(state => state.tabletUser.entities);
  const tabletUserImageEntity = useAppSelector(state => state.tabletUserImage.entity);
  const loading = useAppSelector(state => state.tabletUserImage.loading);
  const updating = useAppSelector(state => state.tabletUserImage.updating);
  const updateSuccess = useAppSelector(state => state.tabletUserImage.updateSuccess);
  const tabletUserImageTypeValues = Object.keys(TabletUserImageType);

  const handleClose = () => {
    navigate('/tablet-user-image' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUploadedFiles({}));
    dispatch(getTabletUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...tabletUserImageEntity,
      ...values,
      file: uploadedFiles.find(it => it.id.toString() === values.file.toString()),
      tabletUser: tabletUsers.find(it => it.id.toString() === values.tabletUser.toString()),
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
          tabletUserImageType: 'TABLET',
          ...tabletUserImageEntity,
          file: tabletUserImageEntity?.file?.id,
          tabletUser: tabletUserImageEntity?.tabletUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.tabletUserImage.home.createOrEditLabel" data-cy="TabletUserImageCreateUpdateHeading">
            <Translate contentKey="danakApp.tabletUserImage.home.createOrEditLabel">Create or edit a TabletUserImage</Translate>
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
                  id="tablet-user-image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.tabletUserImage.tabletUserImageType')}
                id="tablet-user-image-tabletUserImageType"
                name="tabletUserImageType"
                data-cy="tabletUserImageType"
                type="select"
              >
                {tabletUserImageTypeValues.map(tabletUserImageType => (
                  <option value={tabletUserImageType} key={tabletUserImageType}>
                    {translate('danakApp.TabletUserImageType.' + tabletUserImageType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="tablet-user-image-file"
                name="file"
                data-cy="file"
                label={translate('danakApp.tabletUserImage.file')}
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
                id="tablet-user-image-tabletUser"
                name="tabletUser"
                data-cy="tabletUser"
                label={translate('danakApp.tabletUserImage.tabletUser')}
                type="select"
              >
                <option value="" key="0" />
                {tabletUsers
                  ? tabletUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tablet-user-image" replace color="info">
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

export default TabletUserImageUpdate;
