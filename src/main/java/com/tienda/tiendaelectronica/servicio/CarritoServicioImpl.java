/* src/main/java/com/tienda/tiendaelectronica/servicio/CarritoServicioImpl.java */
package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.excepciones.RecursoNoEncontradoException;
import com.tienda.tiendaelectronica.modelo.Carrito;
import com.tienda.tiendaelectronica.modelo.CarritoItem;
import com.tienda.tiendaelectronica.modelo.Pedido;
import com.tienda.tiendaelectronica.modelo.Producto;
import com.tienda.tiendaelectronica.modelo.Usuario;
import com.tienda.tiendaelectronica.repositorio.CarritoItemRepositorio;
import com.tienda.tiendaelectronica.repositorio.CarritoRepositorio;
import com.tienda.tiendaelectronica.repositorio.ProductoRepositorio;
import com.tienda.tiendaelectronica.repositorio.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarritoServicioImpl implements CarritoServicio {

    private final PedidoServicio pedidoServicio;
    private final CarritoRepositorio carritoRepositorio;
    private final CarritoItemRepositorio carritoItemRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final ProductoRepositorio productoRepositorio;

    @Override
    public Carrito obtenerCarritoAbiertoPorUsuario(Long usuarioId) {

        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Usuario no encontrado con id: " + usuarioId));

        Carrito carrito = carritoRepositorio
                .findByUsuario_IdAndEstado(usuarioId, "ABIERTO")
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    nuevo.setEstado("ABIERTO");
                    nuevo.setFechaCreacion(LocalDateTime.now());
                    nuevo.setFechaActualizacion(LocalDateTime.now());
                    return carritoRepositorio.save(nuevo);
                });

        // Inicializar colección LAZY antes de salir del @Transactional
        if (carrito.getItems() != null) {
            carrito.getItems().size();
        }

        return carrito;
    }

    @Override
    public Carrito agregarProducto(Long usuarioId, Long productoId, int cantidad) {

        Carrito carrito = obtenerCarritoAbiertoPorUsuario(usuarioId);

        Producto producto = productoRepositorio.findById(productoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Producto no encontrado con id: " + productoId));

        CarritoItem item = carritoItemRepositorio
                .findByCarrito_IdAndProducto_Id(carrito.getId(), productoId)
                .orElse(null);

        if (item == null) {
            item = new CarritoItem();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(cantidad);
            item.setPrecioUnitario(producto.getPrecio());
        } else {
            item.setCantidad(item.getCantidad() + cantidad);
        }

        carritoItemRepositorio.save(item);

        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepositorio.save(carrito);

        // Recargamos para asegurar que items está inicializado
        Carrito recargado = carritoRepositorio.findById(carrito.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado"));
        recargado.getItems().size();

        return recargado;
    }

@Override
public Carrito actualizarCantidad(Long usuarioId, Long productoId, int cantidad) {

    Carrito carrito = obtenerCarritoAbiertoPorUsuario(usuarioId);

    CarritoItem item = carritoItemRepositorio
            .findByCarrito_IdAndProducto_Id(carrito.getId(), productoId)
            .orElseThrow(() ->
                    new RecursoNoEncontradoException("Item no encontrado en el carrito"));

    item.setCantidad(cantidad);
    carritoItemRepositorio.save(item);

    carrito.setFechaActualizacion(LocalDateTime.now());
    carritoRepositorio.save(carrito);

    Carrito recargado = carritoRepositorio.findById(carrito.getId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado"));
    recargado.getItems().size();

    return recargado;
}


    @Override
    public Carrito vaciarCarrito(Long usuarioId) {

        Carrito carrito = obtenerCarritoAbiertoPorUsuario(usuarioId);

        List<CarritoItem> items = carritoItemRepositorio.findByCarrito_Id(carrito.getId());
        carritoItemRepositorio.deleteAll(items);

        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepositorio.save(carrito);

        Carrito recargado = carritoRepositorio.findById(carrito.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrito no encontrado"));
        recargado.getItems().size();
        return recargado;
    }

    @Override
    public Pedido comprar(Long usuarioId) {

        Carrito carrito = obtenerCarritoAbiertoPorUsuario(usuarioId);

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío, no se puede completar la compra.");
        }

        // 1) Verificar stock y descontar por cada item del carrito
        carrito.getItems().forEach(item -> {
            Producto producto = item.getProducto();
            if (producto == null) {
                return;
            }

            Integer stockActual = producto.getStock() != null ? producto.getStock() : 0;
            int cantidad = item.getCantidad() != null ? item.getCantidad() : 0;

            // Ignoramos cantidades no válidas
            if (cantidad <= 0) {
                return;
            }

            if (cantidad > stockActual) {
                throw new IllegalStateException(
                        "No hay stock suficiente para el producto: " + producto.getNombre()
                );
            }

            int nuevoStock = stockActual - cantidad;
            producto.setStock(nuevoStock);

            // Si se queda sin stock, lo marcamos como NO activo
            if (nuevoStock <= 0) {
                producto.setActivo(false);
            }

            productoRepositorio.save(producto);
        });

        // 2) Registrar el pedido a partir del carrito
        Pedido pedido = pedidoServicio.registrarDesdeCarrito(carrito);

        // 3) Marcar el carrito actual como COMPRADO
        carrito.setEstado("COMPRADO");
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepositorio.save(carrito);

        // 4) Crear un nuevo carrito ABIERTO para próximas compras
        Carrito nuevo = new Carrito();
        nuevo.setUsuario(carrito.getUsuario());
        nuevo.setEstado("ABIERTO");
        nuevo.setFechaCreacion(LocalDateTime.now());
        nuevo.setFechaActualizacion(LocalDateTime.now());
        carritoRepositorio.save(nuevo);

        // 5) Devolvemos el Pedido (el front puede mostrar Nº pedido)
        return pedido;
    }
}
