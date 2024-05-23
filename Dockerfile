# 첫 번째 스테이지: 빌드 스테이지
FROM ghcr.io/graalvm/graalvm-community:21 as builder

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드와 Gradle 래퍼 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Gradle 래퍼에 실행 권한 부여
RUN chmod +x ./gradlew

# 종속성 설치
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew clean build -x test --no-daemon

# 두 번째 스테이지: 실행 스테이지
FROM ghcr.io/graalvm/graalvm-community:21

# 작업 디렉토리 설정
WORKDIR /app

# 첫 번째 스테이지에서 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 필요한 패키지 설치
# 필요한 패키지 설치
RUN microdnf install -y wget curl

RUN microdnf install -y libX11 libXcomposite libXdamage libXext libXfixes libXrandr alsa-lib atk at-spi2-core cairo cups-libs dbus-libs liberation-fonts dbus libdrm libgbm gtk3 nspr nss pango vulkan xcb-util xkbcommon xdg-utils


# Chrome 설치
RUN curl -O https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm \
    && rpm -ivh google-chrome-stable_current_x86_64.rpm \
    && rm -f google-chrome-stable_current_x86_64.rpm

# 실행할 JAR 파일 지정
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
