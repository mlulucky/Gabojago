# 🧑‍🤝‍🧑 GabojagoUser
> 여행 일정공유 웹앱서비스 

## 1. 제작 기간 & 참여 인원
- 2023년 3월 17일 ~ 5월 2일
- 팀 프로젝트(총 5명)
- 역할 - 팀원 (DB구조설계 (전체 팀원참여), 가보자고(여행정보) trip 파트 구현)

## 2. 프로젝트 개요
여행 일정을 실시간으로 계획하고 공유하며, 다양한 여행 정보와 상품을 제공하는 웹앱서비스
- 참고한 서비스 : 핫츠고 플랜, 트립어드바이저

## 3. 사용 기술
#### `Back-end`
- Java 17
- Spring Boot 3.0.5
- Gradle
- MySQL
- MyBatis
- Ajax
- Websocket
#### `Front-end`
- HTML5
- CSS3
- ThymeLeaf
- JavaScript
- BootStrap
#### `협업 & 개발 툴`
- IntelliJ
- github
- Figma
- diagrams.net
- ERD

## 4. ERD 설계
![가보자고 db](https://github.com/mlulucky/Gabojago/assets/117883588/ce0fa165-bf33-4279-b837-04a1d570eb2d)
![가보자고 trip](https://github.com/mlulucky/Gabojago/assets/117883588/73e5b94d-0890-4d2a-ad52-38aa86159fc0)

## 5. 주요 코드
[trip Controller](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/java/com/project/gabojago/gabojagouser/controller/trip)
[trip Dto](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/java/com/project/gabojago/gabojagouser/dto/trip)
[trip Mapper.java](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/java/com/project/gabojago/gabojagouser/mapper/trip)
[trip Service](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/java/com/project/gabojago/gabojagouser/service/trip)
[trip Mapper.xml](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/resources/mapper/trip)
[trip.html](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/resources/templates/trip)

## 6. 어려웠던 점 & 해결
### 6.1. 메인이미지 등록
- 파트너 등급 유저가 여행정보 게시글을 등록할때, 화면에 메인으로 보여지는 이미지를 설정할수 있게 하고자 했습니다.
- 강사님께서 하나의 input 태그로는 등록되는 이미지의 순서가 정해져 있지 않기 때문에 수업에서 배운 내용으로 구현하기에는 조금 어렵다는 조언를 해주셔서 메인이미지를 등록하는 input 태그를 별도로 만들었습니다.
- 메인 이미지와 서브 이미지의 구분은 true,false 의 상태로 주고자 했는데, 2개의 input 으로 등록된 이미지를 하나의 배열로 메인 이미지만을 true 상태를 주는 로직이 개인적으로 많이 어려워서 강사님께 도움을 받았습니다.
- 메인이미지 설정은 컨트롤러에서 기존 이미지 배열을 이미지의 개수만큼 반복문을 돌리고, 인덱스가 마지막인 이미지의 상태를 true 로 바꾸고, 배열에 이미지를 추가하는 방식으로 처리할 수 있었습니다.

<details>
  <summary>코드</summary> 
  <div>
    List<TripImgDto> imgDtos=null;
        if (imgs != null) {
            imgDtos = new ArrayList<>();
            for (int i=0; i<imgs.size(); i++) {
                MultipartFile img=imgs.get(i);
                if (!img.isEmpty()) {
                    String[] contentTypes = img.getContentType().split("/");
                    if (contentTypes[0].equals("image")) {
                        String fileName = System.currentTimeMillis() + "_" + (int) (Math.random() * 10000) + "." + contentTypes[1];
                        Path path = Paths.get(staticPath + "/public/img/trip/" + fileName);
                        try {
                            img.transferTo(path);
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                        TripImgDto imgDto = new TripImgDto();
                        if(i==imgs.size()-1)imgDto.setImgMain(true); // 인덱스 마지막일때 이미지 => 메인 이미지
                        imgDto.setImgPath("/public/img/trip/" + fileName);
                        imgDtos.add(imgDto);
                    }
                }
            }
        }
        trip.setImgs(imgDtos);
  
  </div>
</details>



### 6.2. 메인이미지 수정
- 마찬가지로 이미지가 있는 조건에서 이미지가 없는 경우 배열을 만드는 등 중첩되는 if 조건문과 로직을 생각하는 부분에서 어려웠고 강사님께 도움을 받아 처리할 수 있었습니다. 

### 6.3. 이미지 실제파일 & db 저장
- 컨트롤러에서 이미지를 등록, 수정, 삭제하는 로직이 어려웠는데 그 이유는 컨트롤러에서 등록하는 코드와 삭제하는 코드를 같이 적거나, 등록이 성공했는데 다시 삭제하는 코드를 적거나 또는 이미지 삭제할때는 서비스에서 detail 정보를 가져오와서 getImgs

### 6.2. 리뷰작성하기 모달창 



### 6.4. 좋아요 등록


## 6. 회고

