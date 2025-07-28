import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './proveedor.reducer';

export const ProveedorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const proveedorEntity = useAppSelector(state => state.proveedor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="proveedorDetailsHeading">
          <Translate contentKey="polimarketApp.proveedor.detail.title">Proveedor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{proveedorEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="polimarketApp.proveedor.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{proveedorEntity.nombre}</dd>
          <dt>
            <span id="telefono">
              <Translate contentKey="polimarketApp.proveedor.telefono">Telefono</Translate>
            </span>
          </dt>
          <dd>{proveedorEntity.telefono}</dd>
        </dl>
        <Button tag={Link} to="/proveedor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/proveedor/${proveedorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProveedorDetail;
