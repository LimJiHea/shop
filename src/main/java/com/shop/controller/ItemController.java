package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

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

    //상품 수정
    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,           //@Valid=> 유효성검사
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){

        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);           //상품 수정 로직 호출
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})        //페이지 번호가 없는 경우 or 페이지 번호가 있는 경우 두가지 매핑
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0,3);

        Page<Item> items =
                itemService.getAdminItemPage(itemSearchDto, pageable);  //조회조건, 페이징 정보 파라미터로 넘김
        model.addAttribute("items",items);      //조회한 상품 데이터, 페이징 정보를 뷰에 전달
        model.addAttribute("itemSearchDto",itemSearchDto);      //페이지 전환시 기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 전달
        model.addAttribute("maxPage",5);    //메뉴 하단에 보여줄 페이지 최대 개수
        return "item/itemMng";
    }

    //상품 상세페이지 매핑
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }
}
