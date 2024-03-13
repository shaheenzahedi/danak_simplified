import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getTablets } from 'app/entities/tablet/tablet.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './tablet-watch-list.reducer';

export const TabletWatchListUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tablets = useAppSelector(state => state.tablet.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const tabletWatchListEntity = useAppSelector(state => state.tabletWatchList.entity);
  const loading = useAppSelector(state => state.tabletWatchList.loading);
  const updating = useAppSelector(state => state.tabletWatchList.updating);
  const updateSuccess = useAppSelector(state => state.tabletWatchList.updateSuccess);

  const handleClose = () => {
    navigate('/tablet-watch-list' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTablets({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...tabletWatchListEntity,
      ...values,
      tablet: tablets.find(it => it.id.toString() === values.tablet.toString()),
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
      ? {}
      : {
          ...tabletWatchListEntity,
          tablet: tabletWatchListEntity?.tablet?.id,
          user: tabletWatchListEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.tabletWatchList.home.createOrEditLabel" data-cy="TabletWatchListCreateUpdateHeading">
            <Translate contentKey="danakApp.tabletWatchList.home.createOrEditLabel">Create or edit a TabletWatchList</Translate>
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
                  id="tablet-watch-list-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="tablet-watch-list-tablet"
                name="tablet"
                data-cy="tablet"
                label={translate('danakApp.tabletWatchList.tablet')}
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
              <ValidatedField
                id="tablet-watch-list-user"
                name="user"
                data-cy="user"
                label={translate('danakApp.tabletWatchList.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tablet-watch-list" replace color="info">
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

export default TabletWatchListUpdate;
