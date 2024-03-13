import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { CenterType } from 'app/shared/model/enumerations/center-type.model';
import { createEntity, getEntity, reset, updateEntity } from './center.reducer';

export const CenterUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const centerEntity = useAppSelector(state => state.center.entity);
  const loading = useAppSelector(state => state.center.loading);
  const updating = useAppSelector(state => state.center.updating);
  const updateSuccess = useAppSelector(state => state.center.updateSuccess);
  const centerTypeValues = Object.keys(CenterType);

  const handleClose = () => {
    navigate('/center' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);
    values.updateTimeStamp = convertDateTimeToServer(values.updateTimeStamp);

    const entity = {
      ...centerEntity,
      ...values,
      archivedBy: users.find(it => it.id.toString() === values.archivedBy.toString()),
      createdBy: users.find(it => it.id.toString() === values.createdBy.toString()),
      modifiedBy: users.find(it => it.id.toString() === values.modifiedBy.toString()),
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
          updateTimeStamp: displayDefaultDateTime(),
        }
      : {
          centerType: 'SCHOOL',
          ...centerEntity,
          createTimeStamp: convertDateTimeFromServer(centerEntity.createTimeStamp),
          updateTimeStamp: convertDateTimeFromServer(centerEntity.updateTimeStamp),
          archivedBy: centerEntity?.archivedBy?.id,
          createdBy: centerEntity?.createdBy?.id,
          modifiedBy: centerEntity?.modifiedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.center.home.createOrEditLabel" data-cy="CenterCreateUpdateHeading">
            <Translate contentKey="danakApp.center.home.createOrEditLabel">Create or edit a Center</Translate>
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
                  id="center-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.center.createTimeStamp')}
                id="center-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.center.updateTimeStamp')}
                id="center-updateTimeStamp"
                name="updateTimeStamp"
                data-cy="updateTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('danakApp.center.name')} id="center-name" name="name" data-cy="name" type="text" />
              <ValidatedField label={translate('danakApp.center.city')} id="center-city" name="city" data-cy="city" type="text" />
              <ValidatedField
                label={translate('danakApp.center.country')}
                id="center-country"
                name="country"
                data-cy="country"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.center.archived')}
                id="center-archived"
                name="archived"
                data-cy="archived"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('danakApp.center.centerType')}
                id="center-centerType"
                name="centerType"
                data-cy="centerType"
                type="select"
              >
                {centerTypeValues.map(centerType => (
                  <option value={centerType} key={centerType}>
                    {translate('danakApp.CenterType.' + centerType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('danakApp.center.latitude')}
                id="center-latitude"
                name="latitude"
                data-cy="latitude"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.center.longitude')}
                id="center-longitude"
                name="longitude"
                data-cy="longitude"
                type="text"
              />
              <ValidatedField
                id="center-archivedBy"
                name="archivedBy"
                data-cy="archivedBy"
                label={translate('danakApp.center.archivedBy')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="center-createdBy"
                name="createdBy"
                data-cy="createdBy"
                label={translate('danakApp.center.createdBy')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="center-modifiedBy"
                name="modifiedBy"
                data-cy="modifiedBy"
                label={translate('danakApp.center.modifiedBy')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/center" replace color="info">
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

export default CenterUpdate;
