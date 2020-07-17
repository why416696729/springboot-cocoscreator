package com.wuwhy.whytest.service;


import com.wuwhy.whytest.Dto.GamerDto;
import com.wuwhy.whytest.enums.Gamer;
import com.wuwhy.whytest.utils.ResultMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WuZiQiService {
    static List<Integer> qiziList = new ArrayList<Integer>();

    static Map<Integer,List<String>> GamerMap = new HashMap<>();

    static Gamer gamer = Gamer.wait;

    public ResultMap checkWin(String tag){
        int idx = Integer.parseInt(tag);
        int x0 = idx % 15;
        int y0 = idx / 15;
        GamerDto gamerDto = new GamerDto();
        ResultMap resultMap = new ResultMap();
        qiziList.set(y0*15+x0,gamer.ordinal());
        //判断横向
        int fiveCount = 0;
        for(int x = 0;x < 15;x++){
            if((qiziList.get(y0*15+x) == gamer.ordinal())){
                fiveCount++;
                if(fiveCount==5){
                    if(gamer == Gamer.white){
                        resultMap.message( "白棋胜");
                    }else{
                        resultMap.message( "黑棋胜");
                    }
                    gamerDto.setLastGamer(gamer.ordinal());
                    gamerDto.setNowGamer(gamer.ordinal());
                    gamerDto.setWinUser(gamer.ordinal());
                    gamerDto.setTag(tag);
                    resultMap.data(gamerDto);
                    return resultMap;
                }
            }else{
                fiveCount=0;
            }
        }
        //判断纵向
        fiveCount = 0;
        for(int y = 0;y < 15;y++){
            if((qiziList.get(y*15+x0) == gamer.ordinal())){
                fiveCount++;
                if(fiveCount==5){
                    if(gamer == Gamer.white){
                        resultMap.message( "白棋胜");
                    }else{
                        resultMap.message( "黑棋胜");
                    }
                    gamerDto.setLastGamer(gamer.ordinal());
                    gamerDto.setNowGamer(gamer.ordinal());
                    gamerDto.setWinUser(gamer.ordinal());
                    gamerDto.setTag(tag);
                    resultMap.data(gamerDto);
                    return resultMap;
                }
            }else{
                fiveCount=0;
            }
        }
        //判断右上斜向
        int f = y0 - x0;
        fiveCount = 0;
        for(int x = 0;x < 15;x++){
            if(f+x < 0 || f+x > 14){
                continue;
            }
            if((qiziList.get((f+x)*15+x)== gamer.ordinal())){
                fiveCount++;
                if(fiveCount==5){
                    if(gamer == Gamer.white){
                        resultMap.message( "白棋胜");
                    }else{
                        resultMap.message( "黑棋胜");
                    }
                    gamerDto.setLastGamer(gamer.ordinal());
                    gamerDto.setNowGamer(gamer.ordinal());
                    gamerDto.setWinUser(gamer.ordinal());
                    gamerDto.setTag(tag);
                    resultMap.data(gamerDto);
                    return resultMap;
                }
            }else{
                fiveCount=0;
            }
        }
        //判断右下斜向
        f = y0 + x0;
        fiveCount = 0;
        for(int x = 0;x < 15;x++){
            if(f-x < 0 || f-x > 14){
                continue;
            }
            if((qiziList.get((f-x)*15+x) == gamer.ordinal())){
                fiveCount++;
                if(fiveCount==5){
                    if(gamer == Gamer.white){
                        resultMap.message( "白棋胜");
                    }else{
                        resultMap.message( "黑棋胜");
                    }
                    gamerDto.setLastGamer(gamer.ordinal());
                    gamerDto.setNowGamer(gamer.ordinal());
                    gamerDto.setWinUser(gamer.ordinal());
                    gamerDto.setTag(tag);
                    resultMap.data(gamerDto);
                    return resultMap;
                }
            }else{
                fiveCount=0;
            }
        }
        //没有输赢交换下子顺序
        if(gamer == Gamer.black){
            gamerDto.setLastGamer(Gamer.black.ordinal());
            gamer = Gamer.white;
        }else if(gamer == Gamer.white){
            gamerDto.setLastGamer(Gamer.white.ordinal());
            gamer = Gamer.black;
        }
        gamerDto.setTag(tag);
        gamerDto.setNowGamer(gamer.ordinal());
        resultMap.data(gamerDto);
        return resultMap;
    }

    public ResultMap gameOver(){
        ResultMap resultMap = new ResultMap();
        GamerDto gamerDto = new GamerDto();
        int win = gamer.ordinal();
        gamerDto.setWinUser(win);
        gamer = Gamer.over;
        gamerDto.setNowGamer(gamer.ordinal());
        resultMap.data(gamerDto);
        return resultMap;
    }

    public boolean addGame(String userid){
        if (GamerMap.size() > 0 && GamerMap.get(1)!=null){
            GamerMap.get(1).add(userid);
            if (GamerMap.get(1).size() == 2){
                gamer = Gamer.white;
                for (int i = 0; i < 225; i++) {
                    qiziList.add(0);
                }
                return true;
            }
        }else{
            List<String> s = new ArrayList<>();
            s.add(userid);
            GamerMap.put(1,s);
        }
        return false;
    }

    public void delGame(String userid){
        if (GamerMap.size() > 0 && GamerMap.get(1)!=null) {
            for (int i = 0; i < GamerMap.get(1).size(); i++) {
                if (GamerMap.get(1).get(i) == userid) {
                    GamerMap.get(1).remove(i);
                }
            }
            if (GamerMap.get(1).size() == 0) {
                GamerMap.clear();
            }
            reLoad();
        }
    }

    public void reLoad(){
        gamer = Gamer.wait;
        for (int i = 0; i < 225; i++) {
            qiziList.set(i,0);
        }
        GamerMap.clear();
    }

    public List<Integer> setGamer(){
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < GamerMap.get(1).size(); i++) {
            integers.add(i+1);
        }
        return integers;
    }
}
