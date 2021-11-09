SUMMARY = "C-family abstract syntax tree XML output tool."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/CastXML/CastXML;protocol=https;branch=master"

# 0.4.4 is the release for LLVM/Clang v13.0.0
SRCREV = "2095c10a25610a8ccf6c7ef819440bdac6b13c59"
PV = "0.4.4"

S = "${WORKDIR}/git"

DEPENDS = "clang"

# Match clang's idea of what TOOLCHAIN should be.
TOOLCHAIN = "clang"
TOOLCHAIN:class-native = "gcc"
TOOLCHAIN:class-nativesdk = "clang"

BUILD_CC:class-nativesdk = "clang"
BUILD_CXX:class-nativesdk = "clang++"
BUILD_AR:class-nativesdk = "llvm-ar"
BUILD_RANLIB:class-nativesdk = "llvm-ranlib"
BUILD_NM:class-nativesdk = "llvm-nm"
LDFLAGS:append:class-nativesdk = " -fuse-ld=gold"

inherit cmake cmake-native pkgconfig python3native

BBCLASSEXTEND = "native nativesdk"
