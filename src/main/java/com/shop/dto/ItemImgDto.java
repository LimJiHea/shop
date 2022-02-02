package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    //modelmapper 라이브러리 => 서로다른 클래스의 값을 필드의 이름과 자료형이 같으면 getter, setter를 통해 값을 복사해서 객체를 반환해준다.
    private static ModelMapper modelMapper = new ModelMapper();     //멤버 변수로 ModelMapper 변수를 추가

    public static ItemImgDto of(ItemImg itemImg){           //itemImg 엔티티 객체를 파라미터로 받아서 => ItemImg 객체의 자료형과 멤버변수의 이름이 같을 때 ItemImgDto로 값을 복사해서 반환한다.
                                                            //static으로 선언해 ItemImgDto 객체를 생성하지 않아도 호출할 수 있도록 함.
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
