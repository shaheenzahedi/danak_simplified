import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getTablets } from 'app/entities/tablet/tablet.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './tablet-user.reducer';

export const TabletUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tablets = useAppSelector(state => state.tablet.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const tabletUserEntity = useAppSelector(state => state.tabletUser.entity);
  const loading = useAppSelector(state => state.tabletUser.loading);
  const updating = useAppSelector(state => state.tabletUser.updating);
  const updateSuccess = useAppSelector(state => state.tabletUser.updateSuccess);

  const handleClose = () => {
    navigate('/tablet-user' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTablets({}));
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
      ...tabletUserEntity,
      ...values,
      tablet: tablets.find(it => it.id.toString() === values.tablet.toString()),
      archivedBy: users.find(it => it.id.toString() === values.archivedBy.toString()),
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
          ...tabletUserEntity,
          createTimeStamp: convertDateTimeFromServer(tabletUserEntity.createTimeStamp),
          updateTimeStamp: convertDateTimeFromServer(tabletUserEntity.updateTimeStamp),
          tablet: tabletUserEntity?.tablet?.id,
          archivedBy: tabletUserEntity?.archivedBy?.id,
          modifiedBy: tabletUserEntity?.modifiedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.tabletUser.home.createOrEditLabel" data-cy="TabletUserCreateUpdateHeading">
            <Translate contentKey="danakApp.tabletUser.home.createOrEditLabel">Create or edit a TabletUser</Translate>
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
                  id="tablet-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.tabletUser.createTimeStamp')}
                id="tablet-user-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.tabletUser.updateTimeStamp')}
                id="tablet-user-updateTimeStamp"
                name="updateTimeStamp"
                data-cy="updateTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.tabletUser.firstName')}
                id="tablet-user-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.tabletUser.lastName')}
                id="tablet-user-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.tabletUser.email')}
                id="tablet-user-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.tabletUser.description')}
                id="tablet-user-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.tabletUser.recoveryPhrase')}
                id="tablet-user-recoveryPhrase"
                name="recoveryPhrase"
                data-cy="recoveryPhrase"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.tabletUser.archived')}
                id="tablet-user-archived"
                name="archived"
                data-cy="archived"
                check
                type="checkbox"
              />
              <ValidatedField
                id="tablet-user-tablet"
                name="tablet"
                data-cy="tablet"
                label={translate('danakApp.tabletUser.tablet')}
                type="select"
              >
                <option value="" key="0" />
                {tablets
                  ? tablets.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="tablet-user-archivedBy"
                name="archivedBy"
                data-cy="archivedBy"
                label={translate('danakApp.tabletUser.archivedBy')}
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
                id="tablet-user-modifiedBy"
                name="modifiedBy"
                data-cy="modifiedBy"
                label={translate('danakApp.tabletUser.modifiedBy')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tablet-user" replace color="info">
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

export default TabletUserUpdate;
