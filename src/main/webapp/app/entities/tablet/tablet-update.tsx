import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICenter } from 'app/shared/model/center.model';
import { getEntities as getCenters } from 'app/entities/center/center.reducer';
import { IDonor } from 'app/shared/model/donor.model';
import { getEntities as getDonors } from 'app/entities/donor/donor.reducer';
import { ITablet } from 'app/shared/model/tablet.model';
import { getEntity, updateEntity, createEntity, reset } from './tablet.reducer';

export const TabletUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const centers = useAppSelector(state => state.center.entities);
  const donors = useAppSelector(state => state.donor.entities);
  const tabletEntity = useAppSelector(state => state.tablet.entity);
  const loading = useAppSelector(state => state.tablet.loading);
  const updating = useAppSelector(state => state.tablet.updating);
  const updateSuccess = useAppSelector(state => state.tablet.updateSuccess);
  const handleClose = () => {
    props.history.push('/tablet' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCenters({}));
    dispatch(getDonors({}));
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
      ...tabletEntity,
      ...values,
      center: centers.find(it => it.id.toString() === values.center.toString()),
      donor: donors.find(it => it.id.toString() === values.donor.toString()),
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
          ...tabletEntity,
          createTimeStamp: convertDateTimeFromServer(tabletEntity.createTimeStamp),
          updateTimeStamp: convertDateTimeFromServer(tabletEntity.updateTimeStamp),
          center: tabletEntity?.center?.id,
          donor: tabletEntity?.donor?.id,
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
              <ValidatedField label={translate('danakApp.tablet.name')} id="tablet-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('danakApp.tablet.identifier')}
                id="tablet-identifier"
                name="identifier"
                data-cy="identifier"
                type="text"
              />
              <ValidatedField label={translate('danakApp.tablet.model')} id="tablet-model" name="model" data-cy="model" type="text" />
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
