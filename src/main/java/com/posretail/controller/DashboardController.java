package com.posretail.controller;

import com.posretail.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final VentaRepository ventaRepository;
    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;

    public DashboardController(VentaRepository ventaRepository,
                               CompraRepository compraRepository,
                               ProductoRepository productoRepository) {
        this.ventaRepository = ventaRepository;
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalVentas",   ventaRepository.count());
        model.addAttribute("totalCompras",  compraRepository.count());
        model.addAttribute("totalProductos", productoRepository.count());
        return "dashboard";
    }
}
