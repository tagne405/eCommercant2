package com.ecommerce.ecommerce2.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.ecommerce.ecommerce2.Entity.*;
import com.ecommerce.ecommerce2.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public OrderService(OrderDetailRepository orderDetailRepository, ShoppingCartRepository shoppingCartRepository, OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }


    public void saveOrder(ShoppingCart Cart){
        Order order = new Order();
        order.setStatusOrdre("EN ATTENTE");
        order.setOrderDate(new Date());
        order.setCustomer(Cart.getCustomer());
        order.setPrixTotal(Cart.getPrixTotal());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(CartItem item : Cart.getCartItem()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setQuantite(item.getQuantite());
            orderDetail.setProduit(item.getProduit());
            orderDetail.setPrixUnitaire(item.getProduit().getPrix());
            orderDetailRepository.save(orderDetail);
            orderDetailList.add(orderDetail);
            cartItemRepository.delete(item);
        }

        order.setOrderDetail(orderDetailList);
        Cart.setCartItem(new HashSet<>());
        Cart.setTotalItems(0);
        Cart.setPrixTotal(0);
        shoppingCartRepository.save(Cart);
        orderRepository.save(order);
    }

    public void acceptOrder(Long id){
        Order order = orderRepository.findById(id).orElse(null);
        assert order != null;
        order.setDateLivraison(new Date());
        order.setStatusOrdre("EXPEDITION");
        orderRepository.save(order);
    }

    public void cancelOrder(Long id){
        orderRepository.deleteById(id);
    }

    /*find the order of customer by the admin*/
    public List<OrderDetail> findOrder(int order_id){
        List<Customer> customer = customerRepository.findAll();
//        for(Customer customer2 : customer){
//            customer2.getUsername();
//            Customer lo = customerRepository.findByUsername(String.valueOf(customer2));
//            List<Order> detail = orderRepository.findOrderByCustomer(String.valueOf(lo));
//        }

        List<OrderDetail> orderDetails = orderDetailRepository.findOrderDetailByOrder_Id(order_id);

        return orderDetails;

    }

    public List<Order> findAllOrder(){
        return orderRepository.findAll();
    }


//    public List<Order> findOrderByUsername(){
//        List<Order> detail = orderRepository.findOrderByCustomer();
//        return detail;
//    }
}
