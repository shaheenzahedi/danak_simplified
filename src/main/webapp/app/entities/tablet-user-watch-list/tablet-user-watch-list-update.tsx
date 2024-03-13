import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getTabletUsers } from 'app/entities/tablet-user/tablet-user.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './tablet-user-watch-list.reducer';

export const TabletUserWatchListUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tabletUsers = useAppSelector(state => state.tabletUser.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const tabletUserWatchListEntity = useAppSelector(state => state.tabletUserWatchList.entity);
  const loading = useAppSelector(state => state.tabletUserWatchList.loading);
  const updating = useAppSelector(state => state.tabletUserWatchList.updating);
  const updateSuccess = useAppSelector(state => state.tabletUserWatchList.updateSuccess);

  const handleClose = () => {
    navigate('/tablet-user-watch-list' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTabletUsers({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...tabletUserWatchListEntity,
      ...values,
      tabletUser: tabletUsers.find(it => it.id.toString() === values.tabletUser.toString()),
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
          ...tabletUserWatchListEntity,
          tabletUser: tabletUserWatchListEntity?.tabletUser?.id,
          user: tabletUserWatchListEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="danakApp.tabletUserWatchList.home.createOrEditLabel" data-cy="TabletUserWatchListCreateUpdateHeading">
            <Translate contentKey="danakApp.tabletUserWatchList.home.createOrEditLabel">Create or edit a TabletUserWatchList</Translate>
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
                  id="tablet-user-watch-list-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="tablet-user-watch-list-tabletUser"
                name="tabletUser"
                data-cy="tabletUser"
                label={translate('danakApp.tabletUserWatchList.tabletUser')}
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
              <ValidatedField
                id="tablet-user-watch-list-user"
                name="user"
                data-cy="user"
                label={translate('danakApp.tabletUserWatchList.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tablet-user-watch-list" replace color="info">
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

export default TabletUserWatchListUpdate;
