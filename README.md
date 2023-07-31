# stock

### 재고관리 동시성 문제 해결 프로젝트

- 동시에 100명이 제품을 구매한다면?
- 동시 사는 사람과 파는 사람 있다면?
- 동시에 10명이 제품을 구매한다면?

----


### 동시성 문제 해결 방법

1. Synchronized ( 동기화 ) :
   - 멀티 스레드 환경에서 하나의 스레드만이 한 번에 리소스에 접근할 수 있도록 보장 할 수 있다.
3. Database Lock
   -  데이터베이스에서 제공하는 Lock을 활용해 데이터를 제어해 일관성을 유지할 수 있다.
4. Redis Lock  
   - Redis와 같은 외부 서버를 사용하여 Lock을 처리할 수 있다



----

### mysql을 이용한 방법( database )
#### Pessimistic Lock ( 비관적 락) 
- 실제로 데이터에 Lock 을 걸어서 정합성을 맞추는 방법. 
- exclusive lock 을 걸게되며 다른 트랜잭션에서는 lock 이 해제되기전에 데이터를 가져갈 수 없다.
- 데드락이 걸릴 수 있기때문에 주의하여 사용하여야 합니다.

#### Optimistic Lock ( 낙관적 락 ) 
- 실제로 Lock 을 이용하지 않고 버전을 이용함으로써 정합성을 맞추는 방법. 
- 먼저 데이터를 읽은 후에 update 를 수행할 때 현재 내가 읽은 버전이 맞는지 확인하며 업데이트.

```angular2html
update table_name 
set version = version + 1 
    and quantity = required_quantity
where version = 1;
```
- 내가 읽은 버전에서 수정사항이 생겼을 경우에는 application에서 다시 읽은후에 작업을 수행해야한다.

#### Named Lock
- 이름을 가진 metadata locking. 
- 이름을 가진 lock 을 획득한 후 해제할때까지 다른 세션은 이 lock 을 획득할 수 없도록 한다. 
- 주의할점으로는 transaction 이 종료될 때 lock 이 자동으로 해제되지 않고. 별도의 명령어로 해제를 수행해주거나 선점시간이 끝나야한다.


----

### Redis 를 이용한 방법 ( 외부 서버 )
#### Lettuce
 - setnx(Set if Not eXists) 명령어를 사용
 - 주어진 값이 없을때만 값을 설정하는 Redis 명령어
 - spin lock 방식 -> lock 점유 실패 시 점유 성공 시 까지 반복 접는 하는 방식
#### Redisson
 - pub-sub (publish-subscribe)기반으로 Lock 제공
 - Thread 1 의 점유가 끈나면 Thread 2에 점유하라는 요청을 보낸다.
