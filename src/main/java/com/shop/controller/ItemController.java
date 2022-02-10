package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor            //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){

        model.addAttribute("itemFormDto", new ItemFormDto());       //상품 등록 페이지로 접근할 수 있도록 / ItemFormDto를 model 객체에 담아서 뷰로 전달
        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){

        if (bindingResult.hasErrors()){         //상품 등록 시 필수 값이 없다면 다시 상품 등록 페이지로 전환한다.
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){       //상품 등록 시 첫 번째 이미지가 없다면 에러메시지+ 상품등록 페이지 전환
                                                                                    // (상품 첫번째 이미지는 메인에서 보여줄 상품 이미지로 사용하기 위해 필수값 지정 )
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);                 //상품 저장 로직 호출 (매개 변수로 상품 정보와 상품 이미지 정보를 담고 있는 itemImgFileList를 넘겨준다.)
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";            //상품이 정상적으로 등록되었다면 메인으로 이동

    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){        //@PathVariable => url에서 각 구분자에 들어오는 값을 처리해야 할 때 사용

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);           //조회한 상품 데이터를 모델에 담아서 뷰로 전달한다.
            model.addAttribute("itemFormDto",itemFormDto);
        }catch (EntityNotFoundException e){                                     //상품 엔티티가 존재하지 않을 경우 에러메시지를 담아서 상품 등록 페이지로 이동한다.
            model.addAttribute("errorMessage","존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }
}
