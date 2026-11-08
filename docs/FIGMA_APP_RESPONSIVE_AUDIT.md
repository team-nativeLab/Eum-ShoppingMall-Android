# Figma Android App 반응형 대응 현황

## 기준

- Figma 파일 `N3Ic9TfSnUBVMgAuayBVly`, `06 Design — App` (`178:345`), 화면 기준 390px.
- 화면 및 node ID 목록은 기존 `feature/#28-figma-app-full-ui-audit:docs/FIGMA_APP_UI_AUDIT.md`를 읽기 전용으로 참조했다.
- 구현 목표 폭은 320dp / 390dp / 800dp, 큰 글자는 Preview fixture에 fontScale 1.3 및 1.5를 제공한다.
- 새 화면의 독립 Compose 구현은 화면 표시와 기존 콜백 형태까지만 포함하며 앱 navigation/API 연결은 제외한다.

## 프레임 대조표

| 프레임 | Figma node | Compose 화면 / 파일 | 상태 및 대응 |
|---|---:|---|---|
| A-01 스플래시 | `178:350` | `SplashScreen`, `splash/SplashScreen.kt` | 기존 화면; 반응형 Preview fixture 적용 |
| A-02 홈 | `178:369` | `HomeRoute`, `home/HomeScreen.kt` | 기존 화면; 기존 Figma 기준 캡처 이력, Preview fixture 적용 |
| A-03 카테고리 | `178:722` | `CatalogCategoryScreen`, `catalog/CategoryScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-04 상품 목록 | `179:743` | `CatalogProductListScreen`, `catalog/ProductListScreen.kt` | 기존 화면; scaffold max-width 반응형 패턴 적용 |
| A-05 상품 상세 | `179:938` | `CatalogProductDetailScreen`, `catalog/ProductDetailScreen.kt` | 기존 화면; max-width 반응형 패턴 적용 |
| A-06 검색 | `180:1374` | `CatalogSearchScreen` / `CatalogSearchResultsScreen`, `catalog/Search*Screen.kt` | 기존 화면; Preview fixture 적용 |
| A-07 장바구니 | `180:1780` | `CartScreen`, `cart/view/CartScreen.kt` | 본 작업에서 반응형 영역 및 수량 조작 최소 터치 크기 조정 |
| A-08 주문 배송지 | `181:2106` | `CheckoutAddressScreen`, `checkout/view/CheckoutAddressScreen.kt` | 본 작업에서 폭·sticky action 대응 |
| A-09 주문 결제 | `181:2204` | `CheckoutPaymentScreen`, `checkout/view/CheckoutPaymentScreen.kt` | 본 작업에서 폭·sticky action 대응 |
| A-10 주문 완료 | `181:2335` | `OrderCompleteScreen`, `ordercomplete/view/OrderCompleteScreen.kt` | 본 작업에서 폭 대응 |
| A-11 마이페이지 | `181:2409` | `MyPageScreen`, `account/view/MyPageScreen.kt` | 본 작업에서 max-width 적용 |
| A-12 드로어 | `182:2416` | `AccountDrawerScreen`, `account/view/AccountDrawerScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-13 로그인 | `182:2524` | `AuthLoginScreen`, `auth/view/AuthLoginScreen.kt` | A-13 기본 및 A-13e 오류·좁은 화면 responsive Preview 확인 |
| A-14 회원가입 | `182:2601` | `AuthSignupScreen`, `auth/view/AuthSignupScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-15 견적 문의 | `182:2696` | `QuoteRequestScreen`, `quote/view/QuoteRequestScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-16 주문 내역 | `350:3346` | `OrderHistoryScreen`, `orders/view/OrderHistoryScreen.kt` | 본 작업에서 폭 및 long-content 대응 |
| A-17 주문 상세 | `351:3440` | `OrderDetailScreen`, `orders/view/OrderDetailScreen.kt` | 본 작업에서 폭 대응 |
| A-18 배송 조회 | `351:3570` | `DeliveryTrackingScreen`, `orders/view/DeliveryTrackingScreen.kt` | 본 작업에서 폭 대응 |
| A-19 반품/취소/교환 | `353:3555` | `OrderRequestFormScreen`, `orders/view/OrderRequestFormScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-20 찜 목록 | `354:3685` | `FavoritesScreen`, `favorites/FavoritesScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-21 최근 본 상품 | `354:3814` | `RecentProductsScreen`, `recent/RecentProductsScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-22 쿠폰함 | `355:3895` | `CouponScreen`, `benefits/CouponScreen.kt` | 본 작업에서 고정 높이 텍스트 잘림·최소 컨트롤 높이 조정 |
| A-23 내 정보 관리 | `350:3490` | `AccountProfileScreen`, `account/view/AccountProfileScreen.kt` | 비밀번호 재확인 gate Preview와 캡처 추가 |
| A-24 배송지 관리 | `357:4081` | `AccountAddressListScreen` / `AccountAddressFormScreen`, `account/view/AccountAddressScreens.kt` | 본 작업에서 폭 대응 |
| A-25 견적 문의 내역 | `358:4125` | `QuoteHistoryScreen`, `quote/view/QuoteHistoryScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-26 탈퇴 안내 | `458:4210` | `AccountWithdrawalGuideScreen`, `auth/view/AccountWithdrawalGuideScreen.kt` | 기존 구현 확인; 반응형 런타임 비교 미수행 |
| A-27 이메일 찾기 | `479:4567` | `AccountRecoveryScreen`, `auth/view/AccountRecoveryScreen.kt` | 기존 구현 확인; 반응형 런타임 비교 미수행 |
| A-28 비밀번호 찾기 | `479:4974` | `AuthPasswordResetScreen`, `auth/view/AuthPasswordResetScreen.kt` | 기존 구현 확인; 반응형 런타임 비교 미수행 |
| A-29 비밀번호 변경 오류 | `480:4806` | `AuthPasswordChangeScreen`, `auth/view/AuthPasswordChangeScreen.kt` | 기존 구현 확인; 오류 변형 Preview 비교 미수행 |
| A-30 휴대폰 번호 변경 | `480:4974` | `AuthPhoneNumberChangeScreen`, `auth/view/AuthPhoneNumberChangeScreen.kt` | 기존 구현 확인; 반응형 런타임 비교 미수행 |
| A-31 1:1 문의 내역 | `480:5124` | `InquiryHistoryScreen`, `support/InquiryHistoryScreen.kt` | 신규 독립 화면, 목록/빈 상태·sticky CTA, 320/390/800/큰 글자 Preview 추가 |
| A-32 알림 | `480:5502` | `NotificationScreen`, `support/NotificationScreen.kt` | 신규 독립 화면, 목록/빈 상태 Preview 추가 |
| A-33 브랜드 목록 | `616:12901` | `CatalogBrandListScreen`, `catalog/BrandScreens.kt` | featured brand/검색/문자별 목록 반응형 Preview 확인; 렌더 대조 미수행 |
| A-34 브랜드 상세 | `616:13032` | `CatalogBrandDetailScreen`, `catalog/BrandScreens.kt` | 2/3/4열 adaptive 상품 grid 및 Preview 확인; 렌더 대조 미수행 |
| A-35 설정 | `655:7083` | `AccountSettingsScreen`, `account/view/AccountSettingsScreen.kt` | 본 작업에서 폭 대응 |
| A-36 이용약관 | `656:7470` | `PolicyDocumentScreen`, `policy/PolicyDocumentScreen.kt` | 신규 공통 정책 문서 화면; 약관 Preview 추가 |
| A-37 개인정보 처리방침 | `656:8199` | `PolicyDocumentScreen`, `policy/PolicyDocumentScreen.kt` | 신규 공통 정책 문서 화면; 목차/본문/이전 버전 Preview 추가 |
| A-38 이용안내 | `656:8465` | `PolicyDocumentScreen`, `policy/PolicyDocumentScreen.kt` | 신규 공통 정책 문서 화면; 안내 Preview 추가 |
| A-39 약관 전체 화면 모달 | `657:7899` | `TermsDialogScreen`, `policy/TermsDialogScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-40 공지사항 목록 | `657:35718` | `NoticeListScreen`, `support/NoticeListScreen.kt` | 신규 독립 화면, 공지/빈 상태 Preview 추가 |
| A-41 공지사항 상세 | `657:36024` | `NoticeDetailScreen`, `support/NoticeDetailScreen.kt` | 신규 독립 화면; 긴 본문 scroll Preview 추가 |
| A-42 고객센터 | `661:8066` | `CustomerSupportScreen`, `support/CustomerSupportScreen.kt` | 신규 독립 화면, 검색/카테고리/FAQ Preview 추가 |
| A-43 회사 소개 | `663:8212` | `CompanyIntroScreen`, `company/CompanyIntroScreen.kt` | 신규 독립 화면; 회사 히어로는 `figma_company_about_visual.png`, 위치 영역은 Figma의 회색 이미지 플레이스홀더를 `ic_image`로 표현(지도 비주얼은 원본에 없음) |
| A-43 1:1 문의 작성 | `879:8767` | `OneToOneInquiryFormScreen`, `support/OneToOneInquiryFormScreen.kt` | 기존 독립 화면; 화면 이동은 제외 |
| A-44 상품 문의 작성 | `879:8789` | `ProductInquiryScreen`, `productinquiry/ProductInquiryScreen.kt` | 기존 독립 화면; Preview fixture 적용 |
| A-45 문의 등록 완료 | `879:8811` | `ProductInquiryCompleteScreen`, `productinquiry/ProductInquiryCompleteScreen.kt` | 기존 독립 화면; Preview fixture 적용 |
| A-46 주소 검색 | `1249:9195` (screen child `1249:9196`) | `AddressSearchScreen`, `addresssearch/AddressSearchScreen.kt` | 결과/빈 상태 각 320/390/800dp·fs1.0 및 320dp·fs1.3/1.5 런타임 캡처. 390dp Figma 시각 대조 완료; 320dp·fs1.5 긴 주소 줄바꿈/클리핑 확인 완료 |
| A-47 배송 요청사항 | `1249:9282` (screen child `1249:9283`) | `DeliveryRequestScreen`, `checkout/view/DeliveryRequestScreen.kt` | 문 앞 선택/직접 입력 상태 각각 320/390/800dp·fs1.0 및 320dp·fs1.3/1.5 런타임 캡처. 390dp Figma 시각 대조 및 320dp·fs1.5 옵션·입력·CTA 클리핑 확인 완료 |
| A-48 상품 필터 | `1249:9376` (screen child `1249:9377`) | `ProductFilterScreen`, `productfilter/ProductFilterScreen.kt` | 기본 상태 320/390/800dp·fs1.0 및 320dp·fs1.3/1.5 런타임 캡처. 390dp Figma 시각 대조 완료. pill 높이 보정 후 320dp·fs1.5/390dp·fs1.0/800dp·fs1.0 재캡처; 확대 글자 클리핑 없음 |
| A-49 전체 리뷰 | `879:8887` | `ProductReviewScreen`, `ProductReviewUiModel` / `ProductReviewItemUiModel` | 필수 상품·리뷰 ID 및 작성자/구매인증/날짜/개별별점/옵션/본문/사진 resource 입력을 구조화해 카드에 표시; 예시 데이터·샘플 이미지는 debug Preview 전용 |
| A-50 신청 접수 완료 | `879:8904` | `OrderRequestResultScreen`, `orders/view/OrderRequestStatusScreens.kt` | 기존 구현; Preview fixture 적용 |
| A-51 신청 상세 | `879:44641` | `OrderRequestDetailScreen`, `orders/view/OrderRequestHistoryScreens.kt` | 기존 구현; 취소 완료 변형 `879:45279` Preview 추가, 실제 렌더 비교 미수행 |
| A-52 신청 철회 확인 | `879:44666` | `OrderRequestWithdrawalConfirmScreen`, `orders/view/OrderRequestStatusScreens.kt` | 기존 구현; Preview fixture 적용 |
| A-53 신청 철회 완료 | `879:44682` | `OrderRequestWithdrawalCompleteScreen`, `orders/view/OrderRequestStatusScreens.kt` | 기존 구현; Preview fixture 적용 |
| A-54 이벤트 목록 | `879:44698` | `EventListScreen`, `events/EventListScreen.kt` | 이벤트 목록 Preview 존재; 반려 변형은 `OrderRequestRejectionScreen`의 `927:45901` state Preview 존재, 실제 렌더 비교 미수행 |
| A-55 이벤트 상세 | `879:44715` | `EventDetailScreen`, `events/EventDetailScreen.kt` | 기존 화면; Preview fixture 적용 |
| A-56 마케팅 동의 | `879:44734` | `MarketingConsentScreen`, `policy/MarketingConsentScreen.kt` | 본 작업에서 긴 안내문과 정보 카드 반응형 보완 |
| A-57 오픈소스 라이선스 | `879:44754` | `OpenSourceLicensesScreen`, `policy/OpenSourceLicensesScreen.kt` | 본 작업에서 목록 카드 형태 보완 |

## 상태 프레임 인벤토리

2026-11-08 갱신 metadata 기준 canonical screen/state node는 110개다. 아래의 과거 표와 상세 설명 중 107개라는 수치는 이전 metadata snapshot(2026-11-07)의 역사적 inventory/context 수집 기록이며, 현재 canonical 범위와 혼용하지 않는다. 당시 상태·카드 래퍼를 포함한 이름 있는 관련 frame node는 219개였다. 현재 node별 Preview 연결은 [110-node manifest](FIGMA_APP_RESPONSIVE_MANIFEST.csv)를 기준으로 한다.

| 프레임 변형 | Node ID | 화면·상태 모델 | Preview / 대응 |
|---|---:|---|---|
| A-05h, A-05s, A-05sh | `179:1109`, `180:1142`, `180:1258` | `CatalogProductDetailScreen` | `CatalogPreviews.kt`의 상세/견적 옵션 상태 |
| A-06b, A-06z | `180:1511`, `180:1681` | 검색 결과/빈 결과 composable | `CatalogPreviews.kt` 결과/빈 결과 |
| A-07h, A-07h2 | `181:1738`, `181:1940` | `CartScreen` 선택·금액 상태 | `CartPreviews.kt` 목록/빈 상태 |
| A-11g | `182:2279` | `MyPageScreen` guest | `AccountPreviews.kt` guest |
| A-15d | `557:5389` | `QuoteCompleteScreen` | `QuotePreviews.kt` 완료 상태 |
| A-19a, A-19b | `879:44844`, `879:44922` | `OrderRequestFormScreen` cancel/return/exchange | `OrderRequestPreviews.kt` 3종 상태 |
| A-19c, A-19l, A-19s | `927:8508`, `353:3725`, `929:46104` | 요청 허브/내역/주문 선택 | `OrderRequestPreviews.kt` 허브·내역·주문 선택 상태 |
| A-19l 철회 후, A-19le | `879:45202`, `927:44448` | 요청 내역 | `OrderRequestPreviews.kt`의 A-19le 철회 후 신청 내역 빈 상태 Preview 포함 |
| A-19l cancel/return/exchange filters | `927:45466`, `927:45597`, `927:45728` | `OrderRequestHistoryScreen` 필터 상태 | Preview별 실제 렌더 비교 미수행 |
| A-16e, A-20e, A-21e | `927:44328`, `927:44560`, `927:44613` | 주문 내역/찜/최근 본 빈 상태 | `OrdersPreviews.kt` A-16e, `CollectionEmptyPreviews.kt` A-20e/A-21e 독립 responsive Preview |
| A-22p, A-22e, A-22pe | `355:4019`, `927:44673`, `927:44782` | 적립금/쿠폰/적립금 빈 상태 | `CollectionEmptyPreviews.kt` 적립금 목록/빈 상태, 쿠폰 사용내역 빈 상태/전체 빈 상태 Preview |
| A-23e, A-23-err | `357:3977`, `528:21835` | 프로필 수정/비밀번호 오류 | `AccountPreviews.kt` 수정·오류 상태 |
| A-24e, A-24e(empty) | `357:4147`, `927:44885` | 배송지 추가/빈 목록 | `AccountPreviews.kt` 입력·빈 목록 |
| A-25d, A-25e | `358:4230`, `927:44954` | 견적 상세/빈 목록 | `QuotePreviews.kt` 상세·빈 상태 |
| A-26r/c/d/b/r-pw | `458:4379`, `459:4287`, `459:4328`, `467:4358`, `467:4461` | 탈퇴 사유/최종 확인/완료/불가/비밀번호 오류 | `AuthPreviews.kt` 상태 변형 |
| A-27r/z, A-28n/d | `479:4725`, `479:4854`, `479:5127`, `479:5277` | 이메일 결과·없음, 비밀번호 재설정·완료 | `AuthPreviews.kt` 상태 변형 |
| A-31z/d, A-32z | `480:5262`, `480:5375`, `480:5756` | 문의 내역 빈 상태/문의 상세/알림 빈 상태 | `SupportAndPolicyPreviews.kt`에 빈/응답/대기 상태 추가 |
| A-09t/t-in/c/c-p, A-10v | `557:5580`, `557:5727`, `557:5906`, `566:26719`, `557:5483` | 결제 영수증·세금계산서·가상계좌 | `CheckoutPreviews.kt` 현금영수증 입력/세금계산서 직접·저장 사업자 정보/가상계좌 Preview |
| A-13e, A-14b | `558:5680`, `558:5787` | 로그인 오류/사업자 가입 | `AuthPreviews.kt` A-13e 오류/좁은 화면 및 A-14b 사업자 상태 Preview |
| A-23-err | `528:21835` | 프로필 비밀번호 오류 | `AccountPreviews.kt` 오류 상태 |
| A-35g/p/t/l | `655:7269`, `655:7397`, `655:7579`, `655:7760` | guest/알림 꺼짐/토스트/로그아웃 확인 | `AccountPreviews.kt` 대응 Preview |
| A-36o/u/v, A-39p | `656:7779`, `656:7989`, `657:7797`, `657:8011` | 이전 약관/개정 예정/버전 선택/동의서 | `PolicyDocumentScreen`의 previous/upcoming/version-picker Preview와 `PrivacyConsentDetailScreen` 동의 상세 Preview 추가 |
| A-40z | `657:35872` | 공지 목록 빈 상태 | `SupportAndPolicyPreviews.kt` 빈 상태 |
| A-42r/z | `661:8268`, `661:8437` | FAQ 검색 결과/검색 없음 | `SupportAndPolicyPreviews.kt`에 결과/빈 결과 Preview 추가 |
| A-43 1:1 문의 작성 | `879:8767` | `OneToOneInquiryFormScreen` | 기존 form Preview |
| A-49 리뷰 등록 | `1101:2145` | 리뷰 작성 상태 | `ProductReviewWriteScreen` 및 반응형 Preview 구현 |
| A-51 철회 완료, A-54 반려 사유 | `879:45279`, `927:45901` | 신청 상세 철회/반려 화면 | responsive Preview 존재; 실제 렌더 비교 미수행 |

## 기존 화면 소스(layout) 수동 재검토

`app/src/main/java/com/nativelap/eumshoppingmall/ui`의 Kotlin 소스 162개에 대해 viewport 크기 고정, 높이 고정, maxLines, 스크롤·Insets·IME, widthIn 및 adaptive column 패턴을 검색했다. 그중 diff가 없던 screen 소스 23개는 각각 layout 경로를 다시 읽고 320/390/800dp와 fontScale 1.3 이상에서의 고정 크기·긴 콘텐츠·버튼·키보드 가림 위험을 평가했다. 320/390/800dp 및 큰 글자 에뮬레이터 렌더는 수행하지 못했으며 평가는 코드 경로 검토다.

| source | 점검 결과 |
|---|---|
| `home/HomeScreen.kt` | BoxWithConstraints rail/bottom navigation 분기, 680/1080dp max width와 2~4열 grid, LazyColumn 확인; 추가 수정 없음 |
| `splash/SplashScreen.kt` | 256dp 고정 상단 간격이 낮은 viewport와 큰 글자에서 하단 문구를 가릴 수 있어 화면 비례 간격 + vertical scroll 적용 |
| `favorites/FavoritesScreen.kt` | 2/3열 adaptive grid, LazyColumn, bottom inset 확인; 추가 수정 없음 |
| `catalog/SearchScreen.kt` | scaffold/scroll/header/48dp controls 확인; 긴 인기 검색어는 ellipsis로 잘림 상태 명시 |
| `catalog/ProductListScreen.kt` | 2/3/4열 adaptive grid와 스크롤 목록 확인; 추가 수정 없음 |
| `catalog/CategoryScreen.kt` | 카테고리/브랜드 분리 LazyColumn 및 동적 행 높이 확인; 추가 수정 없음 |
| `benefits/PointsScreen.kt` | 긴 history는 LazyColumn, 필터 chips는 작은 너비/큰 글자에서 수평 스크롤하도록 보완 |
| `recent/RecentProductsScreen.kt` | 2/3열 adaptive grid 및 스크롤, 48dp 삭제 hit target 확인; 상품명·부제 ellipsis 명시 |
| `account/view/AccountDrawerScreen.kt` | 82%/최대 360dp drawer 확인; 긴 메뉴가 작은 높이에서 푸터를 밀 수 있어 메뉴 영역에 vertical scroll 추가 |
| `orders/view/OrderRequestFormScreen.kt` | scroll/하단 CTA 구성 확인; 320dp에서는 요청 유형/사유 chips를 2열로 감싸도록 적용 |
| `quote/view/QuoteCompleteScreen.kt` | LazyColumn 및 sticky actions 확인; 진행 단계 텍스트를 균등 폭, 두 줄 ellipsis로 적용 |
| `quote/view/QuoteInquiryDetailScreen.kt` | LazyColumn, 2개 하단 actions, 84dp 요약 라벨 뒤 값 wrap 확인; 추가 수정 없음 |
| `quote/view/QuoteRequestScreen.kt` | LazyColumn, IME 처리 bottom action, weighted field 행 확인; 선택 상품명/부제를 한 줄 ellipsis로 표시 |
| `quote/view/QuoteHistoryScreen.kt` | LazyRow filter, 빈/목록 상태 및 고정 하단 action 확인; 추가 수정 없음 |
| `auth/view/AccountWithdrawalGuideScreen.kt` | 안내 본문 scroll 및 고정 CTA 확인; 추가 수정 없음 |
| `auth/view/AccountWithdrawalCompletedScreen.kt` | 완료 요약이 좁은 높이/큰 글자에서 넘칠 수 있어 본문 scroll 추가 |
| `auth/view/AccountWithdrawalReasonScreen.kt` | 2열 선택지, scroll/IME-aware CTA 확인; 추가 수정 없음 |
| `auth/view/AuthPasswordChangeScreen.kt` | 본문 scroll, 키보드 및 하단 CTA 처리 확인; 추가 수정 없음 |
| `auth/view/AuthSignupScreen.kt` | 개인/사업자 폼 scroll, IME-aware CTA, weighted 입력·검증 행 확인; 추가 수정 없음 |
| `auth/view/AuthPasswordResetScreen.kt` | 긴 안내와 입력 필드 scroll, IME-aware CTA 확인; 추가 수정 없음 |
| `auth/view/AuthPhoneNumberChangeScreen.kt` | 인증 필드 scroll 및 IME-aware CTA 확인; 추가 수정 없음 |
| `auth/view/AuthPasswordUpdatedScreen.kt` | 짧은 완료 메시지와 하단 action이 800dp 타깃 높이에 들어가는 구성 확인; 추가 수정 없음 |
| `auth/view/AccountRecoveryScreen.kt` | 입력 폼 scroll/IME 및 결과 화면의 두 action 동적 높이 확인; 추가 수정 없음 |

A-46 주소 검색은 `1249:9195`(screen child `1249:9196`), A-47 배송 요청사항은 `1249:9282`(screen child `1249:9283`), A-48 상품 필터는 `1249:9376`(screen child `1249:9377`)로 최신 frame mapping을 반영했다. 이전 audit의 `879:8827`, `879:8843`, `879:8865`는 삭제된 legacy 참조다. 이 세 화면은 390dp Figma 시각 대조와 아래에 적은 런타임 캡처를 확인했다.

## 디자인 맥락 수집

역사 기록(2026-11-07): 당시 recursive metadata에서 `A-*` frame/card/component 계열 219개 중 이름이 `· HF`로 끝나는 화면/상태 프레임 107개를 확인했고, 그 107개에 get_design_context와 screenshot을 확보했다(93개 직접 조회, 14개 기존 context 재사용). 그 뒤 새로 고친 Figma metadata가 제공한 현재 authoritative canonical list는 110개다. 하위 카드 래퍼, component instance, section, 텍스트·아이콘·이미지·system/navigation bar 등은 별도 화면 상태가 아니므로 110개 canonical rows에 넣지 않았다. A-44/A-45/A-50~56처럼 HF 접미사가 없는 독립 화면 프레임은 현재 manifest list에 있는 경우에만 별도 canonical node로 다룬다. 실제 런타임 시각 확인은 A-06 기본 상태와 A-46~A-48의 표기된 상태·크기 조합에 한정된다.

## 반응형·데이터 신뢰성 후속 보완

- 회사 소개 앵커는 Figma처럼 390dp 기준 한 줄 가로 스크롤로 유지하고 각 chip을 최소 48dp로 만들었다. 회사 문의 링크도 전체 폭, 48dp 최소 높이 터치 영역을 갖는다. 고객센터 카테고리 chip은 48dp 높이와 `FlowRow`를 유지한다. 각 화면의 `@ResponsiveUiPreview`는 320dp/fontScale 1.3·1.5, 390dp, 800dp 설정을 포함한다. Preview 소스는 확인했으나 IDE에서 렌더링하지 않았다.
- A-43 위치 영역의 Figma screenshot에는 지도 이미지가 아니라 중앙 이미지 아이콘이 있는 중립 플레이스홀더가 있다. `figma_image_map.csv`의 회사 히어로 이외 위치 이미지 asset은 없어, 지도핀 임시 그래픽 대신 기존 `ic_image`를 사용했다.
- A-26r의 320dp/fontScale 1.5 캡처에서 첫 5개 탈퇴 사유 칩이 같은 `탈퇴 사유` 문구로 보인다. 이는 Figma `458:4379` 원본도 다섯 칩 모두 `탈퇴 사유`, 마지막 칩만 `기타`로 표기한 것과 일치한다.
- A-49 모델은 필수 `productId`, `productName`, 각 리뷰 `id`, 작성자, 구매인증 여부, 날짜 라벨, 개별 별점, 옵션 라벨, 본문, 사진 모델(`imageResId`, placeholder 여부)을 받는다. 상품 및 리뷰 카드는 이 입력값을 사용한다. Figma 원본 이미지 asset mapping에는 사용자 리뷰 사진이 없어 Preview에만 `ic_image` placeholder resource로 썸네일 크기/배치를 표시하고, 실제 사진 콘텐츠는 포함하지 않는다.
- 리뷰 사진은 카드 폭을 채우는 `LazyRow`에서 렌더링하며, 여섯 장 Preview로 320dp 폭에서 가로 오버플로 없이 수평 스크롤되는 구성을 확인할 수 있다.
- 문의 내역 필터, 알림 모두 읽음, 정책 목차 링크, 카테고리 탭, 검색 인기어/최근어/검색 취소, 브랜드 목록 모드·카테고리·정렬 control의 클릭 가능 modifier는 최소 48dp 높이로 설정했다. 가로 chip은 스크롤 컨테이너 안에서도 각 item의 touch bounds를 유지한다.

## 미확인 / 검증 한계

- A-01~57 모든 변형을 에뮬레이터에서 렌더링해 Figma와 픽셀 대조하지 않았다. 실제 기기 렌더링은 A-06 기본 상태 1개와 A-46~A-48의 5개 화면 상태 변형뿐이다. A-51 취소 완료, A-13 로그인 오류, A-54 반려 사유 Preview는 추가/확인했으나 실제 렌더 비교가 남아 있다.
- route-less Compose 렌더 캡처 harness를 `app/src/androidTest/java/com/nativelap/eumshoppingmall/ui/catalog/ResponsiveCatalogRenderTest.kt`에 추가했다. `CatalogSearchScreen`의 A-06 기본 검색 상태를 activity navigation 없이 직접 구성하고, 320/390/800dp × fontScale 1.0/1.3/1.5 조합 9개를 emulator-5554에서 실제 렌더·캡처했다. A-46~A-48 캡처까지 포함한 초기 캡처에서 6개 화면 상태 변형을 확인했다. 후속 11개 node-state 보정 캡처를 별도로 추가했으며, 어느 합계도 110개 Figma node 전수 검증을 의미하지 않는다.
- A-06 캡처 PNG는 `docs/figma-responsive-captures/search-{320,390,800}-fs{1.0,1.3,1.5}.png`에 별도 저장했다. 캡처를 눈으로 확인했으며, 320dp/fontScale 1.5에서 긴 인기어·하단 탭 라벨은 말줄임/줄바꿈되고 800dp에서는 넓은 화면 구성을 확인했다. Figma 원본과 픽셀 단위 시각 비교는 수행하지 않았다.
- A-46 결과 상태 캡처는 `address-search-results-{320,390,800}-fs1.0.png`와 `address-search-results-320-fs{1.3,1.5}.png`, 빈 상태 캡처는 `address-search-empty-{320,390,800}-fs1.0.png`와 `address-search-empty-320-fs{1.3,1.5}.png`이다. 두 상태 모두 요청된 5개 조합(320/390/800dp·fs1.0, 320dp·fs1.3/1.5)을 실제 캡처했다. 390dp에서는 입력 placeholder·도움말·결과 행의 구성과 줄바꿈을 Figma와 대조했다. 320dp·fs1.5에서도 긴 주소는 여러 줄로 표시되고 카드 안에 유지되어 추가 클리핑이 보이지 않았다.
- A-47 문 앞 선택 상태는 `delivery-request-door-{320,390,800}-fs1.0.png`와 `delivery-request-door-320-fs{1.3,1.5}.png`, 직접 입력 상태는 `delivery-request-custom-{320,390,800}-fs1.0.png`와 `delivery-request-custom-320-fs{1.3,1.5}.png`에 저장했다. 두 상태 각각 5개 조합을 캡처했다. 390dp에서는 선택 목록과 custom 입력 상태를 Figma 구조와 비교했고, 320dp·fs1.5에서도 옵션·입력 내용·카운터·저장 CTA가 화면 폭 안에 들어가며 텍스트 클리핑이 보이지 않았다.
- A-48 기본 필터 상태는 `product-filter-{320,390,800}-fs1.0.png` 및 `product-filter-320-fs{1.3,1.5}.png`에 저장했다. 390dp 캡처에서 카테고리·가격 pill의 선택 상태와 정렬 목록을 Figma와 시각 비교했다. 초기 320dp·fs1.5 캡처에서 pill 텍스트가 잘려 `heightIn(min = 34.dp)`로 수정한 뒤 320dp·fs1.5, 390dp·fs1.0, 800dp·fs1.0을 재캡처해 확인했다. 최종 320dp·fs1.5 캡처에서 pill 표면은 글자 크기에 맞게 늘어나고 텍스트 클리핑은 보이지 않는다.
- A-46~A-48의 최신 screen node/child ID는 각각 `1249:9195`/`1249:9196`, `1249:9282`/`1249:9283`, `1249:9376`/`1249:9377`이다. 삭제된 legacy IDs `879:8827`, `879:8843`, `879:8865`는 현재 mapping이 아니다. 초기 캡처에서 A-06 1개와 A-46~A-48 5개 변형을 렌더링했다. 이후 11개 canonical state node를 추가로 캡처했다. 110개 canonical node 전체의 개별 runtime 검증은 완료되지 않았다.
- 앱 내 신규 화면 navigation wiring 및 서버/API 연결은 요구대로 제외했다.
- 110개 canonical node 전체의 runtime 검증은 미수행이다. 초기 6개와 후속 11개 상태 node를 캡처했다. A-46~A-48의 800dp 캡처는 emulator의 wm size를 일시 변경해 수행했으며 실제 태블릿 기기의 런타임 검증을 뜻하지 않는다. 실행 뒤 기본 해상도로 복원했다.
- 새 캡처 harness 검증 명령 `./gradlew :app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.nativelap.eumshoppingmall.ui.catalog.ResponsiveCatalogRenderTest -Pandroid.testInstrumentationRunnerArguments.captureName=search-390-fs1.0 -Pandroid.testInstrumentationRunnerArguments.fontScale=1.0`는 emulator-5554에서 BUILD SUCCESSFUL이었다. 9개 조합은 동일 테스트의 `am instrument` 실행에서 각각 `OK (1 test)`로 확인했다.
- 최종 자동 검증 명령 `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:lintDebug :app:assembleDebug spotlessCheck`는 BUILD SUCCESSFUL로 완료됐다. 정적 whitespace 검사는 `git diff --check`로 별도 확인했다.

## 2026-11-08 후속 런타임 반응형 캡처

- 추가 sweep의 run manifest는 596행이며 576개 instrumentation case가 `OK (1 test)`, 20개는 `SKIP_EXISTING`(이미 존재하는 캡처 재사용)이었다. run manifest에는 서로 다른 Compose Preview 149개가 등록되어 있다. 이는 Preview 함수 × viewport/fontScale 실행 수이며 Figma frame 수와 다른 지표다. 2026-11-08 authoritative node 목록으로 [110-node manifest](FIGMA_APP_RESPONSIVE_MANIFEST.csv)를 작성했다(110 unique node rows, 110 fixture alias mapped, 0 `UNMAPPED`). `MAPPED`는 alias가 `@Preview` 또는 `@ResponsiveUiPreview` 함수로 실제 존재함을 뜻한다. Figma pixel fidelity를 뜻하지 않는다. 110개 node별 runtime evidence는 manifest `runtime_evidence`에 node→Preview→캡처 파일로 연결했다. 105개 node는 320dp/fs1.0, 320dp/fs1.5, 390dp/fs1.0, 800dp/fs1.0의 4개 파일이 있으며, 5개 overlay state node는 root 캡처만 존재하고 overlay는 미검증으로 표시했다. Contact sheet와 상세 캡처의 범위는 기존 sweep 기록을 따른다. sweep 상세 기록은 `/tmp/eum-responsive-all/visual_findings.md`, 실행표는 `/tmp/eum-responsive-all/runs.csv`다.
- 네 시각 결함 수정 후 갱신 캡처는 `/tmp/eum-responsive-all/recaptured/figma-ui-captures/`에 저장했다. `cart-mixed-{320-fs1.5,390-fs1.0,800-fs1.0}.png`, `cart-narrow-320-fs1.5.png`, `review-write-{320-fs1.5,390-fs1.0,800-fs1.0}.png`, `quote-complete-{320-fs1.5,390-fs1.0,800-fs1.0}.png`, `quote-detail-{320-fs1.5,390-fs1.0,800-fs1.0}.png`를 직접 확인했다.
- 캡처에서는 장바구니 금액이 수량 조절기와 세로로 분리되어 한 줄로 표시되고, 리뷰 사진 안내 및 추가 타일이 등록 CTA 위에 보인다. 견적 완료 화면에는 `문의번호` 라벨과 번호 값만 표시되며, 견적 상세의 문의번호 끝자리는 분리되지 않고 320dp sticky CTA는 읽기 쉬운 세로 배치로 표시된다. 이 결과는 Preview fixture의 지정 폭/글자 크기 렌더 확인이며, 실제 Figma 110 node 전체 매핑·pixel-perfect 비교·화면 상호작용 테스트 완료를 뜻하지 않는다.
- 해당 구현의 최종 검증 `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest :app:lintDebug :app:assembleDebug spotlessCheck`와 `git diff --check`는 통과했다. emulator는 1080x2340, density 440, fontScale 1.0으로 복구했다.

## 2026-11-08 authoritative frame-to-preview inventory

- 최신 Figma metadata에서 제공된 110개 canonical node ID를 `FIGMA_APP_RESPONSIVE_MANIFEST.csv`에 한 행씩 기록했다. node ID는 모두 unique하고 모든 alias는 실제 Preview 함수에 연결된다. `UNMAPPED` node는 없다.
- 사후 검토에서 확인한 state 매핑을 수정했다: `656:7470` 이용약관은 본문 fixture `TermsDocumentPreview`, `557:5580` 사업자 정보 표시 상태는 `CheckoutTaxInvoiceSavedPreview`, `557:5727` 직접 입력 상태는 `CheckoutTaxInvoicePreview`(ManualBusinessInformation)로 연결한다. `350:3490`은 `AccountProfileScreen`의 `isPasswordVerification = true` 분기를 표시하는 `AccountProfilePasswordVerificationPreview`로 연결하고, `357:3977` 편집과 `528:21835` 비밀번호 오류는 기존 별도 fixture를 유지한다.
- `350:3490`의 Figma context(2026-11-08 재조회)는 잠금 아이콘, 이메일 표시, 비밀번호 입력란, 확인 CTA가 있는 재인증 gate다. 새 gate Preview 캡처 4개는 `docs/figma-responsive-captures/canonical-350-3490-profile-gate-{320,390,800}-fs1.0.png` 및 `canonical-350-3490-profile-gate-320-fs1.5.png`에 기록한다. 320dp/fontScale 1.5에서도 요소가 가로로 넘치지 않지만 제목·설명은 여러 줄로 확장된다. Figma의 검정 CTA와 맞추기 위해 재인증 분기에만 `textPrimary` 색상을 적용했다. 다른 계정 sticky CTA는 기존 기본 색상을 유지한다.
- manifest의 `MAPPED`는 해당 Preview 함수가 `app/src`에 존재하고 Preview annotation이 있는지 확인한 결과다. 이는 함수 존재 확인일 뿐, 해당 Figma node와 시각적으로 동일하거나 기기 캡처했다는 증거가 아니다. 각 row의 `runtime_evidence`를 별도로 확인한다.
- 처음 `UNMAPPED`였던 9개 node에 화면 모델의 실제 variant를 반영한 Preview alias를 추가하거나 기존 direct-input fixture를 확인해 전부 연결했다. 추가로 A-07h2와 A-26r의 기존 잘못된 alias를 각각 주문 불가 상태와 탈퇴 사유 화면 전용 Preview로 교체했다. 최종 manifest는 110/110 alias mapping이다.
- 149개 Preview fixture, 596행의 sweep run manifest, 110개 Figma canonical node는 서로 다른 집계 단위다. 후속으로 매핑을 보정한 11개 state node를 각 320/390/800dp·fs1.0 및 320dp·fs1.5에서 호출해 44개 instrumentation capture를 생성했다. 이 중 A-05s/A-05sh 옵션 bottom sheet, A-26c 탈퇴 확인 AlertDialog, A-35l 로그아웃 AlertDialog, A-36v 약관 버전 picker bottom sheet의 PNG는 `captureToImage()`가 root Compose node만 저장해 overlay를 담지 못하거나, base screen only를 캡처했으므로 overlay pixel 확인으로 인정하지 않는다. 전체 110 node runtime 전수 검증은 아니다.


## 추가 canonical state fixture 캡처

- 최종 manifest 통계: 110 unique node IDs / 110 Preview alias mappings / 0 `UNMAPPED`; 105 node rows에 4-profile PNG 경로 존재, 5 overlay rows는 `UNVERIFIED_OVERLAY_CAPTURE`다. PNG 경로를 포함한 profile별 행이 440개이며 중복 alias가 공유하는 파일을 제거하면 424개 고유 파일이다. 5 overlay의 root 캡처 PNG는 overlay 자체 증거로 집계하지 않는다.

- 9개 이전 미매핑 상태(A-05h/A-05sh/A-24e/A-09t-in/A-35p/A-35l 및 A-19l 취소·반품·교환 필터)에 대해 source model을 확인하고, 없는 상태에는 debug-only Preview를 추가했다. A-09t-in은 기존 `CheckoutTaxInvoicePreview`가 `ManualBusinessInformation`을 사용해 이미 상태를 표현하므로 재사용했다. 추가로 A-07h2와 A-26r은 기존 alias가 정확한 상태를 나타내지 않아 각각 주문 불가 장바구니와 탈퇴 사유 Preview를 추가했다. 해당 화면의 production model/screen은 필요한 상태를 지원한다.
- 11개 보정 node-state의 320/390/800dp·fontScale 1.0 및 320dp·fontScale 1.5 조합 44개는 `emulator-5554`에서 `ResponsiveCatalogRenderTest`의 ComposableInvoker 인자로 직접 실행했고, 모든 호출이 `OK (1 test)`로 종료됐다. PNG는 `docs/figma-responsive-captures/canonical-*.png`에 저장했다. 320dp/fontScale 1.5 주소 입력 및 필터 결과, 반품 필터, 주문 불가 장바구니, 탈퇴 사유 캡처에서 화면 폭 밖으로 나가는 콘텐츠는 보이지 않았다. 배송지 추가·탈퇴 사유의 긴 본문은 sticky 하단 action 위의 scroll viewport 안에서 이어지므로 정적 한 장으로 스크롤 전체를 검증하지는 않았다.
- 실제 framebuffer를 동시에 캡처해 modal overlay를 보려는 시도는 시험 Activity가 화면 전면에 렌더되지 않아 launcher/blank surface만 얻었다. overlay pixel 검증을 하지 않았다. Manifest에서 `180:1142`, `180:1258`, `459:4287`, `655:7760`, `657:7797`은 `UNVERIFIED_OVERLAY_CAPTURE`로 표시한다. 해당 preview state는 model을 통해 켜졌으나 overlay 자체 렌더 결과는 확인하지 못했다.
- AVD는 size/density reset 후 1080×2340, 440dpi, system fontScale 1.0을 확인했다. runtime capture는 스크롤 제스처, IME 표시 중 입력, 전 화면 pixel comparison을 검증하지 않는다.
