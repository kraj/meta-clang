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

RDEPENDS_${PN} += "bash python3 xz"

SRC_URI = "git://github.com/iovisor/bpftrace;branch=0.11_release \
           "
SRCREV = "0cd90b8b91f67cae9f612a07498bf8d92306fab1"

S = "${WORKDIR}/git"

inherit cmake

EXTRA_OECMAKE = " \
    -DCMAKE_ENABLE_EXPORTS=1 \
    -DCMAKE_BUILD_TYPE=Release \
    -DBUILD_TESTING=OFF \
"

COMPATIBLE_HOST = "(x86_64.*|aarch64.*|powerpc64.*)-linux"
COMPATIBLE_HOST_libc-musl = "null"
