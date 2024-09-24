package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.OrderService;
import vn.hoidanit.laptopshop.service.ProductService;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/order")
    public String getOrderDetail(Model model, @RequestParam("page") Optional<String> pageOptional) {
        int page = 1;
        try {
            if (pageOptional.isPresent()) {
                page = Integer.parseInt(pageOptional.get());
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Order> ods = this.orderService.getAllOrders(pageable);
        List<Order> orders = ods.getContent();
        model.addAttribute("order", orders);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ods.getTotalPages());

        return "admin/order/show_order";
    }

    // detail_order
    @GetMapping("/admin/order/{id}")
    public String OrderDetail(Model model, @PathVariable long id) {
        Order order = this.orderService.fetchOrderById(id).get();
        model.addAttribute("order", order);
        model.addAttribute("id", id);
        model.addAttribute("orderDetail", order.getOrderDetails());

        return "admin/order/detail";
    }

    // update_order
    @GetMapping("/admin/order/update/{id}")
    public String getUpdate(Model model, @PathVariable long id) {
        Optional<Order> order = this.orderService.fetchOrderById(id);
        model.addAttribute("newOrder", order.get());
        return "admin/order/update";
    }

    // button update
    @PostMapping("/admin/order/update")
    public String handleUpdate(Model model, @ModelAttribute("newOrder") Order neworder) {
        Order currentOrder = this.orderService.fetchOrderById(neworder.getId()).get();
        if (currentOrder != null) {
            currentOrder.setStatus(neworder.getStatus());
            this.orderService.handleSaveOrder(currentOrder);
        }
        return "redirect:/admin/order";
    }

    // delete_order
    @GetMapping("/admin/order/delete/{id}")
    public String getdelete(Model model, @PathVariable long id) {
        model.addAttribute("newOrder", new Order());
        model.addAttribute("id", id);
        return "admin/order/delete";
    }

    // button delete
    @PostMapping("/admin/order/delete")
    public String handleDelete(Model model, @ModelAttribute("newOrder") Order order) {
        this.orderService.handleDeleteOrder(order.getId());
        return "redirect:/admin/order";
    }
}
