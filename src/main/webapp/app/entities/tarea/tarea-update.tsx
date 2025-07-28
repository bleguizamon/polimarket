import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getVendedors } from 'app/entities/vendedor/vendedor.reducer';
import { createEntity, getEntity, reset, updateEntity } from './tarea.reducer';

export const TareaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vendedors = useAppSelector(state => state.vendedor.entities);
  const tareaEntity = useAppSelector(state => state.tarea.entity);
  const loading = useAppSelector(state => state.tarea.loading);
  const updating = useAppSelector(state => state.tarea.updating);
  const updateSuccess = useAppSelector(state => state.tarea.updateSuccess);

  const handleClose = () => {
    navigate(`/tarea${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getVendedors({}));
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

    const entity = {
      ...tareaEntity,
      ...values,
      vendedor: vendedors.find(it => it.id.toString() === values.vendedor?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...tareaEntity,
          vendedor: tareaEntity?.vendedor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="polimarketApp.tarea.home.createOrEditLabel" data-cy="TareaCreateUpdateHeading">
            <Translate contentKey="polimarketApp.tarea.home.createOrEditLabel">Create or edit a Tarea</Translate>
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
                  id="tarea-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('polimarketApp.tarea.descripcion')}
                id="tarea-descripcion"
                name="descripcion"
                data-cy="descripcion"
                type="text"
              />
              <ValidatedField
                label={translate('polimarketApp.tarea.estado')}
                id="tarea-estado"
                name="estado"
                data-cy="estado"
                type="text"
              />
              <ValidatedField
                id="tarea-vendedor"
                name="vendedor"
                data-cy="vendedor"
                label={translate('polimarketApp.tarea.vendedor')}
                type="select"
              >
                <option value="" key="0" />
                {vendedors
                  ? vendedors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tarea" replace color="info">
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

export default TareaUpdate;
