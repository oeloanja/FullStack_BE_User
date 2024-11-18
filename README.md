## 1.사용자(User) 기능 테스트
### 대출자 테스트
#### 회원가입
```
POST http://localhost:8085/api/users/borrow/signup
Content-Type: application/json

{
    "email": "borrow@test.com",
    "password": "Test1234!",
    "passwordConfirm": "Test1234!",
    "userName": "대출자",
    "phone": "010-1234-5678"
}
```


#### 로그인
```
POST http://localhost:8085/api/users/borrow/login
Content-Type: application/json

{
    "email": "borrow@test.com",
    "password": "Test1234!"
}
```

#### 마이페이지 조회
```GET http://localhost:8085/api/users/borrow/mypage?userId=1```

#### 비밀번호 변경
```
PUT http://localhost:8085/api/users/borrow/password?userId=1
Content-Type: application/json

{
    "currentPassword": "Test1234!",
    "newPassword": "NewTest1234!",
    "newPasswordConfirm": "NewTest1234!"
}
```

#### 전화번호 변경
```
PUT http://localhost:8085/api/users/borrow/phone?userId=1
Content-Type: application/json

{
    "phone": "010-9999-8888"
}
```


### 투자자 테스트
#### 회원가입
```
POST http://localhost:8085/api/users/invest/signup
Content-Type: application/json

{
    "email": "invest@test.com",
    "password": "Test1234!",
    "passwordConfirm": "Test1234!",
    "userName": "투자자",
    "phone": "010-9876-5432"
}
```
#### 로그인
```
POST http://localhost:8085/api/users/invest/login
Content-Type: application/json

{
    "email": "invest@test.com",
    "password": "Test1234!"
}
```

#### 마이페이지 조회
```GET http://localhost:8085/api/users/invest/mypage?userId=1```

#### 비밀번호 변경
```
PUT http://localhost:8085/api/users/invest/password?userId=1
Content-Type: application/json

{
    "currentPassword": "Test1234!",
    "newPassword": "NewTest1234!",
    "newPasswordConfirm": "NewTest1234!"
}
```

#### 전화번호 변경
```
PUT http://localhost:8085/api/users/invest/phone?userId=1
Content-Type: application/json

{
    "phone": "010-9999-8888"
}
```
## 2. 계좌(Account) 기능 테스트
### 대출자 계좌
#### 계좌 등록
```
POST http://localhost:8085/api/accounts/borrow?userId=1
Content-Type: application/json

{
    "bankName": "신한은행",
    "accountNumber": "110-123-456789",
    "accountHolder": "대출자"
}
```
#### 계좌 목록 조회

```GET http://localhost:8085/api/accounts/borrow?userId=1```

#### 계좌 상태 변경(계좌 삭제)
```
PUT http://localhost:8085/api/accounts/borrow/1/status?userId=1
Content-Type: application/json

{
    "isDeleted": true
}
```
### 투자자 계좌
#### 계좌 등록
```
POST http://localhost:8085/api/accounts/invest?userId=1
Content-Type: application/json

{
    "bankName": "국민은행",
    "accountNumber": "123-456-789012",
    "accountHolder": "투자자"
}
```
#### 계좌 목록 조회

``` GET http://localhost:8085/api/accounts/invest?userId=1 ```

#### 계좌 상태 변경
```
PUT http://localhost:8085/api/accounts/invest/1/status?userId=1
Content-Type: application/json

{
    "isDeleted": true
}
```



테스트 시나리오


회원가입 → 로그인 → 계좌등록 → 목록조회 → 상태변경 순으로 테스트


각 단계별로 응답 코드와 데이터 확인


에러 케이스도 테스트 (잘못된 입력, 권한 없는 접근 등)
