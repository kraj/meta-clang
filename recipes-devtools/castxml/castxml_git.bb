SUMMARY = "C-family abstract syntax tree XML output tool."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://github.com/CastXML/CastXML"

# 0.3.6 is the release for LLVM/Clang v11.0.0
SRCREV = "902ac163f0291fcfc459f58691481e88c9f91dea"
PV = "0.3.6"

S = "${WORKDIR}/git"

DEPENDS = "clang"

# Match clang's idea of what TOOLCHAIN should be.
TOOLCHAIN = "clang"
TOOLCHAIN_class-native = "gcc"
TOOLCHAIN_class-nativesdk = "clang"

BUILD_CC_class-nativesdk = "clang"
BUILD_CXX_class-nativesdk = "clang++"
BUILD_AR_class-nativesdk = "llvm-ar"
BUILD_RANLIB_class-nativesdk = "llvm-ranlib"
BUILD_NM_class-nativesdk = "llvm-nm"
LDFLAGS_append_class-nativesdk = " -fuse-ld=gold"

inherit cmake cmake-native pkgconfig python3native

BBCLASSEXTEND = "native nativesdk"
