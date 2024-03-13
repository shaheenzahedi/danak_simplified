import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getFiles } from 'app/entities/file/file.reducer';
import { getEntities as getVersions } from 'app/entities/version/version.reducer';
import { createEntity, getEntity, reset, updateEntity } from './file-belongings.reducer';

export const FileBelongingsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const files = useAppSelector(state => state.file.entities);
  const versions = useAppSelector(state => state.version.entities);
  const fileBelongingsEntity = useAppSelector(state => state.fileBelongings.entity);
  const loading = useAppSelector(state => state.fileBelongings.loading);
  const updating = useAppSelector(state => state.fileBelongings.updating);
  const updateSuccess = useAppSelector(state => state.fileBelongings.updateSuccess);

  const handleClose = () => {
    navigate('/file-belongings' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFiles({}));
    dispatch(getVersions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...fileBelongingsEntity,
      ...values,
      file: files.find(it => it.id.toString() === values.file.toString()),
      version: versions.find(it => it.id.toString() === values.version.toString()),
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
          ...fileBelongingsEntity,
          file: fileBelongingsEntity?.file?.id,
          version: fileBelongingsEntity?.version?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.fileBelongings.home.createOrEditLabel" data-cy="FileBelongingsCreateUpdateHeading">
            <Translate contentKey="danakApp.fileBelongings.home.createOrEditLabel">Create or edit a FileBelongings</Translate>
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
                  id="file-belongings-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="file-belongings-file"
                name="file"
                data-cy="file"
                label={translate('danakApp.fileBelongings.file')}
                type="select"
              >
                <option value="" key="0" />
                {files
                  ? files.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="file-belongings-version"
                name="version"
                data-cy="version"
                label={translate('danakApp.fileBelongings.version')}
                type="select"
              >
                <option value="" key="0" />
                {versions
                  ? versions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/file-belongings" replace color="info">
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

export default FileBelongingsUpdate;
