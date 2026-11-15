# Figma Android 앱 UI 감사

## 기준과 범위

- Figma 파일: `N3Ic9TfSnUBVMgAuayBVly`, `06 Design — App` 페이지 (`178:345`)
- Figma 프레임은 2026-10-22에 메타데이터 및 화면 캡처로 확인했다. 웹·관리자 화면은 제외했다.
- 코드 기준선: 원격 `dev`의 `d78e565c7806b614038cfa50c31dfb669a2fae69` (Issue #28 작업 브랜치 기준선 `d1ed137`에서 6개 커밋 앞)
- 화면 단위 코드는 존재할 수 있지만, `MainActivity`에서 실제 navigation으로 연결됐는지는 별도 검증 항목이다.

## 상태 의미

- **dev 구현**: 기준선의 Android 코드에서 화면 구현을 확인했다.
- **별도 작업 브랜치**: 다른 Issue의 분리 worktree에서 화면 코드를 확인했으나 기준 `dev`에는 아직 통합되지 않았다.
- **#28 구현**: 이 감사 worktree에 화면 구현을 추가했다.
- **부분 구현 → #28 보완**: 기준선에 관련 UI 일부만 있어 Figma의 독립 화면 표현을 추가했다.

## A-01~A-57 대응표

| Figma 화면 | Node ID | 기준 `dev` 구현 상태 | 감사 결과 / 대응 |
|---|---:|---|---|
| A-01 스플래시 | `178:350` | dev 구현 | 스플래시 화면과 홈 진입 구현 확인 |
| A-02 홈 | `178:369` | dev 구현 | 홈 화면 구현 확인 |
| A-03 카테고리 | `178:722` | dev 구현 | 카테고리 화면 구현 확인 |
| A-04 상품 목록 | `179:743` | dev 구현 | 필터 진입 이벤트가 있으나 별도 필터 화면은 없었음 |
| A-05 상품 상세 | `179:938` | dev 구현 | 상품 상세와 옵션 선택 변형 구현 확인 |
| A-06 검색 | `180:1374` | dev 구현 | 검색 화면 구현 확인 |
| A-07 장바구니 | `180:1780` | dev 구현 | 장바구니 화면 구현 확인 |
| A-08 주문하기 1 배송지 | `181:2106` | dev 구현 | 주소 입력·배송 요청 선택을 주문 화면에서 제공 |
| A-09 주문하기 2 결제 | `181:2204` | dev 구현 | 결제 화면 구현 확인 |
| A-10 주문 완료 | `181:2335` | dev 구현 | 주문 완료 화면 구현 확인 |
| A-11 마이페이지 | `181:2409` | 별도 작업 브랜치 #10 | 코드 확인, `dev` 미통합 |
| A-12 드로어 메뉴 | `182:2416` | 별도 작업 브랜치 #10 | 코드 확인, `dev` 미통합 |
| A-13 로그인 | `182:2524` | 별도 작업 브랜치 #9 | 로그인·기본 상태 코드 확인, `dev` 미통합 |
| A-14 회원가입 | `182:2601` | 별도 작업 브랜치 #9 | 회원가입 코드 확인, `dev` 미통합 |
| A-15 견적 문의 | `182:2696` | dev 구현 | 견적 요청 화면 구현 확인 |
| A-16 주문 내역 | `350:3346` | dev 구현 | 주문 내역 화면 구현 확인 |
| A-17 주문 상세 | `351:3440` | dev 구현 | 주문 상세 화면 구현 확인 |
| A-18 배송 조회 | `351:3570` | dev 구현 | 배송 조회 화면 구현 확인 |
| A-19 반품 신청 | `353:3555` | dev 구현 | 취소·반품·교환 화면 구현 확인 |
| A-20 찜한 상품 | `354:3685` | dev 구현 | 찜 화면 구현 확인 |
| A-21 최근 본 상품 | `354:3814` | dev 구현 | 최근 본 상품 화면 구현 확인 |
| A-22 쿠폰함 | `355:3895` | dev 구현 | 쿠폰 화면 구현 확인 |
| A-23 내 정보 관리 | `350:3490` | 별도 작업 브랜치 #10 | 코드 확인, `dev` 미통합 |
| A-24 배송지 관리 | `357:4081` | 별도 작업 브랜치 #10 | 코드 확인, `dev` 미통합 |
| A-25 견적 문의 내역 | `358:4125` | dev 구현 | 견적 내역 화면 구현 확인 |
| A-26 회원 탈퇴 안내 | `458:4210` | 별도 작업 브랜치 #9 | 탈퇴 안내·완료 UI 코드 확인, `dev` 미통합 |
| A-27 이메일 찾기 | `479:4567` | 별도 작업 브랜치 #9 | 계정 복구 코드 확인, `dev` 미통합 |
| A-28 비밀번호 찾기 | `479:4974` | 별도 작업 브랜치 #9 | 계정 복구 코드 확인, `dev` 미통합 |
| A-29 비밀번호 변경 오류 | `480:4806` | 별도 작업 브랜치 #9 | 오류 상태 코드 확인, `dev` 미통합 |
| A-30 휴대폰 번호 변경 | `480:4974` | 별도 작업 브랜치 #9 | 휴대폰 변경 코드 확인, `dev` 미통합 |
| A-31 1:1 문의 내역 | `480:5124` | 별도 작업 브랜치 #14 | 문의 내역 코드 확인, `dev` 미통합 |
| A-32 알림 | `480:5502` | 별도 작업 브랜치 #14 | 알림 화면 코드 확인, `dev` 미통합 |
| A-33 브랜드 목록 | `616:12901` | dev 구현 | 브랜드 목록 구현 확인 |
| A-34 브랜드 상세 | `616:13032` | dev 구현 | 브랜드 상세 구현 확인 |
| A-35 설정 | `655:7083` | 별도 작업 브랜치 #10 | 설정 화면 코드 확인, `dev` 미통합 |
| A-36 이용약관 | `656:7470` | 별도 작업 브랜치 #14 | 약관 문서 화면 코드 확인, `dev` 미통합 |
| A-37 개인정보처리방침 | `656:8199` | 별도 작업 브랜치 #14 | 정책 문서 화면 코드 확인, `dev` 미통합 |
| A-38 이용안내 | `656:8465` | 별도 작업 브랜치 #14 | 정책 문서 화면 코드 확인, `dev` 미통합 |
| A-39 약관 전체 화면 모달 | `657:7899` | #28 구현 | `TermsDialogScreen` 추가; 약관 동의·닫기 이벤트 제공 |
| A-40 공지사항 목록 | `657:35718` | 별도 작업 브랜치 #25 | 목록 화면 코드 확인, `dev` 미통합 |
| A-41 공지사항 상세 | `657:36024` | 별도 작업 브랜치 #25 | 상세 화면 코드 확인, `dev` 미통합 |
| A-42 고객센터 | `661:8066` | 별도 작업 브랜치 #14 | 고객센터 화면 코드 확인, `dev` 미통합 |
| A-43 회사 소개 | `663:8212` | 별도 작업 브랜치 #26 | 화면 코드 확인, `dev` 미통합 |
| A-43 1:1 문의 작성 | `879:8767` | #28 구현 | Figma에서 중복 A-43 ID를 확인; 문의 유형·제목·본문·첨부 이벤트를 제공 |
| A-44 상품 문의 작성 | `879:8789` | #28 구현 | 상품 문의 폼 추가 |
| A-45 문의 등록 완료 | `879:8811` | #28 구현 | 완료 문구를 상단 제목으로 표시하고 접수 상태·안내·문의 내역 버튼 추가 |
| A-46 주소 검색 | `879:8827` | #28 구현 | 주소 검색 입력·결과·선택 이벤트 추가 |
| A-47 배송 요청사항 | `879:8843` | 부분 구현 → #28 보완 | 기존 주문 화면의 선택 메뉴 외에 독립 선택 화면 추가 |
| A-48 상품 필터 | `879:8865` | #28 구현 | 가격대·브랜드·배송 유형·재고 필드와 적용 이벤트 추가 |
| A-49 전체 리뷰 | `879:8887` | #28 구현 | 100dp 리뷰 입력, 단일 점수 모델 기반 별 벡터·요약 및 리뷰 목록 추가 |
| A-50 신청 접수 완료 | `879:8904` | dev 구현 | 주문 요청 접수 상태 화면 확인 |
| A-51 신청 상세 | `879:44641` | dev 구현 | 신청 상세 화면 확인; 취소 완료 변형은 `879:45279` |
| A-52 신청 철회 확인 | `879:44666` | dev 구현 | 철회 확인 상태 구현 확인 |
| A-53 신청 철회 완료 | `879:44682` | dev 구현 | 철회 완료 상태 구현 확인 |
| A-54 이벤트 | `879:44698` | #28 구현 | 진행 중·종료 이벤트 목록 추가 |
| A-55 이벤트 상세 | `879:44715` | #28 구현 | 혜택·참여 방법·유의 사항·상품 이동 이벤트 추가 |
| A-56 마케팅 정보 수신 동의 | `879:44734` | #28 구현 | 수집 목적·채널·보유 기간·거부 안내를 둥근 정보 카드로 표시; 설명 전체 노출 |
| A-57 오픈소스 라이선스 | `879:44754` | #28 구현 | 라이브러리·라이선스 목록을 평면 구분 행으로 표시 |

## 추가 변형

- A-13 로그인 기본 상태는 `213:3371`의 하위 프레임으로 확인했다.
- A-51 신청 상세의 철회 완료 변형은 `879:45279`다.
- A-54 반려 사유 변형은 `927:45901`이다. 해당 상태를 앱 내 주문 신청 상세·결과 화면과 다시 대조해야 한다.
- A-04 필터 진입과 A-08 배송 요청은 통합 기준 `dev`에 연관 동작이 있지만 별도 화면/상태 디자인과 같지는 않다.

## 현재 확인된 시각·기능 차이 및 한계

- Figma의 신규 폼·정보 화면은 390px 프레임 기준이며 20px 좌우 여백, 입력 필드(48/52 미확정, #38에서 변경하지 않음), 8~10px 둥근 모서리, 중립 계열 표면·선 색을 사용한다. 추가한 Compose 화면은 기존 디자인 토큰과 `Scaffold`/edge-to-edge 구조를 사용한다.
- 서체: 앱은 Pretendard를 사용한다(`ui/theme/Type.kt`, `res/font/pretendard_*`). Figma 스타일가이드는 Noto Sans KR을 지정하지만, 2026-11-09 사용자 결정으로 Pretendard를 유지하고 Figma·문서 쪽을 정정 대상으로 둔다. (정정 전 문구는 "기본 시스템 서체 사용, Noto Sans KR 파일 없음"이었으며 사실과 달라 2026-11-09에 바로잡았다.)
- 기준 `dev`에는 A-11~14, A-23~24, A-26~32, A-35~43의 관련 구현이 다른 Issue 작업 브랜치로 존재한다. GitHub CLI 인증이 거부되어 열린 PR의 최신 상태와 중복 범위를 확인하지 못했고, 해당 브랜치 코드를 #28에 복사하지 않았다.
- 이번 구현 화면들은 `MainActivity` 내 전체 navigation으로 연결되지는 않았다. 기존 앱 전체 navigation 통합은 독립된 #27 작업 브랜치와 겹치므로, 그 PR 상태 확인 전에는 통합하지 않는다.
- 임시 debug 전용 실행기로 A-48 상품 필터, A-49 전체 리뷰, A-55 이벤트 상세, A-56 마케팅 동의 화면을 emulator에서 띄워 Figma 390px 프레임과 비교했다. 비교 후 공통 상단바 제목 정렬·굵기, 필터 간격, 리뷰 별점 컨테이너·외곽선 별, 이벤트 상세 카드 높이·CTA 중앙 정렬, 마케팅 정보 카드 모양을 보정했다. 실행기는 캡처 후 제거하며 앱 navigation에는 추가하지 않았다.
- 사후 비교에서 리뷰 별 외곽선의 짙은 기본 텍스트 색을 Figma 별 외곽선에서 추출한 따뜻한 브라운(`ratingStarOutline`, `#A96F16`)으로 조정했다. 별 간격·좌우 패딩, 입력 필드 중성 경계색, 공통 상단바 제목 시작 위치도 Figma에 맞춰 보정했다. 수정 화면은 2026-10-22 에뮬레이터에서 같은 두 리뷰 문구를 넣어 다시 캡처해 확인했다.
- 최신 `dev` 기준 리소스 병합을 막던 `common_copy` 중복 문자열 정의 한 건을 제거했다. Figma UI와 무관한 기존 빌드 오류를 해소하기 위한 정리다.
- 대표 반응형 확인은 리뷰·필터 320dp, 이벤트 상세 약 800dp 태블릿 폭, 마케팅 동의 글꼴 배율 1.3 조건에서 진행했다. 리뷰 문구 줄바꿈과 동의 설명 확장은 잘렸거나 겹치지 않았다. A-01~A-57 전체 프레임 및 나머지 상태 변형을 emulator에서 모두 캡처한 것은 아니며, 기존 구현 화면 전체와 A-51·A-54 변형에 대한 시각 비교는 후속 통합 화면 확인이 필요하다.
- A-49 별점은 유효하지 않은 수치를 0점으로 대체하고 유한 점수는 0~5 범위로 제한한다. 화면 숫자, TalkBack 안내, 채워지는 별 개수는 같은 정규화 점수를 사용한다.

## #34 UI 점검 결과 (2026-11-09~10)

- 기준: `feature/#34-ui-review-animation`(origin/dev 400219d). 명세와 체크리스트는 `docs/MOTION_SPEC.md`. 아직 커밋 전이며, 실기기 확인이 끝나지 않았다.
- 반영한 항목:
  - 기본 강조색을 Figma 기준 `#111111` 토큰으로 전역 교체(사용자 승인). 기존 파란색 primary 제거.
  - 공용 버튼 `EumPrimaryButton`·`EumOutlinedButton`(radius 8, 최소 높이 56dp 확정, #38. 다이얼로그 버튼만 52dp 유지), 공용 `EumAlertDialog`(radius 12), 탈퇴·설정 다이얼로그에 적용.
  - 찜 색을 `heartActive`로 통일, 공용 `FavoriteToggleButton`을 홈·카탈로그·상세 5곳에 적용.
  - `surfaceContainer*`·`surfaceBright/Dim` 토큰 명시.
  - 장바구니: 품절 상품을 "주문 가능 상품" 집계에서 제외, 품절 배지(`dangerTint`), 비활성 스테퍼 테두리, 체크박스 비활성 색.
  - 하단 탭 라벨 "마이"로 통일, 탭 바 공용화.
  - 배송비는 금액에서 파생(0원 "무료", 금액 표시, 미상 "배송비 확인 필요"). 주소 화면의 고정 "무료" 문구 제거.
  - 결제 버튼 비활성 사유 문구 표시(결제 수단, 약관 동의, 영수증 정보 순).
  - 옵션 0원 행 숨김, 총액은 추가금 합계로 표시(0원이면 숨김). 옵션 미선택 시 담기·구매·견적 비활성과 안내.
  - 견적 문의 필수 항목(납품 지역, 희망 납기: YYYY-MM-DD, 오늘 이후) 검증과 오류 안내.
  - 드로어 열림 동안 상태 바 아이콘 어둡게 고정.
  - 담은 상품은 장바구니에 저장 줄로 반영(수량 변경·삭제 저장, 배지는 저장 줄 수량 합). 저장 줄 가격은 숫자 단가가 있을 때만 금액으로 계산하고, 모르면 "가격 확인 필요".
- 확인하지 않았거나 후속으로 넘긴 항목:
  - 주문(체크아웃) 화면은 장바구니와 무관한 샘플 상품을 보여 준다(후속).
  - 주문 내역의 "주문 상세"는 라우트가 연결되지 않아 이동하지 않는다(후속). 따라서 완료 주문의 "결제 금액" 라벨은 실기기에서 미확인.
  - 저장 장바구니의 배송비 금액 원천 없음(배송 정책 확정 후 후속).
  - 탭 재선택 시 맨 위로 스크롤(P0-2 일부)은 미구현.
  - 찜 버튼 테두리, 비활성 체크박스·disabled 색은 Figma 값 미확인 임시값.
  - 화면 전용 색 상수(#FFE9E9 등) 토큰화는 보류.
  - 견적 CTA(상세 `ProductDetailScreen`의 견적 버튼)는 명세 목록 밖이라 기존 스타일 유지.
- 서체: Pretendard 유지(사용자 결정). Figma 스타일가이드의 Noto Sans KR 표기와 다르므로 디자이너 전달 항목으로 남긴다.

## #38 디자인 토큰·공통 컴포넌트 일치

- 기준: `feature/#38-figma-design-tokens`(dev a43a664). Figma 변수(`178:369`)로 확인한 값이다.
- 색 토큰(Light): background #FAFAFA, textPrimary #171717, textSecondary #4A4A4A, textTertiary #666666, line #E5E5E5, lineStrong #8C8C8C, placeholderGlyph #A6A6A6, surfaceSecondary #F2F2F2, textDisabled #707070.
- 추가 확정 값: lineControl #777777, inverseSecondary #F2F2F2(hf/inverse-2), heartIdle #666666, checkboxChecked #171717, backgroundSubtle #FAFAFA(hf/bg-subtle, 공지·회사소개 노드). danger #C5221F, dangerTint #FDECEA는 기존 값과 같아 유지했다. `accentTint` #E7F5F1 신설. `colors.xml` hf_*와 `ic_close.xml`도 같은 값으로 맞췄다. 값이 같아진 토큰은 역할이 달라 병합하지 않았다.
- 타이포: `labelSmall`을 labelS(12/16 Medium)로 정정했다. headline·price 토큰은 호출부 교체를 화면 이슈로 넘겼고 Type.kt 주석에 의미만 적었다.
- CTA: `EumCtaButtonHeight` 56dp 확정. `UiAuditPrimaryButton`·`AuthPrimaryAction`이 같은 상수를 쓴다. `EumAlertDialog` 버튼은 Figma 값을 확인하지 못해 52dp(`EumDialogButtonHeight`)를 유지한다. 입력 필드 높이(48/52)는 불확정이라 바꾸지 않았다.
- 상단바: 제목 16/24 Medium(titleLarge) 가운데 정렬, 구분선 없음. `UiAuditScreenScaffold` 상단바 아래 구분선을 제거했다. `CatalogTopBar`는 좌우 액션 폭을 측정해 두 슬롯을 같은 폭으로 맞춰 제목이 가운데에 온다. HomeTopBar·AddressSearchTopBar는 제외했다.
- CTA 바 구분선 정책: `showBottomDivider`를 제거하고 CTA 바에 Surface 배경과 상단 1dp 구분선을 항상 그린다. 상단 구분선이 새로 생긴 화면은 ProductFilterScreen, OneToOneInquiryFormScreen, EventDetailScreen, ProductInquiryCompleteScreen, ProductInquiryScreen 5곳이다. 기존에 옵션을 켜던 ProductReviewScreen, DeliveryRequestScreen, InquiryHistoryScreen, PrivacyConsentDetailScreen, TermsDialogScreen은 구분선이 그대로이고 배경만 Surface가 된다.
- `UiAuditIconInfoCard`: 카드 r14, 아이콘 타일 36dp r11 accentTint, 글리프 20dp, 제목 14 SemiBold, 설명 13sp text-3(줄 높이 18sp는 추정). `EumRadius.x11`·`x14` 추가.
- 별 색: 카탈로그 카드의 채운 별을 warning에서 ratingStar로 바꿨다.
- 주의: `backgroundSubtle`(#FAFAFA)이 `background`와 같아져 채움만으로 구분하던 컨테이너가 사라질 수 있다. `AuthPrimaryAction` 비활성 배경은 #38에서 `surfaceSecondary`로 보정했고, 나머지 6곳(BrandScreens, OrderRequestComponents, OrderRequestFormScreen, OrderRequestEntryScreens, SearchResultScreens, MyPageScreen)은 #39/#40에서 후속 확인한다.
- 범위 밖(후속): 카드 그림자, 라디오·체크 커스텀, 필수 * 공용 컴포넌트, caption·가격·헤드라인 호출부 교체.

## #39 쇼핑 화면 Figma 세부 일치 (A-02~A-06)

- 기준: `feature/#39-figma-shopping-screens`(#38 27f135f 위). 노드 A-02 178:369, A-03 178:722, A-04 179:743, A-05 179:938, A-06 180:1374. Compose Preview 렌더와 실기기 확인은 하지 못했다(에뮬레이터 사용 금지).
- 카탈로그 카드: 모서리 12, 그림자는 `Modifier.shadow(1dp)`와 `EumShadows.level1` 색으로 근사(blur·offset 정확 재현 불가), 이미지 모서리 6·그라디언트(#F2F2F2→#E8E8E8), 이미지↔정보·하단 10, 정보 행 간격 4·좌우 추가 패딩 0, 가격 `priceM`(할인가 danger, 가격 문의 textPrimary), 견적 배지 사각 4·높이 20·좌우 6·lineStrong·labelXs. 목록형 행의 backgroundSubtle 채움은 surfaceSecondary로 보정.
- 홈 카드: 배경 surface, 상품명 textPrimary, 가격 문의 priceM textPrimary.
- A-03: 아이콘 타일 44·r12·surface·line 테두리·그림자(0,4,16) 근사, 아이콘↔텍스트 14, 행 높이 64. 제품/브랜드 세그먼트는 36dp 알약, 터치 48dp는 바깥 투명 영역이 맡아 컨테이너 세로 패딩을 0으로 둠(컨테이너 높이는 Figma 값 미확인 근사). 브랜드 행은 노드 값이 없어 유지.
- A-04: 칩 좌우 14·surface·lineStrong(공용 `CatalogSelectionChip`이라 즐겨찾기·검색 결과 칩도 같이 바뀜), 정렬 알약 좌 14·우 10·간격 4·쉐브론 14, 격자 행 간격 24.
- A-05: 밑줄형 탭(48dp), 상품명 h2, 가격 priceL, 별 28dp·간격 7(2색), 정보 상자·표(라벨 72/96), 그룹 간격, 섹션 사이 8dp 띠(색 미확인, surfaceSecondary 가정), 하단 바(패딩 20/12·상단 구분선·찜 56). 할인 색 #DB2129와 채운 별 면 #E6A93D(노드 179:938, 토큰 ratingStar #F5B301과 다른 승인 편차)와 할인 #DB2129는 Figma 직접 지정 값이라 화면 전용 private 상수로 두었고 토큰은 만들지 않았다. 채운 별 윤곽은 `ratingStarOutline`(#A96F16), 빈 별은 surface 면·lineStrong 윤곽 토큰을 쓴다(#38의 녹색 리터럴 제거 합의 유지). 정가 #7A7A7A는 대비 때문에 textTertiary 유지.
- A-06: 입력창 44·surface·1.5dp 테두리, 최근 검색어 FlowRow 칩(보이는 36·터치 48), 그룹 사이 전폭 구분선, 인기 검색어 열 간격 16·순위 폭 20·1~3위만 textPrimary. 빈 결과 화면은 Figma 프레임을 찾지 못해 변경하지 않았다.
- 터치 48 우선으로 Figma 시각 값과 달라진 곳: 전체 삭제·인기 검색어 행(40→48), 최근 검색어 칩 세로 간격(8→시각 12), 검색 입력의 지우기 버튼 44.
- 사후 검토 반영: 검색창 BasicTextField 최소 높이 44dp 복원, 상세 하단 바(일반·견적)는 배경 뒤에 navigationBarsPadding을 적용해 인셋 구간도 surface로 이어짐, 상세 탭은 IntrinsicSize.Max로 밑줄 y를 모든 탭에서 동일하게 맞춤, 검색 결과 없음 원 backgroundSubtle을 surfaceSecondary로 보정.
- 미보정으로 남은 backgroundSubtle 사용처(후속): 홈 배너(테두리 있음), BrandScreens(#45), 리뷰 작성·주문 신청 화면(#40/#46), 마이페이지(#40).
- Preview 추가: 카탈로그 카드(할인·견적·품절), 상세 탭 fontScale 1.5, 검색창 입력 있음·없음. 렌더는 확인하지 못했다.

## #43 주문 내역·혜택 화면 Figma 세부 일치 (A-16~A-22)

- 기준: `feature/#43-figma-orders-benefits-screens`(#38 커밋 27f135f 위). Figma 도구 없이 명세 값만 사용했고, 시각 확인은 Preview(`OrdersFigmaPreviews.kt`, `BenefitPreviews.kt`)로만 했다. Preview 렌더 결과는 눈으로 확인하지 못했다(컴파일만 통과).
- 반영: A-16 칩 36dp·14/20·가로 14(근사)와 카드 이름 16/24·가격 priceM·버튼 S 36(M3 Button 내부 최소 높이 40dp 때문에 Surface로 직접 구성, 터치 48 유지, 근사). A-17 상단·카드 간 16, 상품명 16/24·가격 priceM, 총 결제 금액 priceL, 문서 메뉴 박스 안 56dp MenuRow(근사). A-18 상품 박스 테두리·r12·썸네일 48·이름 16/24, 공용 `NoticeInline`, 진행 스트립 트랙 2dp·라벨 11/14(근사). A-19 사진 칸 72, 스테퍼 시각 36(터치 48), 체크 16, 라디오 카드 56(근사). A-20 정렬 텍스트 링크+chevron-down, 행 간격 20. A-21 카드 패딩·gap·r6·아이콘 32, 제품명 1줄, 평점 줄 상시 표시, 견적 배지, 품절 오버레이, priceM, 목록 리듬, 노티스 가로 14. A-22 티켓 노치·절취선·본문/스텁 치수·타이포, 등록 행·포인트 링크(coins+chevron)·세그먼트 46.
- Figma에 없어 유지하고 결정을 넘긴 요소: A-17 상품 섹션 접기 TextButton, A-21 우상단 삭제 X 버튼(Figma는 찜 44), 단가·수량 줄.
- 값 미확정으로 추정한 항목: 절취선 파선 간격 4/4dp(imgPerforation 자산 없음), 견적 배지 배경(surface), 품절 배지 색(기존 dangerTint 유지), A-19 "라디오 카드 56"의 대상(사유 선택 칩에 적용), 쿠폰 카드 최소 높이 132 유지.
- 미확정·보류: A-16 빈 상태 제목, A-17 주문일 제목, A-18 도착 예정 제목은 H2(22/30) 자리인지 명세에 없어 headlineSmall 유지(T2 미적용). 세그먼트 탭 클릭 영역은 컨테이너 높이 46dp 전체를 쓰며 48dp보다 2dp 작다(Figma 값 우선, 시각 박스 36dp는 안쪽).
- backgroundSubtle 보정: `OrderRequestFormScreen`의 교환 옵션 카드 채움을 `surfaceSecondary`로 바꿨다. 이 화면의 다른 backgroundSubtle 사용은 없다. OrderRequestComponents·OrderRequestEntryScreens는 #46 범위라 건드리지 않았다.
- 공용 컴포넌트: `ui/common/NoticeInline.kt` 신설(A-18·A-21). `CatalogProductCard`·`CatalogSelectionChip`·`EumSpacing`은 변경하지 않아 10/14dp 등은 화면 안 상수로 뒀다(#39 병합 시 토큰으로 교체 가능).
- 사후 검토 반영: 쿠폰 노치는 지름 12(반경 6) 유지(Figma '상하 12px 원'), CouponTicketShape는 remember로 재생성 방지, 할인 수치 줄은 FlowRow로 좁은 폭·큰 글자에서 배지가 아래 줄로 넘어감, 스텁 텍스트 정렬은 Figma 미확인이라 현행 유지. 최근 본 상품 평점 줄은 평점(+리뷰 수) → subtitle → "평점 없음" 순서이며(CatalogProductCard는 subtitle과 평점을 별도 줄로 표시해 규칙이 완전히 같지는 않다), 견적 배지는 가격이 견적 문의일 때만 표시한다.
- 세그먼트 Row는 `heightIn(min=46).height(IntrinsicSize.Min)`(Row IntrinsicSize.Min)이라 탭 fillMaxHeight가 남은 높이를 다 먹지 않고, 큰 글꼴에서는 높이가 늘어난다.

## #44 견적·고객지원·알림·설정·공지 화면 세부 일치

- 기준: `feature/#44-figma-support-notice-screens`(#38 커밋 27f135f 위). Figma 노드 A-15 182:2696, A-25 358:4125, A-31 480:5124, A-32 480:5502, A-35 655:7083, A-40 657:35718, A-40z 657:35872, A-41 657:36024, A-42 661:8066, A-43 663:8212·879:8767, A-44 879:8789. 구현 명세에 적힌 값만 반영했고 확인하지 못한 값은 추정으로 채우지 않았다.
- A-15 견적 문의: 헤드라인 H1(headlineLarge), 개요 오버라인 `EumTypographyTokens.overline`(0.16em), 진행 안내를 높이 40·r12·테두리 line의 4단계+chevron-right 12를 FlowRow(가운데 정렬, 단계 단위 줄바꿈)로 교체(문구는 `quote_progress_step_*` 신설, 접근성 설명은 기존 `quote_breadcrumb`), 문의 유형 14dp 커스텀 라디오(FlowRow), 문의 상품 카드 패딩 16·gap 12·제목 title(16/24)·썸네일 48 r6, 첨부 박스 높이 96·r12·lineControl, 필드 간 20.
- A-25 견적 내역(근사): 칩 시각 36dp·터치 48dp, 빈 상태 제목 headlineSmall.
- A-31 문의 내역: 카드 r12, 제목 16/24 1줄 ellipsis, 메타 caption textTertiary, 본문 14/20 textTertiary 1줄, 상태 배지 h20 px6, 필터 칩 시각 36 px14(A-42 공유), 칩↔목록 gap 16·본문 상 16/하 32.
- A-32 알림: 행마다 배경(읽지 않음 surface·읽음 background)+1px line, 좌우 20·상하 16(행은 scaffold 좌우 여백을 쓰지 않고 화면 폭을 채움), 제목 14/20(안읽음 Medium textPrimary·읽음 Regular textSecondary), 메타 간 6·텍스트 열 gap 4, 아이콘 textPrimary, 점 6×12 래퍼, 섹션 헤더 labelMedium pt16·pb8, 요약 행 bodySmall·py8·'모두 읽음' 시각 36 px12(터치 48), 하단 안내 caption·py24.
- A-35 설정: 그룹 라벨 labelMedium textSecondary, 설명·부가 caption(13sp 오버라이드 제거), 앱 버전 값 14/20 textTertiary, 회원 탈퇴 caption 밑줄, 스위치 행 최소 56, 본문 하단 40. 파일 구조가 같아 최소 hunk로 적용했다(#37과 충돌 가능).
- A-40~A-41 공지: 목록 제목 bodyMedium 최대 2줄 ellipsis, 고정 배지를 `NoticePinnedBadge`(h20 px6 r4 11/14 Medium)로 추출해 목록·상세가 공용, 고정 행 backgroundSubtle·일반 행 surface, 날짜 caption. 빈 상태는 상단 정렬(pt120·pb32), 원 64 surface+`ic_search_x` 28, 제목 headlineSmall '등록된 공지사항이 없습니다', 설명 문구 제거(`notice_empty_description` 삭제). 상세 본문 bodyMedium textSecondary, 날짜는 데이터 모델에 있어 유지하고 caption 적용.
- A-42 고객센터: FAQ Q(title, 폭 32)·질문 bodyLMedium 16/26, 답변 박스 r8·p24·A Medium textTertiary·본문 bodyMedium·질문 바로 아래·아래 spacer 24, 연락처 카드 gap 12·시간 caption, 검색바 시각 44·터치 48(BasicTextField 기반 `CustomerSupportSearchField`)·힌트 bodyMedium, 섹션 gap 32.
- A-43 회사 소개: eyebrow overline 토큰, 히어로 제목 h2 줄바꿈(`company_tagline_home`에 `\n` 추가), 히어로 설명 bodyMedium, 히어로 gap 16·pt16/pb32, 앵커 칩 띠 높이 56·surface·위아래 1dp line, 칩 시각 36 px14 capsule(개요 칩 고정 선택, 스크롤 연동은 범위 밖이라 탭으로 선택이 옮겨지지 않음), 사업 분야 설명 caption, 연혁 설명 caption textTertiary·간격 2, 정보 행 라벨 14/20 textTertiary, 파트너 박스 surface·caption, 지도 비율 1.776·세로 그라디언트(imagePlaceholderStart→End)·r12, 지도 앱 버튼 gap 6·아이콘 18·lineStrong 테두리, 마지막 섹션 구분선 없음·pb40. 코드에만 있는 '1:1 문의하기' 행은 제거하지 않고 유지했다.
- A-43 1:1 문의 작성·A-44 상품 문의: 안내 문구 색 textTertiary(13sp 토큰이 없어 14 유지), 필드 라벨 13sp SemiBold·라벨 상단→필드 상단 24, 필드 간 18.
- backgroundSubtle: 고정 공지 행만 사용하며 값이 background와 같아 채움으로 구분되지 않지만 행 구분선(line)으로 구분된다. 추가 보정은 없다.
- 보류·미확인: 주요 버튼 15/22 SemiBold(`UiAuditPrimaryButton`은 #38 완료분이고 여러 화면이 공유해 재수정하지 않음), '모두 읽음' 버튼의 채움·테두리, 라디오 선택 표시(안쪽 점 6dp·테두리 1dp는 임시), 문의 유형 라디오 gap 12(가로)/세로 0(터치 48 우선), 알림 요약 행 'px20/12'의 12, 알림 행 사이 테두리 겹침(2dp), 라벨 줄 높이 19sp(24-5 계산), 진행 안내 FlowRow의 320dp·fontScale 1.5 실제 줄바꿈 렌더, A-44 다른 상태 변형, 연혁·소개 외 화면 Preview 렌더 확인.
- 사후 검토 반영(1회차): 회사 소개 개요 칩 고정 선택으로 단순화, 견적 진행 안내를 FlowRow로 바꿔 320dp·큰 글자에서 단계 단위 줄바꿈, FAQ 검색 입력은 pointerInput+FocusRequester로 터치 영역을 넓히고 힌트를 필드 내부 장식으로 이동, 견적 라디오에 selectableGroup·selectable 적용, 문의 작성 화면 공용 상수를 `InquiryFormMetrics.kt`로 통합.
- 후속(보류): '모두 읽음' 버튼 r8·채움 Figma 확인, `UiAuditPrimaryButton` 15/22 SemiBold는 공용 컴포넌트라 별도 이슈, 알림 행 테두리 겹침, 라디오 선택 표시 Figma 값 확인.

## #45 브랜드·정책·이벤트 화면 Figma 세부 일치 (A-33·A-34·A-36~A-39·A-54~A-57)

- 기준: #38(27f135f) 위. Figma 값은 이슈 명세에서 받은 값만 썼고, 구현 중 Figma를 직접 열어 보지 않았다. Preview·실기기 렌더는 확인하지 못했다(컴파일·빌드만 확인).
- 공통: `BrandLogoPlate`(흰 판 r8+테두리, 안쪽 로고 r4)를 `catalog/components`에 추출해 배너 64/48, 목록·다른 브랜드 행 56/44, 상세 120×48/88×32에 쓴다. 정책 조항은 `PolicyClauseArticle`·`PolicyChapterGroup`으로 통합해 `PolicyDocumentScreen`과 `TermsDialogScreen`이 같이 쓴다(TermsDialog의 중복 `TermsArticle` 제거).
- A-33: 세그먼트(테두리 r8·패딩 4·세그먼트 36 r6·14 Medium·미선택 textSecondary), 배너(py24·gap16·이름 h3), 툴바(surface+하단 line, 검색바 44 capsule·bodyM, 정렬 44 r8 lineControl, gap 8), 섹션 헤더(h2·하단 lineStrong·섹션 간 32), 행 72(구분선 line·화살표 20). 시각 36·44 요소는 바깥 48dp 박스(터치·선택 의미)와 안쪽 시각 박스 구조로 터치 48을 보장한다.
- A-34: 히어로 220, 소개 영역(pt24 pb32 gap20, 이름 h1, 소개 bodyM 3줄), 스토리(pt48·제목 h2·6줄 clamp+더 보기/접기 토글은 로컬 상태), facts(라벨 88 14/20 textTertiary·값 Medium·행 하단 line), 제품 헤더 h3·칩 36(px14, 선택 textPrimary+line 테두리, 미선택 lineStrong)·그리드 세로 24·가로 12·섹션 py48, 다른 브랜드(단일 r12 카드·행 72·구분선).
- A-36~A-39: 문서 헤더(meta-row+하단선 pb20, 이전 약관 버튼 36 r8), 목차(제목 Medium·개수 caption 제목 오른쪽·아이콘 20), 조항(제목 bodyLMedium·본문 bodyM·gap 12/8/24/32), dl-card r12·w88·gap 12, steps(원 24·제목 labelM·gap 2), 버전 시트(상단 r12·그래버 36×4·헤더 56+X 44·행 56·라디오 14·선택 행 labelL·현재 배지 h20), 약관 동의(Body py24 gap24·caption Medium)·동의서(gap24·카드 r12 패딩 16·내부 gap 8·caption).
- A-54~A-57: 안내 문구 색 textTertiary(14 유지), A-57 카드 minHeight 72, A-56·A-55 카드 80은 기존과 일치해 변경 없음.
- backgroundSubtle 소실 보정: 브랜드 검색 캡슐은 surfaceSecondary+line 테두리, 다른 브랜드 띠는 surfaceSecondary.
- 보류(값·모델 없음, 구현하지 않음): A-33 가나다/ABC 토글·인덱스 칩, A-34 히어로 CTA 교체·'제품 00개 보기'·'더 보기 (6/00)'·'BRAND A · 개인보호장비' 보조 줄·'전체 브랜드 보기' 링크, A-38 '고객센터 가기', A-36o·A-36u, A-54 trailing 아이콘. 버전 시트 그림자(-8/32/-4 16%)는 ModalBottomSheet에 맞는 적용 방법이 없어 보류.
- 편차(터치 48 보장): 바깥 48dp 박스 때문에 세그먼트 컨테이너와 툴바 행이 Figma보다 각각 약 12dp·4dp 높고, 칩 줄 간격도 Figma보다 커진다.
- 접근성: 스토리 더 보기 토글에 펼쳐짐/접힘 stateDescription, 세그먼트·버전 시트 행에 selectable(Tab·RadioButton) 의미, 시트 그래버는 `BottomSheetDefaults.DragHandle`을 쓴다. 스토리 펼침 상태는 `rememberSaveable(brand.id)`.
- 가정: A-39 시행일 색은 값이 없어 textDisabled 유지. 다른 브랜드 "72 타일" 해석(56/44 판의 72dp 행)은 Figma 확인 필요.
- 후속(보류): 리뷰 S3·S4·S7.
- 추정값: 버전 시트 라디오 선택 점 6dp, 다른 브랜드 카드 행은 목록 행과 같은 56/44 판, 칩→그리드 간격은 그리드 행 위 24.

## #46 주문 요청·주소·필터·리뷰 화면 Figma 세부 일치 (A-46~A-53)

- 기준: `feature/#46-figma-order-request-screens`(#38 27f135f 위). Figma 도구 없이 이슈 명세 값만 사용했다.
- 공용: `UiAuditRadioIndicator`(20dp 원, 선택 textPrimary 채움, 미선택 흰 채움+1.5dp 링 #A0A0A0=addressPlaceholder 값). 행 전체 `selectable`이 selected·RadioButton 시맨틱을 맡는다. `UiAuditTextField`에 `contentVerticalPadding` 매개변수(기본 12dp 유지)를 더했다.
- A-47 배송 요청: 안내 caption textTertiary, 옵션 카드 테두리 제거·14/20, 상세 요청 입력 항상 표시(라벨 13→labelMedium 14 대체), 입력 패딩 14, 글자 수 caption 오른쪽 정렬, 본문 하단 32.
- A-48 필터: pt16·섹션 간격 12·라벨-칩 12, 제목 labelMedium, 칩 r17·px14(터치 48 유지), 정렬 카드 테두리 제거·공용 라디오. 칩 글자 13 Medium 토큰이 없어 labelMedium 14 유지.
- A-50~A-53: A-50 안내 textTertiary·라벨 labelMedium·값 14 Regular·입력칸 px14·버튼 r10, A-51 제목 headlineSmall(좌우 3dp 로컬 상수)·`OrderInfoCard` 옵트인 매개변수(`contentVerticalPadding` 14·`contentSpacing` 10, 기본값은 기존 16·0이라 OrderDetail·DetailCard·SelectableRequestProduct·EntryScreens·결제 내역 카드는 변경 없음, 신청 상세·철회 화면만 지정)·상품 카드 아이콘 24·`RequestInfoRow` 아이콘 20/라벨 폭 최소 72 Medium/값 15/24/세로 중앙, A-52·A-53 철회 카드 행 4개(값은 nullable 매개변수 `requestKindLabel`·`productLabel`·`requestDateLabel`로 받고 없으면 행을 숨김, 샘플 값은 debug 프리뷰에만 둠, 본문 세로 스크롤, 행 gap 은 명세에 없어 A-51 과 같은 10으로 통일)·본문 py16·A-53 아이콘 64/32·제목 headlineSmall.
- A-49 리뷰 등록: 상품 이미지 그라디언트 토큰 대체·아이콘 32, 작성자 카드 r10·아바타 surfaceSecondary·뱃지 22/14/accent(#0A6E57 로컬 상수)·구분선, 평점 제목 headlineSmall·gap 8, 입력창 BasicTextField(132h r10 패딩 14 테두리 line), 사진 박스 r10·아이콘 28, 제출 `EumPrimaryButton`(56·비활성 surfaceSecondary)·하단 바 surface+구분선+pb16.
- backgroundSubtle 보정: `HubActionCard`(surface+테두리), `OrderRequestEntryScreens` 안내 박스(surfaceSecondary). `OrderRequestFormScreen.ExchangeOptionCard`는 #43 소관이라 미수정.
- 보류: A-46 자간 0(선택), A-51 철회 완료 CTA 제거(동선 때문에 유지), A-53 상단바 뒤로가기 없음(공통 상단바 유지), A-50·A-51 변형 HF 프레임 미확인.
- 리뷰 등록 후속 처리: 입력창 contentDescription 을 제거해 입력 텍스트가 읽히게 했고 placeholder 시맨틱은 유지. accent(#0A6E57, 대비 약 5.5:1)는 화면 로컬 상수이며 TODO 로 accent 토큰 승격과 `EumDesignTokenTest` 추가를 후속에 남긴다.
- 선택 그룹: 라디오 목록 Column·필터 칩 FlowRow 에 `selectableGroup()` 적용, 필터 라디오 행 gap 8.
- Preview: A-48·A-49 는 `ProductFilterPreviews.kt`·`ProductReviewWritePreviews.kt`(debug)에 이미 있다. 철회 화면 샘플 값은 `OrderRequestPreviews.kt`에서 넘긴다.
- 후속(이번 이슈 보류): A-53 상단바 뒤로가기 제거 여부, 13sp 타입 토큰, accent 색 토큰, 철회 4행 실제 데이터 연동(#41 이후), 라디오 미선택 링 대비 2.61(선택 여부는 채움 유무로 구분 중).
