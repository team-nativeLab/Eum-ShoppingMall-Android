# Eum ShoppingMall Android

이음산업건설 쇼핑몰의 상품 탐색, 장바구니, 주문 흐름을 제공하는 Kotlin·Jetpack Compose Android 앱입니다. 건설 자재 구매 담당자(B2B)와 일반 소비자(B2C)를 위한 화면 프로토타입이며, 실제 서비스 연동은 아직 포함하지 않습니다.

## 프로젝트 현황

- `MainActivity`는 장바구니 DataStore, build type별 화면 데이터 제공자, Navigation 3의 `EumMainNavDisplay`를 연결합니다.
- 화면 흐름에는 홈, 카테고리, 상품 목록·상세, 검색·검색 결과·빈 결과, 브랜드, 장바구니, 로그인, 주문·결제·주문 상세, 마이페이지 및 고객 지원이 포함됩니다.
- 하단의 홈·카테고리·검색·장바구니·마이 탭은 각각 back stack을 유지합니다. 흐름 상세는 [`docs/APP_FLOW.md`](docs/APP_FLOW.md)에 있습니다.
- 장바구니 항목은 Preferences DataStore에 로컬로 저장됩니다. 서버와 동기화되지는 않습니다.
- Debug 빌드는 화면 흐름 확인용 샘플 데이터를 사용하며 로그인은 mock입니다. Release 빌드는 빈 화면 데이터 제공자를 사용합니다. 실제 계정 인증, 결제, 주문 저장 및 1:1 문의 접수는 연결되지 않았습니다.
- 햄버거 버튼은 5개 탭 루트의 왼쪽에 있고, 왼쪽에서 슬라이드 인하는 사이드바에서 베스트·추천·할인 목록, 찜 목록, 공지, 고객센터·FAQ, 이용안내·약관, 알림, 설정으로 이동합니다. 알림과 설정은 서버 연동 전이라 빈 상태·"준비 중" 고지로 열립니다.
- 회원가입은 이메일·이름, 비밀번호, 휴대폰 인증, 약관 동의의 4단계로 진행하며, 앱 로고는 "안전맛집"으로 통일되어 있습니다.
- 가격이 표시되지 않는 상품은 상품 상세·옵션 시트·검색 결과 없음·장바구니에서 상품명을 채운 1:1 문의 작성 화면으로 연결됩니다.
- Debug 화면 갤러리에는 A-01~A-57(A-15·A-25 제외)의 Preview fixture가 있습니다. Fixture 제공 여부는 production 화면 연결이나 시각 검증 완료를 뜻하지 않습니다.
- `docs/FIGMA_APP_RESPONSIVE_AUDIT.md`에는 320dp·390dp·800dp 및 큰 글자 배율의 Preview·캡처 근거가 있습니다. 캡처가 있는 상태도 전체 Figma pixel 비교 완료를 뜻하지 않으며, 일부 overlay 캡처와 110개 node의 런타임 전수 검증은 미완료입니다.
- 전수 시각·반응형 비교는 미완료입니다. 스크롤·IME 입력과 모든 프레임·상태 조합이 검증된 것은 아닙니다.
- Figma Light 디자인 토큰과 공통 Android 리소스가 반영되어 있습니다.
- 제품 요구사항 문서(PRD)는 저장소에서 확인되지 않았습니다.

## 기술 구성

| 항목 | 설정 |
|---|---|
| 모듈 | `:app` |
| UI | Kotlin, Jetpack Compose, Material 3 |
| Navigation | AndroidX Navigation 3 |
| 로컬 저장소 | Preferences DataStore (장바구니) |
| Application ID | `com.nativelap.eumshoppingmall` |
| Compile SDK | Android SDK 37 |
| Min SDK | 34 |
| Target SDK | 36 |
| Java source/target compatibility | 11 |
| Android Gradle Plugin | 9.2.1 |
| Kotlin | 2.2.10 |
| Gradle wrapper | 9.4.1 |

## 시작하기

Android SDK 37과 JDK 21이 필요합니다. Gradle daemon은 `gradle/gradle-daemon-jvm.properties`에서 JDK 21을 사용하도록 고정되어 있습니다. Android Studio 최소 버전은 확인 필요입니다.

```bash
./gradlew :app:assembleDebug
```

## 빌드 및 테스트

```bash
# Debug 앱 빌드
./gradlew :app:assembleDebug

# 로컬 단위 테스트
./gradlew :app:testDebugUnitTest

# Android Lint
./gradlew :app:lintDebug

# Spotless 포맷 검사
./gradlew spotlessCheck

# Android 계측 테스트 (에뮬레이터 또는 연결된 기기 필요)
./gradlew :app:connectedDebugAndroidTest
```

### Debug 화면 갤러리

Figma 대조용으로 A-01~A-57 화면을 에뮬레이터에서 직접 열어볼 수 있다. `app/src/debug` 전용이라 release 빌드에는 포함되지 않으며 launcher에는 노출되지 않는다.

