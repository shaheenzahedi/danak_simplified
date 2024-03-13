import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './donor.reducer';

export const DonorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const donorEntity = useAppSelector(state => state.donor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="donorDetailsHeading">
          <Translate contentKey="danakApp.donor.detail.title">Donor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{donorEntity.id}</dd>
          <dt>
            <span id="createTimeStamp">
              <Translate contentKey="danakApp.donor.createTimeStamp">Create Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {donorEntity.createTimeStamp ? <TextFormat value={donorEntity.createTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updateTimeStamp">
              <Translate contentKey="danakApp.donor.updateTimeStamp">Update Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {donorEntity.updateTimeStamp ? <TextFormat value={donorEntity.updateTimeStamp} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="name">
              <Translate contentKey="danakApp.donor.name">Name</Translate>
            </span>
          </dt>
          <dd>{donorEntity.name}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="danakApp.donor.city">City</Translate>
            </span>
          </dt>
          <dd>{donorEntity.city}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="danakApp.donor.country">Country</Translate>
            </span>
          </dt>
          <dd>{donorEntity.country}</dd>
          <dt>
            <span id="nationalCode">
              <Translate contentKey="danakApp.donor.nationalCode">National Code</Translate>
            </span>
          </dt>
          <dd>{donorEntity.nationalCode}</dd>
          <dt>
            <span id="educationType">
              <Translate contentKey="danakApp.donor.educationType">Education Type</Translate>
            </span>
          </dt>
          <dd>{donorEntity.educationType}</dd>
          <dt>
            <span id="education">
              <Translate contentKey="danakApp.donor.education">Education</Translate>
            </span>
          </dt>
          <dd>{donorEntity.education}</dd>
          <dt>
            <span id="occupation">
              <Translate contentKey="danakApp.donor.occupation">Occupation</Translate>
            </span>
          </dt>
          <dd>{donorEntity.occupation}</dd>
          <dt>
            <span id="workPlace">
              <Translate contentKey="danakApp.donor.workPlace">Work Place</Translate>
            </span>
          </dt>
          <dd>{donorEntity.workPlace}</dd>
          <dt>
            <span id="workPlacePhone">
              <Translate contentKey="danakApp.donor.workPlacePhone">Work Place Phone</Translate>
            </span>
          </dt>
          <dd>{donorEntity.workPlacePhone}</dd>
          <dt>
            <span id="archived">
              <Translate contentKey="danakApp.donor.archived">Archived</Translate>
            </span>
          </dt>
          <dd>{donorEntity.archived ? 'true' : 'false'}</dd>
          <dt>
            <span id="otpPhoneCode">
              <Translate contentKey="danakApp.donor.otpPhoneCode">Otp Phone Code</Translate>
            </span>
          </dt>
          <dd>{donorEntity.otpPhoneCode}</dd>
          <dt>
            <span id="otpPhoneEnable">
              <Translate contentKey="danakApp.donor.otpPhoneEnable">Otp Phone Enable</Translate>
            </span>
          </dt>
          <dd>{donorEntity.otpPhoneEnable ? 'true' : 'false'}</dd>
          <dt>
            <span id="otpPhoneSentTimeStamp">
              <Translate contentKey="danakApp.donor.otpPhoneSentTimeStamp">Otp Phone Sent Time Stamp</Translate>
            </span>
          </dt>
          <dd>
            {donorEntity.otpPhoneSentTimeStamp ? (
              <TextFormat value={donorEntity.otpPhoneSentTimeStamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="latitude">
              <Translate contentKey="danakApp.donor.latitude">Latitude</Translate>
            </span>
          </dt>
          <dd>{donorEntity.latitude}</dd>
          <dt>
            <span id="longitude">
              <Translate contentKey="danakApp.donor.longitude">Longitude</Translate>
            </span>
          </dt>
          <dd>{donorEntity.longitude}</dd>
          <dt>
            <span id="uid">
              <Translate contentKey="danakApp.donor.uid">Uid</Translate>
            </span>
          </dt>
          <dd>{donorEntity.uid}</dd>
          <dt>
            <Translate contentKey="danakApp.donor.user">User</Translate>
          </dt>
          <dd>{donorEntity.user ? donorEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.donor.archivedBy">Archived By</Translate>
          </dt>
          <dd>{donorEntity.archivedBy ? donorEntity.archivedBy.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.donor.createdBy">Created By</Translate>
          </dt>
          <dd>{donorEntity.createdBy ? donorEntity.createdBy.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.donor.modifiedBy">Modified By</Translate>
          </dt>
          <dd>{donorEntity.modifiedBy ? donorEntity.modifiedBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/donor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/donor/${donorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DonorDetail;
