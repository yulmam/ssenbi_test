# ssenbi_test
프로젝트의 센비의 성능 테스트 및 리펙토링을 위한 레포지토리입니다.

# SSENBI Test Repository
센비(SSENBI) 프로젝트의 성능 테스트 및 리팩토링을 위한 레포지토리입니다.

## 1. CI/CD 구축하기
센비 프로젝트의 자동 배포 및 운영 환경을 구축하기 위해 CI/CD를 적용하였습니다.

### 📌 관련 문서
- [CI/CD 개념](https://www.notion.so/17e708433a6d80c48b4dcb85c41a810e)
- [CI/CD 구축 문서](https://www.notion.so/CI-CD-180708433a6d80d1bfdff4df240465d0)
- [EC2 설정](https://www.notion.so/EC2-174708433a6d80cca58ac949aedffb4d)
- [Docker 설정](https://www.notion.so/Docker-174708433a6d8034be5bf01e37eb1e55)
- [nginx 설정](https://www.notion.so/nginx-175708433a6d8074aa0cc42537331200)
- [GitHub Actions 설정](https://www.notion.so/Actions-17b708433a6d800ca6deff739f91405b)
- [Troubleshooting](https://www.notion.so/troubleshooting-17c708433a6d8030b740c37a9d21d236)

---

## 2. 성능 향상시키기
센비 프로젝트의 성능을 최적화하기 위해 다양한 개선 작업을 진행하고 있습니다.

### 2.1 Ehcache를 통한 성능 향상
- 데이터베이스의 부하를 줄이고 응답 속도를 높이기 위해 Ehcache를 적용하였습니다.
- 자세한 내용은 [Ehcache 성능 향상 문서](https://acoustic-rib-4c4.notion.site/ehcache-180708433a6d807db8dec6d4458da1ec?pvs=4)에서 확인할 수 있습니다.

### 2.2 비동기를 통한 성능 향상 (작성 중)
- 요청 처리 속도를 개선하기 위해 비동기 프로그래밍을 적용하는 작업을 진행 중입니다.
- 주요 내용:
  - CompletableFuture를 활용한 효율적인 병렬 처리
  - 비동기 처리 시 예외 처리 및 트랜잭션 관리 고려
- 해당 내용은 작성 후 업데이트될 예정입니다.

---

## 📌 프로젝트 목표
1. CI/CD 파이프라인을 구축하여 배포 자동화를 완성한다.
2. 캐싱 및 비동기 처리를 적용하여 시스템의 응답 속도를 최적화한다.
3. 성능 테스트 및 지속적인 리팩토링을 통해 안정적인 서비스를 제공한다.

