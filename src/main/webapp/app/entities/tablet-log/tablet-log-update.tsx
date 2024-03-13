import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getTablets } from 'app/entities/tablet/tablet.reducer';
import { createEntity, getEntity, reset, updateEntity } from './tablet-log.reducer';

export const TabletLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tablets = useAppSelector(state => state.tablet.entities);
  const tabletLogEntity = useAppSelector(state => state.tabletLog.entity);
  const loading = useAppSelector(state => state.tabletLog.loading);
  const updating = useAppSelector(state => state.tabletLog.updating);
  const updateSuccess = useAppSelector(state => state.tabletLog.updateSuccess);

  const handleClose = () => {
    navigate('/tablet-log' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTablets({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createTimeStamp = convertDateTimeToServer(values.createTimeStamp);

    const entity = {
      ...tabletLogEntity,
      ...values,
      tablet: tablets.find(it => it.id.toString() === values.tablet.toString()),
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
          ...tabletLogEntity,
          createTimeStamp: convertDateTimeFromServer(tabletLogEntity.createTimeStamp),
          tablet: tabletLogEntity?.tablet?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.tabletLog.home.createOrEditLabel" data-cy="TabletLogCreateUpdateHeading">
            <Translate contentKey="danakApp.tabletLog.home.createOrEditLabel">Create or edit a TabletLog</Translate>
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
                  id="tablet-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.tabletLog.createTimeStamp')}
                id="tablet-log-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="tablet-log-tablet"
                name="tablet"
                data-cy="tablet"
                label={translate('danakApp.tabletLog.tablet')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tablet-log" replace color="info">
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

export default TabletLogUpdate;
