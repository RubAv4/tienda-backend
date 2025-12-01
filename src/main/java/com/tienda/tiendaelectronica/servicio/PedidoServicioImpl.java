/* PedidoServicioImpl.java */
package com.tienda.tiendaelectronica.servicio;

import com.tienda.tiendaelectronica.dto.ResumenVentaUsuarioDto;
import com.tienda.tiendaelectronica.modelo.Carrito;
import com.tienda.tiendaelectronica.modelo.CarritoItem;
import com.tienda.tiendaelectronica.modelo.Pedido;
import com.tienda.tiendaelectronica.modelo.PedidoItem;
import com.tienda.tiendaelectronica.repositorio.PedidoRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServicioImpl implements PedidoServicio {

    private final PedidoRepositorio pedidoRepositorio;

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepositorio.findAllByOrderByFechaDesc();
    }

    @Override
    public List<Pedido> listarPorUsuario(Long usuarioId) {
        return pedidoRepositorio.findByUsuario_IdOrderByFechaDesc(usuarioId);
    }

    @Override
    public Pedido registrarDesdeCarrito(Carrito carrito) {

        Pedido pedido = new Pedido();
        pedido.setUsuario(carrito.getUsuario());
        pedido.setFecha(LocalDateTime.now());

        double total = 0.0;

        for (CarritoItem item : carrito.getItems()) {
            PedidoItem pi = new PedidoItem();
            pi.setPedido(pedido);
            pi.setProducto(item.getProducto());
            pi.setCantidad(item.getCantidad());

            BigDecimal precioUnitario = item.getPrecioUnitario();
            pi.setPrecioUnitario(precioUnitario);

            pedido.getItems().add(pi);

            total += item.getCantidad() * precioUnitario.doubleValue();
        }

        pedido.setTotal(BigDecimal.valueOf(total));

        return pedidoRepositorio.save(pedido);
    }

    @Override
    public List<ResumenVentaUsuarioDto> resumenVentasPorUsuario() {

        List<Pedido> pedidos = pedidoRepositorio.findAll();

        Map<Long, ResumenVentaUsuarioDto> mapa = new LinkedHashMap<>();

        for (Pedido p : pedidos) {
            if (p.getUsuario() == null)
                continue;

            Long usuarioId = p.getUsuario().getId();
            String username = p.getUsuario().getUsername();

            ResumenVentaUsuarioDto dto = mapa.get(usuarioId);
            if (dto == null) {
                dto = new ResumenVentaUsuarioDto();
                dto.setUsuarioId(usuarioId);
                dto.setUsername(username);
                dto.setPedidos(0);
                dto.setTotal(0.0);
                mapa.put(usuarioId, dto);
            }

            dto.setPedidos(dto.getPedidos() + 1);

            double totalPedido = 0.0;
            if (p.getTotal() != null) {
                totalPedido = p.getTotal().doubleValue();
            }
            dto.setTotal(dto.getTotal() + totalPedido);
        }

        return new ArrayList<>(mapa.values());
    }
}
