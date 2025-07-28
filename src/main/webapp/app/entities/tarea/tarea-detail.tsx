import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tarea.reducer';

export const TareaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tareaEntity = useAppSelector(state => state.tarea.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tareaDetailsHeading">
          <Translate contentKey="polimarketApp.tarea.detail.title">Tarea</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tareaEntity.id}</dd>
          <dt>
            <span id="descripcion">
              <Translate contentKey="polimarketApp.tarea.descripcion">Descripcion</Translate>
            </span>
          </dt>
          <dd>{tareaEntity.descripcion}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="polimarketApp.tarea.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{tareaEntity.estado}</dd>
          <dt>
            <Translate contentKey="polimarketApp.tarea.vendedor">Vendedor</Translate>
          </dt>
          <dd>{tareaEntity.vendedor ? tareaEntity.vendedor.nombre : ''}</dd>
        </dl>
        <Button tag={Link} to="/tarea" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tarea/${tareaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TareaDetail;
