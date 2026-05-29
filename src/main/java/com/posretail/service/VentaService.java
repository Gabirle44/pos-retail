package com.posretail.service;

import com.posretail.dto.LineaDTO;
import com.posretail.dto.VentaDTO;
import com.posretail.model.*;
import com.posretail.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class VentaService {

    private static final BigDecimal IVA_TASA = new BigDecimal("0.12"); // 12% Guatemala

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final ComprobanteVentaRepository comprobanteRepository;
    private final PagoRepository pagoRepository;
    private final KardexService kardexService;

    public VentaService(VentaRepository ventaRepository,
                        ClienteRepository clienteRepository,
                        ProductoRepository productoRepository,
                        ComprobanteVentaRepository comprobanteRepository,
                        PagoRepository pagoRepository,
                        KardexService kardexService) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.pagoRepository = pagoRepository;
        this.kardexService = kardexService;
    }

    @Transactional
    public Venta registrarVenta(VentaDTO dto, Usuario vendedor) {
        if (dto.getLineas() == null || dto.getLineas().isEmpty())
            throw new IllegalArgumentException("Debe agregar al menos un producto");

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no existe"));

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setUsuario(vendedor);
        venta.setTipoPago(dto.getTipoPago());
        venta.setFecha(LocalDateTime.now());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (LineaDTO l : dto.getLineas()) {
            Producto p = productoRepository.findById(l.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no existe"));

            if (p.getStockActual() < l.getCantidad())
                throw new IllegalStateException("Stock insuficiente de " + p.getNombre());

            BigDecimal precio = l.getPrecioUnitario() != null
                    ? l.getPrecioUnitario() : p.getPrecioVenta();
            BigDecimal sub = precio.multiply(BigDecimal.valueOf(l.getCantidad()))
                    .setScale(2, RoundingMode.HALF_UP);

            DetalleVenta dv = new DetalleVenta();
            dv.setVenta(venta);
            dv.setProducto(p);
            dv.setCantidad(l.getCantidad());
            dv.setPrecioUnitario(precio);
            dv.setSubtotal(sub);
            venta.getDetalles().add(dv);

            subtotal = subtotal.add(sub);
        }

        BigDecimal iva = subtotal.multiply(IVA_TASA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(iva).setScale(2, RoundingMode.HALF_UP);

        venta.setSubtotal(subtotal);
        venta.setIva(iva);
        venta.setTotal(total);

        venta = ventaRepository.save(venta);

        // Actualizar Kardex (salida)
        String ref = "VENTA-" + venta.getId();
        for (DetalleVenta dv : venta.getDetalles()) {
            kardexService.registrarMovimiento(dv.getProducto(), KardexService.SALIDA,
                    dv.getCantidad(), dv.getPrecioUnitario(), ref);
        }

        // Comprobante de venta
        ComprobanteVenta cv = new ComprobanteVenta();
        cv.setVenta(venta);
        cv.setSerie("A");
        cv.setNumero(String.format("%08d", venta.getId()));
        cv.setFechaEmision(LocalDateTime.now());
        cv.setTipo("FACTURA");
        comprobanteRepository.save(cv);

        // Comprobante de pago (solo si es al contado se considera pagado en el acto)
        if ("CONTADO".equalsIgnoreCase(dto.getTipoPago())) {
            Pago pago = new Pago();
            pago.setVenta(venta);
            pago.setMonto(total);
            pago.setMetodo(dto.getMetodoPago() != null ? dto.getMetodoPago() : "EFECTIVO");
            pago.setReferencia("PAGO-" + venta.getId());
            pagoRepository.save(pago);
        }

        return venta;
    }
}
