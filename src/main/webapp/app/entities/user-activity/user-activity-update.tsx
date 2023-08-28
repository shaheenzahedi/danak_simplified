import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITabletUser } from 'app/shared/model/tablet-user.model';
import { getEntities as getTabletUsers } from 'app/entities/tablet-user/tablet-user.reducer';
import { IUserActivity } from 'app/shared/model/user-activity.model';
import { getEntity, updateEntity, createEntity, reset } from './user-activity.reducer';

export const UserActivityUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tabletUsers = useAppSelector(state => state.tabletUser.entities);
  const userActivityEntity = useAppSelector(state => state.userActivity.entity);
  const loading = useAppSelector(state => state.userActivity.loading);
  const updating = useAppSelector(state => state.userActivity.updating);
  const updateSuccess = useAppSelector(state => state.userActivity.updateSuccess);
  const handleClose = () => {
    props.history.push('/user-activity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTabletUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);
    values.updateTimeStamp = convertDateTimeToServer(values.updateTimeStamp);
    values.deviceTimeStamp = convertDateTimeToServer(values.deviceTimeStamp);

    const entity = {
      ...userActivityEntity,
      ...values,
      activity: tabletUsers.find(it => it.id.toString() === values.activity.toString()),
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
          deviceTimeStamp: displayDefaultDateTime(),
        }
      : {
          ...userActivityEntity,
          createTimeStamp: convertDateTimeFromServer(userActivityEntity.createTimeStamp),
          updateTimeStamp: convertDateTimeFromServer(userActivityEntity.updateTimeStamp),
          deviceTimeStamp: convertDateTimeFromServer(userActivityEntity.deviceTimeStamp),
          activity: userActivityEntity?.activity?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.userActivity.home.createOrEditLabel" data-cy="UserActivityCreateUpdateHeading">
            <Translate contentKey="danakApp.userActivity.home.createOrEditLabel">Create or edit a UserActivity</Translate>
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
                  id="user-activity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.userActivity.createTimeStamp')}
                id="user-activity-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.userActivity.updateTimeStamp')}
                id="user-activity-updateTimeStamp"
                name="updateTimeStamp"
                data-cy="updateTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.userActivity.deviceTimeStamp')}
                id="user-activity-deviceTimeStamp"
                name="deviceTimeStamp"
                data-cy="deviceTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.userActivity.listName')}
                id="user-activity-listName"
                name="listName"
                data-cy="listName"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.userActivity.total')}
                id="user-activity-total"
                name="total"
                data-cy="total"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.userActivity.completed')}
                id="user-activity-completed"
                name="completed"
                data-cy="completed"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.userActivity.uniqueName')}
                id="user-activity-uniqueName"
                name="uniqueName"
                data-cy="uniqueName"
                type="text"
              />
              <ValidatedField
                id="user-activity-activity"
                name="activity"
                data-cy="activity"
                label={translate('danakApp.userActivity.activity')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-activity" replace color="info">
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

export default UserActivityUpdate;
