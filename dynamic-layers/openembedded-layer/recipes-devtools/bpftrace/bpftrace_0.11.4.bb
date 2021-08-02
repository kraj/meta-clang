SUMMARY = "bpftrace"
HOMEPAGE = "https://github.com/iovisor/bpftrace"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

DEPENDS += "bison-native \
            flex-native \
            gzip-native \
            elfutils \
            bcc \
            "

PV .= "+git${SRCREV}"
RDEPENDS:${PN} += "bash python3 xz"

SRC_URI = "git://github.com/iovisor/bpftrace;branch=master \
           file://0001-bpforc.h-Include-optional-header.patch \
           "
SRCREV = "6bfa61f505b6b4215328f90762776edd8a22fdb7"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE = " \
    -DCMAKE_ENABLE_EXPORTS=1 \
    -DCMAKE_BUILD_TYPE=Release \
    -DBUILD_TESTING=OFF \
"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*)-linux"
COMPATIBLE_HOST:libc-musl = "null"
