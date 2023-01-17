import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITablet } from 'app/shared/model/tablet.model';
import { getEntities as getTablets } from 'app/entities/tablet/tablet.reducer';
import { ITabletUser } from 'app/shared/model/tablet-user.model';
import { getEntity, updateEntity, createEntity, reset } from './tablet-user.reducer';

export const TabletUserUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tablets = useAppSelector(state => state.tablet.entities);
  const tabletUserEntity = useAppSelector(state => state.tabletUser.entity);
  const loading = useAppSelector(state => state.tabletUser.loading);
  const updating = useAppSelector(state => state.tabletUser.updating);
  const updateSuccess = useAppSelector(state => state.tabletUser.updateSuccess);
  const handleClose = () => {
    props.history.push('/tablet-user');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getTablets({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...tabletUserEntity,
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
      ? {}
      : {
          ...tabletUserEntity,
          tablet: tabletUserEntity?.tablet?.id,
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
