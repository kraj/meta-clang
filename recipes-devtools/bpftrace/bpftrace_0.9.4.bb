SUMMARY = "bpftrace"
HOMEPAGE = "https://github.com/iovisor/bpftrace"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS += "bison-native \
            ninja-native \
            elfutils-native \
            flex-native \
            gtest-native \
            git-native \
            ${MLPREFIX}elfutils \
            ${MLPREFIX}binutils \
            ${MLPREFIX}flex \
            clang \
            bcc \
            "

RDEPENDS_${PN} += "bash python3"

SRC_URI = "git://github.com/iovisor/bpftrace \
           file://0001-Allow-to-build-with-OE-llvm-clang-cross-compiled-pac.patch \
           "
SRCREV = "85f9eea624c83443816e37654d0c1c93366a2e66"

S = "${WORKDIR}/git"

inherit cmake

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
"

FILES_${PN} += "${prefix}/man/*"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*)-linux"
COMPATIBLE_HOST_libc-musl = "null"
