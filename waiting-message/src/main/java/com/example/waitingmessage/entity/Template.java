package com.example.waitingmessage.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum Template {
    WAITING(
            "WA1",
            "[{0}] \n" +
                    "안녕하세요 고객님! \n" +
                    "고객님의 웨이팅이 접수되었습니다. \n" +
                    "\n" +
                    "- 웨이팅 번호 : {1} \n" +
                    "- 예약인원 : {2} \n" +
                    "- 내 앞 웨이팅 : {3} \n" +
                    "- 웨이팅 예상시간 : {4} \n" +
                    "아래 버튼을 누르시면 실시간으로 웨이팅 현황을 확인하실 수 있습니다."),
    CALLING(
            "CA1",
            "[{0}] \n" +
                    "고객님, 입장하실 차례입니다. \n" +
                    "입장 후 직원에게 화면을 보여주세요. \n" +
                    "- 웨이팅 번호 : {1}"),
    CANCELED(
            "CA2",
            "[{0}] \n" +
                    "고객님의 웨이팅이 취소되었습니다. \n" +
                    "다음에 또 방문해주세요.")
    ;

    private String code;
    private String message;

    Template(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
