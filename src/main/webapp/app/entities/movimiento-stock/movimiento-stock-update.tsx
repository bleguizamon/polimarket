import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getProductos } from 'app/entities/producto/producto.reducer';
import { getEntities as getInventarios } from 'app/entities/inventario/inventario.reducer';
import { createEntity, getEntity, reset, updateEntity } from './movimiento-stock.reducer';

export const MovimientoStockUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productos = useAppSelector(state => state.producto.entities);
  const inventarios = useAppSelector(state => state.inventario.entities);
  const movimientoStockEntity = useAppSelector(state => state.movimientoStock.entity);
  const loading = useAppSelector(state => state.movimientoStock.loading);
  const updating = useAppSelector(state => state.movimientoStock.updating);
  const updateSuccess = useAppSelector(state => state.movimientoStock.updateSuccess);

  const handleClose = () => {
    navigate(`/movimiento-stock${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProductos({}));
    dispatch(getInventarios({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.fecha = convertDateTimeToServer(values.fecha);
    if (values.cantidad !== undefined && typeof values.cantidad !== 'number') {
      values.cantidad = Number(values.cantidad);
    }

    const entity = {
      ...movimientoStockEntity,
      ...values,
      producto: productos.find(it => it.id.toString() === values.producto?.toString()),
      inventario: inventarios.find(it => it.id.toString() === values.inventario?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fecha: displayDefaultDateTime(),
        }
      : {
          ...movimientoStockEntity,
          fecha: convertDateTimeFromServer(movimientoStockEntity.fecha),
          producto: movimientoStockEntity?.producto?.id,
          inventario: movimientoStockEntity?.inventario?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="polimarketApp.movimientoStock.home.createOrEditLabel" data-cy="MovimientoStockCreateUpdateHeading">
            <Translate contentKey="polimarketApp.movimientoStock.home.createOrEditLabel">Create or edit a MovimientoStock</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="movimiento-stock-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('polimarketApp.movimientoStock.fecha')}
                id="movimiento-stock-fecha"
                name="fecha"
                data-cy="fecha"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('polimarketApp.movimientoStock.tipo')}
                id="movimiento-stock-tipo"
                name="tipo"
                data-cy="tipo"
                type="text"
              />
              <ValidatedField
                label={translate('polimarketApp.movimientoStock.cantidad')}
                id="movimiento-stock-cantidad"
                name="cantidad"
                data-cy="cantidad"
                type="text"
              />
              <ValidatedField
                label={translate('polimarketApp.movimientoStock.referencia')}
                id="movimiento-stock-referencia"
                name="referencia"
                data-cy="referencia"
                type="text"
              />
              <ValidatedField
                id="movimiento-stock-producto"
                name="producto"
                data-cy="producto"
                label={translate('polimarketApp.movimientoStock.producto')}
                type="select"
              >
                <option value="" key="0" />
                {productos
                  ? productos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="movimiento-stock-inventario"
                name="inventario"
                data-cy="inventario"
                label={translate('polimarketApp.movimientoStock.inventario')}
                type="select"
              >
                <option value="" key="0" />
                {inventarios
                  ? inventarios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.cantidad}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/movimiento-stock" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MovimientoStockUpdate;
