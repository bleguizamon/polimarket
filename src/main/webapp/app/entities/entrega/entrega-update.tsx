import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTareas } from 'app/entities/tarea/tarea.reducer';
import { getEntities as getClientes } from 'app/entities/cliente/cliente.reducer';
import { getEntities as getProductos } from 'app/entities/producto/producto.reducer';
import { createEntity, getEntity, reset, updateEntity } from './entrega.reducer';

export const EntregaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const tareas = useAppSelector(state => state.tarea.entities);
  const clientes = useAppSelector(state => state.cliente.entities);
  const productos = useAppSelector(state => state.producto.entities);
  const entregaEntity = useAppSelector(state => state.entrega.entity);
  const loading = useAppSelector(state => state.entrega.loading);
  const updating = useAppSelector(state => state.entrega.updating);
  const updateSuccess = useAppSelector(state => state.entrega.updateSuccess);

  const handleClose = () => {
    navigate(`/entrega${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTareas({}));
    dispatch(getClientes({}));
    dispatch(getProductos({}));
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

    const entity = {
      ...entregaEntity,
      ...values,
      tarea: tareas.find(it => it.id.toString() === values.tarea?.toString()),
      cliente: clientes.find(it => it.id.toString() === values.cliente?.toString()),
      producto: productos.find(it => it.id.toString() === values.producto?.toString()),
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
          ...entregaEntity,
          fecha: convertDateTimeFromServer(entregaEntity.fecha),
          tarea: entregaEntity?.tarea?.id,
          cliente: entregaEntity?.cliente?.id,
          producto: entregaEntity?.producto?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="polimarketApp.entrega.home.createOrEditLabel" data-cy="EntregaCreateUpdateHeading">
            <Translate contentKey="polimarketApp.entrega.home.createOrEditLabel">Create or edit a Entrega</Translate>
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
                  id="entrega-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('polimarketApp.entrega.fecha')}
                id="entrega-fecha"
                name="fecha"
                data-cy="fecha"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('polimarketApp.entrega.estado')}
                id="entrega-estado"
                name="estado"
                data-cy="estado"
                type="text"
              />
              <ValidatedField
                id="entrega-tarea"
                name="tarea"
                data-cy="tarea"
                label={translate('polimarketApp.entrega.tarea')}
                type="select"
              >
                <option value="" key="0" />
                {tareas
                  ? tareas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.descripcion}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="entrega-cliente"
                name="cliente"
                data-cy="cliente"
                label={translate('polimarketApp.entrega.cliente')}
                type="select"
              >
                <option value="" key="0" />
                {clientes
                  ? clientes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="entrega-producto"
                name="producto"
                data-cy="producto"
                label={translate('polimarketApp.entrega.producto')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/entrega" replace color="info">
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

export default EntregaUpdate;
