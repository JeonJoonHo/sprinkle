### Version

- JAVA 8
- Spring boot 2.4.0
- MySQL 5.7.25
- Redis 5.0.6

### 문제 해결 전략

1. 받기 API 동시성 제어를 위한 Locking
    - 다 수의 인스턴스에서 동시성 제어를 위해 Redis Cache Store를 이용한 Locking(Redisson 라이브러리 사용)
    
2. 3자리의 Unique Token 발급
    - 예측 불가능하기 위해 UUID random String 사용
    - 고유한 Token을 발급하기 위해 총 3번 생성 및 검사를 진행
    - 3번의 시도 후에도 고유한 Token을 발급받지 못하면 특수 문자를 포함해 토큰 획득
    
### API 명세
```$xslt
// 뿌리기 API
POST /api/sprinkles
    HEADER : X-ROOM-ID, X-USER-ID
    Request Body : amount, targetNumbers
    Response Body : token

// 받기 API
POST /api/sprinkles/{token}/receive
    HEADER : X-ROOM-ID, X-USER-ID
    Response Body : amount

// 조회 API
GET /api/sprinkles/{token}
    HEADER : X-USER-ID
``` 