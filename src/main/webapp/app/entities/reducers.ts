import areaDeNegocio from 'app/entities/area-de-negocio/area-de-negocio.reducer';
import proveedor from 'app/entities/proveedor/proveedor.reducer';
import vendedor from 'app/entities/vendedor/vendedor.reducer';
import cliente from 'app/entities/cliente/cliente.reducer';
import producto from 'app/entities/producto/producto.reducer';
import tarea from 'app/entities/tarea/tarea.reducer';
import entrega from 'app/entities/entrega/entrega.reducer';
import inventario from 'app/entities/inventario/inventario.reducer';
import movimientoStock from 'app/entities/movimiento-stock/movimiento-stock.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  areaDeNegocio,
  proveedor,
  vendedor,
  cliente,
  producto,
  tarea,
  entrega,
  inventario,
  movimientoStock,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
