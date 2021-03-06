package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;           //동적 쿼리 생성을 위해 JAPQueryFactory 클래스 사용

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);            //JPAQueryFactory 의 생성자로 EntityManager 객체를 넣어준다.
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
        //상품 판매 상태 조건이 null(전체)일경우 null 리턴 => where 절에서 해당 조건은 무시된다.
        //null 이 아니라 판매중 or 품절 상태라면 해당 조건의 상품만 조회한다.
    }

    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();       //dateTime 에 값을 searchDateType 이전으로 설정

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        //상품명에 검색어를 포함하고 있는 상품 또는 상품 생성자의 아이디에 검색어를 포함하고 있는 상품을 조회하도록 조건값을 반환한다.

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<Item> content = queryFactory           //queryFactory 를 이용해서 쿼리 생성
                .selectFrom(QItem.item)     //상품 데이터를 조회하기 위해 item 지정
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),                  // ',' => and 조건으로 인식
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())       //데이터를 가지고올 시작 인덱스
                .limit(pageable.getPageSize())         //한 번에 가지고 올 최대 개수를 지정한다.
                .fetch();        // fetchResults 대신 List / fetch 사용한다.  // fetchCount도 같이 써야함


       long total = queryFactory.selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .fetchCount();

       //System.out.println("total>>>>"+total);

        return new PageImpl<>(content, pageable,total);        //조회한 데이터를 Page 클래스의 구현체인 PageImpl 객체로 반환한다.
    }


    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery)? null:QItem.item.itemNm.like("%"+searchQuery+"%");
        //검색어가 null이 아니면 상품명에 해당 검색어가 포함되는 상품을 조회하는 조건을 반환한다.
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){

        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(               //QMainItemDto 의 생성자에 반환할 값들을 넣어준다.
                                                        //@QueryProjection을 사용하면 DTO로 바로 조회가 가능하다.
                        item.id,
                        item.itemNm,
                        item.itemDetail,
                        itemImg.imgUrl,
                        item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))            //상품 이미지에 경우 대표상품 이미지만 불러온다.
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(new QMainItemDto(
                        item.id,
                        item.itemNm,
                        item.itemDetail,
                        itemImg.imgUrl,
                        item.price))
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchCount();
        return new PageImpl<>(content, pageable, total);
    }
}

