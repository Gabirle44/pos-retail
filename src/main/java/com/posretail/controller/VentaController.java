package com.posretail.controller;

import com.posretail.dto.LineaDTO;
import com.posretail.dto.VentaDTO;
import com.posretail.model.Usuario;
import com.posretail.model.Venta;
import com.posretail.repository.*;
import com.posretail.security.UsuarioPrincipal;
import com.posretail.service.VentaService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final ComprobanteVentaRepository comprobanteRepository;
    private final PagoRepository pagoRepository;

    public VentaController(VentaService ventaService,
                           VentaRepository ventaRepository,
                           ClienteRepository clienteRepository,
                           ProductoRepository productoRepository,
                           ComprobanteVentaRepository comprobanteRepository,
                           PagoRepository pagoRepository) {
        this.ventaService = ventaService;
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.pagoRepository = pagoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ventas", ventaRepository.findAll());
        return "ventas/lista";
    }

    @GetMapping("/nueva")
    public String formNueva(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("productos",
                productoRepository.findAll().stream()
                        .filter(p -> Boolean.TRUE.equals(p.getActivo()) && p.getStockActual() > 0)
                        .toList());
        return "ventas/nueva";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam Integer idCliente,
                            @RequestParam String tipoPago,
                            @RequestParam(required = false) String metodoPago,
                            @RequestParam List<Integer> productoId,
                            @RequestParam List<Integer> cantidad,
                            @RequestParam List<BigDecimal> precio,
                            @AuthenticationPrincipal UsuarioPrincipal principal,
                            Model model) {
        try {
            VentaDTO dto = new VentaDTO();
            dto.setIdCliente(idCliente);
            dto.setTipoPago(tipoPago);
            dto.setMetodoPago(metodoPago);
            for (int i = 0; i < productoId.size(); i++) {
                if (cantidad.get(i) == null || cantidad.get(i) <= 0) continue;
                dto.getLineas().add(new LineaDTO(productoId.get(i), cantidad.get(i), precio.get(i)));
            }
            Venta v = ventaService.registrarVenta(dto, principal.getUsuario());
            return "redirect:/ventas/" + v.getId() + "/comprobante";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clientes", clienteRepository.findAll());
            model.addAttribute("productos", productoRepository.findAll());
            return "ventas/nueva";
        }
    }

    @GetMapping("/{id}/comprobante")
    public String comprobante(@org.springframework.web.bind.annotation.PathVariable Integer id, Model model) {
        Venta v = ventaRepository.findById(id).orElseThrow();
        model.addAttribute("venta", v);
        model.addAttribute("comprobante",
                comprobanteRepository.findAll().stream()
                        .filter(c -> c.getVenta().getId().equals(id)).findFirst().orElse(null));
        model.addAttribute("pagos",
                pagoRepository.findAll().stream()
                        .filter(p -> p.getVenta().getId().equals(id)).toList());
        return "ventas/comprobante";
    }
}
