SUMMARY = "BPF Compiler Collection (BCC)"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

DEPENDS += "bison-native \
            ninja-native \
            elfutils-native \
            flex-native \
            ${MLPREFIX}elfutils \
            ${MLPREFIX}binutils \
            ${MLPREFIX}flex \
            luajit \
            clang \
            "

RDEPENDS_${PN} += "bash python3 python3-core"

SRC_URI = "git://github.com/iovisor/bcc \
           file://0001-Allow-to-build-with-OE-LLVM-cross-compiled-package.patch \
           file://0001-BCC-Use-python-3.patch \
           "
SRCREV = "942227484d3207f6a42103674001ef01fb5335a0"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE = " \
    -DCMAKE_INSTALL_PREFIX=/usr \
    -DENABLE_LLVM_SHARED=ON \
    -DENABLE_CLANG_JIT=ON \
    -DENABLE_MAN=OFF \
    -DLLVM_PACKAGE_VERSION=${LLVMVERSION} \
    -DPYTHON_CMD=python3 \
"

FILES_${PN} += "${libdir}/python*/dist-packages"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*)-linux"
COMPATIBLE_HOST_libc-musl = "null"
