#SpringBoot For KakaoAPI



#기본 제약사항
   * API 기능명세에서 기술된 API를 모두 개발하세요.
   * Spring boot 기반의 프레임웍을 사용하세요.
   * 단위 테스트(Unit Test) 코드를 개발하여 각 기능을 검증하세요.
   * 모든 입/출력은 JSON 형태로 주고 받습니다.
   * 단, 각 API에 HTTP Method들(GET|POST|PUT|DEL)은 자유롭게 선택하세요.
   * README.md 파일을 추가하여, 개발 프레임웍, 문제해결 방법, 빌드 및 실행 방법을
     기술하세요.



#개발환경
- JDK 1.8 
- Maven 3.6.1
- Eclipse Version: 2019-03 (4.11.0)
- Git
- 네이버 클라우드 설치 (https://www.ncloud.com/) (DB용)
  
  ->Server: OS CentOS 7.3 (64-bit)         
     -서버 접속용 공인 IP : 101.101.164.71, 외부 포트 : 1200
  
  ->Cloud DB for MySQL -> 10.2.24 MariaDB  
     -서버 접속용 공인 IP : 101.101.167.239 외부 포트 : 3306
  
  ->클라우드로 DB를 구성하여 테이블에 데이터를 생성하여 INSERT해 놓았습니다.


#빌드 및 실행 방법
    
    1.다운로드 및 import
    -> 주소 : https://github.com/jeongkwan2/SpringBootForKakaoApi.git
    -> SpringBootForKakaoApi-master 폴더를 Eclipse에서 Existing Maven Projects로 import 한다.
    
    2. 실행
        1)Spring Boot App
          -> Run As -> Spring Boot App으로 실행 

        2)단위테스트(UnitTest)
          -> Run As -> JUnit으로 실행 
        
        *단위 테스트(Unit Test)
            com.kakao.testapp.UnitTest.java에서 junit을 이용하여 단위테스트를 실시하였습니다.
            API 기능명세 중 1,2,3,4번이 호출되며 이중 1,2,3번은 GET방식으로 구현되어있고,
            4번은 POST방식으로 UnitTest.java에서
            public void IntegratedBranch() 안에 
            obj.put("brName", "판교점") 값을 변경하여 테스트 할 수 있습니다.


#API 기능명세 및 문제해결 방법
1. 2018년, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출하는 API 
   (단, 취소여부가 ‘Y’ 거래는 취소된 거래임, 합계 금액은 거래금액에서 수수료를 차감한 금액임)

    #URL: http://localhost:8080/kakaoTradeApi/MostAmount (GET방식)

    MostAmountDAO.java에서 쿼리를 보면 거래내역db와 고객데이터db 테이블을 조인하여 계좌번호에 대한 계좌명을 가져오고
    취소여부가 N이고 합계금액은 거래금액에서 수수료를 뺀 가격이고 연도별, 계좌별로 합산하여 합계금액이 최고많은사람을 SELECT 해온다.
    
    *문제에서 결과 출력자료형
    year,sumAmt->숫자 name,acctNo->String

    결과: [{"year":2018,"name":"테드","acctNo":"11111114","sumAmt":28992000},
          {"year":2019,"name":"에이스","acctNo":"11111112","sumAmt":40998400}]


2. 2018년 또는 2019년에 거래가 없는 고객을 추출하는 API
   (취소여부가 ‘Y’ 거래는 취소된 거래임)
   입력 값은 아래와 같은 값을 사용.
    
    #URL: http://localhost:8080/kakaoTradeApi/NoTradeCustomer (GET방식)

    NoTradeCustomerDAO.java에서 보면 연도별로 거래 합산금액을 가져온 다음, 이중 고객데이터db에서 계좌번호에 대해 조인하고 
    그중에 계좌번호가 없는 값을 추출하면 해당연도에 거래가 없는 사람이 된다.
    최종적으로 2018년도와 2019년도를 UNION하여 SELECT 하였다.

    입력 값은 아래와 같은 값을 사용-> 해당내용이 없어서 PASS하였다.
    
    *문제에서 결과 출력자료형
    year->숫자 name,acctNo->String

    결과: [{"year":2018,"name":"사라","acctNo":"11111115"},
          {"year":2018,"name":"제임스","acctNo":"11111118"},
          {"year":2019,"name":"테드","acctNo":"11111114"},
          {"year":2019,"name":"제임스","acctNo":"11111118"}]


3. 연도별 관리점 별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력하는 API 
   (취소여부가 ‘Y’ 거래는 취소된 거래임)

    #URL: http://localhost:8080/kakaoTradeApi/YearAndBranchSumTradeAmt (GET방식)

    YearAndBranchSumTradeAmtDAO.java에서 보면 연도별와 계좌별로 합계금액을 구하고, 거래정보db와 고객데이터db를 조인하여 지점코드를 가져와 차례대로 정렬 후,
    다시 지점데이터db에서 지점코드에 맞는 지점명을 가져와 연도별 거래 합계금액이 큰순으로 정렬한다.

    *문제에서 결과 출력자료형
    sumAmt->숫자 brName,brCode->String 인데 Array안에 배열이 존재하는데 
    연도별 거래 합계 리스트에 대한 key,value형식이 안맞는거 같아 key값을 data로 설정하였다.

    결과:[{"data":[{"brCode":"B","brName":"분당점","sumAmt":38500000},
    {"brCode":"A","brName":"판교점","sumAmt":20510000},
    {"brCode":"C","brName":"강남점","sumAmt":20234567},
    {"brCode":"D","brName":"잠실점","sumAmt":14000000}],"year":2018},
    {"data":[{"brCode":"A","brName":"판교점","sumAmt":66800000},
    {"brCode":"B","brName":"분당점","sumAmt":45400000},
    {"brCode":"C","brName":"강남점","sumAmt":19500000},
    {"brCode":"D","brName":"잠실점","sumAmt":6000000}],"year":2019}]


4. 분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 하였습니다. 지점명을 입력하면
   해당지점의 거래금액 합계를 출력하는 API 
   ( 취소여부가 ‘Y’ 거래는 취소된 거래임,)

    #URL: http://localhost:8080/kakaoTradeApi/IntegratedBranch (POST방식)
    입력: JSON
     {
         "brName":"관리지점명"
     }

    분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 위해 새로운 테이블이 필요할거같아서
    새 테이블(newtrade_info)을 하나 추가하였고 거래내역db에서 계좌별로 거래금액을 합산 후 해당하는 지점코드를 매칭시켜준다.
    다음 지점 코드에 맞는 지점명을 지점데이터db에서 가져온뒤 미리 만들어 놓은 테이블에 
    계좌번호,거래금액합계,지점코드로 INSERT 해놓는다

    이후 분당점이 판교점으로 이관하였기 떄문에 분당점에 대해 정보를 판교점으로 UPDATE 시켜놓고
    POST방식에서 "brName":"관리지점명" 이 넘어 올때 관리지점명에 판교,강남,잠실 글자가 존재하면 해당하는 지점코드로
    새 테이블(newtrade_info)에서 조회를 한다.

    *문제에서 결과 출력자료형
     sumAmt->숫자 brName,brCode->String 이고 결과가 바로 배열{}로 나온다.

    판교로 조회했을때 분당점과 판교점이 합산한 결과가 나온다
    결과: {"brCode":"A","brName":"판교점","sumAmt":171210000}

    분당 또는 다른이름으로 조회했을때
    결과: {"code":"404","메세지":"brCode not found error"}

    POST로 올바른 JSON형식이 오지 않을 경우
    결과: {"code":"404","메세지":"No JSON format"}
    








