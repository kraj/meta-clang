SUMMARY = "C-family abstract syntax tree XML output tool."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/CastXML/CastXML;protocol=https;branch=master"

SRCREV = "6d5a3c62ce28fa930456d64c0cfe338e9e3ae08e"

PV .= "+0.5.2+${SRCPV}"

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
