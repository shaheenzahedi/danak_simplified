import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { getSortState, JhiItemCount, JhiPagination, TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './donor.reducer';

export const Donor = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const donorList = useAppSelector(state => state.donor.entities);
  const loading = useAppSelector(state => state.donor.loading);
  const totalItems = useAppSelector(state => state.donor.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <div>
      <h2 id="donor-heading" data-cy="DonorHeading">
        <Translate contentKey="danakApp.donor.home.title">Donors</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="danakApp.donor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/donor/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="danakApp.donor.home.createLabel">Create new Donor</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {donorList && donorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="danakApp.donor.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createTimeStamp')}>
                  <Translate contentKey="danakApp.donor.createTimeStamp">Create Time Stamp</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('updateTimeStamp')}>
                  <Translate contentKey="danakApp.donor.updateTimeStamp">Update Time Stamp</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="danakApp.donor.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('city')}>
                  <Translate contentKey="danakApp.donor.city">City</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('country')}>
                  <Translate contentKey="danakApp.donor.country">Country</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('nationalCode')}>
                  <Translate contentKey="danakApp.donor.nationalCode">National Code</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('educationType')}>
                  <Translate contentKey="danakApp.donor.educationType">Education Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('education')}>
                  <Translate contentKey="danakApp.donor.education">Education</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('occupation')}>
                  <Translate contentKey="danakApp.donor.occupation">Occupation</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('workPlace')}>
                  <Translate contentKey="danakApp.donor.workPlace">Work Place</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('workPlacePhone')}>
                  <Translate contentKey="danakApp.donor.workPlacePhone">Work Place Phone</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('archived')}>
                  <Translate contentKey="danakApp.donor.archived">Archived</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('otpPhoneCode')}>
                  <Translate contentKey="danakApp.donor.otpPhoneCode">Otp Phone Code</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('otpPhoneEnable')}>
                  <Translate contentKey="danakApp.donor.otpPhoneEnable">Otp Phone Enable</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('otpPhoneSentTimeStamp')}>
                  <Translate contentKey="danakApp.donor.otpPhoneSentTimeStamp">Otp Phone Sent Time Stamp</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('latitude')}>
                  <Translate contentKey="danakApp.donor.latitude">Latitude</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('longitude')}>
                  <Translate contentKey="danakApp.donor.longitude">Longitude</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('uid')}>
                  <Translate contentKey="danakApp.donor.uid">Uid</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="danakApp.donor.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="danakApp.donor.archivedBy">Archived By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="danakApp.donor.createdBy">Created By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="danakApp.donor.modifiedBy">Modified By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {donorList.map((donor, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/donor/${donor.id}`} color="link" size="sm">
                      {donor.id}
                    </Button>
                  </td>
                  <td>
                    {donor.createTimeStamp ? <TextFormat type="date" value={donor.createTimeStamp} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {donor.updateTimeStamp ? <TextFormat type="date" value={donor.updateTimeStamp} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{donor.name}</td>
                  <td>{donor.city}</td>
                  <td>{donor.country}</td>
                  <td>{donor.nationalCode}</td>
                  <td>
                    <Translate contentKey={`danakApp.EducationType.${donor.educationType}`} />
                  </td>
                  <td>{donor.education}</td>
                  <td>{donor.occupation}</td>
                  <td>{donor.workPlace}</td>
                  <td>{donor.workPlacePhone}</td>
                  <td>{donor.archived ? 'true' : 'false'}</td>
                  <td>{donor.otpPhoneCode}</td>
                  <td>{donor.otpPhoneEnable ? 'true' : 'false'}</td>
                  <td>
                    {donor.otpPhoneSentTimeStamp ? (
                      <TextFormat type="date" value={donor.otpPhoneSentTimeStamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{donor.latitude}</td>
                  <td>{donor.longitude}</td>
                  <td>{donor.uid}</td>
                  <td>{donor.user ? donor.user.id : ''}</td>
                  <td>{donor.archivedBy ? donor.archivedBy.id : ''}</td>
                  <td>{donor.createdBy ? donor.createdBy.id : ''}</td>
                  <td>{donor.modifiedBy ? donor.modifiedBy.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/donor/${donor.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/donor/${donor.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        to={`/donor/${donor.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="danakApp.donor.home.notFound">No Donors found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={donorList && donorList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Donor;
