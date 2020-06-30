SUMMARY = "BPF Compiler Collection (BCC)"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit cmake python3native manpages

DEPENDS += "bison-native \
            flex-native \
            elfutils \
            luajit \
            clang \
            "

RDEPENDS_${PN} += "bash python3 python3-core python3-setuptools xz"

SRC_URI = "gitsm://github.com/iovisor/bcc \
           file://0001-python-CMakeLists.txt-Remove-check-for-host-etc-debi.patch \
           file://0001-tools-opensnoop-snoop-do_sys_openat2-for-kernel-v5.6.patch \
           file://0001-Add-KBUILD_MODNAME-flag-to-default-cflags.patch \
           file://0001-Delete-existing-kbuild_modname-definitions.patch \
           "

SRCREV = "942227484d3207f6a42103674001ef01fb5335a0"

S = "${WORKDIR}/git"

PACKAGECONFIG ??= ""
PACKAGECONFIG[manpages] = "-DENABLE_MAN=ON,-DENABLE_MAN=OFF,"

EXTRA_OECMAKE = " \
    -DENABLE_LLVM_SHARED=ON \
    -DENABLE_CLANG_JIT=ON \
    -DLLVM_PACKAGE_VERSION=${LLVMVERSION} \
    -DPYTHON_CMD=${PYTHON} \
    -DPYTHON_FLAGS=--install-lib=${PYTHON_SITEPACKAGES_DIR} \
"

do_install_append() {
        sed -e 's@#!/usr/bin/python@#!/usr/bin/env python3@g' \
            -i $(find ${D}${datadir}/${PN} -type f)
}

FILES_${PN} += "${PYTHON_SITEPACKAGES_DIR}"
FILES_${PN}-doc += "${datadir}/${PN}/man"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*)-linux"
