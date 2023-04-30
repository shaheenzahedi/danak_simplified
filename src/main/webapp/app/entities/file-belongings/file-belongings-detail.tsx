import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './file-belongings.reducer';

export const FileBelongingsDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const fileBelongingsEntity = useAppSelector(state => state.fileBelongings.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fileBelongingsDetailsHeading">
          <Translate contentKey="danakApp.fileBelongings.detail.title">FileBelongings</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{fileBelongingsEntity.id}</dd>
          <dt>
            <Translate contentKey="danakApp.fileBelongings.file">File</Translate>
          </dt>
          <dd>{fileBelongingsEntity.file ? fileBelongingsEntity.file.id : ''}</dd>
          <dt>
            <Translate contentKey="danakApp.fileBelongings.version">Version</Translate>
          </dt>
          <dd>{fileBelongingsEntity.version ? fileBelongingsEntity.version.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/file-belongings" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/file-belongings/${fileBelongingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FileBelongingsDetail;
