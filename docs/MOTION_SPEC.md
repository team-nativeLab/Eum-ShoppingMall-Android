# 모션 명세 및 UI 점검 결과 (#34)

- 작성일: 2026-11-09
- 출처: 이전 세션 scratchpad의 `designer-audit-motion.md`(디자이너 UI 감사 + 모션 명세 P0~P2 + 사용자 답변), `figma-findings.md`(피그마 구조 조사 요약). 세션 종료 시 사라질 수 있어 이 문서로 옮김.
- Figma: fileKey N3Ic9TfSnUBVMgAuayBVly, `06 Design — App`(178:345)
- 관련 이슈: #34 (선행 #27, 범위 분리 #30)

## 1. 사용자 확정 사항 (다시 묻지 않음)
- 서체: Pretendard 유지. Figma·문서의 Noto Sans KR/시스템 서체 표기는 정정 대상(디자이너 전달 항목 포함).
- 기본 강조색(primary): 파란색(info) → Figma 기준 #111 계열로 전역 교체 승인.
- 담기 완료 후: Snackbar "장바구니에 담았어요 · 장바구니 보기" (안내 바 + 바로가기).
- 견적 문의 "납품 지역"·"희망 납기"는 필수. 둘 다 유효해야 접수 버튼 활성, 날짜 형식 오류 시 안내.
- 핵심 사용자: B2B 구매 담당자 + B2C 소비자. 가장 중요한 것: 빠르고 정확한 구매·견적.
- Navigation 3 등 의존성 추가 허용.

## 2. Figma 구조 요약
- 06 Design — App(178:345): 390폭, 약 120 프레임(A-01~A-57 + 변형). 래퍼 `A-xx card` 안에 `A-xx … · HF` 화면.
- 01 IA & Flow(72:3): 하단 탭 5개 = 홈 A-02 / 카테고리 A-03 / 검색 A-06 / 장바구니 A-07 / 마이 A-11. 드로어 A-12 = 이벤트·견적문의·고객지원·공지·회사소개.
- 플로우 F0~F7: F1 장바구니 구매, F2 검색(0건·네트워크 실패), F3 바로구매, F4 비로그인 인증 후 원래 화면 복귀, F5 마이페이지, F6 B2B 견적, F7 금액 미표시 상품 구매.
- IA는 디자인보다 오래됨(A-17/18/24, A-33~39, A-19 클레임, A-40~43 네이티브는 IA 밖). 프로토타입 transition은 확인 불가.
- 스타일가이드: 무채색, primary=#111, brand 색 없음. 서체 Noto Sans KR(앱은 Pretendard 유지). radius 4/6/8/12/999, sh-1~3. spacing 토큰 없음. **모션 정의 없음.**
- 정의된 상태: 빈 상태 다수, 로그인/비번 오류, 바텀시트(A-05s, A-36v, A-47/48 추정), 다이얼로그(A-35l, A-52, A-26c, A-39), 토스트 A-35t.
- 누락 상태: 로딩·스켈레톤, 네트워크 오류, 결제 실패, 품절·옵션 미선택 경고, 빈 장바구니(0개), 상품목록 0건, 담기 완료 토스트·찜 피드백, 앱 공용 Dialog, 세션 만료, 이미지 실패.
- 캔버스 문제(디자이너 전달): ID 중복(A-43 회사소개/1:1문의작성, A-54 이벤트/반려사유, A-24e, A-19l×4), 카드명 불일치(558:5679), 그리드 이탈(A-03 등), 잔여 프레임(929:46260, 906:8528).
- 시각 일관성(디자이너 전달): 품절 썸네일 placeholder 누락, 카드 가격 줄 정렬 불일치, 결제 선택 UI 혼용(RadioCard vs 원형), 라벨 불일치(결제예정금액/최종 결제금액, 문의/상품 문의), 결제 placeholder ₩00,000, 별점 스케일 차이, 할인가가 danger 색 재사용, 검색바 capsule vs 8~12 radius.

## 3. UI 점검 결과 (앱 코드 기준)

