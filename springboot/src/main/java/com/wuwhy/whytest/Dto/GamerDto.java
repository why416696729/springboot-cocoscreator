package com.wuwhy.whytest.Dto;

import com.wuwhy.whytest.enums.Gamer;
import lombok.Data;

@Data
public class GamerDto {
    int myGamer;
    int nowGamer;
    int lastGamer;
    String tag;
    int winUser;
}
