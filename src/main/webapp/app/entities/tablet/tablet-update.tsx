import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCenters } from 'app/entities/center/center.reducer';
import { getEntities as getDonors } from 'app/entities/donor/donor.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './tablet.reducer';

export const TabletUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const centers = useAppSelector(state => state.center.entities);
  const donors = useAppSelector(state => state.donor.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const tabletEntity = useAppSelector(state => state.tablet.entity);
  const loading = useAppSelector(state => state.tablet.loading);
  const updating = useAppSelector(state => state.tablet.updating);
  const updateSuccess = useAppSelector(state => state.tablet.updateSuccess);

  const handleClose = () => {
    navigate('/tablet' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCenters({}));
    dispatch(getDonors({}));
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
    values.identifier = convertDateTimeToServer(values.identifier);
    values.tag = convertDateTimeToServer(values.tag);

    const entity = {
      ...tabletEntity,
      ...values,
      center: centers.find(it => it.id.toString() === values.center.toString()),
      donor: donors.find(it => it.id.toString() === values.donor.toString()),
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
          identifier: displayDefaultDateTime(),
          tag: displayDefaultDateTime(),
        }
      : {
          ...tabletEntity,
          createTimeStamp: convertDateTimeFromServer(tabletEntity.createTimeStamp),
          updateTimeStamp: convertDateTimeFromServer(tabletEntity.updateTimeStamp),
          identifier: convertDateTimeFromServer(tabletEntity.identifier),
          tag: convertDateTimeFromServer(tabletEntity.tag),
          center: tabletEntity?.center?.id,
          donor: tabletEntity?.donor?.id,
          archivedBy: tabletEntity?.archivedBy?.id,
          modifiedBy: tabletEntity?.modifiedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.tablet.home.createOrEditLabel" data-cy="TabletCreateUpdateHeading">
            <Translate contentKey="danakApp.tablet.home.createOrEditLabel">Create or edit a Tablet</Translate>
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
                  id="tablet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.tablet.createTimeStamp')}
                id="tablet-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.tablet.updateTimeStamp')}
                id="tablet-updateTimeStamp"
                name="updateTimeStamp"
                data-cy="updateTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.tablet.identifier')}
                id="tablet-identifier"
                name="identifier"
                data-cy="identifier"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.tablet.tag')}
                id="tablet-tag"
                name="tag"
                data-cy="tag"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('danakApp.tablet.name')} id="tablet-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('danakApp.tablet.androidId')}
                id="tablet-androidId"
                name="androidId"
                data-cy="androidId"
                type="text"
              />
              <ValidatedField label={translate('danakApp.tablet.macId')} id="tablet-macId" name="macId" data-cy="macId" type="text" />
              <ValidatedField label={translate('danakApp.tablet.model')} id="tablet-model" name="model" data-cy="model" type="text" />
              <ValidatedField
                label={translate('danakApp.tablet.description')}
                id="tablet-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.tablet.archived')}
                id="tablet-archived"
                name="archived"
                data-cy="archived"
                check
                type="checkbox"
              />
              <ValidatedField id="tablet-center" name="center" data-cy="center" label={translate('danakApp.tablet.center')} type="select">
                <option value="" key="0" />
                {centers
                  ? centers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tablet-donor" name="donor" data-cy="donor" label={translate('danakApp.tablet.donor')} type="select">
                <option value="" key="0" />
                {donors
                  ? donors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="tablet-archivedBy"
                name="archivedBy"
                data-cy="archivedBy"
                label={translate('danakApp.tablet.archivedBy')}
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
                id="tablet-modifiedBy"
                name="modifiedBy"
                data-cy="modifiedBy"
                label={translate('danakApp.tablet.modifiedBy')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tablet" replace color="info">
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

export default TabletUpdate;