```bash
# 목록 열기
adb shell am start -n com.nativelap.eumshoppingmall/.debuggallery.ScreenGalleryActivity
# 화면 바로 열기 (폭 dp, 글자 배율은 선택)
adb shell am start -n com.nativelap.eumshoppingmall/.debuggallery.ScreenGalleryActivity --es screen A-33 --ei width 320 --ef fontScale 1.5
# 수동 확인: A-57을 열어 오픈소스 라이선스 목록이 렌더되는지 본다
adb shell am start -n com.nativelap.eumshoppingmall/.debuggallery.ScreenGalleryActivity --es screen A-57
```

- 800dp는 기기 폭에 맞춰 density를 줄인 축소 표시라 터치 영역 비율 검증에는 맞지 않는다.
- 화면 Preview를 리플렉션으로 호출하므로 Preview가 예외를 던지면 갤러리도 크래시한다(원인은 logcat `ScreenGallery` 태그에 남는다).
- 알 수 없는 `--es screen` id는 토스트와 `Log.w`로 알리고 목록을 연다.

## 디자인 시스템과 공통 리소스

- Compose 토큰: `app/src/main/java/com/nativelap/eumshoppingmall/ui/theme/Color.kt`, `Type.kt`, `EumSpacing.kt`, `Shapes.kt`, `Elevation.kt` (중립색은 Figma 변수 값, CTA 버튼 최소 높이 56dp, `labelSmall`은 12/16 Medium)
- Figma Light 팔레트와 타이포그래피를 기본 테마로 사용하며 Android 동적 색상은 사용하지 않습니다.
- Figma 화면은 390px 폭의 모바일 프레임이며 spacing 값은 해당 dp 기준과 1:1로 옮겼습니다.
- `app/src/main/res/font/`의 Pretendard Regular·Medium·SemiBold·Bold 글꼴을 `ui/theme/Type.kt`의 `EumTypographyTokens`에서 사용합니다.
- UI 문구는 주로 `app/src/main/res/values/strings.xml`에 있으며 일부 화면 문구는 코드에 남아 있습니다. Debug 앱에서는 샘플 상품·가격·사용자 데이터를 표시하며 실제 서비스 데이터와 구분해야 합니다.
- `app/src/main/res/drawable/`에는 `ic_*.xml` VectorDrawable 리소스 107개가 있으며 런처 아이콘 2개를 포함합니다. 원본 노드명 대응표는 `app/src/main/assets/figma_icon_map.csv`에 있고, CSV는 header를 포함해 103줄입니다.
- Figma 앱 페이지 이미지 7종은 `drawable-nodpi/figma_*.png`에 원본 픽셀로 보관합니다.
- Compose 기반 확인 명령: `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest`

## CI와 개발 스킬

- `.github/workflows/`에는 PR 빌드·테스트, 포맷 검사, PR 규칙 검사, Secret Scan, 비활성 기본값의 Claude/Codex 자동 수정 워크플로가 있습니다.
- CI는 JDK 21과 Android SDK 37을 설치합니다. 자동 수정은 저장소 변수 `AUTOFIX_PROVIDER` 및 승인자 보호가 설정된 `ci-autofix` Environment가 있어야 동작합니다.
- 자동 수정은 읽기 권한만 가진 작업에서 PR 수정·빌드·단위 테스트를 수행하고, 별도 작업이 허용 경로의 검증된 패치만 게시합니다. 자동 수정 API secret은 해당 공급자와 `ci-autofix` Environment가 설정되기 전에는 주입되지 않습니다.
- `.agents/skills/`에는 Android 개발용 HeartGuard 스킬 15개가 있습니다.

## 프로젝트 구조

```text
EumShoppingMall/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/com/nativelap/eumshoppingmall/
│       │   │   ├── MainActivity.kt # 앱 진입점과 앱 상태 연결
│       │   │   ├── data/local/      # 장바구니 DataStore
│       │   │   ├── navigation/     # 목적지와 Navigation 3 화면 흐름
│       │   │   └── ui/              # 기능별 Compose Route·Screen·ViewModel
│       │   └── res/                # Android 리소스
│       ├── debug/                  # 샘플 데이터 제공자·화면 갤러리
│       ├── release/                # 빈 화면 데이터 제공자
│       ├── test/                   # 로컬 단위 테스트
│       └── androidTest/            # Android 계측 테스트
├── gradle/libs.versions.toml       # 라이브러리 및 플러그인 버전 카탈로그
├── docs/APP_FLOW.md                 # 앱 화면 흐름과 Navigation 3 설명
├── build.gradle.kts
└── settings.gradle.kts
```

## 문서

- 화면 흐름과 알려진 구현 단계: [`docs/APP_FLOW.md`](docs/APP_FLOW.md)
- 제품 의도 브리프: [`docs/PRODUCT_BRIEF.md`](docs/PRODUCT_BRIEF.md)
- 제품 요구사항 문서(PRD): 저장소에서 찾지 못했습니다.

## 저장소

- GitHub: [team-nativeLab/Eum-ShoppingMall-Android](https://github.com/team-nativeLab/Eum-ShoppingMall-Android)
- 기초 설정 이슈: [#1](https://github.com/team-nativeLab/Eum-ShoppingMall-Android/issues/1)

## 라이선스

저장소에서 라이선스 파일을 확인하지 못했습니다. 라이선스는 확인 필요입니다.
