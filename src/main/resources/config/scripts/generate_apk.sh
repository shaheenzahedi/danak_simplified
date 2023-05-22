#!/bin/bash

# Parse the input arguments
for i in "$@"
do
case $i in
    --android-path=*)
    ANDROID_PATH="${i#*=}"
    shift # past argument=value
    ;;
    --alias=*)
    SIGNING_KEY_ALIAS="${i#*=}"
    shift # past argument=value
    ;;
    --key-password=*)
    SIGNING_KEY_PASSWORD="${i#*=}"
    shift # past argument=value
    ;;
    --store-password=*)
    SIGNING_STORE_PASSWORD="${i#*=}"
    shift # past argument=value
    ;;
    --keystore=*)
    JKS_PATH="${i#*=}"
    shift # past argument=value
    ;;
    --gradlew=*)
    GRADLEW_PATH="${i#*=}"
    shift # past argument=value
    ;;
    --target-folder=*)
    TARGET_FOLDER="${i#*=}"
    shift # past argument=value
    ;;
    --apk-name=*)
    APK_NAME="${i#*=}"
    shift # past argument=value
    ;;
    --compile-sdk-version=*)
    COMPILE_SDK_VERSION="${i#*=}"
    shift # past argument=value
    ;;
    --dropbox-api-key=*)
    DROPBOX_API_KEY="${i#*=}"
    shift # past argument=value
    ;;
    --appmetrica-api-key=*)
    APPMETRICA_API_KEY="${i#*=}"
    shift # past argument=value
    ;;
    --appmetrica-post-api-key=*)
    APPMETRICA_POST_API_KEY="${i#*=}"
    shift # past argument=value
    ;;
    *)
    # unknown option
    echo "Unknown option: $i"
    exit 1
    ;;
esac
done

if [[ -z "${ANDROID_SDK_ROOT}" ]] && ! command -v "${ANDROID_PATH}"/cmdline-tools/latest/bin/sdkmanager &> /dev/null
then
    # Download the Android tools
    echo "Android SDK not found. Downloading the command line tools..."
    wget -q "https://dl.google.com/android/repository/commandlinetools-linux-7302050_latest.zip" -O "/tmp/cli.zip"
    mkdir -p "${ANDROID_PATH}/cmdline-tools/latest"
    unzip -qo "/tmp/cli.zip" -d "${ANDROID_PATH}/"
    mv "${ANDROID_PATH}"/cmdline-tools/* "${ANDROID_PATH}/cmdline-tools/latest/"
    rm "/tmp/cli.zip"

    # Download the Android SDK
    echo "Downloading the Android SDK..."
    wget -q "https://dl.google.com/android/repository/platform-tools_r34.0.1-linux.zip" -O "/tmp/sdk.zip"
    mkdir -p "${ANDROID_PATH}"
    unzip -qo "/tmp/sdk.zip" -d "${ANDROID_PATH}"
    rm "/tmp/sdk.zip"

    export ANDROID_SDK_ROOT="${ANDROID_PATH}"
    export PATH="${PATH}:${ANDROID_SDK_ROOT}/tools:${ANDROID_SDK_ROOT}/tools/bin:${ANDROID_SDK_ROOT}/platform-tools"
fi

# Check if the SDK for the specified compileSdkVersion is installed
if ! "${ANDROID_PATH}"/cmdline-tools/latest/bin/sdkmanager --list | grep -q "platforms;android-${COMPILE_SDK_VERSION}"
then
    # Determine the highest version of the SDK that is lower than the specified version
    HIGHEST_COMPATIBLE_VERSION=$("$ANDROID_PATH"/cmdline-tools/latest/bin/sdkmanager --list | grep -Eo "platforms;android-[0-9]+" | awk -F"android-" '{print $2}' | sort -rn | awk -v version="$COMPILE_SDK_VERSION" '$1 <= version {print $1}' | head -1)

    if [ -n "$HIGHEST_COMPATIBLE_VERSION" ]
    then
        echo "The specified compileSdkVersion $COMPILE_SDK_VERSION is not installed. Downloading the highest compatible version, $HIGHEST_COMPATIBLE_VERSION."
        "${ANDROID_PATH}"/cmdline-tools/latest/bin/sdkmanager "platforms;android-$HIGHEST_COMPATIBLE_VERSION"
    else
        echo "No compatible SDK version found for compileSdkVersion $COMPILE_SDK_VERSION."
        exit 1
    fi
fi

yes | "${ANDROID_PATH}"/cmdline-tools/latest/bin/sdkmanager --licenses


# Build the APK using Gradle and sign it with the provided keystore
cd $GRADLEW_PATH || exit

printf "sdk.dir=%s\nDROPBOX_API_KEY=%s\nAPPMETRICA_API_KEY=%s\nAPPMETRICA_POST_API_KEY=%s" \
  "${ANDROID_PATH}" \
  "$DROPBOX_API_KEY" \
  "$APPMETRICA_API_KEY" \
  "$APPMETRICA_POST_API_KEY" \
  >> ./local.properties

./gradlew assembleChildRelease \
    -Pandroid.injected.signing.store.file="$JKS_PATH" \
    -Pandroid.inject.injected.signing.store.password="$SIGNING_STORE_PASSWORD" \
    -Pandroid.injected.signing.key.alias="$SIGNING_KEY_ALIAS" \
    -Pandroid.injected.signing.key.password="$SIGNING_KEY_PASSWORD"

# Move the APK to the target folder with the specified name
mkdir -p "$TARGET_FOLDER"
mv "${GRADLEW_PATH}"/app/build/outputs/apk/Child/release/app-Child-release.apk "$TARGET_FOLDER"/"$APK_NAME"
rm -f ./local.properties
