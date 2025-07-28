import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getAreaDeNegocios } from 'app/entities/area-de-negocio/area-de-negocio.reducer';
import { createEntity, getEntity, reset, updateEntity } from './vendedor.reducer';

export const VendedorUpdate = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isAdmin = account && account.authorities && account.authorities.includes('ROLE_ADMIN');
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const areaDeNegocios = useAppSelector(state => state.areaDeNegocio.entities);
  const vendedorEntity = useAppSelector(state => state.vendedor.entity);
  const loading = useAppSelector(state => state.vendedor.loading);
  const updating = useAppSelector(state => state.vendedor.updating);
  const updateSuccess = useAppSelector(state => state.vendedor.updateSuccess);

  const handleClose = () => {
    navigate(`/vendedor${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAreaDeNegocios({}));
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
      ...vendedorEntity,
      ...values,
      areaDeNegocio: areaDeNegocios.find(it => it.id.toString() === values.areaDeNegocio?.toString()),
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
          ...vendedorEntity,
          areaDeNegocio: vendedorEntity?.areaDeNegocio?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="polimarketApp.vendedor.home.createOrEditLabel" data-cy="VendedorCreateUpdateHeading">
            <Translate contentKey="polimarketApp.vendedor.home.createOrEditLabel">Create or edit a Vendedor</Translate>
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
                  id="vendedor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('polimarketApp.vendedor.nombre')}
                id="vendedor-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('polimarketApp.vendedor.autorizado')}
                id="vendedor-autorizado"
                name="autorizado"
                data-cy="autorizado"
                check
                type="checkbox"
                readOnly={!isAdmin}
                disabled={!isAdmin}
              />
              <ValidatedField
                id="vendedor-areaDeNegocio"
                name="areaDeNegocio"
                data-cy="areaDeNegocio"
                label={translate('polimarketApp.vendedor.areaDeNegocio')}
                type="select"
              >
                <option value="" key="0" />
                {areaDeNegocios
                  ? areaDeNegocios.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nombre}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vendedor" replace color="info">
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

export default VendedorUpdate;
