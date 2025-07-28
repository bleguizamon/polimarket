import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './area-de-negocio.reducer';

export const AreaDeNegocioDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const areaDeNegocioEntity = useAppSelector(state => state.areaDeNegocio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="areaDeNegocioDetailsHeading">
          <Translate contentKey="polimarketApp.areaDeNegocio.detail.title">AreaDeNegocio</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{areaDeNegocioEntity.id}</dd>
          <dt>
            <span id="nombre">
              <Translate contentKey="polimarketApp.areaDeNegocio.nombre">Nombre</Translate>
            </span>
          </dt>
          <dd>{areaDeNegocioEntity.nombre}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="polimarketApp.areaDeNegocio.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{areaDeNegocioEntity.descripcion}</dd>
        </dl>
        <Button tag={Link} to="/area-de-negocio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/area-de-negocio/${areaDeNegocioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AreaDeNegocioDetail;
