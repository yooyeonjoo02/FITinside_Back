package com.team2.fitinside.order.entity;

public enum OrderStatus {
    ORDERED("주문 완료"),
    SHIPPING("배송 중"),
    COMPLETED("배송 완료"),
    CANCELLED("주문 취소");

    private final String displayName;

    OrderStatus(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
