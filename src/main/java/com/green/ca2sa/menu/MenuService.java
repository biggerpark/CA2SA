package com.green.ca2sa.menu;

import com.green.ca2sa.common.MyFileUtils;
import com.green.ca2sa.menu.model.MenuDelReq;
import com.green.ca2sa.menu.model.MenuGetReq;
import com.green.ca2sa.menu.model.MenuGetRes;
import com.green.ca2sa.menu.model.MenuPostReq;
import com.green.ca2sa.menu.option.MenuOptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuMapper mapper;
    private final MenuOptionMapper optionMapper;
    private final MyFileUtils myFileUtils;

    @Transactional
    public int postMenu(MultipartFile pic, MenuPostReq p) {
        int result = mapper.insMenu(p);

        long menuId = p.getMenuId();

        String middlePath = String.format("/menu/%d", menuId);
        myFileUtils.makeFolders(middlePath);

        String savedPicName = myFileUtils.makeRandomFileName(pic);
        String filePath = String.format("%s/%s", middlePath, savedPicName);
        try {
            myFileUtils.transferTo(pic, filePath);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<MenuGetRes> getMenuList(MenuGetReq p) {
        List<MenuGetRes> menuGetResList = mapper.selMenuList(p);


return null;
    }








    @Transactional
    public int deleteMenu(MenuDelReq p) {

        int res = optionMapper.deleteMenuOption(p.getMenuId());

        int result = mapper.deleteMenu(p);

        String deletePath = String.format("%s/menu/%d", myFileUtils.getUploadPath(), p.getMenuId());
        myFileUtils.deleteFolder(deletePath, true);

        return result;
    }
}