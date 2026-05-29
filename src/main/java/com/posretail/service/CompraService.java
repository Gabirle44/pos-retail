package com.posretail.service;

import com.posretail.dto.CompraDTO;
import com.posretail.dto.LineaDTO;
import com.posretail.model.*;
import com.posretail.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class CompraService {

    private static final BigDecimal IVA_TASA = new BigDecimal("0.12");

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final DevolucionCompraRepository devolucionRepository;
    private final KardexService kardexService;

    public CompraService(CompraRepository compraRepository,
                         ProveedorRepository proveedorRepository,
                         ProductoRepository productoRepository,
                         DevolucionCompraRepository devolucionRepository,
                         KardexService kardexService) {
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.devolucionRepository = devolucionRepository;
        this.kardexService = kardexService;
    }

    @Transactional
    public Compra registrarCompra(CompraDTO dto, Usuario comprador) {
        if (dto.getLineas() == null || dto.getLineas().isEmpty())
            throw new IllegalArgumentException("Debe agregar al menos un producto");

        Proveedor prov = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no existe"));

        Compra compra = new Compra();
        compra.setProveedor(prov);
        compra.setUsuario(comprador);
        compra.setTipoPago(dto.getTipoPago());
        compra.setFecha(LocalDateTime.now());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (LineaDTO l : dto.getLineas()) {
            Producto p = productoRepository.findById(l.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

            BigDecimal precio = l.getPrecioUnitario() != null
                    ? l.getPrecioUnitario() : p.getPrecioCompra();
            BigDecimal sub = precio.multiply(BigDecimal.valueOf(l.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            DetalleCompra dc = new DetalleCompra();
            dc.setCompra(compra);
            dc.setProducto(p);
            dc.setCantidad(l.getCantidad());
            dc.setPrecioUnitario(precio);
            dc.setSubtotal(sub);
            compra.getDetalles().add(dc);

            // Actualizar costo del producto al último precio de compra
            p.setPrecioCompra(precio);
            productoRepository.save(p);

            subtotal = subtotal.add(sub);
        }

        BigDecimal iva = subtotal.multiply(IVA_TASA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(iva).setScale(2, RoundingMode.HALF_UP);
        compra.setSubtotal(subtotal);
        compra.setIva(iva);
        compra.setTotal(total);

        compra = compraRepository.save(compra);

        // Kardex entrada
        String ref = "COMPRA-" + compra.getId();
        for (DetalleCompra dc : compra.getDetalles()) {
            kardexService.registrarMovimiento(dc.getProducto(), KardexService.ENTRADA,
                    dc.getCantidad(), dc.getPrecioUnitario(), ref);
        }
        return compra;
    }

    @Transactional
    public DevolucionCompra registrarDevolucion(Integer idCompra, String motivo,
                                                 java.util.List<LineaDTO> lineas) {
        Compra compra = compraRepository.findById(idCompra)
                .orElseThrow(() -> new IllegalArgumentException("Compra no existe"));

        DevolucionCompra dev = new DevolucionCompra();
        dev.setCompra(compra);
        dev.setMotivo(motivo);

        BigDecimal total = BigDecimal.ZERO;
        for (LineaDTO l : lineas) {
            Producto p = productoRepository.findById(l.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

            DetalleDevolucion dd = new DetalleDevolucion();
            dd.setDevolucion(dev);
            dd.setProducto(p);
            dd.setCantidad(l.getCantidad());
            dd.setPrecioUnitario(l.getPrecioUnitario());
            dev.getDetalles().add(dd);

            total = total.add(l.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(l.getCantidad())));
        }
        dev.setTotal(total.setScale(2, RoundingMode.HALF_UP));
        dev = devolucionRepository.save(dev);

        // Kardex devolución (salida)
        String ref = "DEVOL-" + dev.getId();
        for (DetalleDevolucion dd : dev.getDetalles()) {
            kardexService.registrarMovimiento(dd.getProducto(), KardexService.DEV_COMP,
                    dd.getCantidad(), dd.getPrecioUnitario(), ref);
        }
        return dev;
    }
}
