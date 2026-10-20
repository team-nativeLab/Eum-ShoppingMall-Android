# Eum ShoppingMall Android

이음산업건설 쇼핑몰 Android 앱을 위한 단일 모듈 Jetpack Compose 프로젝트입니다. 제품 탐색을 위한 카탈로그·검색·상품 상세·브랜드 Compose UI와 Figma 디자인 토큰을 포함합니다.

## 프로젝트 현황

- 앱 진입점 `MainActivity`는 여전히 `Greeting("Android")`를 표시합니다. `ui/catalog`의 화면은 별도의 UI Composable로 구현되어 있지만 앱 탐색이나 `MainActivity`에 연결되어 있지 않습니다.
- 카탈로그 UI에는 카테고리, 상품 목록, 검색, 검색 결과·빈 결과, 상품 상세, 브랜드 목록·상세 화면과 공용 상단·하단 바 및 상품 카드가 있습니다. 각 화면은 UI 모델과 이벤트 콜백을 입력으로 받습니다. 검색 결과와 브랜드 상품 그리드는 화면 폭에 따라 2·3·4열로 바뀝니다.
- 상품 상세 UI 모델은 리뷰·문의·반품 정보와 이미지 갤러리 상태를 지원하며, 갤러리 선택 이벤트를 전달합니다. 견적 상품은 별도의 옵션 시트 상태와 안내 UI를 제공합니다.
- 이번 작업 대상은 Figma 프레임 A-03~A-06 및 A-33/A-34입니다. 이 범위의 구현이 전체 Figma 페이지를 완성한 것은 아니며, 나머지 프레임은 미구현입니다.
- `CatalogPreviews.kt`의 화면 예시는 Preview 전용 합성 상품·브랜드 데이터를 사용합니다. 실행 화면의 실제 상품 데이터 연동은 포함되어 있지 않습니다.
- 상품 이미지용 리소스 대응은 아직 없습니다. 현재 `figma_image_map.csv`의 이미지는 스플래시, 홈, 드로어, 브랜드 상세 배너, 회사 소개 이미지입니다.
- 카테고리·상품 목록·검색·검색 결과·빈 결과·브랜드 목록·브랜드 상세·상품 상세·견적 상품 옵션 시트의 390dp Compose Preview가 있습니다. Preview 선언은 화면 캡처나 시각 검증 완료를 뜻하지 않습니다.
- 320dp, 태블릿 폭, 글자 크기 확대 조건의 스크린샷 검증은 에뮬레이터가 없어 수행하지 않았습니다. 대상 프레임의 Figma 대조와 반응형·상태별 검증도 남아 있습니다.
- Figma Light 디자인 토큰과 공통 Android 리소스가 반영되어 있습니다.
- 제품 요구사항 문서(PRD)는 저장소에서 확인되지 않았습니다.

## 기술 구성

| 항목 | 설정 |
|---|---|
| 모듈 | `:app` |
| UI | Kotlin, Jetpack Compose, Material 3 |
| Application ID | `com.nativelap.eumshoppingmall` |
| Compile SDK | Android SDK 37 |
| Min SDK | 34 |
| Target SDK | 36 |
| Java source/target compatibility | 11 |
| Android Gradle Plugin | 9.2.1 |
| Kotlin | 2.2.10 |
| Gradle wrapper | 9.4.1 |

## 시작하기

Android Studio와 Android SDK 37을 설치한 뒤 프로젝트를 엽니다. Gradle daemon은 `gradle/gradle-daemon-jvm.properties`에서 JDK 21을 사용하도록 고정되어 있습니다.

```bash
./gradlew :app:assembleDebug
```

## 빌드 및 테스트

```bash
# Debug 앱 빌드
./gradlew :app:assembleDebug

# 로컬 단위 테스트
./gradlew :app:testDebugUnitTest

# Android 계측 테스트 (에뮬레이터 또는 연결된 기기 필요)
./gradlew :app:connectedDebugAndroidTest
```

## 디자인 시스템과 공통 리소스

- Compose 토큰: `ui/theme/Color.kt`, `Type.kt`, `EumSpacing.kt`, `Shapes.kt`, `Elevation.kt`
- Figma Light 팔레트와 타이포그래피를 기본 테마로 사용하며 Android 동적 색상은 사용하지 않습니다.
- Figma 화면은 390px 폭의 모바일 프레임이며 spacing 값은 해당 dp 기준과 1:1로 옮겼습니다.
- Figma의 타이포그래피 변수는 Noto Sans KR로 정의되어 있지만, 프로젝트에는 별도 글꼴 파일이나 라이선스 정보가 없어 시스템 기본 글꼴로 대체합니다.
- 고정 UI 문구는 `app/src/main/res/values/strings.xml`에 둡니다. 실행 화면은 실제 상품·가격·사용자 데이터를 제공하지 않으며, 합성 예시 값은 Compose Preview에만 있습니다.
- Figma의 아이콘 102종은 `drawable/ic_*.xml` VectorDrawable로 가져왔으며 원본 노드명과 Android 리소스 대응표는 `app/src/main/assets/figma_icon_map.csv`에 있습니다.
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
│       │   │   ├── MainActivity.kt # 현재 앱 진입점과 기본 예제 화면
│       │   │   ├── ui/catalog/      # 독립 카테고리·검색·상품·브랜드 Compose 화면
│       │   │   └── ui/theme/       # Compose 테마와 디자인 토큰
│       │   └── res/                # Android 리소스
│       ├── test/                   # 로컬 단위 테스트
│       └── androidTest/            # Android 계측 테스트
├── gradle/libs.versions.toml       # 라이브러리 및 플러그인 버전 카탈로그
├── build.gradle.kts
└── settings.gradle.kts
```

## 저장소

- GitHub: [team-nativeLab/Eum-ShoppingMall-Android](https://github.com/team-nativeLab/Eum-ShoppingMall-Android)
- 기초 설정 이슈: [#1](https://github.com/team-nativeLab/Eum-ShoppingMall-Android/issues/1)

## 라이선스

저장소에서 라이선스 파일을 확인하지 못했습니다. 라이선스는 확인 필요입니다.
