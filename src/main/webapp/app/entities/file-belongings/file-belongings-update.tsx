import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFile } from 'app/shared/model/file.model';
import { getEntities as getFiles } from 'app/entities/file/file.reducer';
import { IVersion } from 'app/shared/model/version.model';
import { getEntities as getVersions } from 'app/entities/version/version.reducer';
import { IFileBelongings } from 'app/shared/model/file-belongings.model';
import { getEntity, updateEntity, createEntity, reset } from './file-belongings.reducer';

export const FileBelongingsUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const files = useAppSelector(state => state.file.entities);
  const versions = useAppSelector(state => state.version.entities);
  const fileBelongingsEntity = useAppSelector(state => state.fileBelongings.entity);
  const loading = useAppSelector(state => state.fileBelongings.loading);
  const updating = useAppSelector(state => state.fileBelongings.updating);
  const updateSuccess = useAppSelector(state => state.fileBelongings.updateSuccess);
  const handleClose = () => {
    props.history.push('/file-belongings' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
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
