import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './version.reducer';

export const VersionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const versionEntity = useAppSelector(state => state.version.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="versionDetailsHeading">
          <Translate contentKey="danakApp.version.detail.title">Version</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{versionEntity.id}</dd>
          <dt>
            <span id="version">
              <Translate contentKey="danakApp.version.version">Version</Translate>
            </span>
          </dt>
          <dd>{versionEntity.version}</dd>
          <dt>
            <span id="tag">
              <Translate contentKey="danakApp.version.tag">Tag</Translate>
            </span>
          </dt>
          <dd>{versionEntity.tag}</dd>
        </dl>
        <Button tag={Link} to="/version" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/version/${versionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VersionDetail;
