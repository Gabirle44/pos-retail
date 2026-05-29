package com.posretail.controller;

import com.posretail.dto.CompraDTO;
import com.posretail.dto.LineaDTO;
import com.posretail.model.Compra;
import com.posretail.repository.*;
import com.posretail.security.UsuarioPrincipal;
import com.posretail.service.CompraService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final ProductoRepository productoRepository;
    private final DevolucionCompraRepository devolucionRepository;

    public CompraController(CompraService compraService,
                            CompraRepository compraRepository,
                            ProveedorRepository proveedorRepository,
                            ProductoRepository productoRepository,
                            DevolucionCompraRepository devolucionRepository) {
        this.compraService = compraService;
        this.compraRepository = compraRepository;
        this.proveedorRepository = proveedorRepository;
        this.productoRepository = productoRepository;
        this.devolucionRepository = devolucionRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("compras", compraRepository.findAll());
        return "compras/lista";
    }

    @GetMapping("/nueva")
    public String formNueva(Model model) {
        model.addAttribute("proveedores", proveedorRepository.findAll());
        model.addAttribute("productos",
                productoRepository.findAll().stream()
                        .filter(p -> Boolean.TRUE.equals(p.getActivo())).toList());
        return "compras/nueva";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam Integer idProveedor,
                            @RequestParam String tipoPago,
                            @RequestParam List<Integer> productoId,
                            @RequestParam List<Integer> cantidad,
                            @RequestParam List<BigDecimal> precio,
                            @AuthenticationPrincipal UsuarioPrincipal principal,
                            Model model) {
        try {
            CompraDTO dto = new CompraDTO();
            dto.setIdProveedor(idProveedor);
            dto.setTipoPago(tipoPago);
            for (int i = 0; i < productoId.size(); i++) {
                if (cantidad.get(i) == null || cantidad.get(i) <= 0) continue;
                dto.getLineas().add(new LineaDTO(productoId.get(i), cantidad.get(i), precio.get(i)));
            }
            Compra c = compraService.registrarCompra(dto, principal.getUsuario());
            return "redirect:/compras/" + c.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("proveedores", proveedorRepository.findAll());
            model.addAttribute("productos", productoRepository.findAll());
            return "compras/nueva";
        }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        Compra c = compraRepository.findById(id).orElseThrow();
        model.addAttribute("compra", c);
        return "compras/detalle";
    }

    @GetMapping("/{id}/devolver")
    public String formDevolucion(@PathVariable Integer id, Model model) {
        Compra c = compraRepository.findById(id).orElseThrow();
        model.addAttribute("compra", c);
        return "compras/devolucion";
    }

    @PostMapping("/{id}/devolver")
    public String devolver(@PathVariable Integer id,
                           @RequestParam String motivo,
                           @RequestParam List<Integer> productoId,
                           @RequestParam List<Integer> cantidad,
                           @RequestParam List<BigDecimal> precio,
                           Model model) {
        try {
            List<LineaDTO> lineas = new java.util.ArrayList<>();
            for (int i = 0; i < productoId.size(); i++) {
                if (cantidad.get(i) == null || cantidad.get(i) <= 0) continue;
                lineas.add(new LineaDTO(productoId.get(i), cantidad.get(i), precio.get(i)));
            }
            compraService.registrarDevolucion(id, motivo, lineas);
            return "redirect:/compras/" + id;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            Compra c = compraRepository.findById(id).orElseThrow();
            model.addAttribute("compra", c);
            return "compras/devolucion";
        }
    }
}
