import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vendedor.reducer';

export const VendedorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vendedorEntity = useAppSelector(state => state.vendedor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vendedorDetailsHeading">
          <Translate contentKey="polimarketApp.vendedor.detail.title">Vendedor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vendedorEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="polimarketApp.vendedor.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{vendedorEntity.nombre}</dd>
          <dt>
            <span id="autorizado">
              <Translate contentKey="polimarketApp.vendedor.autorizado">Autorizado</Translate>
            </span>
          </dt>
          <dd>{vendedorEntity.autorizado ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="polimarketApp.vendedor.areaDeNegocio">Area De Negocio</Translate>
          </dt>
          <dd>{vendedorEntity.areaDeNegocio ? vendedorEntity.areaDeNegocio.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/vendedor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vendedor/${vendedorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VendedorDetail;
