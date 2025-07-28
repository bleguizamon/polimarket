import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './inventario.reducer';

export const InventarioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const inventarioEntity = useAppSelector(state => state.inventario.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inventarioDetailsHeading">
          <Translate contentKey="polimarketApp.inventario.detail.title">Inventario</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{inventarioEntity.id}</dd>
          <dt>
            <span id="cantidad">
              <Translate contentKey="polimarketApp.inventario.cantidad">Cantidad</Translate>
            </span>
          </dt>
          <dd>{inventarioEntity.cantidad}</dd>
          <dt>
            <span id="ubicacion">
              <Translate contentKey="polimarketApp.inventario.ubicacion">Ubicacion</Translate>
            </span>
          </dt>
          <dd>{inventarioEntity.ubicacion}</dd>
          <dt>
            <Translate contentKey="polimarketApp.inventario.producto">Producto</Translate>
          </dt>
          <dd>{inventarioEntity.producto ? inventarioEntity.producto.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/inventario" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/inventario/${inventarioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InventarioDetail;
