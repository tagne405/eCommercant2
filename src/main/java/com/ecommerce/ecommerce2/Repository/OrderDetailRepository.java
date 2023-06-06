package com.ecommerce.ecommerce2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce2.Entity.OrderDetail;

import java.util.List;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{

//    List<OrderDetail> findOrderDetailByOrder_Id(int order_Id);
//    List<OrderDetail> find();
//    List<OrderDetail> findDistinctBy
}
