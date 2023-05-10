# 📌 GabojagoUser
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
- [trip Controller](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/java/com/project/gabojago/gabojagouser/controller/trip)
- [trip Dto](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/java/com/project/gabojago/gabojagouser/dto/trip)
- [trip Mapper.java](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/java/com/project/gabojago/gabojagouser/mapper/trip)
- [trip Service](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/java/com/project/gabojago/gabojagouser/service/trip)
- [trip Mapper.xml](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/resources/mapper/trip)
- [trip.html](https://github.com/HINZOO/GabojagoUser/tree/trip/src/main/resources/templates/trip)

## 6. 어려웠던 점 & 해결
### 6.1. 메인이미지 등록
- 파트너 등급 유저가 여행정보 게시글을 등록할때, 화면에 메인으로 보여지는 이미지를 설정할수 있게 하고자 했다.
- 강사님께서 하나의 input 태그로는 등록되는 이미지의 순서가 정해져 있지 않기 때문에 수업에서 배운 내용으로 구현하기에는 조금 어렵다는 조언를 해주셔서 메인이미지를 등록하는 input 태그를 별도로 만들었다.
- 메인 이미지와 서브 이미지의 구분은 true,false 의 상태로 주고자 했는데, 2개의 input 으로 등록된 이미지를 하나의 배열로 메인 이미지만을 true 상태를 주는 로직이 개인적으로 많이 어려워서 강사님께 도움을 받았다.
- 메인이미지 설정은 컨트롤러에서 기존 이미지 배열을 이미지의 개수만큼 반복문을 돌리고, 인덱스가 마지막인 이미지의 상태를 true 로 바꾸고, 배열에 이미지를 추가하는 방식으로 처리할 수 있었다.

<details>
<summary>코드</summary> 
<div markdown="1">
    
~~~java
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

 ~~~
</div>
</details>

### 6.2. 메인이미지 수정
- 이미지 수정과 삭제를 위한 배열을 만드는 조건문 
- 메인이미지가 있고, 이미지의 인덱스가 마지막일때 이미지 상태를 true로 주는 등 중첩되는 if 조건문과 로직을 생각하는 부분에서 많이 어려웠고 
결국 강사님께 도움을 받아 처리할 수 있었다. 
    
<details>
<summary>코드</summary> 
<div markdown="1">

~~~java
 if(!mainImg.isEmpty()){ // 메인이미지가 있을때
            if(imgs==null)imgs=new ArrayList<>(); // 서브이미지가 없으면
            imgs.add(mainImg);
            if(delImgIds==null) {
                delImgIds=new ArrayList<>();
                delImgIds.add(delMainImgId);
            }
        }

        // 제목 입력 여부 확인
        if (trip.getTitle() == null || trip.getTitle().equals("")) {
            msg = "제목을 입력하세요.";
            redirectAttributes.addFlashAttribute("msg",msg);
            return redirectPage;
        }
        List<TripImgDto> imgDtos = null;
        if(imgs!=null){
            imgDtos=new ArrayList<>();
            for (int i=0; i<imgs.size(); i++) {
                MultipartFile img=imgs.get(i);
                if (!img.isEmpty()) {
                    String[] contentTypes = img.getContentType().split("/");
                    if (contentTypes[0].equals("image")) {
                        log.info(img.getOriginalFilename());
                        System.out.println("staticPath = " + staticPath);
                        System.out.println("img.getOriginalFilename() = " + img.getOriginalFilename());
                        String fileName = System.currentTimeMillis() + "_" + (int) (Math.random() * 10000) + "." + contentTypes[1];
                        Path path = Paths.get(staticPath + "/public/img/trip/" + fileName);
                        img.transferTo(path);
                        TripImgDto imgDto = new TripImgDto();
                        if(!mainImg.isEmpty() && i==imgs.size()-1)imgDto.setImgMain(true);
                        imgDto.setTId(trip.getTId());
                        imgDto.setImgPath("/public/img/trip/" + fileName);
                        imgDtos.add(imgDto); // 이미지는 마지막에 저장하기
                        log.info(imgDto);
                    }
                }
            }
        }
        trip.setImgs(imgDtos);
        List<TripImgDto> delImgDtos=null; // 삭제할 이미지 리스트
        int modify = 0;
        msg="등록실패";
        try {
            if (delImgIds != null) delImgDtos = tripService.imgList(delImgIds); // 삭제할 이미지아이디가 있으면 => 수정
            modify =  tripService.modify(trip,delImgIds,tags,delTags); // db 에서 삭제
        } catch (Exception e) {
            log.error(e.getMessage());
            msg+="에러:"+e.getMessage();
        }
        if (modify > 0) { // 수정성공
           if(delImgDtos!=null) { // 삭제할 이미지 있으면
                    for (TripImgDto ti : delImgDtos) {
                        File imgFile = new File(staticPath + ti.getImgPath());
                        if (imgFile.exists()) imgFile.delete(); // 실제 삭제
                    }
            }
            msg="수정성공";
            redirectPage = "redirect:/trip/list.do";
        }
        redirectAttributes.addFlashAttribute("msg",msg);
        return redirectPage;
    }

