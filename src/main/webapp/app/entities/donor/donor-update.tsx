import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { EducationType } from 'app/shared/model/enumerations/education-type.model';
import { createEntity, getEntity, reset, updateEntity } from './donor.reducer';

export const DonorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const donorEntity = useAppSelector(state => state.donor.entity);
  const loading = useAppSelector(state => state.donor.loading);
  const updating = useAppSelector(state => state.donor.updating);
  const updateSuccess = useAppSelector(state => state.donor.updateSuccess);
  const educationTypeValues = Object.keys(EducationType);

  const handleClose = () => {
    navigate('/donor' + location.search);
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
    values.otpPhoneSentTimeStamp = convertDateTimeToServer(values.otpPhoneSentTimeStamp);

    const entity = {
      ...donorEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          otpPhoneSentTimeStamp: displayDefaultDateTime(),
        }
      : {
          educationType: 'SCHOOL',
          ...donorEntity,
          createTimeStamp: convertDateTimeFromServer(donorEntity.createTimeStamp),
          updateTimeStamp: convertDateTimeFromServer(donorEntity.updateTimeStamp),
          otpPhoneSentTimeStamp: convertDateTimeFromServer(donorEntity.otpPhoneSentTimeStamp),
          user: donorEntity?.user?.id,
          archivedBy: donorEntity?.archivedBy?.id,
          createdBy: donorEntity?.createdBy?.id,
          modifiedBy: donorEntity?.modifiedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.donor.home.createOrEditLabel" data-cy="DonorCreateUpdateHeading">
            <Translate contentKey="danakApp.donor.home.createOrEditLabel">Create or edit a Donor</Translate>
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
                  id="donor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.donor.createTimeStamp')}
                id="donor-createTimeStamp"
                name="createTimeStamp"
                data-cy="createTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.donor.updateTimeStamp')}
                id="donor-updateTimeStamp"
                name="updateTimeStamp"
                data-cy="updateTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label={translate('danakApp.donor.name')} id="donor-name" name="name" data-cy="name" type="text" />
              <ValidatedField label={translate('danakApp.donor.city')} id="donor-city" name="city" data-cy="city" type="text" />
              <ValidatedField label={translate('danakApp.donor.country')} id="donor-country" name="country" data-cy="country" type="text" />
              <ValidatedField
                label={translate('danakApp.donor.nationalCode')}
                id="donor-nationalCode"
                name="nationalCode"
                data-cy="nationalCode"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.donor.educationType')}
                id="donor-educationType"
                name="educationType"
                data-cy="educationType"
                type="select"
              >
                {educationTypeValues.map(educationType => (
                  <option value={educationType} key={educationType}>
                    {translate('danakApp.EducationType.' + educationType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('danakApp.donor.education')}
                id="donor-education"
                name="education"
                data-cy="education"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.donor.occupation')}
                id="donor-occupation"
                name="occupation"
                data-cy="occupation"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.donor.workPlace')}
                id="donor-workPlace"
                name="workPlace"
                data-cy="workPlace"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.donor.workPlacePhone')}
                id="donor-workPlacePhone"
                name="workPlacePhone"
                data-cy="workPlacePhone"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.donor.archived')}
                id="donor-archived"
                name="archived"
                data-cy="archived"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('danakApp.donor.otpPhoneCode')}
                id="donor-otpPhoneCode"
                name="otpPhoneCode"
                data-cy="otpPhoneCode"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.donor.otpPhoneEnable')}
                id="donor-otpPhoneEnable"
                name="otpPhoneEnable"
                data-cy="otpPhoneEnable"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('danakApp.donor.otpPhoneSentTimeStamp')}
                id="donor-otpPhoneSentTimeStamp"
                name="otpPhoneSentTimeStamp"
                data-cy="otpPhoneSentTimeStamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('danakApp.donor.latitude')}
                id="donor-latitude"
                name="latitude"
                data-cy="latitude"
                type="text"
              />
              <ValidatedField
                label={translate('danakApp.donor.longitude')}
                id="donor-longitude"
                name="longitude"
                data-cy="longitude"
                type="text"
              />
              <ValidatedField label={translate('danakApp.donor.uid')} id="donor-uid" name="uid" data-cy="uid" type="text" />
              <ValidatedField id="donor-user" name="user" data-cy="user" label={translate('danakApp.donor.user')} type="select">
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
                id="donor-archivedBy"
                name="archivedBy"
                data-cy="archivedBy"
                label={translate('danakApp.donor.archivedBy')}
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
                id="donor-createdBy"
                name="createdBy"
                data-cy="createdBy"
                label={translate('danakApp.donor.createdBy')}
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
                id="donor-modifiedBy"
                name="modifiedBy"
                data-cy="modifiedBy"
                label={translate('danakApp.donor.modifiedBy')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/donor" replace color="info">
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

export default DonorUpdate;