### Major
- **M1** A-05/05s CTA(바로 구매·견적 문의·옵션 시트)가 기본 Button = 파란 캡슐. Figma 기준 #111, radius 8, 찜 버튼은 48dp 1dp 라인 테두리 r8. → 공용 `EumPrimaryButton` / `EumOutlinedButton` (container=textPrimary, shape=EumRadius.x8, disabled=surfaceSecondary/textTertiary). 대상: `ProductDetailScreen.kt:501,531,708,716,728`
- **M2** 전역 CTA shape 미지정 → 캡슐. 대상: `CartScreen.kt:220`, `CheckoutAddressScreen.kt:58`, `CheckoutPaymentScreen.kt:66`, `OrderCompleteScreen.kt:67,73` → 공용 버튼으로 통일.
- **M3** `theme/Color.kt` `EumLightColorScheme.primary = info`(파랑) → RadioButton/TextButton/Switch/커서가 파랑. 권고: primary=textPrimary, onPrimary=inverse. (사용자 승인 완료)
- **M4** ColorScheme `surfaceContainer*` 미정의 → ModalBottomSheet/AlertDialog가 M3 기본 표면색을 쓸 가능성. 대상: `ProductDetailScreen.kt:269`, `AccountWithdrawalReasonScreen.kt:99`, `AccountSettingsScreen.kt:191`. → surfaceContainerLowest~Highest, surfaceBright/Dim 정의.
- **M5** 다이얼로그 radius 12/16, 버튼 체계 불일치 → 공용 `EumAlertDialog`(r12, 확인 Primary, 취소 Outlined, 파괴 동작은 danger 글자).
- **M6** `CartScreen.kt:49,133` 품절 상품이 "주문 가능 상품 (1)"에 포함됨 → selectableItems 기준 집계 또는 주문 불가 구역 분리.

