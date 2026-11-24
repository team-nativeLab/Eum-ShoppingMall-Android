# 웹 형식 점검 보고서 (앱 UI 정리, Issue #74)

## 기준과 범위

- 웹 기준: https://eum-shopping-mall-frontend.vercel.app/ (홈 히어로 3장, 하단 푸터, 검색·필터 화면)
- 앱 기준선: `origin/dev` `96029d6` 기반 `feature/#74-home-search-filter-ui`
- 참고 이미지 1~12는 사용자 제공 캡처다. 이 보고서는 웹 형식 중 앱 디자인으로 옮길 수 있는 패턴을 찾고, 이번 PR에서 바꾼 항목과 보류 항목을 구분한다.
- 이번 PR은 요청 항목만 수정한다. 나머지 패턴은 조치 없이 보류로 남긴다.

## 상태 의미

- **적용**: 이번 PR에서 코드를 바꿨다.
- **보류**: 웹에서 확인했지만 이번 요청 범위 밖이다. 후속 이슈 후보다.
- **미확인**: 웹에서 동작을 직접 확인하지 못했다. 추정으로 구현하지 않는다.

## 점검 결과

| # | 패턴 | 웹 형태 | 앱 이전 상태 | 조치 | 근거 (코드 위치) |
|---|---|---|---|---|---|
| 1 | 홈 히어로 캐러셀 | 3장 슬라이드, 점 인디케이터 | 2장, 일시정지 버튼과 보조 링크 있음 | **적용**: 3번째 장 추가(웹 문구), 보조 링크 제거. 일시정지 버튼 제거는 사용자 결정(2026-11-24, 현 상태 유지)으로 적용. **접근성 위험 잔존**: 아래 '접근성 결정 기록' 참조 | `ui/home/components/HomeHeroCarousel.kt`, `strings.xml` |
| 2 | 히어로 자동 넘김 방향 | 웹 전환 방향 기준 | 기존 구현 유지 | **미확인**: 웹 전환 방향을 직접 확인하지 못했다. 기존 방향 유지 | `HomeHeroCarousel.kt` |
| 3 | 홈 하단 회사 정보 푸터 | 웹 하단에 링크와 사업자 정보 | 모바일 홈 하단에 푸터 있음 | **적용**: 푸터 전체 삭제, 관련 이벤트·네비게이션·문자열 정리 | `HomeScreen.kt`, `HomeModels.kt`, `EumMainNavDisplay.kt` |
| 4 | 홈 상단 브랜드 로고 | 웹 헤더 로고는 별도 | 햄버거 옆 로고 있음 | **적용**: 로고 삭제 | `ui/home/components/HomeTopBar.kt` |
| 5 | 인기 검색어 | 순위 목록 | 2열 칩 그리드 | **적용**: 1~10위 순위 목록(번호 + 검색어), 변동 표시 없음 | `ui/catalog/SearchScreen.kt` |
| 6 | 검색 헤더 취소 버튼 | 웹 검색창은 별도 취소 없음 | 헤더에 취소 텍스트 | **적용**: 검색 헤더 전체에서 취소 삭제 | `SearchScreen.kt`, `SearchResultScreens.kt` |
| 7 | 상품 목록 정렬 드롭다운 | 화살표가 우측 끝 | 화살표가 텍스트 옆 | **적용**: 상품 목록 정렬만 화살표를 우측 끝으로 | `ui/catalog/ProductListScreen.kt` |
| 8 | 필터 체크박스 | 작은 체크박스 | Material 기본 크기 | **적용**: 20dp, 테마 색상 적용 | `ui/productfilter/ProductFilterScreen.kt` |
| 9 | 필터 최소·최대 입력칸 | 버튼과 높이 맞춤 | 56dp | **적용**: 48dp (적용 버튼과 같음). M3 최소 높이 제약으로 실제 높이는 기기에서 확인 필요 | `ProductFilterScreen.kt` |
| 10 | 페이지 좌우 거터 | 웹 컨테이너 기준 | 화면마다 20dp, 16dp 혼재 | **적용(범위 결정 2026-11-24)**: 화면 루트(스크롤 컨테이너·contentPadding)와 하단 고정 바·sticky 액션은 `EumSpacing.pageGutter`(16dp). 카드·행·구분선 등 내부 여백은 20dp. **섹션 기준(2026-11-24 결정)**: 같은 섹션 안의 가로 스크롤 행은 그 섹션 헤더와 같은 20dp를 쓴다(홈 `HomeScreen.kt:394`). 예외: 상품 상세(`ProductDetailScreen.kt`)는 루트 판정이 불명확해 이번 변경에서 제외했다(확인 필요). 루트와 내부 여백이 다른 것은 의도된 구조다 | `ui/theme/EumSpacing.kt` |
| 11 | 뒤로가기 버튼 | 좌상단 고정 | 화면마다 크기·위치·아이콘 상이 | **적용**: 공용 `EumTopBarBackButton`(48dp 터치, 24dp 아이콘, `ic_arrow_left`)으로 7곳 통일. 시작 인셋 4dp로 아이콘 왼쪽이 16dp 거터에 맞춤 | `ui/common/EumTopBarBackButton.kt` |
| 12 | 뒤로가기 아이콘 종류 | 화살표 | 주문 화면만 chevron | **보류(유지)**: 요청 범위 밖이라 기존 chevron을 유지하고 `iconResId`로 명시 | `ui/orders/view/components/OrdersTopBar.kt` |
| 13 | 웹 타이포그래피 토큰 (`webDisplayL`, `webBodyXl`) | 웹 전용 크기 | 앱 타이포 토큰과 별도 | **보류**: 앱 전체 타이포 체계 변경은 범위 밖. 별도 이슈 | `ui/theme/` |
| 14 | 중앙 정렬 최대 폭 (`widthIn(max = 600)`) | 웹 본문 중앙 컬럼 | 일부 화면에만 적용 | **보류**: 태블릿·폴더블 대응과 함께 별도 이슈 | `ui/addresssearch/AddressSearchTopBar.kt` 등 |
| 15 | 브랜드 히어로 대형 이미지 (220dp) | 웹 상단 대형 비주얼 | 화면별 상이 | **보류**: 디자인 기준 확정 후 별도 이슈 | 해당 화면 |
| 16 | 히어로 "회사소개 보기" 보조 링크 | 웹 히어로 하단 보조 링크 | 3번째 슬라이드 주 CTA가 회사소개였고 보조 링크가 있었음 | **적용(2026-11-24 재확인)**: 모든 슬라이드의 주 CTA를 "상품 둘러보기"로 통일하고 보조 링크 표시 코드를 제거. `ViewCompany` 행동 값은 목적지 매핑 테스트를 위해 남겨 둠 | `HomeHeroCarousel.kt`, `HeroCtaTest.kt` |
| 17 | 홈 상단 바 시작 인셋 | 웹 헤더 정렬 | 햄버거가 본문보다 4dp 더 안쪽(8dp 인셋) | **적용**: 기본 인셋을 `topBarInset`(4dp)으로 맞춰 아이콘이 16dp 거터에 정렬 | `ui/home/components/HomeTopBar.kt` |
| 18 | 푸터 관련 죽은 코드 | - | 푸터 삭제 후 모델·파라미터·문자열·아이콘 잔존 | **적용**: `HomeCompanyInfoUiModel`·테스트, `homeCompanyIntro`, 사업자 안내 문자열 3개, `ic_pause`·`ic_play` drawable 제거 | `HomeModels.kt`, `ShoppingScreenDataProvider.kt`, `strings.xml` |

