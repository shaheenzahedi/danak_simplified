import React, {useEffect} from 'react';
import {Link, RouteComponentProps} from 'react-router-dom';
import {Button, Col, Row} from 'reactstrap';
import {Translate, translate, ValidatedField, ValidatedForm} from 'react-jhipster';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {useAppDispatch, useAppSelector} from 'app/config/store';
import {getEntities as getCenters} from 'app/entities/center/center.reducer';
import {getEntities as getDonors} from 'app/entities/donor/donor.reducer';
import {createEntity, getEntity, reset, updateEntity} from './center-donor.reducer';

export const CenterDonorUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();


  const isNew = props.match.params.id === undefined;

  const centers = useAppSelector(state => state.center.entities);
  const donors = useAppSelector(state => state.donor.entities);
  const centerDonorEntity = useAppSelector(state => state.centerDonor.entity);
  const loading = useAppSelector(state => state.centerDonor.loading);
  const updating = useAppSelector(state => state.centerDonor.updating);
  const updateSuccess = useAppSelector(state => state.centerDonor.updateSuccess);

  const handleClose = () => {
    props.history.push('/center-donor' + props.location.search);

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
    const entity = {
      ...centerDonorEntity,
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
      ? {}
      : {
          ...centerDonorEntity,
          center: centerDonorEntity?.center?.id,
          donor: centerDonorEntity?.donor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.centerDonor.home.createOrEditLabel" data-cy="CenterDonorCreateUpdateHeading">
            <Translate contentKey="danakApp.centerDonor.home.createOrEditLabel">Create or edit a CenterDonor</Translate>
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
                  id="center-donor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="center-donor-center"
                name="center"
                data-cy="center"
                label={translate('danakApp.centerDonor.center')}
                type="select"
              >
                <option value="" key="0" />
                {centers
                  ? centers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="center-donor-donor"
                name="donor"
                data-cy="donor"
                label={translate('danakApp.centerDonor.donor')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/center-donor" replace color="info">
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

export default CenterDonorUpdate;