### Minor
- **m1** `CartQuantityStepper` 비활성 테두리 제거/라인 처리.
- **m2** `CartProductRow.kt:107` 품절 빨간 글자 → dangerTint 배지(r4, labelS).
- **m3** `eumCheckboxColors`에 `disabledUncheckedBoxColor` 추가.
- **m4** 찜 색 danger → `heartActive`(#E5484D). 대상: `HomeProductCard.kt:110`, `ProductDetailScreen.kt:497`.
- **m5** 하단 탭 라벨 "마이페이지" → Figma "마이". `HomeBottomNavigation.kt` / `CatalogBottomNavigation.kt` 두 벌을 공용화.
- **m6** 서체: Type.kt Pretendard / Figma Noto Sans KR / 문서 "시스템 서체" 표기 불일치 → Pretendard 유지, 문서 정정.
- **m7** 하드코딩 색 `Color(0xFFFFE9E9)`(`ProductDetailScreen.kt:590`, `AccountSettingsScreen.kt:65-66`), `.dp` 리터럴 → 사이즈 토큰화.
- **m8** CTA 높이 52/56 혼재 → Figma 기준 통일.
- **m9** Figma 캔버스 문제 → 디자이너 전달 (앱 작업 범위 아님).

### 누락 상태
- 이번 작업에서 보완: 담기 완료 Snackbar, 찜 피드백, 스켈레톤(정적, P1 shimmer), 빈 장바구니(EmptyCollectionState + 쇼핑 계속하기), 네트워크 오류(EmptyCollectionState + 다시 시도).
- 대기: 결제 실패, 세션 만료, 옵션 미선택 경고, 품절 placeholder, 상품목록 0건(임시 EmptyCollectionState).

### #34 범위에서 넘어온 실행 점검 항목
- 배송 "무료"인데 ₩3,000이 합산되는 문제 (U-01)
- 결제 버튼 비활성 안내 위치 (U-03)
- 옵션 시트에 ₩0이 보이고 총 금액이 비는 문제 (U-04)
- 드로어가 열렸을 때 상태 바 아이콘 대비 (U-06)
- 견적 필수 항목(납품 지역·희망 납기) 검증 (N-01)
- 장바구니 상태 계층(담기·주문 후 반영, U-05)은 후속

## 4. 모션 명세

### 4.1 모션 토큰 (`ui/theme/Motion.kt`)
- `EumDuration`: SHORT1=100, SHORT2=150, MEDIUM1=250, MEDIUM2=300, LONG1=400 (ms)
- `EumEasing`: Standard(0.2,0,0,1), StandardDecelerate(0,0,0,1), StandardAccelerate(0.3,0,1,1), EmphasizedDecelerate(0.05,0.7,0.1,1), EmphasizedAccelerate(0.3,0,0.8,0.15)
- `EumSpring`: Press(dampingRatio 1, StiffnessMedium), Settle(1, MediumLow), Confirm(0.7, MediumLow — 주문 완료 전용)
- 원칙: 퇴장 ≈ 진입 × 0.6
- 정정(2026-11-09, reviewer 사전 검토): material3 1.4.0(BOM 2026.06.01 고정)에서 `MotionScheme`과 `MaterialTheme(motionScheme=)`는 internal이라 쓸 수 없다. 사용자 결정(D1): 1.4.0 유지, `Motion.kt` 토큰을 tween·spring·CubicBezierEasing에 직접 적용. M3 내부 애니메이션(ModalBottomSheet, Snackbar, ripple)은 앱에서 바꿀 수 없다. 1.5.x 상향은 후속 검토 항목.

### 4.2 P0 (#34 1차 구현 대상 7개)
| # | 항목 | 명세 | 축소 동작(reduced motion) |
|---|---|---|---|
| P0-1 | Nav3 push/pop 화면 전환 | 진입: slideX +10%→0 + fade, 300ms EmphasizedDecelerate / 이탈: fade 150ms EmphasizedAccelerate. predictivePop 동일. 전환 정의는 `navigation/EumNavTransitions.kt` 한 곳에만 둠 | fade 150ms |
| P0-2 | 탭 전환 fade-through | out 90ms StandardAccelerate, in 210ms(delay 90) StandardDecelerate. 같은 탭 재탭 시 맨 위로 스크롤 | 즉시 |
| P0-3 | 바텀시트/다이얼로그 | M3 기본 유지. Nav3 오버레이 NavKey 사용, onRemove에서 sheetState.hide() 완료 후 제거(옵션 시트 닫힘 보장), scrim dim | — |
| P0-4 | 담기 완료 Snackbar | fade + translateY 8dp. 등장 250ms StandardDecelerate / 퇴장 150ms. 4초 표시, 액션 "장바구니 보기". CTA 위에 위치. `common/EumSnackbar.kt` | fade |
| P0-5 | 찜 토글 | 아이콘 crossfade 150ms, heartIdle→heartActive. 켤 때만 scale 1→1.12→1 (Press). 공용 `FavoriteToggleButton` | 즉시 |
| P0-6 | 버튼·카드 누름 scale | 리플 유지. 주 CTA·상품 카드 scale 0.98 (누름 100ms Standard, 놓음 Press 스프링) | 리플만 |
| P0-7 | 수량 스테퍼 숫자 | AnimatedContent: 증가 위로·감소 아래로 6dp + fade 150ms Standard. 금액은 즉시 변경. 대상: `CartQuantityStepper.kt:47`, `CatalogSelectedOptionCard.kt` | 즉시 |

### 4.3 P1 (명세만 기록, 후속 이슈)
- shimmer 1200ms linear (스켈레톤)
- 리스트 첫 등장 stagger (200ms, 간격 40ms, 앞 6개)
- 검색 결과 Crossfade 250ms
- 장바구니 배지 scale 1.2 (Settle)
- 장바구니 삭제 animateItem + 되돌리기(Undo)
- 주문 완료 체크 (Confirm + path 300ms, 400ms 이내)
- 상세 탭 인디케이터 150ms

### 4.4 P2 (명세만 기록, 후속 이슈)
- shared element 전환
- 입력 오류 shake
- 체크아웃 단계 진행 애니메이션
- 폼 expand

### 4.5 접근성
- `rememberReducedMotion()` = `!ValueAnimator.areAnimatorsEnabled()` (기존 `HomeHeroCarousel.kt:118`에서 사용 중인 방식과 통일)
- 줄이기 설정 시: 이동·scale·stagger·shimmer 제거, 무한 반복 정지
- 모션으로만 전달되는 상태는 텍스트 또는 contentDescription으로도 전달

## 5. 검증 기준 (완료 조건)
- `./gradlew :app:compileDebugKotlin`, `:app:testDebugUnitTest` 통과
- primary(#111) 교체 전·후 Preview·스크린샷 비교 (전역 영향 확인)
- 에뮬레이터: 전용 AVD에서 P0 7개 동작과 줄이기 설정 시 축소 동작 확인
- 실행 점검(user-reviewer 빠른 점검) 보고서 수령
