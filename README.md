# Eum ShoppingMall Android

이음산업건설 쇼핑몰 Android 앱을 위한 단일 모듈 Jetpack Compose 프로젝트입니다. 현재 앱 진입 화면으로 Figma Home A-02를 기반으로 한 홈 UI를 제공하며, 나머지 쇼핑몰 화면과 실제 서비스 연동은 아직 구현되지 않았습니다.

## 프로젝트 현황

- `MainActivity`에서 홈 화면 `HomeRoute`를 표시합니다. 홈 화면에는 상단 바, 검색창, 카테고리, 배너, 상품 섹션, 견적 배너, 하단 탐색 UI가 포함되어 있습니다.
- 홈 UI는 콜백과 UI 모델을 통해 입력을 전달하며, 상품 데이터는 기본값으로 비어 있습니다. 실제 검색·상품 조회·화면 이동을 처리하는 서비스 로직은 확인되지 않았습니다.
- Figma Light 디자인 토큰과 공통 Android 리소스가 반영되어 있습니다. 다른 Figma 화면의 화면별 UI 구현 여부는 확인되지 않았으며, 현재 앱 진입 화면에는 홈 UI만 연결되어 있습니다.
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
- 고정 UI 문구는 `app/src/main/res/values/strings.xml`에 두고, 예시 상품·가격·사용자 데이터는 포함하지 않습니다.
- Figma의 아이콘 102종은 `drawable/ic_*.xml` VectorDrawable로 가져왔으며 원본 노드명과 Android 리소스 대응표는 `app/src/main/assets/figma_icon_map.csv`에 있습니다. (총 104개 `ic_*.xml` 중 런처 아이콘 2개 제외)
- Figma 앱 페이지 이미지 7종은 `drawable-nodpi/figma_*.png`에 원본 픽셀로 보관합니다.
- Compose foundation 확인: `./gradlew :app:compileDebugKotlin :app:testDebugUnitTest`

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
│       │   │   ├── MainActivity.kt # 앱 진입점, 홈 Route 연결
│       │   │   ├── ui/home/         # 홈 화면, UI 모델, 화면 컴포넌트
│       │   │   └── ui/theme/        # Compose 테마 및 디자인 토큰
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
