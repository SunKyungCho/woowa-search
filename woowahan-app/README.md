# woowahan-app

`resources/static/index.html` 에는 가게를 조회, 수정, 삭제 할 수 있는 어플리케이션이 작성되어 있습니다.
`/` Path 진입 시 index.html 페이지에 접근될 수 있도록 추가하고 아래 요구사항을 보고 정상동작 할 수 있도록 만들어 주세요.

## 요구사항

1. `index.html` 에 구현되어 있는 아래 기능이 정상 동작하여야 합니다.
    - 기능 
        - 가게 목록 조회
            ```
            1. filter 파라미터를 사용하여 원하는 가게만 필터가 가능합니다. 필수로 `OPEN`값을 받아 오픈된가게만 조회하는 기능이 가능하도록 해주세요.
            2. sort 파라미터를 사용하여 가게 정렬이 가능해야 합니다. 필수로 `SCORE`값을 받아 `랭킹순`으로 정렬이 가능하도록 해주세요.
            -> `랭킹점수`는 score점수와 가게 이름의 길이를 합친 점수를 의미한다.
            3. shopName으로 가게를 검색할 수 있어야한다. 
            -> 추가하고자 하는 가게명이 정상적으로 조회가 되지 않을 시 `woowahan-provisioning`에서 정의한 사전을 활용한다.
            ## 위 3가지 조건은 or로 사용될 수 있으며 필터와 정렬은 필수 조건 값(OPEN, SCORE) 이외에도 다른 조건값이 추가될 수 있다는 가정하에 유연하게 개발되어야한다.
            ex)  
            curl localhost:8080/shops?sort=SCORE&filter=OPEN&shopName=짜장면 -X GET -H 'Content-Type: application/json'
            ```
        - 가게 단건 조회
            ```
            curl localhost:8080/shops/1 -X GET -H 'Content-Type: application/json'
            ```
        - 가게 추가
            ```
            curl localhost:8080/shops -X POST -H 'Content-Type: application/json' -d "{ \"address\": \"송파구 방이동\", \"isOpen\": true, \"lat\": 37.515877, \"lon\": 127.1171972, \"score\": 9999, \"shopName\": \"짜장면집\", \"shopNumber\": 1232 }"
            ```
        - 가게 수정
            ```
            curl localhost:8080/shops/1 -X PUT -H 'Content-Type: application/json' -d "{ \"address\": \"송파구 방이동\", \"isOpen\": true, \"lat\": 37.515877, \"lon\": 127.1171972, \"score\": 9999, \"shopName\": \"짜장면집\", \"shopNumber\": 1232 }"
            ```
        - 가게 삭제
            ```
            curl localhost:8080/shops/1 -X DELETE -H 'Content-Type: application/json'
            ```
        
    - 가게 최소 스팩
      ```
      {
          "address": "송파구 방이동",
          "isOpen": true,
          "lat": 37.515877,
          "lon": 127.1171972
          "score": 9999,
          "shopName": "짜장면집",
          "shopNumber": 1232
      }

      ```
      
2. logback 프레임워크(http://logback.qos.ch)를 이용하여 의미있는 로그를 남깁니다.
3. test container와 JUnit5 를 이용해서 테스트 케이스 작성합니다.

## 안내
- 요구사항을 구현함에 있어 충분한 의존성이 제공되고 있습니다. 설명할 수 없는 불필요한 의존성은 추가할 수 없습니다.
- 사전 작성된 코드는 자유롭게 변형, 분리, 리펙토링 되어도 무방합니다.