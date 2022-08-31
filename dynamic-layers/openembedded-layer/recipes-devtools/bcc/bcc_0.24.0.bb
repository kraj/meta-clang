SUMMARY = "BPF Compiler Collection (BCC)"
HOMEPAGE = "https://github.com/iovisor/bcc"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=e3fc50a88d0a364313df4b21ef20c29e"

inherit cmake python3native manpages

DEPENDS += "bison-native \
            flex-native \
            flex \
            elfutils \
            ${LUAJIT} \
            clang \
            libbpf \
            "

LUAJIT ?= "luajit"
LUAJIT:powerpc64le = ""
LUAJIT:powerpc64 = ""
LUAJIT:riscv64 = ""

RDEPENDS:${PN} += "bash python3 python3-core python3-setuptools xz"

SRC_URI = "gitsm://github.com/iovisor/bcc;branch=master;protocol=https \
           file://0001-python-CMakeLists.txt-Remove-check-for-host-etc-debi.patch \
           file://0001-tools-trace.py-Fix-failing-to-exit.patch \
           file://0001-CMakeLists.txt-override-the-PY_CMD_ESCAPED.patch \
           file://0001-Vendor-just-enough-extra-headers-to-allow-libbpf-to-.patch \
           "

SRCREV = "8f40d6f57a8d94e7aee74ce358572d34d31b4ed4"

PV .= "+git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGECONFIG ??= "examples"
PACKAGECONFIG:remove:libc-musl = "examples"

PACKAGECONFIG[manpages] = "-DENABLE_MAN=ON,-DENABLE_MAN=OFF,"
PACKAGECONFIG[examples] = "-DENABLE_EXAMPLES=ON,-DENABLE_EXAMPLES=OFF,"

EXTRA_OECMAKE = " \
    -DCMAKE_USE_LIBBPF_PACKAGE=ON \
    -DENABLE_LLVM_SHARED=ON \
    -DENABLE_CLANG_JIT=ON \
    -DLLVM_PACKAGE_VERSION=${LLVMVERSION} \
    -DPYTHON_CMD=${PYTHON} \
    -DPYTHON_FLAGS=--install-lib=${PYTHON_SITEPACKAGES_DIR} \
"

do_install:append() {
        sed -e 's@#!/usr/bin/python@#!/usr/bin/env python3@g' \
            -i $(find ${D}${datadir}/${PN} -type f)
}

FILES:${PN} += "${PYTHON_SITEPACKAGES_DIR}"
FILES:${PN}-doc += "${datadir}/${PN}/man"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*|riscv64.*)-linux"
