package kr.kro.namohagae.mall.controller;


import jakarta.servlet.http.HttpSession;
import kr.kro.namohagae.global.security.MyUserDetails;
import kr.kro.namohagae.mall.dto.AddressDto;
import kr.kro.namohagae.mall.dto.ProductOrderDto;
import kr.kro.namohagae.mall.service.KakaoPayService;
import kr.kro.namohagae.mall.service.ProductOrderService;
import kr.kro.namohagae.mall.vo.KakaoPayApprovalVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ProductOrderController {
    private final ProductOrderService service;
    private final KakaoPayService kakaoPayService;

    @PostMapping("/mall/order")
    public String order(HttpSession session, @RequestParam(value = "checkedProductNos", required = false) List<Integer> checkedProductNos,
                        @RequestParam(value = "productOrderDetailCount", required = false, defaultValue = "1") Integer productOrderDetailCount) {
        if (checkedProductNos != null) {
            session.setAttribute("checkedProductNos", checkedProductNos);
            session.setAttribute("productOrderDetailCount", productOrderDetailCount);
        } else {
            return "redirect:/mall/main";
        }
        return "redirect:/mall/order/ready";
    }

    @GetMapping("/mall/order/ready")
    public ModelAndView orderList(HttpSession session, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        List<Integer> checkedProductNos = (List<Integer>)session.getAttribute("checkedProductNos");
        Integer productOrderDetailCount = (Integer)session.getAttribute("productOrderDetailCount");
        ProductOrderDto.Read order;
        if (checkedProductNos != null) {
            order = service.orderReady(myUserDetails.getMemberNo(), checkedProductNos, productOrderDetailCount);
        } else {
            return new ModelAndView("redirect:/mall/main");
        }
        List<AddressDto.Read> addresses = service.findAddress(myUserDetails.getMemberNo());
        Map<String, Object> map = new HashMap<>();
        map.put("orderItems", order.getOrderItems());
        map.put("memberPoint", order.getMemberPoint());
        map.put("orderTotalPrice", order.getOrderTotalPrice());
        map.put("addresses", addresses);
        session.setAttribute("map",map);
        return new ModelAndView("mall/order/ready").addObject("map", map);
    }

    @GetMapping("/mall/order/success")
    public String orderSuccess(@RequestParam("pg_token") String pgToken, HttpSession session, Model model) {
        KakaoPayApprovalVO res = kakaoPayService.kakaoPayApprove(pgToken, session);
        session.removeAttribute("partner_order_id");
        session.removeAttribute("tid");
        session.removeAttribute("checkedProductNos");
        session.removeAttribute("productOrderDetailCount");

        Object orderNoObj = session.getAttribute("orderNo");
        if (orderNoObj instanceof Integer) {
            Integer orderNo = (Integer) orderNoObj;
            ProductOrderDto.OrderResult order = service.findById(orderNo);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String productOrderDate = order.getProductOrderDate().format(formatter);

            model.addAttribute("order", order);
            model.addAttribute("productOrderDate", productOrderDate);
        }
        session.removeAttribute("orderNo");

        return "mall/order/success";
    }


    // 주문 목록 보기
    @GetMapping(value = "/mall/order/list")
    public void orderList(Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        model.addAttribute("orders", service.orderList(myUserDetails.getMemberNo()));
    }

    @GetMapping("/member/order/list")
    public ResponseEntity<?> myOrderList(@AuthenticationPrincipal MyUserDetails myUserDetails,Integer pageno){
        Integer memberNo = myUserDetails.getMemberNo();
        return ResponseEntity.ok(service.listMyOrder(pageno,memberNo));
    }

    @GetMapping("/pay/cancel")
    public String cancel() {
        return "mall/pay/cancel";
    }

    @GetMapping("/pay/fail")
    public String fail() {
        return "mall/pay/fail";
    }
}
