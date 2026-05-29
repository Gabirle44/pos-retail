package com.posretail.service;

import com.posretail.model.Kardex;
import com.posretail.model.Producto;
import com.posretail.repository.KardexRepository;
import com.posretail.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class KardexService {

    public static final String ENTRADA  = "ENTRADA";
    public static final String SALIDA   = "SALIDA";
    public static final String DEV_COMP = "DEVOLUCION_COMPRA";

    private final KardexRepository kardexRepository;
    private final ProductoRepository productoRepository;

    public KardexService(KardexRepository kardexRepository, ProductoRepository productoRepository) {
        this.kardexRepository = kardexRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Registra un movimiento de Kardex y actualiza el stock del producto.
     * cantidad debe ser positiva. El signo lo determina el tipo de movimiento.
     */
    @Transactional
    public void registrarMovimiento(Producto producto, String tipo, int cantidad,
                                    BigDecimal costoUnitario, String referencia) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad inválida");

        int stockAnterior = producto.getStockActual();
        int stockNuevo;

        if (ENTRADA.equals(tipo)) {
            stockNuevo = stockAnterior + cantidad;
        } else if (SALIDA.equals(tipo) || DEV_COMP.equals(tipo)) {
            if (stockAnterior < cantidad)
                throw new IllegalStateException("Stock insuficiente para " + producto.getNombre());
            stockNuevo = stockAnterior - cantidad;
        } else {
            throw new IllegalArgumentException("Tipo de movimiento inválido: " + tipo);
        }

        Kardex k = new Kardex();
        k.setProducto(producto);
        k.setTipoMovimiento(tipo);
        k.setCantidad(cantidad);
        k.setCostoUnitario(costoUnitario);
        k.setStockAnterior(stockAnterior);
        k.setStockNuevo(stockNuevo);
        k.setReferencia(referencia);
        kardexRepository.save(k);

        producto.setStockActual(stockNuevo);
        productoRepository.save(producto);
    }
}
