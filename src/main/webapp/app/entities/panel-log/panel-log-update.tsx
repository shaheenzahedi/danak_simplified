import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { PanelLogType } from 'app/shared/model/enumerations/panel-log-type.model';
import { createEntity, getEntity, reset, updateEntity } from './panel-log.reducer';

export const PanelLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const panelLogEntity = useAppSelector(state => state.panelLog.entity);
  const loading = useAppSelector(state => state.panelLog.loading);
  const updating = useAppSelector(state => state.panelLog.updating);
  const updateSuccess = useAppSelector(state => state.panelLog.updateSuccess);
  const panelLogTypeValues = Object.keys(PanelLogType);

  const handleClose = () => {
    navigate('/panel-log' + location.search);
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

    const entity = {
      ...panelLogEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
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
        }
      : {
          panelLogType: 'DELETE',
          ...panelLogEntity,
          createTimeStamp: convertDateTimeFromServer(panelLogEntity.createTimeStamp),
          user: panelLogEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.panelLog.home.createOrEditLabel" data-cy="PanelLogCreateUpdateHeading">
            <Translate contentKey="danakApp.panelLog.home.createOrEditLabel">Create or edit a PanelLog</Translate>
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
                  id="panel-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.panelLog.createTimeStamp')}
                id="panel-log-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.panelLog.panelLogType')}
                id="panel-log-panelLogType"
                name="panelLogType"
                data-cy="panelLogType"
                type="select"
              >
                {panelLogTypeValues.map(panelLogType => (
                  <option value={panelLogType} key={panelLogType}>
                    {translate('danakApp.PanelLogType.' + panelLogType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="panel-log-user" name="user" data-cy="user" label={translate('danakApp.panelLog.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/panel-log" replace color="info">
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

export default PanelLogUpdate;
