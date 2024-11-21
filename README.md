## 1.사용자(User) 기능 테스트
### 1. 대출자 테스트
#### 회원가입
```
POST http://localhost:8085/api/v1/user_service/users/borrow/signup
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
POST http://localhost:8085/api/v1/user_service/users/borrow/login
Content-Type: application/json

{
    "email": "borrow@test.com",
    "password": "Test1234!"
}
```

#### 새로운 토큰 발급 
```
POST /api/v1/user_service/users/borrow/refresh
Header: Authorization: Bearer {refreshToken}
```


#### 마이페이지 조회
```
GET http://localhost:8085/api/v1/user_service/users/borrow/mypage?userId=1
Authorization: Bearer eyJhbG... // 로그인에서 받은 토큰
```

#### 전체정보 조회
```
GET http://localhost:8085/api/v1/user_service/users/borrow?userId=1
```

#### 비밀번호 변경
```
PUT http://localhost:8085/api/v1/user_service/users/borrow/password?userId=1
Content-Type: application/json

{
    "currentPassword": "Test1234!",
    "newPassword": "NewTest1234!",
    "newPasswordConfirm": "NewTest1234!"
}
```

#### 전화번호 변경
```
PUT http://localhost:8085/api/v1/user_service/users/borrow/phone?userId=1
Content-Type: application/json

{
    "phone": "010-9999-8888"
}
```

#### 로그아웃 
```
POST http://localhost:8085/api/v1/user_service/users/borrow/logout
Header:Authorization: Bearer {저장한 리프레시 토큰}
```


### 2. 투자자 테스트
#### 회원가입
```
POST http://localhost:8085/api/v1/user_service/users/invest/signup
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
POST http://localhost:8085/api/v1/user_service/users/invest/login
Content-Type: application/json

{
    "email": "invest@test.com",
    "password": "Test1234!"
}
```


#### 새로운 토큰 발급 
```
POST /api/v1/user_service/users/invest/refresh
Header: Authorization: Bearer {refreshToken}
```


#### 마이페이지 조회
```
GET http://localhost:8085/api/v1/user_service/users/invest/mypage?userId=1
Authorization: Bearer eyJhbG... // 로그인에서 받은 토큰
```

#### 전체정보 조회
```
GET http://localhost:8085/api/v1/user_service/users/invest?userId=1
```

#### 비밀번호 변경
```
PUT http://localhost:8085/api/v1/user_service/users/invest/password?userId=1
Content-Type: application/json

{
    "currentPassword": "Test1234!",
    "newPassword": "NewTest1234!",
    "newPasswordConfirm": "NewTest1234!"
}
```

#### 전화번호 변경
```
PUT http://localhost:8085/api/v1/user_service/users/invest/phone?userId=1
Content-Type: application/json

{
    "phone": "010-9999-8888"
}
```


#### 로그아웃 
```
POST http://localhost:8085/api/v1/user_service/users/invest/logout
Header:Authorization: Bearer {저장한 리프레시 토큰}
```


## 2. 계좌(Account) 기능 테스트
### 1. 대출자 계좌
#### 계좌 등록
```
POST http://localhost:8085/api/v1/user_service/accounts/borrow?userId=1
Content-Type: application/json

{
    "bankName": "신한은행",
    "accountNumber": "110-123-456789",
    "accountHolder": "대출자"
}
```
#### 계좌 목록 조회

```GET http://localhost:8085/api/v1/user_service/accounts/borrow?userId=1```

#### 계좌 상태 변경(계좌 삭제)
```
PUT http://localhost:8085/api/v1/user_service/accounts/borrow/1/status?userId=1
Content-Type: application/json

{
    "isDeleted": true
}
```
### 2. 투자자 계좌
#### 계좌 등록
```
POST http://localhost:8085/api/v1/user_service/accounts/invest?userId=1
Content-Type: application/json

{
    "bankName": "국민은행",
    "accountNumber": "123-456-789012",
    "accountHolder": "투자자"
}
```
#### 계좌 목록 조회

``` GET http://localhost:8085/api/v1/user_service/accounts/invest?userId=1 ```

#### 계좌 상태 변경
```
PUT http://localhost:8085/api/v1/user_service/accounts/invest/1/status?userId=1
Content-Type: application/json

{
    "isDeleted": true
}
```

## 3. 거래(Transaction) 기능 테스트

### 1. 대출자 계좌 거래 테스트

#### 잔액 조회
```GET http://localhost:8085/api/v1/user_service/accounts/transaction/borrow/balance/1?userId=1```

#### 입금
```
POST http://localhost:8085/api/v1/user_service/accounts/transaction/borrow/deposit?userId=1
Content-Type: application/json

{
    "accountNumber": "110-123-456789",
    "amount": 100000,
    "description": "급여 입금"
}
```
#### 출금
```
POST http://localhost:8085/api/v1/user_service/accounts/transaction/borrow/withdraw?userId=1
Content-Type: application/json

{
    "accountNumber": "110-123-456789",
    "amount": 30000,
    "description": "출금"
}
```
#### 송금
```
POST http://localhost:8085/api/v1/user_service/accounts/transaction/borrow/transfer?userId=1
Content-Type: application/json

{
    "fromAccountNumber": "110-123-456789",
    "toAccountNumber": "123-456-789012",
    "amount": 50000,
    "description": "송금"
}
```
#### 거래 내역 조회
```
GET http://localhost:8085/api/v1/user_service/accounts/transaction/borrow/history/1?userId=1
```

## 2. 투자자 계좌 거래 테스트
text
#### 잔액 조회
```
GET http://localhost:8085/api/v1/user_service/accounts/transaction/invest/balance/1?userId=1
```

#### 입금
```
POST http://localhost:8085/api/v1/user_service/accounts/transaction/invest/deposit?userId=1
Content-Type: application/json

{
    "accountNumber": "123-456-789012",
    "amount": 100000,
    "description": "급여 입금"
}
```
#### 출금
```
POST http://localhost:8085/api/v1/user_service/accounts/transaction/invest/withdraw?userId=1
Content-Type: application/json

{
    "accountNumber": "123-456-789012",
    "amount": 30000,
    "description": "출금"
}
```
#### 송금
```
POST http://localhost:8085/api/v1/user_service/accounts/transaction/invest/transfer?userId=1
Content-Type: application/json

{
    "fromAccountNumber": "123-456-789012",
    "toAccountNumber": "110-123-456789",
    "amount": 50000,
    "description": "송금"
}
```
#### 거래 내역 조회
```
GET http://localhost:8085/api/v1/user_service/accounts/transaction/invest/history/1?userId=1
```
