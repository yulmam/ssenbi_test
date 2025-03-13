# SSENBI Test Repository
센비(SSENBI) 프로젝트의 성능 테스트 및 리팩토링을 위한 레포지토리입니다.

## 📌 프로젝트 목표
1. **성능 최적화**: 캐싱 및 비동기 처리 적용
2. **CI/CD 파이프라인 구축**: 배포 자동화 완료
3. **안정적인 서비스 제공**: 성능 테스트 및 지속적인 리팩토링


## 1. 성능 최적화
센비 프로젝트의 성능을 최적화하기 위해 다양한 개선 작업을 진행하고 있습니다.  
[노션에서 보기](https://www.notion.so/2-CMS-SSENBI-180708433a6d80d5869dcc7932835396)

### **🎯 목표**
1. **멀티 쓰레드 및 비동기 처리**를 활용한 성능 향상 ✅
2. **로컬 캐시**(Ehcache)를 사용한 성능 향상 ✅
3. **데이터베이스 인덱스**를 활용한 성능 향상 ⏳ (진행 중)

### **📌 성능 최적화 과정**

#### **1. 멀티 쓰레드로 응답 시간 단축하기**

- **문제 상황**: Coolsms API를 활용한 **문자 전송 시스템에서 대량 메시지 전송 시 응답 시간이 선형적으로 증가**하는 문제가 발생했습니다.
  - **단일 요청당 응답 시간: 약 1000ms**
  - **100명에게 메시지를 보내면 약 100초 대기 필요**
  - **비동기 처리를 통한 성능 개선 필요**

- **개념 정리**
  - [동시성 vs 병렬성](https://www.notion.so/vs-1a6708433a6d80b1a528fd938fc11af6?pvs=21)
  - [Java에서 비동기](https://www.notion.so/Java-1a6708433a6d80e1b800e449e40c3297?pvs=21)
  - [@Async 어노테이션](https://www.notion.so/Async-1b4708433a6d8064b730ebb2e9252ac7?pvs=21)
  - [쓰레드 풀(Thread Pool) 개념](https://www.notion.so/1b5708433a6d805ebbd6e90f0b8f9b37?pvs=21)

- **구현 과정**
  - [CoolSMS API 테스트를 위한 의존성 역전 및 페이크 객체 주입](https://www.notion.so/CoolSMS-API-19d708433a6d80b9a19ed6c996ca6896?pvs=21)
  - [비동기 처리 도입⭐⭐⭐⭐⭐](https://www.notion.so/1b4708433a6d80faa5c4f9559797ab84?pvs=21)

#### **2. Ehcache 적용**

- **준비 과정**
  - [더미 데이터 생성](https://www.notion.so/1-192708433a6d800d8250e59c566ada25?pvs=21)
  - [ngrinder 및 scouter 설치](https://www.notion.so/2-ngrinder-scouter-181708433a6d805babd1fccabf983492?pvs=21)

- **개념 정리**
  - [Ehcache란?](https://www.notion.so/ehcache-196708433a6d802f9547d7d344062ef3?pvs=21)

- **구현 과정**
  - [Ehcache 적용 과정](https://www.notion.so/1a5708433a6d806695abd9ad756c32ba?pvs=21)
  - [Troubleshooting: Ehcache 2.x에서 3.x 변경 이슈](https://www.notion.so/trouble-shooting-1-ehcache-2-x-3-x-196708433a6d8022be51d3248cb50b6c?pvs=21)

- **성능 테스트**
  - [성능 테스트 결과](https://www.notion.so/196708433a6d805b9e0dd2217bfcc01b?pvs=21)

---

## 2. CI/CD 구축
센비 프로젝트의 자동 배포 및 운영 환경을 구축하기 위해 CI/CD를 적용하였습니다.  
[노션에서 보기](https://www.notion.so/1-CMS-SSENBI-CICD-17e708433a6d80369823cec996496dee)
### **📌 CI/CD 구축 과정**
- **개념 정리**
  - [CI/CD 개념](https://www.notion.so/17e708433a6d80c48b4dcb85c41a810e)

- **구현 과정**
  - [CI/CD 구축 문서](https://www.notion.so/CI-CD-180708433a6d80d1bfdff4df240465d0)
  - [EC2 설정](https://www.notion.so/EC2-174708433a6d80cca58ac949aedffb4d)
  - [Docker 설정](https://www.notion.so/Docker-174708433a6d8034be5bf01e37eb1e55)
  - [nginx 설정](https://www.notion.so/nginx-175708433a6d8074aa0cc42537331200)
  - [GitHub Actions 설정](https://www.notion.so/Actions-17b708433a6d800ca6deff739f91405b)
  - [Troubleshooting](https://www.notion.so/troubleshooting-17c708433a6d8030b740c37a9d21d236)