~~~
</div>
</details>

### 6.3. 이미지 실제파일 & db 저장
- 컨트롤러에서 이미지를 등록하는 코드와 삭제하는 코드를 같이 적어서 이미지가 등록이 안되는 경우, 등록이 성공했는데 다시 삭제하는 코드를 적어서 마찬가지로 등록이 안되는 경우, 이미지를 삭제할때 서비스에서 detail 정보를 가져와서 이미지를 getImgs 불러오지 않고 삭제를 하여 이미지가 삭제가 안되는 등 여러 코드오류가 있었다.
- 챗지피티에도 검색해보고 여러번 코드를 바꿔도 보다가 결국 강사님께 도움을 받았는데, 일부 코드의 순서 변경 및 코드의 추가로 문제가 해결되었다.
    
<details>
<summary>코드</summary> 
<div markdown="1">

~~~java
 List<TripImgDto> imgDtos=null;
        int remove=0;
        try{
            // 파라미터 tId 로 detail 정보를 db 에서 불러온다
            trip=tripService.detail(tId,loginUser);
            imgDtos=trip.getImgs();
            remove=tripService.remove(tId,imgDtos);
        }catch (Exception e){
            log.error(e);
        }
        if(remove>0){
            if(imgDtos!=null){
                for(TripImgDto ti : imgDtos){
                    File imgFile = new File(staticPath + ti.getImgPath());
                    System.out.println("ti.getImgPath() = " + ti.getImgPath());
                    if(imgFile.exists()) imgFile.delete();
                }
            }
            msg="삭제성공!";

~~~
</div>
</details>

### 6.4. 리뷰수정하기 모달창 
- 여행정보 상세페이지에는 리뷰작성과 리뷰수정을 모달창으로 구현하였는데, 리뷰를 수정하는 모달창이 열리지가 않는 문제가 발생
- 리뷰수정 모달창은 등록된 리뷰가 있는 경우에만 생성되기 때문에 추가적인 if 조건문이 필요하였다.
- 리뷰수정 모달창을 만들때 document 태그가 있을때 만들겠다라는 if 조건문 추가 
- 리뷰 삭제, 수정 폼 제출시 리뷰수정 모달창이 있다는 if 조건문 추가

<details>
<summary>코드</summary> 
<div markdown="1">

~~~javascript
    if(document.getElementById("reviewModifyModal")){
            reviewModifyModal=new bootstrap.Modal(document.getElementById("reviewModifyModal"),{})
        }
    
    if(reviewModifyModal){
        async function removeReview(){
            let c = confirm("삭제 하시겠습니까"); // 삭제 알림창 (확인 true / 취소 false)
            let url="/review/handler.do";
            let data=new FormData(reviewModifyForm);
            if(c) { // 삭제 확인버튼
                const resp=await fetch(url, {method:"DELETE", body:data});
                if(resp.status===200){
                    const json=await resp.json(); // json 객체
                    if(json.remove>0){
                        alert("삭제 성공");
                        await loadReviews(reviewModifyForm.tId.value);
                    } else{
                        alert("삭제 실패(삭제된 레코드)");
                    }
                } else { // 오류발생
                    alert("삭제실패 status : " + resp.status);
                }
            }
            reviewModifyModal.hide();
        }
            // 리뷰 삭제 버튼
            removeReviewBtn.addEventListener("click",removeReview);
    }

    if(reviewModifyModal){
            reviewModifyForm.onsubmit=modifyReview;
            async function modifyReview(e) {
                e.preventDefault();
                reviewModifyModal.hide();
                let url="/review/handler.do";
                const data=new FormData(reviewModifyForm);
                console.log(reviewModifyForm.grade.value);
                const resp=await fetch(url,{method:"PUT", body:data});
                if(resp.status===200){
                    const json=await resp.json();
                    if(json.modify>0) {
                        alert("수정성공");
                        await loadReviews(reviewRegisterForm.tId.value);
                    }
                }else{
                    alert("수정 실패 status : " + resp.status);
                }
            }
        }

~~~
</div>
</details>
    
### 6.5. 좋아요 등록
- 게시글의 좋아요 등록은 자바스크립트 ajax 로 구현을 했는데, 좋아요 등록을 연속으로 클릭 시 게시글의 id 가 null 이라는 에러가 발생했다.
- 좋아요 등록은 수업시간때 구현했던 기능이라서, 수업때 코드를 다시 비교해보며 한가지 다른점을 발견하였고, 문제를 해결했다.
- 해결방법은 컨트롤러에서 ajax 로 통신하는 url 에 게시글의 id 를 model.addAttriubte 로 전달하는 것이었다.
- 이 부분에서 내가 했던 생각은 좋아요 html 을 include 할때 th:with 타임리프 변수로 해당 페이지에 게시글의 id를 전달해서 문제되지 않는다고 생각했던 거였다.
- 하지만 ajax 로 통신하는 url 에도 별도로 id 를 전달해야 한다는 것을 알게되었다.

<details>
<summary>코드</summary> 
<div markdown="1">

~~~javascript
   async function readLike(tId) {
    let url = `/trip/like/${tId}/read.do`;
    console.log(tId)
    const resp = await fetch(url);
    if (resp.status === 200) {
        const html = await resp.text();
        console.log(html);
        return html;
    }
}
~~~
~~~java
 @GetMapping("/{tId}/read.do")
    public String readLikeStatusCnt(
           @PathVariable int tId,
           @SessionAttribute(required = false)UserDto loginUser,
           Model model) {
      TripLikeStatusCntDto likes;
      model.addAttribute("id",tId);
      if(loginUser!=null) {
         likes=tripLikeService.read(tId, loginUser.getUId()); // 게시글 유저 like 좋아요 개수
      }else{
         likes=tripLikeService.read(tId); // countStatusByTId // 게시글 like 좋아요 개수
      }
      model.addAttribute("likes",likes);
      log.info(likes);
      return "/trip/likes";
   }
    
~~~

</div>
</details>
    
       
## 7. 회고
- 프로젝트를 하면서 좋았던 점은 모든 팀원들이 함께 DB 설계에 참여한 것이다. 
처음에는 DB 설계에 대한 감이 잡히지 않았지만, 서비스에 필요한 것들을 먼저 생각하고 연결 관계를 하나씩 찾아가보니 어설프지만 모양은 갖춘 db 테이블을 만들 수 있었고 이후 테이블을 최종적으로 수정하는 과정에서 필요없는 것은 제거하고 참조 관계를 연결하면서 테이블 간의 참조 관계, RDBMS 등을 이해할 수 있게 되었다. 
- 프로젝트를 하면서 느꼈던 점은
직접 코드를 작성해보면서 수업에서 이해했다고 생각했던 것들이 많은 부분에서 제대로 이해하지 못했다는 것을 깨달았고
어려웠던 부분들을 해결 해나가면서 새롭게 이해하고 깨우친 것들이 많아서 개인적으로 조금은 더 성장할 수 있는 시간이었던 것 같다. 
