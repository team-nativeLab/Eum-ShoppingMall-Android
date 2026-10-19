# Eum ShoppingMall Android

이음산업건설 쇼핑몰 Android 앱을 위한 단일 모듈 Jetpack Compose 프로젝트입니다. 현재 저장소는 앱 기초 프로젝트이며, 쇼핑몰 화면과 기능은 아직 구현되지 않았습니다.

## 프로젝트 현황

- 앱 UI는 현재 Android 기본 예제인 `Hello Android!` 화면입니다.
- Figma Light 디자인 토큰과 공통 Android 리소스가 반영되어 있습니다. 화면별 UI 구현은 포함하지 않습니다.
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
- Figma의 아이콘 102종은 `drawable/ic_*.xml` VectorDrawable로 가져왔으며 원본 노드명과 Android 리소스 대응표는 `app/src/main/assets/figma_icon_map.csv`에 있습니다.
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
│       │   │   ├── MainActivity.kt
│       │   │   └── ui/theme/       # Compose 테마 파일
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
