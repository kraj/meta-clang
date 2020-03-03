SUMMARY = "BPF Compiler Collection (BCC)"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit cmake python3native

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
           file://0001-python-CMakeLists.txt-Remove-check-for-host-etc-debi.patch \
           "

SRCREV = "942227484d3207f6a42103674001ef01fb5335a0"

S = "${WORKDIR}/git"

EXTRA_OECMAKE = " \
    -DCMAKE_INSTALL_PREFIX=/usr \
    -DENABLE_LLVM_SHARED=ON \
    -DENABLE_CLANG_JIT=ON \
    -DENABLE_MAN=OFF \
    -DLLVM_PACKAGE_VERSION=${LLVMVERSION} \
    -DPYTHON_CMD=${PYTHON} \
"

do_install_append() {
        sed -e 's@#!/usr/bin/python@#!/usr/bin/env python3@g' \
            -i $(find ${D}${datadir}/${PN} -type f)
}

FILES_${PN} += "${PYTHON_SITEPACKAGES_DIR}"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*)-linux"
