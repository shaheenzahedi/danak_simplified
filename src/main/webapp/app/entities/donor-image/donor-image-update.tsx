import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getUploadedFiles } from 'app/entities/uploaded-file/uploaded-file.reducer';
import { getEntities as getDonors } from 'app/entities/donor/donor.reducer';
import { DonorImageType } from 'app/shared/model/enumerations/donor-image-type.model';
import { createEntity, getEntity, reset, updateEntity } from './donor-image.reducer';

export const DonorImageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const uploadedFiles = useAppSelector(state => state.uploadedFile.entities);
  const donors = useAppSelector(state => state.donor.entities);
  const donorImageEntity = useAppSelector(state => state.donorImage.entity);
  const loading = useAppSelector(state => state.donorImage.loading);
  const updating = useAppSelector(state => state.donorImage.updating);
  const updateSuccess = useAppSelector(state => state.donorImage.updateSuccess);
  const donorImageTypeValues = Object.keys(DonorImageType);

  const handleClose = () => {
    navigate('/donor-image' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUploadedFiles({}));
    dispatch(getDonors({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...donorImageEntity,
      ...values,
      file: uploadedFiles.find(it => it.id.toString() === values.file.toString()),
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
      ? {}
      : {
          donorImageType: 'DOCUMENT',
          ...donorImageEntity,
          file: donorImageEntity?.file?.id,
          donor: donorImageEntity?.donor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.donorImage.home.createOrEditLabel" data-cy="DonorImageCreateUpdateHeading">
            <Translate contentKey="danakApp.donorImage.home.createOrEditLabel">Create or edit a DonorImage</Translate>
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
                  id="donor-image-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('danakApp.donorImage.donorImageType')}
                id="donor-image-donorImageType"
                name="donorImageType"
                data-cy="donorImageType"
                type="select"
              >
                {donorImageTypeValues.map(donorImageType => (
                  <option value={donorImageType} key={donorImageType}>
                    {translate('danakApp.DonorImageType.' + donorImageType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="donor-image-file" name="file" data-cy="file" label={translate('danakApp.donorImage.file')} type="select">
                <option value="" key="0" />
                {uploadedFiles
                  ? uploadedFiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="donor-image-donor"
                name="donor"
                data-cy="donor"
                label={translate('danakApp.donorImage.donor')}
                type="select"
              >
                <option value="" key="0" />
                {donors
                  ? donors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/donor-image" replace color="info">
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

export default DonorImageUpdate;
