import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './entrega.reducer';

export const EntregaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const entregaEntity = useAppSelector(state => state.entrega.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="entregaDetailsHeading">
          <Translate contentKey="polimarketApp.entrega.detail.title">Entrega</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{entregaEntity.id}</dd>
          <dt>
            <span id="fecha">
              <Translate contentKey="polimarketApp.entrega.fecha">Fecha</Translate>
            </span>
          </dt>
          <dd>{entregaEntity.fecha ? <TextFormat value={entregaEntity.fecha} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="polimarketApp.entrega.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{entregaEntity.estado}</dd>
          <dt>
            <Translate contentKey="polimarketApp.entrega.tarea">Tarea</Translate>
          </dt>
          <dd>{entregaEntity.tarea ? entregaEntity.tarea.descripcion : ''}</dd>
          <dt>
            <Translate contentKey="polimarketApp.entrega.cliente">Cliente</Translate>
          </dt>
          <dd>{entregaEntity.cliente ? entregaEntity.cliente.nombre : ''}</dd>
          <dt>
            <Translate contentKey="polimarketApp.entrega.producto">Producto</Translate>
          </dt>
          <dd>{entregaEntity.producto ? entregaEntity.producto.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/entrega" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entrega/${entregaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EntregaDetail;
