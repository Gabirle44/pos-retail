package com.posretail.controller;

import com.posretail.repository.KardexRepository;
import com.posretail.repository.ProductoRepository;
import com.posretail.repository.VentaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final KardexRepository kardexRepository;

    public ReporteController(VentaRepository ventaRepository,
                             ProductoRepository productoRepository,
                             KardexRepository kardexRepository) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.kardexRepository = kardexRepository;
    }

    @GetMapping("/ventas")
    public String reporteVentas(Model model) {
        model.addAttribute("ventas", ventaRepository.findAll());
        return "reportes/ventas";
    }

    @GetMapping("/stock")
    public String reporteStock(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        return "reportes/stock";
    }

    @GetMapping("/kardex")
    public String kardex(Model model) {
        model.addAttribute("movimientos", kardexRepository.findAll());
        return "reportes/kardex";
    }
}
