# url-shortener



-------

# **221102 변경사항**

![img](./image/README/image-20221112220417361.png)

## index가 계속 노출되고 ConverterWithBase64와 강한 의존관계를 발생시키는 구조 변경

1. Url 도메인객체 필드에 shortenUrl 추가 : index에 의존적이지 않도록 상태를 유지하도록 변경
2. UrlFactory로 객체생성하도록 변경
   - Factory에서 인코딩할 Sequence값과 인코딩을 책임지고 더이상 Index를 활용하지 않게 캡슐화함
3. 정적클래스였던 ConverterWithBase62 를 전략패턴으로 변경하고 UrlFactory로 의존성주입
   - interface로 UrlConverter를 두고, 구현체로 Base62UrlConverter 로 변경하여 결합도를 낮춤.
   - 추후 Base62가 아닌 다른 인코딩방식도 유연하게 대응되도록 변경
4. ConverterWithBase62에 지속적으로 의존하던 UrlShorteningServce 의 getUrl() 메서드가 사라짐.
   - Url객체를 UrlFactory로 생성하면 객체가 shortenUrl 상태값을 들고있기 때문에 더이상 Converter를 사용하지 않음.
5. UrlRepository에서 save는 더이상 Index를 참조하지 않고 shortenUrl을 조회할 수 있도록 변경.
6. UrlRepository에서 findById 삭제(db를 통한 인덱싱이 이루어지지 않기 때문에 해당 서비스에서 있을 이유 없다고 판단)
7. UrlAccessCountDto에서 전달하던 index 삭제(캡슐화개념 없이 클라이언트에게 모든정보를 친절히 보여주는 것이 좋을거라고 막연히 판단했던 부분 시정)



## DTO의 변환위치 Controller vs Service 중 Service로 채택

1. 'POST /urls' 요청시  UrlIn dto내부의 url이 아니라 dto자체를 Service객체로 넘김.

   - Controller 가 임의로 작성된 String을 넘기면 service객체의 의도대로 사용할 수 없음.
   - Controller는 캡슐화된 dto를 조작할 수 없게됨. 

2. 'GET /urls/{shortenUrl}' 요청시 서비스객체는 UrlAccessCountOut dto를 반환하여 Controller에서 받음.

   - Url 도메인객체에서의 컨버팅은 UrlAccessCountOut dto에 from이라는 변환메서드를 생성하여 변환책임 부여.

     (dto양식이 변하더라도 변한 객체 내애서 수정하면 됨.)

   - 캡슐화된 dto자체를 service객체에서 반환

     (불필요한 domain필드값을 Controller에 넘기지 않으며, Controller는 임의적으로 dto를 수정 변경할 수 없음.)

3. dto의 형태가 변화한다면, service에서 dto의 변환을 담당하기 때문에 dto에 의존적일수밖에 없을것같음.



## Post로 Url 정보를 받을때 Controller 유효성 검사 추가

1. spring-boot-starter-validation 을 이용하여 dto를 받을시에 url인지 아닌지 파악하는 검증로직 추가 

   - @URL 어노테이션은 scheme여부만 가지고 url인지 아닌지 판단하고 끝남 scheme이 없어도 url인지 찾아낼수있도록 개선 필요

     **※ 해결과제 :** 검증시 url이 아닌경우 계속 500에러를 뱉어내고있음. 400에러가 기본적용된다는데 억지로 HttpClientErrorException을 던져도 json응답에서는 500에러를 던짐 확인이 필요함.

![image](./image/README/image-20221112220555705.png)

## Base62UrlConverter 코드 가독성 증대

``` java
//변경전
BASE62_TABLE[(int) (quotient % 62)] ;
  
//변경후
int digitIndex = (int) (quotient % 62); 
char digit = BASE62_TABLE[digitIndex];
```

1. index의 값의 의미가 무엇인지 나타도록 분리하여 digitIndex(자릿수 내에 표현될수있는 값의 Index)으로 나타내고 테이블에 대입
2. Long -> String String-> Long 타입으로 인코딩과 디코딩 하는 역할이라 좀더 의미적으로 가독성있게 작성을 못하는중



## ArrayList 저장공간에서 Map 저장공간으로 변경

1. UrlRepository를 implements하여 MapUrlRepository를 생성

2. Map Url 레포지토리를 작성하기만 하면 다른코드의 변동없이 작성되길 기대했으나, Interface의 설계문제로 Interface수정 및 Service수정이 이루어짐(OCP 위반)

   -> Interface 내 findAll 반환타입을 Optional<List\<Url>>에서 List\<Url>로 변경 어차피 인스턴스화가 되어있는데, null로 나갈 확률이 없고 없으면 없는대로 전달해도 의미상 문제가 없음.

3. Interface개선후 Map 구현은 상당히 빠른 구현이 가능했음(대략 10분도 안걸림). 역할과 구현을 분리한 장점을 느낄 수 있었음. 단, 좋은 Interface설계가 전제되어야함을 파악함.



## 주요 어플리케이션 Layer class 접근제어자를 package-private로 변경

1. Controller는 public, Service와 Repository는 package-private로 캡슐화(생각해보니 그러면 test는 ..? 어떻게?... 되네? 뭐지.. 왜 되는거지; )

2. Domain객체인 Url은 dto 객체의 변환메서드에 의존함에 따라 public으로 둘 수 밖에 없음(dto와 domain의 패키지가 다름)

   

## 해결이 필요한 과제

1. Test가 Base62UrlConverter 외에 전무함. 테스트코드 작성을 잘 할줄 알아야함.
2. 테스트전략이나 프레임워크와 함께 공존하는 테스트를 다룰줄 몰라서 한계가 있음. 테스트관련 공부가 필요
