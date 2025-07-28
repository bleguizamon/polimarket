import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './movimiento-stock.reducer';

export const MovimientoStock = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const movimientoStockList = useAppSelector(state => state.movimientoStock.entities);
  const loading = useAppSelector(state => state.movimientoStock.loading);
  const totalItems = useAppSelector(state => state.movimientoStock.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
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
  }, [pageLocation.search]);

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

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="movimiento-stock-heading" data-cy="MovimientoStockHeading">
        <Translate contentKey="polimarketApp.movimientoStock.home.title">Movimiento Stocks</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="polimarketApp.movimientoStock.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/movimiento-stock/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="polimarketApp.movimientoStock.home.createLabel">Create new Movimiento Stock</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {movimientoStockList && movimientoStockList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="polimarketApp.movimientoStock.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('fecha')}>
                  <Translate contentKey="polimarketApp.movimientoStock.fecha">Fecha</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fecha')} />
                </th>
                <th className="hand" onClick={sort('tipo')}>
                  <Translate contentKey="polimarketApp.movimientoStock.tipo">Tipo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tipo')} />
                </th>
                <th className="hand" onClick={sort('cantidad')}>
                  <Translate contentKey="polimarketApp.movimientoStock.cantidad">Cantidad</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cantidad')} />
                </th>
                <th className="hand" onClick={sort('referencia')}>
                  <Translate contentKey="polimarketApp.movimientoStock.referencia">Referencia</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('referencia')} />
                </th>
                <th>
                  <Translate contentKey="polimarketApp.movimientoStock.producto">Producto</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="polimarketApp.movimientoStock.inventario">Inventario</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {movimientoStockList.map((movimientoStock, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/movimiento-stock/${movimientoStock.id}`} color="link" size="sm">
                      {movimientoStock.id}
                    </Button>
                  </td>
                  <td>
                    {movimientoStock.fecha ? <TextFormat type="date" value={movimientoStock.fecha} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{movimientoStock.tipo}</td>
                  <td>{movimientoStock.cantidad}</td>
                  <td>{movimientoStock.referencia}</td>
                  <td>
                    {movimientoStock.producto ? (
                      <Link to={`/producto/${movimientoStock.producto.id}`}>{movimientoStock.producto.nombre}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {movimientoStock.inventario ? (
                      <Link to={`/inventario/${movimientoStock.inventario.id}`}>{movimientoStock.inventario.cantidad}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/movimiento-stock/${movimientoStock.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/movimiento-stock/${movimientoStock.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                        onClick={() =>
                          (window.location.href = `/movimiento-stock/${movimientoStock.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
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
              <Translate contentKey="polimarketApp.movimientoStock.home.notFound">No Movimiento Stocks found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={movimientoStockList && movimientoStockList.length > 0 ? '' : 'd-none'}>
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

export default MovimientoStock;
