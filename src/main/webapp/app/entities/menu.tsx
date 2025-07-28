import React from 'react';
import { Translate } from 'react-jhipster'; // eslint-disable-line

import MenuItem from 'app/shared/layout/menus/menu-item'; // eslint-disable-line

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/area-de-negocio">
        <Translate contentKey="global.menu.entities.areaDeNegocio" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/proveedor">
        <Translate contentKey="global.menu.entities.proveedor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/vendedor">
        <Translate contentKey="global.menu.entities.vendedor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cliente">
        <Translate contentKey="global.menu.entities.cliente" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/producto">
        <Translate contentKey="global.menu.entities.producto" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tarea">
        <Translate contentKey="global.menu.entities.tarea" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/entrega">
        <Translate contentKey="global.menu.entities.entrega" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/inventario">
        <Translate contentKey="global.menu.entities.inventario" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/movimiento-stock">
        <Translate contentKey="global.menu.entities.movimientoStock" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
