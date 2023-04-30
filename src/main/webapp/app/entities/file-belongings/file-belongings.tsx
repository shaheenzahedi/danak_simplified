import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFileBelongings } from 'app/shared/model/file-belongings.model';
import { getEntities } from './file-belongings.reducer';

export const FileBelongings = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const fileBelongingsList = useAppSelector(state => state.fileBelongings.entities);
  const loading = useAppSelector(state => state.fileBelongings.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="file-belongings-heading" data-cy="FileBelongingsHeading">
        <Translate contentKey="danakApp.fileBelongings.home.title">File Belongings</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="danakApp.fileBelongings.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/file-belongings/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="danakApp.fileBelongings.home.createLabel">Create new File Belongings</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {fileBelongingsList && fileBelongingsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="danakApp.fileBelongings.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.fileBelongings.file">File</Translate>
                </th>
                <th>
                  <Translate contentKey="danakApp.fileBelongings.version">Version</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fileBelongingsList.map((fileBelongings, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/file-belongings/${fileBelongings.id}`} color="link" size="sm">
                      {fileBelongings.id}
                    </Button>
                  </td>
                  <td>{fileBelongings.file ? <Link to={`/file/${fileBelongings.file.id}`}>{fileBelongings.file.id}</Link> : ''}</td>
                  <td>
                    {fileBelongings.version ? <Link to={`/version/${fileBelongings.version.id}`}>{fileBelongings.version.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/file-belongings/${fileBelongings.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/file-belongings/${fileBelongings.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/file-belongings/${fileBelongings.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="danakApp.fileBelongings.home.notFound">No File Belongings found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FileBelongings;
