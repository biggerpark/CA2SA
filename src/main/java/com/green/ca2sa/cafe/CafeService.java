package com.green.ca2sa.cafe;

import com.green.ca2sa.cafe.model.*;
import com.green.ca2sa.common.MyFileUtils;
import com.green.ca2sa.common.PicUrlMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CafeService {
    private final CafeMapper cafeMapper;
    private final MyFileUtils myFileUtils;


    // 카페 회원 가입
    public int signUpCafe(MultipartFile pic, CafeSignUpReq p){
        String fileName = pic != null ? myFileUtils.makeRandomFileName(pic) : null;
        p.setCafePic(fileName);
        int res = cafeMapper.insCafe(p);
        long cafeId = p.getCafeId();
        String folderPath = String.format("cafe/%d", cafeId);
        myFileUtils.makeFolders(folderPath);
        if(pic == null){
            return res;
        }
        String filePath = String.format("%s/%s",folderPath,fileName);
        try {
            myFileUtils.transferTo(pic,filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    // 카페 정보 변경
    @Transactional
    public int updCafe(MultipartFile pic, CafePutReq p){
        String fileName = pic != null ? myFileUtils.makeRandomFileName(pic) : null;
        long cafeId = p.getCafeId();
        String folderPath = String.format("cafe/%d",cafeId);
        myFileUtils.makeFolders(folderPath);
        p.setCafePic(fileName);

        String deletePath = String.format("cafe/%d",cafeId);
        myFileUtils.deleteFolder(deletePath,false);

        if(p.getCafePic() == null){
            int res = cafeMapper.updCafe(p);
            return res;
        }

        String filePath = String.format("%s/%s",folderPath,fileName);
        try{
            myFileUtils.transferTo(pic,filePath);
        }catch(IOException e){
            e.printStackTrace();
        }
        int result = cafeMapper.updCafe(p);

        return result;
    }


    // 카페 판매액 조회
    public CafeGetSalesRes selCafeSales(CafeGetSalesReq p){
        List<CafeGetSalesDto> cafeGetSalesDtoList = cafeMapper.selSumPriceWeekOfDay(p);
        int weekSum = 0;
        for(CafeGetSalesDto c : cafeGetSalesDtoList){
            weekSum += c.getDaySumPrice();
        }
        CafeGetSalesRes res = new CafeGetSalesRes();
        res.setWeekSumPrice(weekSum);
        res.setSelWeek(cafeGetSalesDtoList);
        return res;
    }

    // 카페 조회
    public CafeGetOneRes selCafe(CafeGetOneReq p){
        CafeGetOneRes res = cafeMapper.selCafe(p);

        return res;
    }


    public List<CafeGetRes> selSearchCafe(CafeGetReq p){
        List<CafeGetRes> res;
        if(p.getSearchCafeName()!= null || p.getSearchMenuName()!= null){
            res = cafeMapper.searchCafe(p);
        } else {
            res = cafeMapper.selAllCafe(p);
        }
        return res;
    }

}
