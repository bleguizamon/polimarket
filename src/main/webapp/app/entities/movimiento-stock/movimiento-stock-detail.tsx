import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './movimiento-stock.reducer';

export const MovimientoStockDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const movimientoStockEntity = useAppSelector(state => state.movimientoStock.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="movimientoStockDetailsHeading">
          <Translate contentKey="polimarketApp.movimientoStock.detail.title">MovimientoStock</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{movimientoStockEntity.id}</dd>
          <dt>
            <span id="fecha">
              <Translate contentKey="polimarketApp.movimientoStock.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>
            {movimientoStockEntity.fecha ? <TextFormat value={movimientoStockEntity.fecha} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="tipo">
              <Translate contentKey="polimarketApp.movimientoStock.tipo">Tipo</Translate>
            </span>
          </dt>
          <dd>{movimientoStockEntity.tipo}</dd>
          <dt>
            <span id="cantidad">
              <Translate contentKey="polimarketApp.movimientoStock.cantidad">Cantidad</Translate>
            </span>
          </dt>
          <dd>{movimientoStockEntity.cantidad}</dd>
          <dt>
            <span id="referencia">
              <Translate contentKey="polimarketApp.movimientoStock.referencia">Referencia</Translate>
            </span>
          </dt>
          <dd>{movimientoStockEntity.referencia}</dd>
          <dt>
            <Translate contentKey="polimarketApp.movimientoStock.producto">Producto</Translate>
          </dt>
          <dd>{movimientoStockEntity.producto ? movimientoStockEntity.producto.nombre : ''}</dd>
          <dt>
            <Translate contentKey="polimarketApp.movimientoStock.inventario">Inventario</Translate>
          </dt>
          <dd>{movimientoStockEntity.inventario ? movimientoStockEntity.inventario.cantidad : ''}</dd>
        </dl>
        <Button tag={Link} to="/movimiento-stock" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/movimiento-stock/${movimientoStockEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MovimientoStockDetail;