## 접근성 결정 기록 (히어로 자동 넘김, WCAG 2.2.2)

- 현재 동작: 홈 히어로는 5초마다 자동으로 넘어간다. 터치하면 15초 동안 멈추고 다시 넘어간다. 사용자가 영구 정지할 버튼은 없다. OS의 애니메이션 끄기 설정을 켜면 멈춘다.
- 위험: WCAG 2.2.2(자동 움직임 정지, Level A)는 5초를 넘고 다른 콘텐츠와 함께 움직이는 자동 콘텐츠에 정지·일시정지 수단을 요구한다. 현재 구조는 이 요구를 충족하지 않을 수 있다.
- 결정: 사용자가 2026-11-24에 **현 상태 유지**를 선택했다. 제품 결정으로 기록하며, 접근성 준수 여부는 확정되지 않았다.
- 적용 위치: `ui/home/components/HomeHeroCarousel.kt`의 `LaunchedEffect(isResumed, isDragged, autoAdvanceResetKey)`, `HERO_INTERACTION_PAUSE_MILLIS`(15초), `HOME_HERO_AUTO_ADVANCE_MILLIS`(5초).
- 재검토 조건: 접근성 검수나 출시 전 점검에서 지적되면 정지·재생 컨트롤 복원 또는 한 바퀴 후 정지로 바꾼다. 출시 전 점검 항목이며 담당과 시점은 미정이다.
- OS 애니메이션 끄기 설정을 WCAG 2.2.2 충족 수단으로 인정할지는 **미정**이다.

## 후속 작업 후보

- 공용 체크박스 컴포넌트 통합 (`AuthCheckbox`, `FilterCheckboxRow`, `ProductCheckbox`, `AccountConsentRow`, `AuthConsentRow` 등 7종 이상 중복)
- 기본 버튼 통합 (`EumPrimaryButton` 56dp r8 외 `UiAuditPrimaryButton` r10, `AccountPrimaryButton` 48dp)
- 리터럴 `dp` 약 758곳을 `EumSpacing`/토큰으로 정리
- 웹 타이포·최대 폭·브랜드 히어로 기준 (보류 #13~#15)

## 미확인 항목

- 사용자 실행 판정(`user-reviewer`) 결과는 이 문서에 아직 반영하지 않았다.
- 큰 글꼴(fontScale 1.3~2.0)과 360dp 폭에서 개수 라벨, 순위 번호, 필터 입력칸 48dp가 잘리는지 확인하지 않았다.
- 디버그 빌드의 인기 검색어 샘플은 10개로 맞췄다. 릴리스 빌드(`ShoppingScreenDataProvider.searchScreen()`)는 서버 연동 전이라 인기 검색어가 비어 있고 안내 문구가 나온다.

- 히어로 자동 넘김 방향 (#2): 웹 전환 방향을 확인하지 못했다.
- 필터 입력칸 48dp (#9): M3 `OutlinedTextField`의 최소 높이 제약 때문에 실제 적용 높이가 다를 수 있다. 기기 스크린샷으로 확인이 필요하다.
